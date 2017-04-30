package nl.entreco.reversi.game;

import android.support.annotation.NonNull;

import nl.entreco.reversi.model.Stone;

interface StoneClickListener {
    void onStoneClicked(@NonNull final Stone stone);
}
