package nl.entreco.reversi.model;

interface Player {
    void yourTurn();

    void doMove(String s);

    void onMoveRejected();
}
