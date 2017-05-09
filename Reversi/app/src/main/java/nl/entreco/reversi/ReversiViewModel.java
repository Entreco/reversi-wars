package nl.entreco.reversi;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import nl.entreco.reversi.data.FetchPlayersUsecase;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Player;

public class ReversiViewModel implements FetchPlayersUsecase.Callback, PlayerSelectedListener {

    public final ObservableList<Player> players;
    public final ItemBinding<Player> playersBinding =
            ItemBinding.of(BR.player, R.layout.item_player);

    public final ObservableField<Game> game;
    public final ObservableField<Player> player1;
    public final ObservableField<Player> player2;
    private final FetchPlayersUsecase fetchPlayersUsecase;

    @NonNull private final Game newGame;

    ReversiViewModel(@NonNull final Game game, @NonNull final FetchPlayersUsecase fetchPlayersUsecase) {

        this.newGame = game;
        this.game = new ObservableField<>();
        this.player1 = new ObservableField<>();
        this.player2 = new ObservableField<>();
        this.players = new ObservableArrayList<>();

        this.playersBinding.bindExtra(BR.listener, this);
        this.fetchPlayersUsecase = fetchPlayersUsecase;
    }

    void fetchPlayers() {
        player1.set(null);
        player2.set(null);
        game.set(null);
        newGame.clear();
        players.clear();

        fetchPlayersUsecase.registerCallback(this);
        fetchPlayersUsecase.fetch();
    }

    @Override
    public void onPlayerRetrieved(@NonNull Player player) {
        players.add(player);
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

        final String uuid = new MatchReference().start(player1.get(), player2.get());

        newGame.setWhitePlayer(player1.get());
        newGame.setBlackPlayer(player2.get());

        // Start
        game.set(newGame);
        newGame.startGame(uuid);
        fetchPlayersUsecase.unregisterCallback();
    }
}
