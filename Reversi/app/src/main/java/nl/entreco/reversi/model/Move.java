package nl.entreco.reversi.model;

import java.util.ArrayList;

class Move extends ArrayList<Integer> {

    private static final long serialVersionUID = 6405366992958717476L;

    public Move() {
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
        if(getRow() < 0 || getRow() > 8) return false;
        if(getCol() < 0 || getCol() > 8) return false;
        return true;
    }
}
