package nl.entreco.reversi;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import nl.entreco.reversi.game.Game;

public class ReversiViewModel {

    public final ObservableField<Game> game;

    ReversiViewModel(@NonNull final Game reversiGame) {
        this.game = new ObservableField<>(reversiGame);
    }
}
