package nl.entreco.reversi.model.util;

import com.google.gson.Gson;

import nl.entreco.reversi.data.BoardData;
import nl.entreco.reversi.model.Board;

public class BoardUtil {

    public static Board extractBoard(Gson gson, String board) {
        return gson.fromJson(board, BoardData.class).getBoard();
    }

}
