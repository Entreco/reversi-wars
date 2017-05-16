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
    void onRejected(@NonNull String board) {
        handleTurn(board);
    }

    @Override
    void handleTurn(@NonNull String board) {
        Board gameBoard = BoardUtil.extractBoard(gson, board);
        moveList = gameBoard.findAllMoves(getStoneColor());

        final MoveValue minimax =
                minimax(gameBoard, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, getStoneColor(),
                        moveList.get(0));
        final Move best = minimax.move();
        Log.i("EVAL", "new BestMove:" + best + " score:" + minimax.value);
        Log.i("EVAL", best.toString());
        submitMove(best);
    }

    private MoveValue minimax(final Board board, int depth, int alpha, int beta, int stone, Move bestMove) {
        if (depth == 0) return new MoveValue(bestMove, evalBoard(board, stone, bestMove));

        final List<Move> moves = board.findAllMoves(stone);
        if (moves.isEmpty()) return new MoveValue(bestMove, evalBoard(board, stone, bestMove));

        if (stone == getStoneColor()) {
            // MAX
            int score = Integer.MIN_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);
                Log.i("APPLY", "try move:" + move + " stone:" + stone);
                Log.i("APPLY", child.toString());
                Log.i("EVAL", "try move:" + move);
                final int maxi = minimax(child, depth - 1, alpha, beta, -1 * stone, move).value();
                Log.i("EVAL", "maxi:"+maxi + " score:" + score + " alpha:" + alpha);

                if(maxi > score){
                    bestMove = move;
                }

                score = Math.max(score, maxi);
                Log.i("EVAL", "max(maxi:"+maxi + " score:" + score + "): " + score);
                alpha = Math.max(alpha, score);
                Log.i("EVAL", "max(alpha:"+alpha + " score:" + score + "): " + alpha);
                Log.i("EVAL", "move: "+move+"score:" + score + " alpha:" + alpha + " beta:" + beta);

                Log.i("APPLY", "try move:" + move + " stone:" + stone + " score:" + score);
                if (beta <= alpha) break;
            }
            Log.i("EVAL", "new BestMove:" + bestMove + " score:" + score);
            return new MoveValue(bestMove, score);
        } else {
            // MIN
            int score = Integer.MAX_VALUE;
            for (final Move move : moves) {
                final Board child = board.clone();
                child.apply(move, stone);
                Log.i("APPLY", "try move:" + move + " stone:" + stone);
                Log.i("APPLY", child.toString());
                Log.i("EVAL", "try move:" + move);
                final int mini = minimax(child, depth - 1, alpha, beta, -1 * stone, move).value();
                Log.i("EVAL", "mini:"+mini + " score:" + score + " beta:" + beta);


                if(mini < score){
                    bestMove = move;
                }

                score = Math.min(score, mini);
                Log.i("EVAL", "min(mini:"+mini + " score:" + score + "): " + score);

                beta = Math.min(beta, score);
                Log.i("EVAL", "min(beta:"+beta + " score:" + score + "): " + beta);
                Log.i("EVAL", "move: "+move+"score:" + score + " alpha:" + alpha + " beta:" + beta);
                Log.i("APPLY", "try move:" + move + " stone:" + stone + " score:" + score);
                if (beta <= alpha) break;
            }
            Log.i("EVAL", "new BestMove:" + bestMove + " score:" + score);
            return new MoveValue(bestMove, score);
        }
    }

    private int evalBoard(final Board board, int stoneColor, final Move move) {
        int corners = 100 * evalCorners(board, stoneColor);
        int safe = 10 * evalSafeStones(board, stoneColor);
        int stones = 1 * evalScore(board, stoneColor);
        int eval = corners + safe + stones;
        Log.i("EVAL", "board(stone:"+stoneColor+")");
        Log.i("EVAL", "" + board.toString());
        Log.i("EVAL", "eval:" + eval + "(c:"+corners+", s:"+safe+", s:"+stones+")");
        Log.i("BOARD EVAL APPLY", "" + board.toString());
        Log.i("BOARD EVAL APPLY", "eval:" + eval + "(c:"+corners+", s:"+safe+", s:"+stones+") stoneColor:" + stoneColor + " move:" + move);
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
        return 0;
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
