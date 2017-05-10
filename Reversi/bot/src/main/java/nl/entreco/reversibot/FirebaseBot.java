package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class FirebaseBot implements MatchListener.Callback, TurnListener.Callback, RejectListener.Callback {

    public String name = "Remco";

    @NonNull private final TurnListener turnListener;
    @NonNull private final RejectListener rejectListener;
    @NonNull private final DatabaseReference matchReference;

    @Nullable private DatabaseReference moveReference;

    FirebaseBot(DatabaseReference playerReference, String playerUid) {
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
        int row = (int) (Math.random() * 8);
        int col = (int) (Math.random() * 8);
        moveReference.child("try").setValue(String.format("[%s,%s]", row, col));
    }


    @Override
    public void onMoveRejected(@NonNull String move) {
        onYourTurn("");
    }
}
