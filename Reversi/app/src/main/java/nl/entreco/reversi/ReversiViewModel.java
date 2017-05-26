package nl.entreco.reversi;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import java.util.function.Predicate;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import nl.entreco.reversi.api.MatchData;
import nl.entreco.reversi.data.CreateMatchUsecase;
import nl.entreco.reversi.data.FetchPlayersUsecase;
import nl.entreco.reversi.data.RemoteUsecase;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Player;

public class ReversiViewModel implements FetchPlayersUsecase.Callback, PlayerSelectedListener,
        CreateMatchUsecase.Callback {

    public final ObservableList<Player> players;
    public final ItemBinding<Player> playersBinding =
            ItemBinding.of(BR.player, R.layout.item_player);

    public final ObservableField<Game> game;
    public final ObservableField<Player> player1;
    public final ObservableField<Player> player2;

    @NonNull private final Game newGame;
    @NonNull private final RemoteUsecase remoteUsecase;

    ReversiViewModel(@NonNull final Game game,
                     @NonNull final RemoteUsecase remoteUsecase) {

        this.newGame = game;
        this.remoteUsecase = remoteUsecase;
        this.game = new ObservableField<>();
        this.player1 = new ObservableField<>();
        this.player2 = new ObservableField<>();
        this.players = new ObservableArrayList<>();

        this.playersBinding.bindExtra(BR.listener, this);
    }

    void fetchPlayers() {
        player1.set(null);
        player2.set(null);
        game.set(null);
        newGame.clear();
        players.clear();

        remoteUsecase.fetchPlayers(this);
    }

    @SuppressWarnings("Since15")
    @Override
    public void onPlayerRemoved(@NonNull final String name) {
        players.removeIf(new Predicate<Player>() {
            @Override
            public boolean test(Player player) {
                return name.equals(player.getName());
            }
        });
    }

    @Override
    public void onPlayerRetrieved(@NonNull Player player) {
        players.add(player);
        if(!players.contains(player)) {
    }

    @Override
    public void onPlayerSelected(@NonNull Player player) {

        players.remove(player);

        // Add Player to game
        if(player1.get() == null){
            player1.set(player);
        } else if(player2.get() == null){
            player2.set(player);

            startNewGame();
        }
    }

    private void startNewGame() {
        remoteUsecase.createMatch(this, player1.get(), player2.get());
    }

    @Override
    public void onMatchCreated(@NonNull MatchData matchData,
                               @NonNull String matchUuid) {
        newGame.setWhitePlayer(player1.get());
        newGame.setBlackPlayer(player2.get());

        // Start
        game.set(newGame);
        newGame.startGame(matchUuid);
    }
}
