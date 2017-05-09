package nl.entreco.reversi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.players.BeatMePlayer;
import nl.entreco.reversi.model.players.RandomPlayer;
import nl.entreco.reversi.model.players.RemotePlayer;
import nl.entreco.reversi.model.players.SpiralPlayer;
import nl.entreco.reversi.model.players.UserPlayer;

public class FetchPlayersUsecase {

    @Nullable private Callback callback;

    public interface Callback{
        void onPlayerRetrieved(@NonNull final Player player);
    }

    public FetchPlayersUsecase() {}

    public void fetch() {
        foundPlayer(new UserPlayer());
        foundPlayer(new UserPlayer());
        foundPlayer(new RandomPlayer());
        foundPlayer(new RandomPlayer());
        foundPlayer(new SpiralPlayer());
        foundPlayer(new SpiralPlayer());
        foundPlayer(new BeatMePlayer());

        // Fetch Remotes...
        FirebaseDatabase.getInstance().getReference("players").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final RemotePlayer player = dataSnapshot.getValue(RemotePlayer.class);
                        foundPlayer(player);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void foundPlayer(@NonNull final Player player) {
        if(callback != null){
            callback.onPlayerRetrieved(player);
        }
    }

    public void registerCallback(@NonNull Callback callback) {
        this.callback = callback;
    }
    public void unregisterCallback(){
        this.callback = null;
    }
}
