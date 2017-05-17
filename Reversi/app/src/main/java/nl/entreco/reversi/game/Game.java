package nl.entreco.reversi.game;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;
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

    public Game(@NonNull final BoardAdapter adapter, @NonNull final Arbiter arbiter) {

        this.adapter = adapter;
        this.arbiter = arbiter;

        this.player1 = new ObservableField<>();
        this.player2 = new ObservableField<>();

        this.score1 = new ObservableInt(0);
        this.score2 = new ObservableInt(0);
        this.winner = new ObservableField<>();
        this.current = new ObservableField<>();
        this.rejected = new ObservableField<>();
    }

    public void startGame(@NonNull final String uuid) {

        addDefaultPlayersIfEmpty();
        this.winner.set(null);

        this.player1.get().onJoinedGame(uuid);
        this.player2.get().onJoinedGame(uuid);

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

    @Override
    public void setCurrentPlayer(@NonNull Player player) {
        arbiter.startTimer(player);
        current.set(player);
        adapter.setCurrentPlayer(player, this);
    }


    @Override
    public void submitMove(@NonNull final Player player, @NonNull final Move move) {
        final List<Stone> flipped = arbiter.onMoveReceived(player, move.toString());
        if (flipped.size() > 0) {

            if (player.getStoneColor() == Stone.WHITE) {
                score1.set(score1.get() + 1 + flipped.size());
                score2.set(score2.get() - flipped.size());
            } else {
                score1.set(score1.get() - flipped.size());
                score2.set(score2.get() + 1 + flipped.size());
            }

            adapter.update(move, player.getStoneColor());
            arbiter.notifyNextPlayer(player);
        }
    }

    @Override
    public void onMoveRejected(@Nullable final Player player) {
        rejected.set(player);
    }


    @Override
    public void onGameFinished(int score) {
        current.set(null);
        winner.set(score <= Stone.WHITE ? player1.get() : player2.get());
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
