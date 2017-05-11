package nl.entreco.reversi.api;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReversiResult {
    public String match;
    public int me;
    public int opponent;
    public int points;

    public ReversiResult(){}

    public ReversiResult(String matchKey, int yourScore, int opponentScore, int points) {
        this.match = matchKey;
        this.me = yourScore;
        this.opponent = opponentScore;
        this.points = points;
    }
}
