package nl.entreco.reversibot;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class RejectListener implements ValueEventListener {
    private final Callback callback;

    public interface Callback {
        void onMoveRejected(@NonNull final String move);
    }
    public RejectListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null && dataSnapshot.getValue().equals("rejected")) {
            callback.onMoveRejected(dataSnapshot.getValue().toString());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
