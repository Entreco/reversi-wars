package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class RegisterPlayerUsecase {

    @Nullable private Callback callback;

    interface Callback{
        void onPlayerAdded(@NonNull final String matchId);
    }

    private final DatabaseReference players;

    RegisterPlayerUsecase() {
        players = FirebaseDatabase.getInstance().getReference("players");
    }

    void register(@NonNull final Callback callback){
        this.callback = callback;
        final DatabaseReference push = this.players.push();
        final String playerUid = push.getKey();
        push.setValue(new BeatMeBot(players, playerUid));
        this.callback.onPlayerAdded(playerUid);
    }
    void unregister(){
        this.callback = null;
    }
}
