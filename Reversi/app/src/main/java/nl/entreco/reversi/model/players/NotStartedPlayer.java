package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class NotStartedPlayer implements Player {

    @Override
    public void setCallback(@NonNull GameCallback gameCallback) {

    }

    @Override
    public void onJoinedGame(@NonNull String gameUuid) {

    }

    @Override
    public void yourTurn(@NonNull String board) {

    }

    @Override
    public void onMoveRejected(@NonNull String board) {

    }

    @Override
    public void onGameFinished(int yourScore, int opponentScore) {

    }

    @Override
    public int getStoneColor() {
        return Stone.EMPTY;
    }

    @Override
    public void setStoneColor(@Stone.Color int stoneColor) {

    }

    @NonNull
    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
