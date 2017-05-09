package nl.entreco.reversi.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.api.MatchData;
import nl.entreco.reversi.model.Player;

public class CreateMatchUsecase {

    public interface Callback {
        void onMatchCreated(@NonNull final MatchData matchData, @NonNull final String matchUuid);
    }

    @NonNull private final DatabaseReference matchRef;

    public CreateMatchUsecase(@NonNull final FirebaseDatabase database) {
        matchRef = database.getReference("matches");
    }

    public void registerCallback(@NonNull final Callback callback, @NonNull final Player white, @NonNull final Player black) {
        final DatabaseReference newMatch = matchRef.push();
        final MatchData matchData = new MatchData(white, black);
        newMatch.setValue(matchData);
        callback.onMatchCreated(matchData, newMatch.getKey());
    }
}
