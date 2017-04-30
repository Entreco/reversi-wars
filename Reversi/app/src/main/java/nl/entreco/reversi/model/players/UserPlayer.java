package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Stone;

public class UserPlayer extends BasePlayer {

    public UserPlayer(@Stone.Color int stoneColor) {
        super(stoneColor);
    }

    @Override
    public void yourTurn(@NonNull String board) {
        super.yourTurn(board);
        // Nothing
    }

    @Override
    public void onMoveRejected(@NonNull String board) {
        super.onMoveRejected(board);
        // Let user Click again...
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
