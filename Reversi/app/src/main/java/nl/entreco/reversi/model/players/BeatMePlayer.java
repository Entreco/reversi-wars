package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

public class BeatMePlayer extends BasePlayer {

    @Override
    public void yourTurn(@NonNull String board) {
        super.yourTurn(board);
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
        return "BeatMe";
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
