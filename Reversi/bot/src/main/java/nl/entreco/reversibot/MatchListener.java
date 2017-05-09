package nl.entreco.reversibot;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MatchListener implements ChildEventListener {
    private final Callback callback;

    public interface Callback{
        void onJoinedMatch(@NonNull final String uuid);
    }

    public MatchListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        final String matchUuid = dataSnapshot.getKey();
        this.callback.onJoinedMatch(matchUuid);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
