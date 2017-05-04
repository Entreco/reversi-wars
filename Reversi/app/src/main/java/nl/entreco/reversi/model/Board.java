package nl.entreco.reversi.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board extends ArrayList<Stone>{

    private static final long serialVersionUID = -4736231613346515717L;
    private final int boardSize;

    public Board(int size){
        boardSize = size;
        init();
    }

    public int getBoardSize(){
        return boardSize;
    }

    public void start() {
        set(new Move(3, 3), Stone.WHITE);
        set(new Move(4, 4), Stone.WHITE);
        set(new Move(3, 4), Stone.BLACK);
        set(new Move(4, 3), Stone.BLACK);
    }

    private int add(Move move, @Stone.Color int stoneColor) {
        final int itemPosition = getItemPosition(move);
        final Stone stone = new Stone(move.getRow(), move.getCol(), stoneColor);
        add(itemPosition, stone);
        return 1;
    }

    private Stone set(Move move, @Stone.Color int stoneColor) {
        final int itemPosition = getItemPosition(move);
        final Stone stone = new Stone(move.getRow(), move.getCol(), stoneColor);
        return set(itemPosition, stone);
    }

    public int getItemPosition(final Move move){
        return getItemPosition(move.getRow(), move.getCol());
    }

    public int getItemPosition(final int row, final int col){
        return row * boardSize + col;
    }

    public List<Stone> apply(Move move, @Stone.Color int stone) {

        set(move, stone);

        final List<Stone> positions = new ArrayList<>();
        // Look Left
        positions.addAll(flipLeft(move, stone));
        positions.addAll(flipRight(move, stone));
        positions.addAll(flipUp(move, stone));
        positions.addAll(flipDown(move, stone));
        positions.addAll(flipNW(move, stone));
        positions.addAll(flipNE(move, stone));
        positions.addAll(flipSE(move, stone));
        positions.addAll(flipSW(move, stone));

        return positions;
    }

    private List<Stone> flipMoves(@NonNull final List<Stone> stones) {
        for (final Stone stone : stones) {
            stone.flip();
            set(indexOf(stone), stone);
        }
        return stones;
    }

    private boolean check(int row, int col, @Stone.Color int stone, @NonNull final List<Stone> positions){
        if(row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
            positions.clear();
            return true;
        }

        Stone other = get(getItemPosition(row, col));

        if (other.color() == Stone.EMPTY) {
            positions.clear();
            return true;
        } else  if(other.color() == stone){
            return true;
        } else {
            positions.add(other);
        }
        return false;
    }

    private List<Stone> flipLeft(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();
        for (int x = move.getCol() - 1; x >= -1; x--) {
            if(check(move.getRow(), x, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipRight(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int x = move.getCol() + 1; x <= boardSize; x++) {
            if(check(move.getRow(), x, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipUp(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int y = move.getRow() - 1; y >= -1; y--) {
            if(check(y, move.getCol(), stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipDown(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int y = move.getRow() + 1; y <= boardSize; y++) {
            if(check(y, move.getCol(), stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipNW(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset <= boardSize; offset++) {
            if(check(move.getRow() - offset, move.getCol() - offset, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipNE(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset <= boardSize; offset++) {
            if(check(move.getRow() - offset, move.getCol() + offset, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipSE(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset <= boardSize; offset++) {
            if(check(move.getRow() + offset, move.getCol() + offset, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }

    private List<Stone> flipSW(Move move, @Stone.Color int stone) {
        final List<Stone> positions = new ArrayList<>();

        for (int offset = 1; offset <= boardSize; offset++) {
            if(check(move.getRow() + offset, move.getCol() - offset, stone, positions)){
                break;
            }
        }

        return flipMoves(positions);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (int rows = 0; rows < boardSize; rows++) {
            for (int cols = 0; cols < boardSize; cols++) {
                @Stone.Color final int stone = get(getItemPosition(rows, cols)).color();
                switch (stone) {
                    case Stone.WHITE:
                        builder.append("x");
                        break;
                    case Stone.BLACK:
                        builder.append("o");
                        break;
                    case Stone.EMPTY:
                    default:
                        builder.append("_");
                }

                if (cols < boardSize - 1) {
                    builder.append("|");
                }
            }
            builder.append("|\n");
            if (rows < boardSize - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

    String toJson() {
        StringBuilder builder = new StringBuilder("{\"size\":");
        builder.append(boardSize);
        builder.append(",");
        builder.append("\"board\":");
        builder.append("[");
        for (int rows = 0; rows < boardSize; rows++) {
            builder.append("[");
            for (int cols = 0; cols < boardSize; cols++) {
                builder.append(get(getItemPosition(rows, cols)));
                if (cols < boardSize - 1) {
                    builder.append(",");
                }
            }
            builder.append("]");
            if (rows < boardSize - 1) {
                builder.append(",");
            }
        }
        return builder.append("]}").toString();
    }

    void restart() {
        clear();
        init();
        start();
    }

    private void init() {
        for(int row=0 ; row < boardSize ; row++){
            for(int col=0 ; col < boardSize ; col++){
                add(new Move(row, col), Stone.EMPTY);
            }
        }
    }

    boolean canMove(@Stone.Color int stone) {
        final Board copy = clone();

        // 1) Find Empty Stones
        final Iterator<Stone> iterator = copy.iterator();
        while (iterator.hasNext()){
            if(iterator.next().color() != Stone.EMPTY){
                iterator.remove();
            }
        }

        // 2) Generate Moves for Empty Stones
        for(final Stone empty : copy){
            final Move move = new Move(empty.row(), empty.col());
            final Board copyOfCurrentBoard = clone();
            // 3) Check if the move actually flips some stones;
            if(copyOfCurrentBoard.apply(move, stone).size() > 0) {
                return true;
            }
        }

        return false;
    }

    @NonNull
    List<Move> findAllMoves(@Stone.Color int stone) {
        final Board copy = clone();

        // 1) Find Empty Stones
        final Iterator<Stone> iterator = copy.iterator();
        while (iterator.hasNext()){
            if(iterator.next().color() != Stone.EMPTY){
                iterator.remove();
            }
        }


        // 2) Generate Moves for Empty Stones
        final List<Move> possibleMoves = new ArrayList<>();
        for(final Stone empty : copy){
            final Move move = new Move(empty.row(), empty.col());
            final Board copyOfCurrentBoard = clone();
            // 3) Check if the move actually flips some stones;
            if(copyOfCurrentBoard.apply(move, stone).size() > 0) {
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    @Override
    public Board clone() {
        super.clone();
        Board clone = new Board(boardSize);
        clone.clear();
        for (final Stone item : this){
            clone.add(item.clone());
        }
        return clone;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    List<Stone> getStones() {
        return this;
    }
}
