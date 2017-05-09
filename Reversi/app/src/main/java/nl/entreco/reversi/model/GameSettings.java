package nl.entreco.reversi.model;

public class GameSettings {
    private final int uiDelay = 500;
    public static final long timeout = 20_000L;
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

    public long getUiDelay() {
        return uiDelay;
    }
}
