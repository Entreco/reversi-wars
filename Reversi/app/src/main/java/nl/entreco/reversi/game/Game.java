package nl.entreco.reversi.game;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.entreco.reversi.data.History;
import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.players.DrawPlayer;
import nl.entreco.reversi.model.players.RandomPlayer;
import nl.entreco.reversi.model.players.UserPlayer;

public class Game implements GameCallback {

    @NonNull public final ObservableInt score1;
    @NonNull public final ObservableInt score2;
    @NonNull public final ObservableField<Player> winner;
    @NonNull public final ObservableField<Player> current;
    @NonNull public final ObservableField<Player> rejected;

    @NonNull public final ObservableField<Player> player1;
    @NonNull public final ObservableField<Player> player2;

    @NonNull private final BoardAdapter adapter;
    @NonNull private final Arbiter arbiter;
    @NonNull private final CreateMatchUsecase createMatchUsecase;

    @NonNull private final Handler main;
    @NonNull private final ScheduledExecutorService background;

    @Nullable private DatabaseReference matchReference;

    public Game(@NonNull final BoardAdapter adapter, @NonNull final Arbiter arbiter, @NonNull final
                CreateMatchUsecase createMatchUsecase) {

        this.adapter = adapter;
        this.arbiter = arbiter;
        this.createMatchUsecase = createMatchUsecase;

        this.player1 = new ObservableField<>();
        this.player2 = new ObservableField<>();

        this.score1 = new ObservableInt(0);
        this.score2 = new ObservableInt(0);
        this.winner = new ObservableField<>();
        this.current = new ObservableField<>();
        this.rejected = new ObservableField<>();
        this.main = setupHandler();
        this.background = setupScheduler();
    }

    @NonNull
    ScheduledExecutorService setupScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @NonNull
    Handler setupHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public void startGame() {
        addDefaultPlayersIfEmpty();

        matchReference = createMatchUsecase.createRemoteMatch(player1.get(), player2.get());

        this.winner.set(null);

        this.player1.get().onJoinedGame(matchReference.getKey());
        this.player2.get().onJoinedGame(matchReference.getKey());

        this.arbiter.addPlayer(player1.get());
        this.arbiter.addPlayer(player2.get());

        this.player1.get().setCallback(this);
        this.player2.get().setCallback(this);

        this.arbiter.start(this);
        this.score1.set(2);
        this.score2.set(2);

        this.adapter.start();
    }

    private void addDefaultPlayersIfEmpty() {
        if (player1.get() == null || player2.get() == null) {

            final Player p1 = new UserPlayer();
            p1.setStoneColor(Stone.WHITE);

            final Player p2 = new RandomPlayer();
            p1.setStoneColor(Stone.BLACK);

            this.player1.set(p1);
            this.player2.set(p2);
        }
    }

    @MainThread
    @Override
    public void setCurrentPlayer(@NonNull final Player player) {
        main.post(new Runnable() {
            @Override
            public void run() {
                arbiter.startTimer(player);
                current.set(player);
                adapter.setCurrentPlayer(player, Game.this);
            }
        });
    }


    @Override
    public void submitMove(@NonNull final Player player, @NonNull final Move move) {
        main.post(new Runnable() {
            @Override
            public void run() {
                final List<Stone> flipped = arbiter.onMoveReceived(player, move.toString());
                if (flipped.size() > 0) {

                    if (player.getStoneColor() == Stone.WHITE) {
                        score1.set(score1.get() + 1 + flipped.size());
                        score2.set(score2.get() - flipped.size());
                    } else {
                        score1.set(score1.get() - flipped.size());
                        score2.set(score2.get() + 1 + flipped.size());
                    }

                    if (matchReference != null) {
                        matchReference.child("history").push().setValue(new History(player, move, score1.get(), score2.get()));
                    }

                    current.set(null);
                    adapter.update(move, player.getStoneColor());
                    arbiter.notifyNextPlayer(player);
                }
            }
        });
    }

    @Override
    public void onMoveRejected(@Nullable final Player player) {
        main.post(new Runnable() {
            @Override
            public void run() {
                rejected.set(player);
                main.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rejected.set(null);
                    }
                }, 10L);
            }
        });
    }


    @Override
    public void onGameFinished(final int score) {
        main.post(new Runnable() {
            @Override
            public void run() {
                current.set(null);
                if(score == 0){
                    // draw
                    winner.set(new DrawPlayer());
                } else {
                    // we have a winner
                    winner.set(score <= Stone.WHITE ? player1.get() : player2.get());
                }
                matchReference.child("result").setValue(score);
            }
        });
    }

    @Override
    public void notifyNextPlayer(@NonNull final Player player, @NonNull final String board) {
        background.schedule(new Runnable() {
            @Override
            public void run() {
                player.yourTurn(board);
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void notifyMoveRejected(@NonNull final Player player, @NonNull final String board) {
        background.schedule(new Runnable() {
            @Override
            public void run() {
                player.onMoveRejected(board);
            }
        }, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void notifyPlayerGameFinished(@NonNull final Player player, final int yourScore,
                                         final int opponentScore) {
        main.post(new Runnable() {
            @Override
            public void run() {
                player.onGameFinished(yourScore, opponentScore);
            }
        });
    }

    public void clear() {
        this.arbiter.clear();
        this.player1.set(null);
        this.player2.set(null);

        this.score1.set(0);
        this.score2.set(0);
        this.winner.set(null);
        this.current.set(null);
        this.rejected.set(null);
    }

    public void setWhitePlayer(Player player) {
        player.setStoneColor(Stone.WHITE);
        this.player1.set(player);
    }
    public void setBlackPlayer(Player player) {
        player.setStoneColor(Stone.BLACK);
        this.player2.set(player);
    }
}
