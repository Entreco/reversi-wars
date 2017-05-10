package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.entreco.reversi.api.PlayerData;
import nl.entreco.reversi.model.Move;

public class RemotePlayer extends BasePlayer {

    private static final int POINTS_WIN = 3;
    private static final int POINTS_DRAW = 1;
    private static final int POINTS_LOSS = 0;
    @NonNull private final DatabaseReference playersReference;
    @NonNull private final PlayerData playerData;
    @NonNull private final String remoteUuid;
    @NonNull private final Gson gson;

    @Nullable private DatabaseReference matchReference;

    public RemotePlayer(@NonNull final DatabaseReference playersReference, @NonNull final PlayerData playerData, @NonNull final String remoteUuid) {
        this.playerData = playerData;
        this.remoteUuid = remoteUuid;
        this.playersReference = playersReference.child(remoteUuid);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public void onJoinedGame(@NonNull String gameUuid) {
        super.onJoinedGame(gameUuid);
        // Create new Node to hold our moves
        this.matchReference = this.playersReference.child("matches").push();
        this.matchReference.child("stoneColor").setValue(getStoneColor());
        this.matchReference.child("matchId").setValue(gameUuid);
    }

    @Override
    public void onGameFinished(int yourScore, int opponentScore) {
        super.onGameFinished(yourScore, opponentScore);
        // Notify
        String matchKey = matchReference.getKey();
        int points = getPointsForScores(yourScore, opponentScore);
        this.playersReference.child("results").push().setValue(new ReversiResult(matchKey, yourScore, opponentScore, points));
    }

    private int getPointsForScores(int yourScore, int opponentScore) {
        return yourScore > opponentScore ? POINTS_WIN : (yourScore == opponentScore) ? POINTS_DRAW : POINTS_LOSS;
    }

    @Override
    public void yourTurn(@NonNull String board) {
        super.yourTurn(board);
        // Push board to Firebase match
        if(matchReference != null) {
            this.matchReference.child("board").setValue(board);
            this.matchReference.child("try").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null && !dataSnapshot.getValue().equals("rejected")) {
                        final Move move = parseMove(dataSnapshot.getValue().toString());
                        submitMove(move);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private Move parseMove(String moveData) {
        return gson.fromJson(moveData, Move.class);
    }

    @Override
    public void onMoveRejected(@NonNull String board) {
        super.onMoveRejected(board);
        this.matchReference.child("try").setValue("rejected");
    }

    @NonNull
    @Override
    public String getName() {
        return String.format("%s [%s]", playerData.name, obfuscate(remoteUuid, 6));
    }

    private String obfuscate(@NonNull final String remoteUuid, final int maxChars) {
        if (remoteUuid.length() < maxChars) {
            return remoteUuid;
        }
        return remoteUuid.substring(0, maxChars / 2) + remoteUuid.substring(remoteUuid.length() - maxChars / 2);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @IgnoreExtraProperties
    public class ReversiResult {
        public String match;
        public int me;
        public int opponent;
        public int points;

        public ReversiResult(String matchKey, int yourScore, int opponentScore, int points) {
            this.match = matchKey;
            this.me = yourScore;
            this.opponent = opponentScore;
            this.points = points;
        }
    }
}
