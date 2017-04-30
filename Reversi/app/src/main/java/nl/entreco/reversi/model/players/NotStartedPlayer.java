package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class NotStartedPlayer implements Player {
    @Override
    public void yourTurn(@NonNull String board) {

    }

    @Override
    public void onMoveRejected(@NonNull String board) {

    }

    @Override
    public int getStoneColor() {
        return Stone.EMPTY;
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
