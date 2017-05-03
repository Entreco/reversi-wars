package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Move;

public class RandomPlayer extends BasePlayer {

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

    @NonNull
    @Override
    public String getName() {
        return "RandomBot";
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
