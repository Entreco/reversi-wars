package nl.entreco.reversi.model;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameTimer implements Runnable {


    interface Callback {
        void onTimedOut(@NonNull final Player player);
    }

    @NonNull private final ScheduledExecutorService executor;
    @NonNull private final Handler handler;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Nullable Callback callback;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Nullable ScheduledFuture<?> scheduledFuture;

    @Nullable private Player player;

    public GameTimer(@NonNull final ScheduledExecutorService executor) {
        this.handler = new Handler(Looper.getMainLooper());
        this.executor = executor;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void onTimedout() {
        Log.i("THREAD", "GameTimer::onTimedOut: " + Thread.currentThread() + " main:" + (Looper
                .myLooper() == Looper.getMainLooper()));
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null && player != null) {
                    callback.onTimedOut(player);
                }
                stop();
            }
        });
    }

    @Override
    public void run() {
        onTimedout();
    }

    public void start(@NonNull final Callback callback, @NonNull final Player player,
                      long timeoutInMilis) {
        Log.i("THREAD", "GameTimer::start: " + Thread.currentThread() + " main:" + (Looper
                .myLooper() == Looper.getMainLooper()));
        stop();

        this.callback = callback;
        this.player = player;
        this.scheduledFuture = executor.schedule(this, timeoutInMilis, TimeUnit.MILLISECONDS);
    }

    void stop() {
        Log.i("THREAD", "GameTimer::stop: " + Thread.currentThread() + " main:" + (Looper
                .myLooper() == Looper.getMainLooper()));
        this.callback = null;
        this.player = null;
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
        this.scheduledFuture = null;
    }
}
