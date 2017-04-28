package nl.entreco.reversi.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Stone {
    public @Value int value() {
        return value;
    }

    @Retention(SOURCE)
    @IntDef({WHITE, BLACK, EMPTY})
    public @interface Value {

    }
    public static final @Value int WHITE = -1;
    public static final @Value int BLACK = 1;
    public static final @Value int EMPTY = 0;

    private final int row;
    private final int col;
    private @Value int value;

    public Stone(int row, int col, @Value int stone) {
        this.row = row;
        this.col = col;
        this.value = stone;
    }

    public int row(){
        return row;
    }
    public int col(){
        return col;
    }
    public void set(@Value int val) {
        this.value = val;
    }
    public void flip(){
        switch (value){
            case Stone.BLACK: value = Stone.WHITE; break;
            case Stone.WHITE: value = Stone.BLACK; break;
            default: break;
        }
    }

    public int getItemPosition(){
        return row * 8 + col;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
