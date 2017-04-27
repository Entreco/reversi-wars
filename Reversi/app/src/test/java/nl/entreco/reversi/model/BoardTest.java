package nl.entreco.reversi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

    private static final String EMPTY = "{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}";
    private static final String START = "{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,-1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}";

    @InjectMocks private Board subject;

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
    public void itShouldFlipCorrectStone1() throws Exception {
        subject.apply(new Move(0, 1), Board.WHITE);
        assertEquals("{\"board\":[[0,-1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone2() throws Exception {
        subject.apply(new Move(1, 0), Board.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[-1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone3() throws Exception {
        subject.apply(new Move(0, 1), Board.BLACK);
        assertEquals("{\"board\":[[0,1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipCorrectStone4() throws Exception {
        subject.apply(new Move(1, 0), Board.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[1,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }

    @Test
    public void itShouldFlipAllWhiteStonesInLine() throws Exception {
        subject.start();
        subject.apply(new Move(2, 3), Board.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,0,1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(4, 2), Board.WHITE);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,-1,-1,-1,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());

        subject.apply(new Move(5, 1), Board.BLACK);
        assertEquals("{\"board\":[[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,1,0,0,0,0],[0,0,0,1,1,0,0,0],[0,0,1,-1,-1,0,0,0],[0,1,0,0,0,0,0,0],[0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0]]}", subject.toJson());
    }
}