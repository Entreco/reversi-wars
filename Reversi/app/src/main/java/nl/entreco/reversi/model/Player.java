package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

public interface Player {

    void setCallback(@NonNull GameCallback gameCallback);

    @NonNull
    String getName();

    void onJoinedGame(@NonNull final String gameUuid);

    void yourTurn(@NonNull final String board);

    void onMoveRejected(@NonNull final String board);

    void onGameFinished(final int yourScore, final int opponentScore);

    @Stone.Color int getStoneColor();

    void setStoneColor(@Stone.Color int stoneColor);

    boolean isHuman();
}
