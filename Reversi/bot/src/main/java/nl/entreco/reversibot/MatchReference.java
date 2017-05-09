package nl.entreco.reversibot;

import com.google.firebase.database.FirebaseDatabase;

public class MatchReference {
    MatchReference() {
        FirebaseDatabase.getInstance().getReference("players");
    }
}
