package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class DrawPlayer implements Player {
    @Override
    public void setCallback(@NonNull GameCallback gameCallback) {

    }

    @NonNull
    @Override
    public String getName() {
        return "Draw";
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
        return 0;
    }

    @Override
    public void setStoneColor(@Stone.Color int stoneColor) {

    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
