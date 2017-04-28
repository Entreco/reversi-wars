package nl.entreco.reversi.model;

public interface Player {
    void yourTurn();

    void doMove(String s);

    void onMoveRejected();
}
