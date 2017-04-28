package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

import java.util.List;

public interface Arbiter {
    void addPlayer(@NonNull final Player player);

    List<Player> getPlayers();

    void startMatch();

    void onMoveReceived(@NonNull final Player player, String move);

    void onTimedOut(@NonNull final Player player);
}
