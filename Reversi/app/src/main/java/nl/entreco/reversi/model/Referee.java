package nl.entreco.reversi.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import nl.entreco.reversi.model.players.NotStartedPlayer;

public class Referee implements Arbiter {

    private static final String TAG = Referee.class.getSimpleName();

    @NonNull private final GameSettings settings;
    @NonNull private List<Player> playersList = new ArrayList<>();
    @NonNull private final Gson gson;
    @NonNull private final Board board;

    private int currentPlayer;

    public Referee(@NonNull final GameSettings settings) {
        this.settings = settings;
        this.gson = new GsonBuilder().create();
        this.board = new Board(settings.getBoardSize());
        this.currentPlayer = settings.getStartIndex();
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
    public void restart() {
        currentPlayer = settings.getStartIndex();
        playersList.clear();
        board.restart();
    }

    @Override
    public void startMatch() {
        if (playersList.size() <= 1)
            throw new IllegalStateException("Need at least 2 players to start");

        board.start();
        switchToPlayer(playersList.get(currentPlayer));

    }

    private void switchToPlayer(@NonNull final Player player) {
        Log.d(TAG,
                "switchToPlayer -> player.yourTurn():" + player + " currentPlayer:" +
                        currentPlayer);
        player.yourTurn();
    }

    @Override
    public List<Stone> onMoveReceived(@NonNull Player player, String move) {
        Log.d(TAG, "onMoveReceived -> player:" + player + " move:" + move);
        if (isValidPosition(move)
                && isPlayersTurn(player)) {
            final List<Stone> updated = updateBoard(move, player.getStoneColor());
            if (updated.size() > 0) {
                notifyNextPlayer(player);
                return updated;
            }
        }

        Log.d(TAG, "onMoveReceived -> onMoveRejected:" + player + " move:" + move);
        revertBoard(move);
        player.onMoveRejected();
        return new ArrayList<>(0);
    }

    private void revertBoard(String moveString) {
        try {
            final Move move = gson.fromJson(moveString, Move.class);
            if (move != null) {
                board.set(move, Stone.EMPTY);
            }
        } catch (JsonSyntaxException ignore) {
        }
    }

    private List<Stone> updateBoard(String moveString, @Stone.Color int color) {
        final Move move = gson.fromJson(moveString, Move.class);
        return board.apply(move, color);
    }

    boolean isPlayersTurn(Player player) {
        return currentPlayer == playersList.indexOf(player);
    }

    boolean isValidPosition(String move) {
        if (move == null || "".equals(move)) return false;
        try {
            final Move aMove = gson.fromJson(move, Move.class);
            return aMove.isValid();
        } catch (JsonSyntaxException ignore) {
        }
        return false;
    }

    @Override
    public void onTimedOut(@NonNull Player player) {
        Log.d(TAG, "onTimedOut:" + player);
        notifyNextPlayer(player);
    }

    private void notifyNextPlayer(Player previousPlayer) {
        int indexOfPreviousPlayer = playersList.indexOf(previousPlayer);
        currentPlayer = (indexOfPreviousPlayer + 1) % playersList.size();
        switchToPlayer(playersList.get(currentPlayer));
    }

    @NonNull
    public Board getBoard() {
        return board;
    }

    @NonNull
    public Player getCurrentPlayer() {
        if(playersList.isEmpty()) return new NotStartedPlayer();
        return playersList.get(currentPlayer);
    }

}
