package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

class MatchListener implements ChildEventListener {
    private final Callback callback;

    interface Callback{
        void onJoinedMatch(@NonNull final String uuid);
    }

    MatchListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public final void onChildAdded(DataSnapshot dataSnapshot, String s) {
        final String matchUuid = dataSnapshot.getKey();
        Log.i("FirebaseBot", "MatchListener onChildAdded:" + matchUuid);
        this.callback.onJoinedMatch(matchUuid);
    }

    @Override
    public final void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.i("FirebaseBot", "MatchListener onChildChanged:" + dataSnapshot);
    }

    @Override
    public final void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i("FirebaseBot", "MatchListener onChildRemoved:" + dataSnapshot);
    }

    @Override
    public final void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.i("FirebaseBot", "MatchListener onChildMoved:" + dataSnapshot);
    }

    @Override
    public final void onCancelled(DatabaseError databaseError) {
        Log.i("FirebaseBot", "MatchListener onCancelled:" + databaseError);
    }
}
