package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Board extends ArrayList<Stone>{

    @Override
    public Stone get(int index) {
        int row = index / ROWS;
        int col = index % COLS;
        return board[row][col];
    }

    private int ROWS = 8;
    private int COLS = 8;
    private Stone[][] board = new Stone[ROWS][COLS];

    public Board(){
        applyWithoutChecks(new Move(0, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(0, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(1, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(1, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(2, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(2, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(3, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(3, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(4, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(4, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(5, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(5, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(6, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(6, 7), Stone.EMPTY);

        applyWithoutChecks(new Move(7, 0), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 1), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 2), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 3), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 4), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 5), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 6), Stone.EMPTY);
        applyWithoutChecks(new Move(7, 7), Stone.EMPTY);
    }

    public void start() {
        applyWithoutChecks(new Move(3, 3), Stone.WHITE);
        applyWithoutChecks(new Move(4, 4), Stone.WHITE);
        applyWithoutChecks(new Move(3, 4), Stone.BLACK);
        applyWithoutChecks(new Move(4, 3), Stone.BLACK);
    }

    void applyWithoutChecks(Move move, @Stone.Value int stone) {
        board[move.getRow()][move.getCol()] = new Stone(move.getRow(), move.getCol(), stone);
    }

    public void apply(Move move, @Stone.Value int stone) {
        applyWithoutChecks(move, stone);
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

    private void flipMoves(@NonNull final List<Stone> moves) {
        for (final Stone move : moves) {
            move.flip();
        }
    }

    private boolean check(int row, int col, @Stone.Value int stone, @NonNull final List<Stone> positions){
        if(row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            positions.clear();
            return true;
        }

        Stone other = board[row][col];

        if (other.value() == Stone.EMPTY) {
            positions.clear();
            return true;
        } else  if(other.value() == stone){
            return true;
        } else {
            positions.add(other);
        }
        return false;
    }

    private void flipLeft(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();
        for (int x = move.getCol() - 1; x >= 0; x--) {
            if(check(move.getRow(), x, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipRight(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int x = move.getCol() + 1; x < COLS; x++) {
            if(check(move.getRow(), x, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipUp(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int y = move.getRow() - 1; y >= 0; y--) {
            if(check(y, move.getCol(), stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipDown(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int y = move.getRow() + 1; y < ROWS; y++) {
            if(check(y, move.getCol(), stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipNW(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset < ROWS; offset++) {
            if(check(move.getRow() - offset, move.getCol() - offset, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipNE(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset < ROWS; offset++) {
            if(check(move.getRow() - offset, move.getCol() + offset, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipSE(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset < ROWS; offset++) {
            if(check(move.getRow() + offset, move.getCol() + offset, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }

    private void flipSW(Move move, @Stone.Value int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset < ROWS; offset++) {
            if(check(move.getRow() + offset, move.getCol() - offset, stone, positions)){
                break;
            }
        }

        flipMoves(positions);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (int rows = 0; rows < ROWS; rows++) {
            for (int cols = 0; cols < COLS; cols++) {
                @Stone.Value final int stone = board[rows][cols].value();
                switch(stone){
                    case Stone.WHITE: builder.append("x"); break;
                    case Stone.BLACK: builder.append("o"); break;
                    default: builder.append("_");
                }

                if (cols < COLS - 1) {
                    builder.append("|");
                }
            }
            builder.append("|\n");
            if (rows < ROWS - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder("{\"board\":");
        builder.append("[");
        for (int rows = 0; rows < ROWS; rows++) {
            builder.append("[");
            for (int cols = 0; cols < COLS; cols++) {
                builder.append(board[rows][cols]);
                if (cols < COLS - 1) {
                    builder.append(",");
                }
            }
            builder.append("]");
            if (rows < ROWS - 1) {
                builder.append(",");
            }
        }
        return builder.append("]}").toString();
    }
}
