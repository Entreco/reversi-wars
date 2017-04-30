package nl.entreco.reversi.model.players;

import android.os.Handler;
import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Stone;

public class RandomPlayer extends BasePlayer {

    private final Handler handler;

    public RandomPlayer(@Stone.Color int stoneColor) {
        super(stoneColor);
        handler = new Handler();
    }

    @Override
    public void yourTurn(@NonNull final String board) {
        super.yourTurn(board);
        int row = (int) (Math.random() * 8);
        int col = (int) (Math.random() * 8);
        submitMove(new Move(row, col));
    }

    @Override
    public void onMoveRejected(@NonNull final String board) {
        super.onMoveRejected(board);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                yourTurn(board);
            }
        }, 10);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    public Handler getHandler() {
        return handler;
    }
}
