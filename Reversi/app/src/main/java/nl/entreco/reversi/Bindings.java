package nl.entreco.reversi;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class Bindings {

    @BindingAdapter("score")
    public static void updateScore(@NonNull final TextView view, int score) {
        view.setText(String.valueOf(score));
    }

    @BindingAdapter({"game", "winner", "behaviour"})
    public static void setCollapsedState(@NonNull final View view,
                                         final Game game,
                                         final Player winner,
                                         final BottomSheetBehavior behavior) {
        final boolean hasPlayers = game != null && game.player1 != null && game.player2 != null;
        behavior.setState(
                hasPlayers ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior
                        .STATE_EXPANDED);
    }

    @BindingAdapter("winner")
    public static void doWinnerAnimation(@NonNull final View view, @Nullable final Player winner){
        if(winner == null){
            view.animate().alpha(0f).scaleX(0f).scaleY(0f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }).start();
        } else {
            view.animate().alpha(1F).scaleX(1f).scaleY(1f).withStartAction(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                }
            }).start();
        }
    }

    @BindingAdapter({"player1", "player2"})
    public static void addPlayersToGame(@NonNull final TextView view,
                                        @Nullable final Player player1,
                                        @Nullable final Player player2) {
        if (player1 == null) {
            view.setText(R.string.select_player_1);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_stone_white, 0);
        } else if (player2 == null) {
            view.setText(R.string.select_player_2);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_stone_black, 0);
        } else {
            view.setText("");
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
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

    @BindingAdapter("lastMove")
    public static void doShowLastMoveOnboard(@NonNull final RecyclerView view, final int move){
        if(move > 0){
            view.getLayoutManager().findViewByPosition(move).setSelected(true);
        }
    }

    @BindingAdapter("stone")
    public static void setStoneDrawable(@NonNull final ImageView view, Stone stone) {
        if (stone == null) {
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
