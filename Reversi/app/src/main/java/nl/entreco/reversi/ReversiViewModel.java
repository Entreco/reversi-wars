package nl.entreco.reversi;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.entreco.reversi.game.BoardAdapter;
import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class ReversiViewModel {

    public final ObservableField<Player> player1;
    public final ObservableField<Player> player2;

    public ReversiViewModel(Player player1, Player player2) {
        this.player1 = new ObservableField<>(player1);
        this.player2 = new ObservableField<>(player2);
    }

    public void start(@NonNull final Arbiter arbiter, @NonNull final BoardAdapter adapter){
        arbiter.restart();
        arbiter.addPlayer(player1.get());
        arbiter.addPlayer(player2.get());
        arbiter.startMatch();
        adapter.notifyDataSetChanged();
    }

    @BindingAdapter("stone")
    public static void setStoneDrawable(@NonNull final ImageView view, Stone stone) {
        if(stone == null){
            view.setImageDrawable(null);
        } else {
            switch (stone.color()) {
                case Stone.BLACK:
                    view.setImageResource(R.drawable.ic_stone_black);
                    break;
                case Stone.WHITE:
                    view.setImageResource(R.drawable.ic_stone_white);
                    break;
                case Stone.EMPTY:
                    view.setImageDrawable(null);
            }
        }
    }

    @BindingAdapter("player")
    public static void setPlayerName(@NonNull final TextView view, @Nullable final Player player){
        view.setText(player == null ? "" : String.valueOf(player.getStoneColor()));
    }

    @BindingAdapter({"player", "current"})
    public static void setPlayerHere(@NonNull final View view, @Stone.Color int stone, @Nullable final Player player){
        if(player != null) {
            if (stone == player.getStoneColor()) {
                view.animate().scaleX(1.1F).scaleY(1.1F).alpha(1F).start();
            } else {
                view.animate().scaleX(.9F).scaleY(.9F).alpha(.8F).start();
            }
        }
    }

}
