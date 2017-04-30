package nl.entreco.reversi;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class Bindings {

    @BindingAdapter("score")
    public static void updateScore(@NonNull final TextView view, int score) {
        view.setText(String.valueOf(score));
    }

    @BindingAdapter({"player", "current", "rejectAnimation"})
    public static void doPlayerRelatedAnimations(@NonNull final View view, @Stone.Color int stone,
                                                 @Nullable final Player player,
                                                 @Nullable final Player rejected) {
        if (rejected != null) {
            AnimationUtils.reject(view, stone, rejected);
        }

        if (player != null) {
            AnimationUtils.current(view, stone, player);
        }
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
}
