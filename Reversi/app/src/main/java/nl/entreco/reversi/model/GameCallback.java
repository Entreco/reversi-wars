package nl.entreco.reversi.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface GameCallback {

    void setCurrentPlayer(@NonNull final Player player);

    void submitMove(@NonNull final Player player,@NonNull final Move move);

    void onMoveRejected(@Nullable final Player player);

    void onGameFinished(final int score);

    void notifyNextPlayer(@NonNull final Player player, @NonNull final String board);

    void notifyMoveRejected(@NonNull final Player player, @NonNull final String board);

    void notifyPlayerGameFinished(@NonNull final Player player, final int yourScore, final int opponentScore);
}
