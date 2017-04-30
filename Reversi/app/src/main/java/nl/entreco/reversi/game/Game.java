package nl.entreco.reversi.game;

import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.players.BasePlayer;

public class Game implements GameCallback {

    @NonNull public final ObservableBoolean inProgress;
    @NonNull public final ObservableField<BasePlayer> player1;
    @NonNull public final ObservableInt score1;
    @NonNull public final ObservableField<BasePlayer> player2;
    @NonNull public final ObservableInt score2;
    @NonNull public final ObservableField<Player> current;
    @NonNull private final BoardAdapter adapter;
    @NonNull private final Arbiter arbiter;

    public Game(@NonNull final BoardAdapter adapter, @NonNull final Arbiter arbiter,
                @NonNull final BasePlayer p1, @NonNull final BasePlayer p2) {

        this.adapter = adapter;
        this.arbiter = arbiter;

        this.player1 = new ObservableField<>(p1);
        this.player2 = new ObservableField<>(p2);
        this.score1 = new ObservableInt(0);
        this.score2 = new ObservableInt(0);
        this.current = new ObservableField<>();
        this.inProgress = new ObservableBoolean(false);

        this.arbiter.addPlayer(p1);
        this.arbiter.addPlayer(p2);
    }

    public void startGame() {
        this.player1.get().setCallback(this);
        this.player2.get().setCallback(this);

        this.arbiter.restart();
        this.score1.set(2);
        this.score2.set(2);

        this.inProgress.set(true);
        this.adapter.start();
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
    public void currentPlayer(@NonNull Player player) {
        current.set(player);
        adapter.setCurrentPlayer(player, this);
    }


    @BindingAdapter("score")
    public static void updateScore(@NonNull final TextView view, int score) {
        view.setText(String.valueOf(score));
    }

    @BindingAdapter({"player", "current"})
    public static void setPlayerHere(@NonNull final View view, @Stone.Color int stone,
                                     @Nullable final Player player) {
        if (player != null) {
            if (stone == player.getStoneColor()) {
                view.animate().scaleX(1F).scaleY(1F).alpha(1F).start();
            } else {
                view.animate().scaleX(.8F).scaleY(.8F).alpha(.5F).start();
            }
        }
    }
}
