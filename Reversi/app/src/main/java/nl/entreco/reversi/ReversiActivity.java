package nl.entreco.reversi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import nl.entreco.reversi.databinding.ActivityReversiBinding;
import nl.entreco.reversi.game.BoardAdapter;
import nl.entreco.reversi.game.CreateMatchUsecase;
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
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        final Game game = new Game(adapter, ref, new CreateMatchUsecase(firebaseDatabase));

        final ReversiViewModel viewModel =
                new ReversiViewModel(game, new FetchPlayersUsecase(firebaseDatabase));

        final BottomSheetBehavior<? extends View> behavior = setupBottomSheet(binding, viewModel);

        binding.setBehaviour(behavior);
        binding.setArbiter(ref);
        binding.setViewModel(viewModel);

        setupBoard(binding.reversiBoard, adapter, ref.getBoard().getBoardSize());
        setupPlayersList(binding.playersList);


        if (savedInstanceState == null) {
            viewModel.fetchPlayers();
        }
    }

    @NonNull
    private BottomSheetBehavior<? extends View> setupBottomSheet(ActivityReversiBinding binding,
                                                                 ReversiViewModel viewModel) {
        final BottomSheetBehavior<? extends View> behavior =
                BottomSheetBehavior.from(binding.bottomSheet);
        behavior.setBottomSheetCallback(new ReloadCallback(viewModel));
        return behavior;
    }

    private void setupPlayersList(@NonNull final RecyclerView playersList) {
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        playersList.setItemAnimator(animator);
    }

    private void setupBoard(@NonNull final RecyclerView board, BoardAdapter adapter,
                            int boardSize) {
        board.setLayoutManager(new GridLayoutManager(this, boardSize));
        board.setAdapter(adapter);
    }
}
