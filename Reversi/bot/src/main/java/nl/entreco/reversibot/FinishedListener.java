package nl.entreco.reversibot;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class FinishedListener implements ValueEventListener {

    @NonNull private final Callback callback;

    interface Callback{
        void onMatchFinished();
    }

    FinishedListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        callback.onMatchFinished();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
