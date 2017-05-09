package nl.entreco.reversi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.api.PlayerData;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.players.BeatMePlayer;
import nl.entreco.reversi.model.players.RandomPlayer;
import nl.entreco.reversi.model.players.RemotePlayer;
import nl.entreco.reversi.model.players.SpiralPlayer;
import nl.entreco.reversi.model.players.UserPlayer;

public class FetchPlayersUsecase implements ChildEventListener {

    @NonNull private final DatabaseReference playersRef;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Nullable Callback callback;

    public interface Callback {

        void onPlayerRetrieved(@NonNull final Player player);

        void onPlayerRemoved(@NonNull final String name);
    }

    public FetchPlayersUsecase(@NonNull final FirebaseDatabase database) {
        playersRef = database.getReference("players");
    }

    public void registerCallback(@NonNull Callback callback) {
        this.callback = callback;

        foundPlayer(new UserPlayer());
        foundPlayer(new UserPlayer());
        foundPlayer(new RandomPlayer());
        foundPlayer(new RandomPlayer());
        foundPlayer(new SpiralPlayer());
        foundPlayer(new SpiralPlayer());
        foundPlayer(new BeatMePlayer());

        // Fetch Remotes...
        getDbRef().addChildEventListener(this);
    }

    public void unregisterCallback(){
        this.callback = null;
        this.getDbRef().removeEventListener(this);
    }

    private void foundPlayer(@NonNull final Player player) {
        if(callback != null){
            callback.onPlayerRetrieved(player);
        }
    }

    private void lostPlayer(@NonNull final String name) {
        if (callback != null) {
            callback.onPlayerRemoved(name);
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    DatabaseReference getDbRef() {
        return playersRef;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        try {
            final PlayerData player = dataSnapshot.getValue(PlayerData.class);
            foundPlayer(new RemotePlayer(playersRef, player, dataSnapshot.getKey()));
        } catch (Exception gottaCatchEmAll){
            Log.w("FetchPlayersUsecase", "Error getting PlayerData from snapShot", gottaCatchEmAll);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        try {
            final PlayerData player = dataSnapshot.getValue(PlayerData.class);
            lostPlayer(player.name);
        } catch (Exception gottaCatchEmAll){
            Log.w("FetchPlayersUsecase", "Error getting PlayerData from snapShot", gottaCatchEmAll);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
