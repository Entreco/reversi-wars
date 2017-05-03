package nl.entreco.reversi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.players.BeatMePlayer;
import nl.entreco.reversi.model.players.RandomPlayer;
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
        foundPlayer(new RandomPlayer());
        foundPlayer(new UserPlayer());
        foundPlayer(new SpiralPlayer());
        foundPlayer(new BeatMePlayer());
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
