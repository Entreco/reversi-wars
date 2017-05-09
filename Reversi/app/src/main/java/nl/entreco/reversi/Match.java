package nl.entreco.reversi;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import nl.entreco.reversi.model.Player;

@IgnoreExtraProperties
public class Match {
    public String white;
    public String black;
    public List<String> moves = new ArrayList<>();

    public Match() {
    }

    public Match(Player white, Player black) {
        this.white = white.getName();
        this.black = black.getName();
    }
}
