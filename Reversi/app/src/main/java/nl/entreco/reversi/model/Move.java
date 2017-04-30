package nl.entreco.reversi.model;

import java.util.ArrayList;

public class Move extends ArrayList<Integer> {

    private static final long serialVersionUID = 6405366992958717476L;

    Move() {
    }

    public Move(int row, int col) {
        add(row);
        add(col);
    }

    public int getRow(){
        return get(0);
    }
    public int getCol(){
        return get(1);
    }

    boolean isValid() {
        if(size() != 2) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", getRow(), getCol());
    }
}
