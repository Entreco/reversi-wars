package nl.entreco.reversi.api;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReversiResult {
    public int me;
    public int opponent;
    public int points;

    public ReversiResult(){}

    public ReversiResult(int yourScore, int opponentScore, int points) {
        this.me = yourScore;
        this.opponent = opponentScore;
        this.points = points;
    }
}
