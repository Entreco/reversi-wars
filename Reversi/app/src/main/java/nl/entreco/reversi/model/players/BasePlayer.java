package nl.entreco.reversi.model.players;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public abstract class BasePlayer implements Player {

    @Nullable private GameCallback callback;
    private int stoneColor;

    BasePlayer() {
        stoneColor = Stone.WHITE;
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
        ourTurn();
        handleTurn(board);

    }

    @CallSuper
    @Override
    public final void onMoveRejected(@NonNull final String board) {
        reject();
        onRejected(board);
    }

    abstract void onRejected(@NonNull final String board);

    abstract void handleTurn(@NonNull final String board);

    private void reject() {
        if (callback != null) {
            callback.onMoveRejected(BasePlayer.this);
        }
    }

    private void ourTurn() {
        if (callback != null) {
            callback.setCurrentPlayer(BasePlayer.this);
        }
    }

    final void submitMove(@NonNull final Move move) {
        if (callback != null) {
            callback.submitMove(BasePlayer.this, move);
        }
    }
}
