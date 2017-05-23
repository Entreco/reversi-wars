package nl.entreco.reversi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import nl.entreco.reversi.data.RemoteUsecase;
import nl.entreco.reversi.databinding.ActivityReversiBinding;
import nl.entreco.reversi.game.BoardAdapter;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.GameSettings;
import nl.entreco.reversi.model.GameTimer;
import nl.entreco.reversi.model.Referee;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReversiActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityReversiBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_reversi);

        final GameSettings settings = new GameSettings();
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        final GameTimer timer = new GameTimer(executor, handler);
        final Board board = new Board(settings.getBoardSize());
        final Referee ref = new Referee(settings, timer, board);
        final BoardAdapter adapter = new BoardAdapter(ref);
        final Game game = new Game(adapter, ref);

        final ReversiViewModel viewModel = new ReversiViewModel(game, new RemoteUsecase(FirebaseDatabase.getInstance()));
        final BottomSheetBehavior<? extends View> behavior =
                BottomSheetBehavior.from(binding.bottomSheet);
        behavior.setBottomSheetCallback(new ReloadCallback(viewModel));

        binding.setBehaviour(behavior);
        binding.setArbiter(ref);
        binding.setViewModel(viewModel);

        binding.reversiBoard.setLayoutManager(new GridLayoutManager(this, ref.getBoard().getBoardSize()));
//        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
//        binding.reversiBoard.setItemAnimator(animator);
        binding.reversiBoard.setAdapter(adapter);


        if (savedInstanceState == null) {
            viewModel.fetchPlayers();
        }
    }
}
