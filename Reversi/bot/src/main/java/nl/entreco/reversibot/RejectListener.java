package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class RejectListener implements ValueEventListener {
    private final Callback callback;

    interface Callback {
        void onMoveRejected(@NonNull final String move);
    }

    RejectListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public final void onDataChange(DataSnapshot dataSnapshot) {
        Log.i("FirebaseBot", "RejectListener onDataChange:" + dataSnapshot.getValue());
        if(dataSnapshot.getValue() != null && dataSnapshot.getValue().equals("rejected")) {
            callback.onMoveRejected(dataSnapshot.getValue().toString());
        }
    }

    @Override
    public final void onCancelled(DatabaseError databaseError) {
        Log.i("FirebaseBot", "RejectListener onCancelled:" + databaseError);
    }
}
