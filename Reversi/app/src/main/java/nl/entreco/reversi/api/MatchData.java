package nl.entreco.reversi.api;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import nl.entreco.reversi.model.Player;

@IgnoreExtraProperties
public class MatchData {

    public String white;
    public String black;
    public List<MoveData> moves;

    public MatchData() {
    }

    public MatchData(Player white, Player black) {
        this.white = white.getName();
        this.black = black.getName();
        this.moves = new ArrayList<>();
    }
}
