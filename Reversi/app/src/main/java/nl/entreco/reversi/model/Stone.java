package nl.entreco.reversi.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Stone {
    public @Color int color() {
        return value;
    }

    @Retention(SOURCE)
    @IntDef({WHITE, BLACK, EMPTY})
    public @interface Color {

    }
    public static final @Color int WHITE = -1;
    public static final @Color int BLACK = 1;
    public static final @Color int EMPTY = 0;

    private final int row;
    private final int col;
    private @Color int value;

    Stone(){
        this(0,0, EMPTY);
    }

    public Stone(int row, int col, @Color int stone) {
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
    public void set(@Color int val) {
        this.value = val;
    }
    public void flip(){
        switch (value){
            case Stone.BLACK: value = Stone.WHITE; break;
            case Stone.WHITE: value = Stone.BLACK; break;
            default: break;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
