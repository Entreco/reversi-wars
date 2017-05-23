package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.util.BoardUtil;

public class BeatMePlayer extends BasePlayer {

    private static final int MAX_DEPTH = 4;
    private final Gson gson;
    private List<Move> moveList;

    public BeatMePlayer() {
        gson = new GsonBuilder().create();
    }

    @Override
    void onRejected(@NonNull String board) {
        handleTurn(board);
    }

    @Override
    void handleTurn(@NonNull String board) {
        Board gameBoard = BoardUtil.extractBoard(gson, board);
        moveList = gameBoard.findAllMoves(getStoneColor());

        final MoveValue minimax =
                minimax(gameBoard, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, getStoneColor(),
                        moveList.get(0));
        final Move best = minimax.move();
        Log.i("EVAL", "new BestMove:" + best + " score:" + minimax.value);
        Log.i("EVAL", best.toString());
        submitMove(best);
    }

    private MoveValue minimax(final Board board, int depth, int alpha, int beta, int stone,
                              Move bestMove) {
        if (depth == 0) {
            return new MoveValue(bestMove, evalBoard(board, stone));
        }

        final List<Move> moves = board.findAllMoves(stone);
        if (moves.isEmpty()) return new MoveValue(bestMove, evalBoard(board, stone));

        if (stone == getStoneColor()) {
            // MAX
            int score = Integer.MIN_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);
                final int maxi = minimax(child, depth - 1, alpha, beta, -1 * stone, move).value();

                if (maxi > score) {
                    bestMove = move;
                }

                score = Math.max(score, maxi);
                alpha = Math.max(alpha, score);

                if (beta <= alpha) break;
            }
            Log.i("EVAL", "NEW BEST MOVE MAXI:" + bestMove + " score will be:" + score);
            return new MoveValue(bestMove, score);
        } else {
            // MIN
            int score = Integer.MAX_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);
                final int mini = minimax(child, depth - 1, alpha, beta, -1 * stone, move).value();


                if (mini < score) {
                    bestMove = move;
                }

                score = Math.min(score, mini);

                beta = Math.min(beta, score);
                if (beta <= alpha) break;
            }
            Log.i("EVAL", "NEW BEST MOVE MINI:" + bestMove + " score will be:" + score);
            return new MoveValue(bestMove, score);
        }
    }

    private int evalBoard(final Board board, int stoneColor) {
        final int winFactor = ((board.getBoardSize() * board.getBoardSize())-board.findEmpty()) / 4;
        final int corners = 100 * evalCorners(board, stoneColor);
        final int safe = 10 * evalSafeStones(board, stoneColor);
        final int stones = winFactor * evalScore(board, stoneColor);
        final int opponentMoves = board.findAllMoves(-1 * stoneColor).size();
        final int eval = corners + safe + stones - opponentMoves;
        return eval;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int evalCorners(final Board board, int stoneColor) {
        final int size = board.getBoardSize() - 1;
        int corners = getStone(board, 0, 0).color()
                + getStone(board, 0, size).color()
                + getStone(board, size, 0).color()
                + getStone(board, size, size).color();
        return stoneColor * corners;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int evalSafeStones(final Board board, int stoneColor) {
        Map<Integer, Stone> safeStones = new HashMap<>();
        new ZigZag(board, safeStones, stoneColor).go();

        return safeStones.size();
    }

    private class ZigZag {
        private final Board board;
        private final int size;
        private final Map<Integer, Stone> safeStones;
        private int stoneColor;

        public ZigZag(Board board, Map<Integer, Stone> safeStones, int stoneColor) {
            this.board = board;
            this.size = board.getBoardSize();
            this.safeStones = safeStones;
            this.stoneColor = stoneColor;
        }

        public void go() {
            topLeft();
            topRight();
            bottomLeft();
            bottomRight();
        }

        private Stone get(int row, int col){
            return board.get(board.getItemPosition(row, col));
        }

        private void bottomRight() {
            int row = size - 1;
            int col = size - 1;
            loop(row, col, -1, -1);
        }

        private void bottomLeft() {
            int row = size - 1;
            int col = 0;
            loop(row, col, -1, 1);
        }

        private void topLeft() {
            int row = 0;
            int col = 0;
            loop(row, col, 1, 1);
        }

        private void topRight() {
            int row = 0;
            int col = size - 1;
            loop(row, col, 1, -1);
        }

        private void loop(int row, int col, int a, int b) {
            if(get(row, col).color() == stoneColor) {
                boolean shouldBreak = false;
                for (int offset = 0; offset < size; offset++) {
                    for (int i = 0; i <= offset; i++) {
                        int checkRow = row + (a * (offset - i));
                        int checkCol = col + (b * i);
                        if(get(checkRow, checkCol).color() == stoneColor) {
                            safeStones.put(checkRow * 10 + checkCol, get(checkRow, checkCol));
                        } else {
                            shouldBreak = true;
                            break;
                        }
                    }

                    if(shouldBreak){
                        break;
                    }
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    Stone getStone(Board board, int row, int col) {
        return board.get(board.getItemPosition(row, col));
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int evalScore(final Board board, int stoneColor) {
        int score = 0;
        for (final Stone stone : board.getStones()) {
            score += stone.color();
        }
        return stoneColor * score;
    }

    @NonNull
    @Override
    public String getName() {
        return "BeatMe";
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    private static class MoveValue {
        private final Move move;
        private int value;

        MoveValue(final Move move, int value) {
            this.move = move;
            this.value = value;
        }

        int value() {
            return value;
        }

        Move move() {
            return move;
        }
    }
}
