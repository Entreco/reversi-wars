package nl.entreco.reversibot;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class TurnListener implements ValueEventListener {
    private final Callback callback;

    public interface Callback {
        void onYourTurn(@NonNull final String board);
    }
    public TurnListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            callback.onYourTurn(dataSnapshot.getValue().toString());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
