package nl.entreco.reversi;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Player;

public interface PlayerSelectedListener {
    void onPlayerSelected(@NonNull final Player player);
}
