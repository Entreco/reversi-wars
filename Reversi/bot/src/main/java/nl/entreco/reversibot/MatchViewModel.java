package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

public class MatchViewModel implements FetchMatchesUsecase.Callback {
    private final FetchMatchesUsecase fetchMatchesUsecase;

    MatchViewModel(FetchMatchesUsecase fetchMatchesUsecase) {
        this.fetchMatchesUsecase = fetchMatchesUsecase;
    }

    void registerForMatches() {
        this.fetchMatchesUsecase.register(this);
    }

    void unregisterForMatches() {
        this.fetchMatchesUsecase.unregister();
    }

    @Override
    public void onMatchAdded(@NonNull String matchId) {
        Log.i("MATCHES", "onMatchAdded:" + matchId);
    }
}
