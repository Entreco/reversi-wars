package nl.entreco.reversi.model;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Referee implements Arbiter, GameTimer.Callback {

    private static final String TAG = Referee.class.getSimpleName();

    @NonNull private final GameSettings settings;
    @NonNull private final GameTimer timer;
    @NonNull private List<Player> playersList = new ArrayList<>();
    @NonNull private final Gson gson;
    @NonNull private final Board board;
    @NonNull private volatile AtomicInteger currentPlayer;
    @Nullable private GameCallback gameCallback;

    public Referee(@NonNull final GameSettings settings,
                   @NonNull final GameTimer timer,
                   @NonNull final Board board) {
        this.settings = settings;
        this.timer = timer;
        this.gson = new GsonBuilder().create();
        this.board = board;
        this.currentPlayer = new AtomicInteger(settings.getStartIndex());
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
    public void start(@NonNull final GameCallback gameCallback) {
        this.gameCallback = gameCallback;
        this.currentPlayer.set(settings.getStartIndex());
        this.board.restart();
        startMatch();
    }

    @Override
    public void clear() {
        this.playersList.clear();
        this.currentPlayer.set(settings.getStartIndex());

    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void startMatch() {
        if (playersList.size() <= 1)
            throw new IllegalStateException("Need at least 2 players to start");

        board.start();
        switchPlayers();

    }

    private void switchPlayers() {
        Log.i("THREAD", "Arbiter::switchPlayers:" + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        final Player player = playersList.get(currentPlayer.get());
        final @Stone.Color int stoneColor = player.getStoneColor();
        if (board.canMove(stoneColor)) {

            notifyPlayer(player);

        } else if (board.canMove(-1 * stoneColor)) {
            notifyNextPlayer(player);
        } else {
            notifyGameFinished();
        }
    }

    private void notifyPlayer(@NonNull final Player player) {
        Log.i("THREAD", "Arbiter::notifyPlayer: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        player.yourTurn(board.toJson());
        if (!player.isHuman()) {
            timer.start(this, player, settings.getTimeout());
        }
    }

    private void notifyGameFinished() {
        if (this.gameCallback != null) {
            final int score = getScore();
            this.gameCallback.onGameFinished(score);
        } else {
            Log.w(TAG, "notifyGameFinished() but gameCallback is null");
        }

        for (final Player player : playersList) {
            player.onGameFinished(getScore(player.getStoneColor()),
                    getScore(player.getStoneColor() * -1));
        }

        this.timer.stop();
        this.gameCallback = null;
    }

    private int getScore() {
        int score = 0;
        for (final Stone stone : board.getStones()) {
            score += stone.color();
        }
        return score;
    }

    private int getScore(@Stone.Color int color) {
        int score = 0;
        for (final Stone stone : board.getStones()) {
            if (stone.color() == color) {
                score += stone.color();
            }
        }
        return score;
    }

    @NonNull
    @Override
    public List<Stone> onMoveReceived(@NonNull Player player, String move) {
        Log.i("THREAD", "Arbiter::onMoveReceived: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        Log.d(TAG, "onMoveReceived -> player:" + player + " move:" + move);

        if (isPlayersTurn(player)) {
            if (isValidPosition(move)) {
                final List<Stone> updated = updateBoard(move, player.getStoneColor());
                if (updated.size() > 0) {
                    timer.stop();
                    return updated;
                }
            }

            Log.d(TAG, "onMoveReceived -> onMoveRejected:" + player + " move:" + move);
            player.onMoveRejected(board.toJson());
        }
        return new ArrayList<>(0);
    }


    private List<Stone> updateBoard(String moveString, @Stone.Color int color) {
        final Move move = gson.fromJson(moveString, Move.class);
        return board.clone().apply(move, color);
    }

    synchronized boolean isPlayersTurn(Player player) {
        return currentPlayer.get() == playersList.indexOf(player);
    }

    boolean isValidPosition(String move) {
        if (move == null || "".equals(move)) return false;
        try {
            final Move aMove = gson.fromJson(move, Move.class);
            return aMove.isValid() && inBounds(aMove) && isEmptySpot(aMove);
        } catch (JsonSyntaxException ignore) {
        }
        return false;
    }

    private boolean isEmptySpot(@NonNull final Move aMove) {
        return board.get(board.getItemPosition(aMove)).color() == Stone.EMPTY;
    }

    private boolean inBounds(@NonNull final Move aMove) {
        if (aMove.getCol() < 0 || aMove.getCol() >= board.getBoardSize()) return false;
        if (aMove.getRow() < 0 || aMove.getRow() >= board.getBoardSize()) return false;
        return true;
    }

    @Override
    public void onTimedOut(@NonNull final Player player) {
        Log.i("THREAD", "onTimedOut: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        Log.d(TAG, "onTimedOut:" + player);
        notifyNextPlayer(player);

    }

    public void notifyNextPlayer(@NonNull Player previousPlayer) {
        int indexOfPreviousPlayer = playersList.indexOf(previousPlayer);
        currentPlayer.set((indexOfPreviousPlayer + 1) % playersList.size());
        switchPlayers();
    }

    @NonNull
    public Board getBoard() {
        return board;
    }
}
