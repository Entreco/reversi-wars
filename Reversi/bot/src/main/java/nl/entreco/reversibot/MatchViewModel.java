package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

public class MatchViewModel implements RegisterPlayerUsecase.Callback {
    private final RegisterPlayerUsecase registerPlayerUsecase;

    MatchViewModel(RegisterPlayerUsecase registerPlayerUsecase) {
        this.registerPlayerUsecase = registerPlayerUsecase;
    }

    void registerForMatches() {
        this.registerPlayerUsecase.register(this);
    }

    void unregisterForMatches() {
        this.registerPlayerUsecase.unregister();
    }

    @Override
    public void onPlayerAdded(@NonNull String matchId) {
        Log.i("MATCHES", "onPlayerAdded:" + matchId);
    }
}
