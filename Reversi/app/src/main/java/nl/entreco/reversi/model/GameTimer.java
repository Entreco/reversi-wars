package nl.entreco.reversi.model;

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Nullable Callback callback;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Nullable ScheduledFuture<?> scheduledFuture;

    @Nullable private Player player;

    public GameTimer(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void onTimedout() {
        if (callback != null && player != null) {
            callback.onTimedOut(player);
        }
        stop();
    }

    @Override
    public void run() {
        onTimedout();
    }

    public void start(@NonNull final Callback callback, @NonNull final Player player,
                      long timeoutInMilis) {
        stop();

        this.callback = callback;
        this.player = player;
        this.scheduledFuture = executor.schedule(this, timeoutInMilis, TimeUnit.MILLISECONDS);
    }

    void stop() {
        this.callback = null;
        this.player = null;
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
        this.scheduledFuture = null;
    }
}