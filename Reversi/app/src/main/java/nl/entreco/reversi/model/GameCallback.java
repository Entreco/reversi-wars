package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

public interface GameCallback {
    void submitMove(@NonNull final Player player,@NonNull final Move move);

    void currentPlayer(@NonNull final Player player);
}
