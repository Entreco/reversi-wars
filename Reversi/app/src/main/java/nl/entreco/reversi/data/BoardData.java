package nl.entreco.reversi.data;

import com.google.gson.annotations.SerializedName;

import nl.entreco.reversi.model.Board;

public class BoardData {
    @SerializedName("size")
    private int size;

    @SerializedName("board")
    private int[][] rawBoard;

    public Board getBoard(){
        final Board board = new Board(size);
        board.setupWithData(rawBoard);
        return board;
    }
}
