package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

public class UserPlayer extends BasePlayer {

    public UserPlayer() {
        super();
    }

    @Override
    void onRejected(@NonNull String board) {

    }

    @Override
    void handleTurn(@NonNull String board) {

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
