package nl.entreco.reversi.api;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class RemoteMatch {
    public final String gameUuid;
    public List<MoveData> moves;

    public RemoteMatch(String gameUuid) {
        this.gameUuid = gameUuid;
        this.moves = new ArrayList<>();
    }
}
