package nl.entreco.reversi.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    private static final String EMPTY = "{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}";
    private static final String START = "{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}";


    private static final int BOARD_SIZE = 8;

    private Board subject;

    @Before
    public void setUp() throws Exception {
        subject = new Board(BOARD_SIZE);
    }

    @Test
    public void itShouldReportBoardSize() throws Exception {
        assertEquals(BOARD_SIZE, subject.getBoardSize());
    }

    @Test
    public void itShouldCreateEmptyBoard() throws Exception {
        assertEquals(EMPTY, subject.toJson());
    }

    @Test
    public void itShouldCreateStartPosition() throws Exception {
        subject.start();
        assertEquals(START, subject.toJson());
    }

    @Test
    public void itShouldGetCorrectStone() throws Exception {
        assertEquals(Stone.EMPTY, subject.get(0).color());
    }

    @Test
    public void itShouldFlipCorrectStone1() throws Exception {
        subject.apply(new Move(0, 1), Stone.WHITE);
        assertEquals("{\"board\":[[0,-1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone2() throws Exception {
        subject.apply(new Move(1, 0), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[-1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone3() throws Exception {
        subject.apply(new Move(0, 1), Stone.BLACK);
        assertEquals("{\"board\":[[0,1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone4() throws Exception {
        subject.apply(new Move(1, 0), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipAllWhiteStonesInLine() throws Exception {
        subject.start();
        subject.apply(new Move(2, 3), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(4, 2), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,-1,-1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(5, 1), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,1,-1,-1,0,0,0],[0,1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(5, 1), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,1,-1,-1,0,0,0],[0,1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldNotFlipCorners() throws Exception {
        subject.start();
        subject.apply(new Move(2, 3), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
        subject.apply(new Move(1, 3), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,-1,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
        subject.apply(new Move(0, 3), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,1,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
        subject.apply(new Move(5, 3), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,1,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,-1,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrect() throws Exception {
        subject.start();
        subject.apply(new Move(2, 3), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(2, 2), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,-1,1,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(3, 2), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,-1,1,0,0,0,0],[0,0,1,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(2, 4), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,-1,-1,-1,0,0,0],[0,0,1,1,-1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(1, 5), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,1,0,0,0],[0,0,1,1,-1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(4, 2), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,1,0,0,0],[0,0,-1,1,-1,0,0,0],[0,0,-1,-1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(5, 5), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,1,0,0,0],[0,0,-1,1,-1,0,0,0],[0,0,-1,-1,1,0,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(4, 5), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,1,0,0,0],[0,0,-1,1,-1,0,0,0],[0,0,-1,-1,-1,-1,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(3, 5), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,1,0,0,0],[0,0,-1,1,1,1,0,0],[0,0,-1,-1,-1,1,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(2, 5), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,-1,-1,0,0],[0,0,-1,1,-1,1,0,0],[0,0,-1,-1,-1,1,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(4, 1), Stone.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,0,0],[0,0,-1,-1,-1,-1,0,0],[0,0,-1,1,-1,1,0,0],[0,1,1,1,1,1,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(0, 5), Stone.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,-1,0,0],[0,0,0,0,0,-1,0,0],[0,0,-1,-1,-1,-1,0,0],[0,0,-1,1,-1,1,0,0],[0,1,1,1,1,1,0,0],[0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        System.out.println(subject);
    }

    @Test
    public void itShouldFlipLeftCorrect() throws Exception {
        subject.start();

        subject.apply(new Move(3, 2), Stone.BLACK);
        subject.apply(new Move(4, 2), Stone.WHITE);
        System.out.println(subject);
    }

    @Test
    public void itShouldReturnPossibleMovesForWhite() throws Exception {
        subject.start();

        assertEquals(4, subject.findMoves(Stone.WHITE).size());
    }

    @Test
    public void itShouldReturnPossibleMovesForBlack() throws Exception {
        subject.start();

        assertEquals(4, subject.findMoves(Stone.BLACK).size());
    }

    @Test
    public void iWant100ProcentCoverage() throws Exception {
        subject.start();

        assertNotEquals(subject.toJson(), subject.toString());
    }
}