package nl.entreco.reversi.model.players;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class NotStartedPlayer implements Player {
    @Override
    public void yourTurn() {

    }

    @Override
    public void onMoveRejected() {

    }

    @Override
    public int getStoneColor() {
        return Stone.EMPTY;
    }
}
