package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

import java.util.List;

public interface Arbiter {
    void addPlayer(@NonNull final Player player);

    List<Player> getPlayers();

    void startMatch();

    List<Stone> onMoveReceived(@NonNull final Player player, String move);

    void onTimedOut(@NonNull final Player player);

    @NonNull
    Board getBoard();

    @NonNull
    Player getCurrentPlayer();

    void restart();
}
