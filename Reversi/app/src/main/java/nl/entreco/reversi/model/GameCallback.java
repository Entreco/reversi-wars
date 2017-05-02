package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

public interface GameCallback {

    void setCurrentPlayer(@NonNull final Player player);

    void submitMove(@NonNull final Player player,@NonNull final Move move);

    void onMoveRejected(@NonNull final Player player);

    void onGameFinished(final int score);
}
