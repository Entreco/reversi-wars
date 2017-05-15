package nl.entreco.reversi.model.players;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.util.BoardUtil;

public class BeatMePlayer extends BasePlayer {

    private final Gson gson;
    private List<Move> moveList;

    public BeatMePlayer() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void yourTurn(@NonNull final String board) {
        super.yourTurn(board);

        Board gameBoard = BoardUtil.extractBoard(gson, board);
        moveList = gameBoard.findAllMoves(getStoneColor());

        final MoveValue minimax =
                minimax(gameBoard, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, getStoneColor(),
                        moveList.get(0));
        final Move best = minimax.move();
        Log.i("EVAL", "new BestMove:" + best);
        Log.i("EVAL", best.toString());
        submitMove(best);

    }

    MoveValue minimax(final Board board, int depth, int alpha, int beta, int stone, Move bestMove) {
        if (depth == 0) return new MoveValue(bestMove, evalBoard(board, stone));

        final List<Move> moves = board.findAllMoves(stone);
        if (moves.isEmpty()) return new MoveValue(bestMove, evalBoard(board, stone));

        if (stone == getStoneColor()) {
            // MAX
            int score = Integer.MIN_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);

                Log.i("EVAL", "MAX:");
                Log.i("EVAL", child.toString());

                score = Math.max(score,
                        minimax(child, depth - 1, alpha, beta, -1 * stone, bestMove).value());
                alpha = Math.max(alpha, score);
                if (beta <= alpha) break;
            }
            return new MoveValue(bestMove, score);
        } else {
            // MIN
            int score = Integer.MAX_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);

                Log.i("EVAL", "MIN:");
                Log.i("EVAL", child.toString());

                score = Math.min(score,
                        minimax(child, depth - 1, alpha, beta, -1 * stone, bestMove).value());
                beta = Math.min(beta, score);
                if (beta <= alpha) break;
            }
            return new MoveValue(bestMove, score);
        }
    }

    private int evalBoard(final Board board, int stoneColor) {
        int corners = 100 * evalCorners(board, stoneColor);
        int safe = 10 * evalSafeStones(board, stoneColor);
        int stones = 1 * evalScore(board, stoneColor);
        int eval = corners + safe + stones;
        Log.i("EVAL", "eval:" + eval + "[c:" + corners + ",s:" + safe + ",s:" + stones + "]");
        return eval;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int evalCorners(final Board board, int stoneColor) {
        final int size = board.getBoardSize() - 1;
        int corners = getStone(board, 0, 0).color() == stoneColor ? 1 : -1;
        corners += getStone(board, 0, size).color() == stoneColor ? 1 : -1;
        corners += getStone(board, size, 0).color() == stoneColor ? 1 : -1;
        corners += getStone(board, size, size).color() == stoneColor ? 1 : -1;
        return corners;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    int evalSafeStones(final Board board, int stoneColor) {
        int safeStones = 0;
        final int s = board.getBoardSize() - 1;
        for (int index = 0; index <= s; index++) {
            safeStones += safe(board, 0, 0, index, stoneColor, 1);
            safeStones += safe(board, 0, s, index, stoneColor, 2);
            safeStones += safe(board, s, 0, index, stoneColor, 3);
            safeStones += safe(board, s, s, index, stoneColor, 4);
        }
        return safeStones;
    }

    private int safe(final Board board, int row, int col, int offset, int stoneColor,
                     int direction) {
        int safe = 0;
        int rowd = row + ((direction == 1 || direction == 2) ? offset : -offset);
        int cold = col + ((direction == 1 || direction == 3) ? offset : -offset);
        for (int index = 0; index <= offset; index++) {

            if (getStone(board, rowd, cold).color() == stoneColor) {
                safe++;
            }
            break;
        }
        return safe;
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

    @Override
    public void onMoveRejected(@NonNull final String board) {
        super.onMoveRejected(board);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitMove(moveList.remove(0));
            }
        }, 10);
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
