package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class BeatMeBot implements MatchListener.Callback, TurnListener.Callback, RejectListener.Callback {

    public String name = "Remco";

    @NonNull private final TurnListener turnListener;
    @NonNull private final RejectListener rejectListener;
    @NonNull private final DatabaseReference matchReference;

    @Nullable private DatabaseReference moveReference;

    BeatMeBot(DatabaseReference playerReference, String playerUid) {
        matchReference = playerReference.child(playerUid).child("matches");
        matchReference.addChildEventListener(new MatchListener(this));
        turnListener = new TurnListener(this);
        rejectListener = new RejectListener(this);
    }

    @Override
    public void onJoinedMatch(@NonNull String uuid) {
        Log.i(name, "onJoinedMatch");
        // Dummy Response
        moveReference = matchReference.child(uuid);

        moveReference.child("board").addValueEventListener(turnListener);
        moveReference.child("try").addValueEventListener(rejectListener);
    }

    @Override
    public void onYourTurn(@NonNull String board) {
        moveReference.child("try").setValue("[0,2]");
    }


    @Override
    public void onMoveRejected(@NonNull String move) {
        moveReference.child("try").setValue("[5,4]");
    }
}
