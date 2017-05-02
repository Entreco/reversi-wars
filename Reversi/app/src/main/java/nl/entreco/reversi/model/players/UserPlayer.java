package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

public class UserPlayer extends BasePlayer {

    public UserPlayer() {
        super();
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

    @NonNull
    @Override
    public String getName() {
        return "Human";
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
