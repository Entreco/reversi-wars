package nl.entreco.reversi.model;

public class GameSettings {
    private final long timeout = 4_000L;
    private final int startIndex = 0;
    private final int boardSize = 8;

    public GameSettings() {
    }

    long getTimeout() {
        return timeout;
    }

    int getStartIndex() {
        return startIndex;
    }

    public int getBoardSize() {
        return boardSize;
    }
}
