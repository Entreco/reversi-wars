package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class FetchMatchesUsecase {

    @Nullable private Callback callback;

    interface Callback{
        void onMatchAdded(@NonNull final String matchId);
    }

    private final DatabaseReference matches;

    FetchMatchesUsecase() {
        matches = FirebaseDatabase.getInstance().getReference("players");
    }

    void register(@NonNull final Callback callback){
        this.callback = callback;
        this.matches.push().setValue(new BeatMeBot());
    }
    void unregister(){
        this.callback = null;
    }
}
