package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Move;

public class RandomPlayer extends BasePlayer {

    @Override
    void onRejected(@NonNull String board) {
        yourTurn(board);
    }

    @Override
    void handleTurn(@NonNull String board) {
        int row = (int) (Math.random() * 8);
        int col = (int) (Math.random() * 8);
        submitMove(new Move(row, col));
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
