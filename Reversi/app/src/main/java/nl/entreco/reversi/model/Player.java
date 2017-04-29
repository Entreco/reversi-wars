package nl.entreco.reversi.model;

public interface Player {
    void yourTurn();

    void onMoveRejected();

    @Stone.Color int getStoneColor();

}
