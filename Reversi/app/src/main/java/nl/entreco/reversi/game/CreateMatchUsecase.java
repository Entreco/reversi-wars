package nl.entreco.reversi.game;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.api.MatchData;
import nl.entreco.reversi.model.Player;

public class CreateMatchUsecase {

    @NonNull private final DatabaseReference matchRef;

    public CreateMatchUsecase(@NonNull final FirebaseDatabase database) {
        matchRef = database.getReference("matches");
    }

    @NonNull
    public DatabaseReference createRemoteMatch(@NonNull final Player white, @NonNull final Player black) {
        final DatabaseReference newMatch = matchRef.push();
        final MatchData matchData = new MatchData(white, black);
        newMatch.setValue(matchData);
        return newMatch;
    }
}
