package nl.entreco.reversi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import nl.entreco.reversi.databinding.ActivityReversiBinding;
import nl.entreco.reversi.game.BoardAdapter;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.GameSettings;
import nl.entreco.reversi.model.Referee;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.players.RandomPlayer;
import nl.entreco.reversi.model.players.UserPlayer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReversiActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityReversiBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reversi);

        final Referee ref = new Referee(new GameSettings());
        final BoardAdapter adapter = new BoardAdapter(ref);
        final Game game = new Game(adapter, ref, new UserPlayer(Stone.WHITE), new RandomPlayer(Stone.BLACK));
        final ReversiViewModel viewModel = new ReversiViewModel(game);

        binding.setArbiter(ref);
        binding.setViewModel(viewModel);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, ref.getBoard().getBoardSize()));
        binding.recyclerView.setAdapter(adapter);
    }

}
