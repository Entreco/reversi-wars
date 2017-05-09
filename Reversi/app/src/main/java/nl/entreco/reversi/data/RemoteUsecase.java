package nl.entreco.reversi.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import nl.entreco.reversi.model.Player;

public class RemoteUsecase {

    @NonNull private final FetchPlayersUsecase fetchPlayersUsecase;
    @NonNull private final CreateMatchUsecase createMatchUsecase;

    public RemoteUsecase(@NonNull final FirebaseDatabase database) {
        this.fetchPlayersUsecase = new FetchPlayersUsecase(database);
        this.createMatchUsecase = new CreateMatchUsecase(database);
    }

    public void fetchPlayers(@NonNull final FetchPlayersUsecase.Callback callback){
        this.fetchPlayersUsecase.registerCallback(callback);
    }
    public void createMatch(@NonNull final CreateMatchUsecase.Callback callback, @NonNull final Player white, @NonNull final Player black){
        this.fetchPlayersUsecase.unregisterCallback();
        this.createMatchUsecase.registerCallback(callback, white, black);
    }
}
