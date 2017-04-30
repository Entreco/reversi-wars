package nl.entreco.reversi;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Stone;

public class ReversiViewModel {

    public final ObservableField<Game> game;

    public ReversiViewModel(@NonNull final Game reversiGame) {
        this.game = new ObservableField<>(reversiGame);
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
