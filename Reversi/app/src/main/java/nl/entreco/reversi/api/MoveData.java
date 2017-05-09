package nl.entreco.reversi.api;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MoveData {
    public int row;
    public int col;

    public MoveData(){}
}
