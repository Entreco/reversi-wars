package nl.entreco.reversi.data;

import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;

public class History {

    private final int color;
    private final String move;
    private final String score;

    public History(Player player, Move move, int score1, int score2) {
        this.color = player.getStoneColor();
        this.move = String.format("[%s,%s]", move.getRow(), move.getCol());
        this.score = String.format("[%s,%s]", score1, score2);
    }

    public int getColor() {
        return color;
    }

    public String getMove() {
        return move;
    }

    public String getScore() {
        return score;
    }
}
