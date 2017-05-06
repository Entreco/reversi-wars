package nl.entreco.reversi.model.players;

import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public abstract class BasePlayer implements Player {

    @NonNull private final Handler handler;
    @Nullable private GameCallback callback;
    private int stoneColor;

    BasePlayer(){
        stoneColor = Stone.WHITE;
        handler = new Handler();
    }

    @NonNull
    protected Handler getHandler() {
        return handler;
    }

    @Override
    public final void setCallback(@Nullable GameCallback callback) {
        this.callback = callback;
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
    public void yourTurn(@NonNull String board) {
        Log.i("BOARD", board);
        ourTurn();
    }

    @CallSuper
    @Override
    public void onMoveRejected(@NonNull String board) {
        reject();
    }

    private void reject(){
        if (callback != null){
            callback.onMoveRejected(this);
        }
    }

    private void ourTurn(){
        if (callback != null) {
            callback.setCurrentPlayer(this);
        }
    }

    final void submitMove(@NonNull final Move move) {
        if (callback != null) {
            callback.submitMove(this, move);
        }
    }
}
