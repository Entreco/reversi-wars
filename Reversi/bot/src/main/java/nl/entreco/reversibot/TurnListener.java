package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class TurnListener implements ValueEventListener {
    private final Callback callback;

    interface Callback {
        void onYourTurn(@NonNull final String board);
    }
    TurnListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public final void onDataChange(DataSnapshot dataSnapshot) {
        Log.i("FirebaseBot", "TurnListener onDataChange:" + dataSnapshot.getValue());
        if(dataSnapshot.getValue() != null) {
            callback.onYourTurn(dataSnapshot.getValue().toString());
        }
    }

    @Override
    public final void onCancelled(DatabaseError databaseError) {
        Log.i("FirebaseBot", "TurnListener onCancelled:" + databaseError);
    }
}
