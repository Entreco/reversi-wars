package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class Referee implements Arbiter {

    private List<Player> playersList = new ArrayList<>();
    private final Gson gson;
    private final Board board;

    public Referee(@NonNull final Board board) {
        this.gson = new GsonBuilder().create();
        this.board = board;
    }

    @Override
    public void addPlayer(@NonNull Player player) {
        if (!playersList.contains(player)) {
            playersList.add(player);
        }
    }

    @Override
    public List<Player> getPlayers() {
        return playersList;
    }

    @Override
    public void startMatch() {
        if (playersList.size() <= 1)
            throw new IllegalStateException("Need at least 2 players to start");

        board.start();
        playersList.get(0).yourTurn();
    }

    @Override
    public void onMoveReceived(@NonNull Player player, String move) {
        if (isValidPosition(move)) {
            notifyNextPlayer(player);
        } else {
            player.onMoveRejected();
        }
    }

    boolean isValidPosition(String move) {
        if (move == null || "".equals(move)) return false;
        try {
            final Move aMove = gson.fromJson(move, Move.class);
            return aMove.isValid();
        }  catch(JsonSyntaxException ignore){}
        return false;
    }

    @Override
    public void onTimedOut(@NonNull Player player) {
        notifyNextPlayer(player);
    }

    private void notifyNextPlayer(Player previousPlayer) {
        int indexOfPreviousPlayer = playersList.indexOf(previousPlayer);
        int indexOfNextPlayer = (indexOfPreviousPlayer + 1) % playersList.size();
        playersList.get(indexOfNextPlayer).yourTurn();
    }
}
