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
        assertEquals(-8, subject.evalSafeStones(board, Stone.WHITE));
        assertEquals(8, subject.evalSafeStones(board, Stone.BLACK));

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
}