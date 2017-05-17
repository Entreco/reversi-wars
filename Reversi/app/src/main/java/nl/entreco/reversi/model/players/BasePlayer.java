package nl.entreco.reversi.model.players;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public abstract class BasePlayer implements Player {

    @NonNull private final ScheduledExecutorService executor;
    @NonNull private final Handler handler;
    @Nullable private GameCallback callback;
    private int stoneColor;

    BasePlayer() {
        stoneColor = Stone.WHITE;
        handler = new Handler(Looper.getMainLooper());
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @NonNull
    ScheduledExecutorService getExecutor() {
        return executor;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @NonNull
    Handler getMainLooper() {
        return handler;
    }

    @Override
    public final void setCallback(@Nullable GameCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onJoinedGame(@NonNull String gameUuid) {
        // Means a new game was started
    }

    @Override
    public void onGameFinished(int yourScore, int opponentScore) {
        // You can see if you won
    }

    @Override
    public final int getStoneColor() {
        return stoneColor;
    }

    @Override
    public final void setStoneColor(int stoneColor) {
        this.stoneColor = stoneColor;
    }

    @CallSuper
    @Override
    public final void yourTurn(@NonNull final String board) {
        Log.i("THREAD", "BasePlayer::yourTurn: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                Log.i("THREAD", "BasePlayer::handleTurn: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
                ourTurn();
                handleTurn(board);
            }
        }, 50, TimeUnit.MILLISECONDS);
    }

    @CallSuper
    @Override
    public final void onMoveRejected(@NonNull final String board) {
        Log.i("THREAD CURRENT", "BasePlayer::onMoveRejected: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        reject();

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                Log.i("THREAD", "BasePlayer::onRejected: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
                onRejected(board);
            }
        }, 10, TimeUnit.MILLISECONDS);
    }

    abstract void onRejected(@NonNull final String board);

    abstract void handleTurn(@NonNull final String board);

    private void reject() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onMoveRejected(BasePlayer.this);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           callback.onMoveRejected(null);
                        }
                    }, 50);
                }
            }
        });
    }

    private void ourTurn() {
        Log.i("THREAD", "BasePlayer::ourTurn: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.setCurrentPlayer(BasePlayer.this);
                }
            }
        });
    }

    final void submitMove(@NonNull final Move move) {
        Log.i("THREAD", "BasePlayer::submitMove: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.submitMove(BasePlayer.this, move);
                }
            }
        });
    }
}
