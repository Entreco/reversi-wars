package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

public interface Player {
    void yourTurn(@NonNull final String board);

    void onMoveRejected(@NonNull final String board);

    @Stone.Color int getStoneColor();

    boolean isHuman();

}
