package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import nl.entreco.reversi.api.PlayerData;

public class RemotePlayer extends BasePlayer {

    @NonNull private final PlayerData playerData;

    public RemotePlayer(@NonNull final PlayerData playerData) {
        this.playerData = playerData;
    }

    @NonNull
    @Override
    public String getName() {
        return playerData.name;
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
