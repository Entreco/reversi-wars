package nl.entreco.reversi;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.model.Player;

class MatchReference {

    @NonNull final DatabaseReference matchReference;

    MatchReference() {
        matchReference = FirebaseDatabase.getInstance().getReference();
    }

    public String start(Player white, Player black) {
        final DatabaseReference match = matchReference.child("matches").push();
        match.setValue(new Match(white, black));
        return match.getKey();
    }
}
