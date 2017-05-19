package nl.entreco.reversi.model.players;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.util.BoardUtil;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BeatMePlayerTest {

    private static final String SAFE_STONES_BLACK = "{\"size\":8,\"board\":[[1,1,1,1,1,1,1,1],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[-1,0,0,0,0,0,0,-1]]}";
    private static final String CORNER_BOARD_4_WHITE = "{\"size\":8,\"board\":[[-1,0,0,0,0,0,0,-1],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[-1,0,0,0,0,0,0,-1]]}";
    private static final String CORNER_BOARD_4_BLACK = "{\"size\":8,\"board\":[[1,0,0,0,0,0,0,1],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[1,0,0,0,0,0,0,1]]}";
    private static final String CORNER_BOARD_2_BLACK = "{\"size\":8,\"board\":[[1,0,0,0,0,0,0,1],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[-1,0,0,0,0,0,0,-1]]}";

    @InjectMocks BeatMePlayer subject;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new GsonBuilder().create();

    }

    @Test
    public void itShouldEvaluateCorners() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, CORNER_BOARD_4_BLACK);
        assertEquals(-4, subject.evalCorners(board, Stone.WHITE));
        assertEquals(4, subject.evalCorners(board, Stone.BLACK));
    }

    @Test
    public void itShouldEvaluateEvenCorners() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, CORNER_BOARD_2_BLACK);
        assertEquals(0, subject.evalCorners(board, Stone.WHITE));
        assertEquals(0, subject.evalCorners(board, Stone.BLACK));
    }

    @Test
    public void itShouldEvaluateScoreForWhite() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, CORNER_BOARD_4_BLACK);
        assertEquals(-4, subject.evalScore(board, Stone.WHITE));
        assertEquals(4, subject.evalScore(board, Stone.BLACK));
    }

    @Test
    public void itShouldExtractSafeStones() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, SAFE_STONES_BLACK);
        System.out.println(board);
        assertEquals(2, subject.evalSafeStones(board, Stone.WHITE));
        assertEquals(2, subject.evalSafeStones(board, Stone.BLACK));

    }

    @Test
    public void itShouldEvaluateScoreForBlack() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, CORNER_BOARD_4_WHITE);
        assertEquals(4, subject.evalScore(board, Stone.WHITE));
        assertEquals(-4, subject.evalScore(board, Stone.BLACK));
    }

    @Test
    public void itShouldEvaluateEvenScore() throws Exception {
        final Board board = BoardUtil.extractBoard(gson, CORNER_BOARD_2_BLACK);
        assertEquals(0, subject.evalScore(board, Stone.WHITE));
        assertEquals(0, subject.evalScore(board, Stone.BLACK));
    }

    @Test
    public void itShouldZigZag() throws Exception {
        int[][] board = {{1,2,3},
                        {4,5,6},
                        {7,8,9}};
        assertEquals("142753",new ZigZag(board, 0).go());
        assertEquals("362951", new ZigZag(board, 1).go());
        assertEquals("748159", new ZigZag(board, 2).go());
        assertEquals("968357", new ZigZag(board, 3).go());
    }

    private class ZigZag {
        private final int[][] board;
        private final int direction;

        public ZigZag(int[][] board, int direction) {
            this.board = board;
            this.direction = direction;
        }

        public String go() {
            switch(direction){
                case 0: return topLeft();
                case 1: return topRight();
                case 2: return bottomLeft();
                case 3: return bottomRight();
            }
            return "";
        }

        private String bottomRight() {
            int row = board.length - 1;
            int col = board.length - 1;
            int steps = board.length;
            StringBuilder builder = new StringBuilder();
            for(int offset = 0; offset < steps ; offset ++) {
                for (int i = 0; i <= offset; i++) {
                    int checkRow = row - (offset - i);
                    int checkCol = col - i;
                    builder.append(board[checkRow][checkCol]);
                }
            }
            return builder.toString();
        }

        private String bottomLeft() {
            int row = board.length - 1;
            int col = 0;
            int steps = board.length;
            StringBuilder builder = new StringBuilder();
            for(int offset = 0; offset < steps ; offset ++) {
                for (int i = 0; i <= offset; i++) {
                    int checkRow = row - (offset - i);
                    int checkCol = col + i;
                    builder.append(board[checkRow][checkCol]);
                }
            }
            return builder.toString();
        }

        private String topLeft() {
            int row = 0;
            int col = 0;
            int steps = board.length;
            StringBuilder builder = new StringBuilder();
            for(int offset = 0; offset < steps ; offset ++) {
                for (int i = 0; i <= offset; i++) {
                    int checkRow = row + (offset - i);
                    int checkCol = col + i;
                    builder.append(board[checkRow][checkCol]);
                }
            }
            return builder.toString();
        }

        private String topRight() {
            int row = 0;
            int col = board.length - 1;
            int steps = board.length;
            StringBuilder builder = new StringBuilder();
            for(int offset = 0; offset < steps ; offset ++) {
                for (int i = 0; i <= offset; i++) {
                    int checkRow = row + (offset - i);
                    int checkCol = col - i;
                    builder.append(board[checkRow][checkCol]);
                }
            }
            return builder.toString();
        }
    }
}