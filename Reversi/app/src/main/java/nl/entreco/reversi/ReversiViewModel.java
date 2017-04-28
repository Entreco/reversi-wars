package nl.entreco.reversi;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import nl.entreco.reversi.model.Referee;
import nl.entreco.reversi.model.Stone;

public class ReversiViewModel {

    public final ObservableField<Referee> referee;

    public interface Clicker {
        void onClick();
    }

    ReversiViewModel(@NonNull final Referee ref) {

//        ref.addPlayer(new Player() {
//            @Override
//            public void yourTurn() {
//                int row = (int) (Math.random() * 8);
//                int col = (int) (Math.random() * 8);
//                doMove(String.format("[%s, $s]", row, col));
//            }
//
//            @Override
//            public void doMove(String s) {
//                ref.onMoveReceived(this, s);
//            }
//
//            @Override
//            public void onMoveRejected() {
//                yourTurn();
//            }
//        });
//        ref.addPlayer(new Player() {
//            @Override
//            public void yourTurn() {
//                int row = (int) (Math.random() * 8);
//                int col = (int) (Math.random() * 8);
//                doMove(String.format("[%s, $s]", row, col));
//            }
//
//            @Override
//            public void doMove(String s) {
////                ref.onMoveReceived(this, s);
//            }
//
//            @Override
//            public void onMoveRejected() {
//                yourTurn();
//            }
//        });
//        ref.startMatch();
        referee = new ObservableField<>(ref);
    }

    @BindingAdapter("stone")
    public static void setStoneDrawable(@NonNull final ImageView view, Stone stone) {
        if(stone == null){
            view.setImageDrawable(null);
        } else {
            switch (stone.value()) {
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
