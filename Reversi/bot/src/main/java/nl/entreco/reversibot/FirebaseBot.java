package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public abstract class FirebaseBot implements MatchListener.Callback, TurnListener.Callback, RejectListener.Callback {

    @Nullable public String name;

    @Nullable private TurnListener turnListener;
    @Nullable private RejectListener rejectListener;
    @Nullable private DatabaseReference matchReference;
    @Nullable private DatabaseReference moveReference;

    void init(DatabaseReference playerReference, String playerUid, String botName) {
        Log.i("FirebaseBot", "init playerUid:" + playerUid + " botName:" + botName);
        name = botName;
        matchReference = playerReference.child("matches");
        matchReference.addChildEventListener(new MatchListener(this));
        turnListener = new TurnListener(this);
        rejectListener = new RejectListener(this);
    }

    @Override
    public final void onJoinedMatch(@NonNull String uuid) {
        Log.i("FirebaseBot", "onJoinedMatch:" + uuid);
        // Dummy Response
        moveReference = matchReference.child(uuid);

        moveReference.child("board").addValueEventListener(turnListener);
        moveReference.child("try").addValueEventListener(rejectListener);
        onStartMatch(uuid, 0);
    }

    @Override
    public final void onYourTurn(@NonNull String board) {
        Log.i("FirebaseBot", "onYourTurn:" + board);
        onCalculateMove(board);
    }

    @Override
    public final void onMoveRejected(@NonNull String move) {
        Log.i("FirebaseBot", "onMoveRejected:" + move);
        onRejected("TODO -> Pass Current Board");
    }

    protected final void submitMove(String move) {
        Log.i("FirebaseBot", "submitMove:" + move);
        moveReference.child("try").setValue(move);
    }

    public abstract void onStartMatch(@NonNull final String matchUid, final int yourStoneColor);
    public abstract void onRejected(@NonNull final String board);
    public abstract void onCalculateMove(@NonNull final String board);
}
