package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

public interface GameCallback {
    void submitMove(@NonNull final Player player,@NonNull final Move move);

    void setCurrentPlayer(@NonNull final Player player);

    void onMoveRejected(@NonNull final Player player);
}
