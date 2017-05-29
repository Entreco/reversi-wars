package nl.entreco.reversibot;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

class PingListener implements ValueEventListener {

    @NonNull private final Callback callback;

    interface Callback{
        void onPing(@NonNull final DatabaseReference reference);
    }

    PingListener(@NonNull final Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final String value = dataSnapshot.getValue(String.class);
        if(dataSnapshot.exists() && "Yo dude, you there?".equals(value)) {
            callback.onPing(dataSnapshot.getRef());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
