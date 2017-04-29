package nl.entreco.reversi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import nl.entreco.reversi.databinding.ActivityReversiBinding;
import nl.entreco.reversi.game.BoardAdapter;
import nl.entreco.reversi.model.GameSettings;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Referee;
import nl.entreco.reversi.model.Stone;

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
        final ReversiViewModel viewModel = new ReversiViewModel(new Player() {
            @Override
            public void yourTurn() {

            }

            @Override
            public void onMoveRejected() {

            }

            @Override
            public int getStoneColor() {
                return Stone.BLACK;
            }
        }, new Player() {
            @Override
            public void yourTurn() {

            }

            @Override
            public void onMoveRejected() {

            }

            @Override
            public int getStoneColor() {
                return Stone.WHITE;
            }
        });

        binding.setArbiter(ref);
        binding.setViewModel(viewModel);
        binding.setAdapter(adapter);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, ref.getBoard().getBoardSize()));
        binding.recyclerView.setAdapter(adapter);
    }

    public interface StoneClickListener{
        void onStoneClicked(@NonNull final Stone stone);
    }
}
