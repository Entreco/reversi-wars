package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class PingListener implements ValueEventListener {
    @NonNull private final String botName;

    PingListener(@NonNull String botName) {
        this.botName = botName;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final String value = dataSnapshot.getValue(String.class);
        if(dataSnapshot.exists() && !botName.equals(value)) {
            dataSnapshot.getRef().setValue(botName);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
