package nl.entreco.reversibot;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public abstract class FirebaseBot
        implements PingListener.Callback, MatchListener.Callback, TurnListener.Callback,
        RejectListener.Callback, FinishedListener.Callback {

    @Nullable public String name;

    @Nullable private FinishedListener finishedListener;
    @Nullable private TurnListener turnListener;
    @Nullable private RejectListener rejectListener;
    @Nullable private DatabaseReference playerReference;
    @Nullable private DatabaseReference matchReference;
    @Nullable private DatabaseReference moveReference;
    @Nullable private PingListener pingListener;
    @Nullable private String board;

    void init(DatabaseReference reference, String playerUid, String botName) {
        Log.i("FirebaseBot", "init playerUid:" + playerUid + " botName:" + botName);
        name = botName;
        playerReference = reference;

        matchReference = playerReference.child("matches");
        matchReference.addChildEventListener(new MatchListener(this));

        pingListener = new PingListener(this);
        playerReference.child("ping").addValueEventListener(pingListener);

        turnListener = new TurnListener(this);
        rejectListener = new RejectListener(this);
        finishedListener = new FinishedListener(this);
    }

    @Override
    public final void onJoinedMatch(@NonNull String matchUuid, final int stoneColor) {
        Log.i("FirebaseBot", "onJoinedMatch:" + matchUuid + " stoneColor:" + stoneColor);
        // Dummy Response
        moveReference = matchReference.child(matchUuid);
        playerReference.child("results").addValueEventListener(finishedListener);

        moveReference.child("board").addValueEventListener(turnListener);
        moveReference.child("try").addValueEventListener(rejectListener);
        moveReference.child("results").addValueEventListener(finishedListener);
        onStartMatch(matchUuid, stoneColor);
    }

    @CallSuper
    @Override
    public void onMatchFinished() {
        if (playerReference != null) {
            playerReference.removeEventListener(pingListener);
            playerReference.child("results").removeEventListener(finishedListener);
        }
    }

    @Override
    public final void onPing(@NonNull DatabaseReference reference) {
        onPingCheck(reference);
    }

    @Override
    public final void onYourTurn(@NonNull String board) {
        Log.i("FirebaseBot", "onYourTurn:" + board);
        this.board = board;
        onCalculateMove(board);
    }

    @Override
    public final void onMoveRejected(@NonNull String move) {
        Log.i("FirebaseBot", "onMoveRejected:" + move);
        onRejected(move, board);
    }

    protected final void submitMove(String move) {
        Log.i("FirebaseBot", "submitMove:" + move);
        moveReference.child("try").setValue(move);
    }

    public abstract void onPingCheck(@NonNull final DatabaseReference ref);

    public abstract void onStartMatch(@NonNull final String matchUid, final int yourStoneColor);

    public abstract void onRejected(@NonNull final String move, @NonNull final String board);

    public abstract void onCalculateMove(@NonNull final String board);
}
