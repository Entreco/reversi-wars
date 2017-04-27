package nl.entreco.reversi.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Board {

    @Retention(SOURCE)
    @IntDef({WHITE, BLACK, EMPTY})
    public @interface Stone {}

    static final @Stone int WHITE = -1;
    static final @Stone int BLACK = 1;
    static final @Stone int EMPTY = 0;

    private int ROWS = 8;

    private int COLS = 8;
    private @Stone int[][] board = new int[ROWS][COLS];

    void start() {
        set(new Move(3,3), WHITE);
        set(new Move(4,4), WHITE);
        set(new Move(3,4), BLACK);
        set(new Move(4,3), BLACK);
    }

    void set(Move move, @Stone int stone) {
        board[move.getRow()][move.getCol()] = stone;
    }

    void apply(Move move, @Stone int stone) {
        set(move, stone);
        // Look Left
        flipLeft(move, stone);
        flipRight(move, stone);
        flipUp(move, stone);
        flipDown(move, stone);
        flipNW(move, stone);
        flipNE(move, stone);
        flipSE(move, stone);
        flipSW(move, stone);

    }

    private void flipMoves(boolean flip, List<Move> moves, int stone){
        if(flip) {
            for (final Move move : moves) {
                set(move, stone);
            }
        }
    }

    private void flipLeft(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();
        for(int x = move.getCol() - 1 ; x >= 0 ; x--){
            @Stone int other = board[move.getRow()][x];

            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow(), x));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipRight(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int x = move.getCol() + 1 ; x < COLS ; x++){
            @Stone int other = board[move.getRow()][x];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow(), x));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipUp(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int y = move.getRow() - 1 ; y >= 0 ; y--){
            @Stone int other = board[y][move.getCol()];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(y, move.getCol()));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipDown(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int y = move.getRow() + 1 ; y < ROWS ; y++){
            @Stone int other = board[y][move.getCol()];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(y, move.getCol()));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipNW(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int offset = 1 ; offset < ROWS ; offset++){
            @Stone int other = board[move.getRow() - offset][move.getCol() - offset];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow() - offset,move.getCol() - offset));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipNE(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int offset = 1 ; offset < ROWS ; offset++){
            @Stone int other = board[move.getRow() - offset][move.getCol() + offset];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow() - offset,move.getCol() + offset));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipSE(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int offset = 1 ; offset < ROWS ; offset++){
            @Stone int other = board[move.getRow() + offset][move.getCol() + offset];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow() + offset,move.getCol() + offset));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }

    private void flipSW(Move move, @Stone int stone) {
        boolean needsFlip = false;
        final List<Move> positions = new ArrayList<>();

        for(int offset = 1 ; offset < ROWS ; offset++){
            @Stone int other = board[move.getRow() + offset][move.getCol() - offset];
            if(other == EMPTY) {
                needsFlip = false;
                break;
            }
            if(other == stone){
                needsFlip = true;
                break;
            }
            else {
                positions.add(new Move(move.getRow() + offset,move.getCol() - offset));
            }
        }

        flipMoves(needsFlip, positions, stone);
    }



    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder("{\"board\":");
        builder.append("[");
        for (int rows = 0; rows < ROWS; rows++) {
            builder.append("[");
            for (int cols = 0; cols < COLS; cols++) {
                builder.append(board[rows][cols]);
                if(cols < COLS - 1){
                    builder.append(",");
                }
            }
            builder.append("]");
            if(rows < ROWS - 1){
                builder.append(",");
            }
        }
        return builder.append("]}").toString();
    }
}
