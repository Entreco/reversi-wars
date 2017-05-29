package nl.entreco.reversi;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import java.util.function.Predicate;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Player;

public class ReversiViewModel implements FetchPlayersUsecase.Callback, PlayerSelectedListener {

    public final ObservableList<Player> players;
    public final ItemBinding<Player> playersBinding =
            ItemBinding.of(BR.player, R.layout.item_player);

    public final ObservableField<Game> game;
    public final ObservableField<Player> player1;
    public final ObservableField<Player> player2;

    @NonNull private final Game newGame;
    @NonNull private final FetchPlayersUsecase fetchPlayersUsecase;

    ReversiViewModel(@NonNull final Game game,
                     @NonNull final FetchPlayersUsecase fetchPlayersUsecase) {

        this.newGame = game;
        this.fetchPlayersUsecase = fetchPlayersUsecase;
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

        fetchPlayersUsecase.registerCallback(this);
    }

    void clearPlayers() {
        players.clear();
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
        if(!players.contains(player)) {
            players.add(player);
        }
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
        fetchPlayersUsecase.unregisterCallback();

        newGame.setWhitePlayer(player1.get());
        newGame.setBlackPlayer(player2.get());

        // Start
        game.set(newGame);
        newGame.startGame();
    }

}
