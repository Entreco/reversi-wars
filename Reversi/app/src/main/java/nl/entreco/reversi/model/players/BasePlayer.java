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
    private final int stoneColor;

    BasePlayer(@Stone.Color int stoneColor){
        this.stoneColor = stoneColor;
    }

    public void setCallback(@Nullable GameCallback callback) {
        this.callback = callback;
    }

    @CallSuper
    @Override
    public void yourTurn(@NonNull String board) {
        ourTurn();
    }

    @Override
    public final int getStoneColor() {
        return stoneColor;
    }

    final void ourTurn(){
        if (callback != null) {
            callback.currentPlayer(this);
        }
    }
    final void submitMove(@NonNull final Move move) {
        if (callback != null) {
            callback.submitMove(this, move);
        }
    }
}