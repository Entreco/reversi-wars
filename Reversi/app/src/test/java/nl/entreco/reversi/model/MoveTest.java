package nl.entreco.reversi.model;

import android.support.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MoveTest {

    private Move subject;

    @Test
    public void getRow() throws Exception {
        createMove(0,1);
        assertEquals(0, subject.getRow());
    }

    @Test
    public void getCol() throws Exception {
        createMove(0,1);
        assertEquals(1, subject.getCol());
    }

    @Test
    public void isValid() throws Exception {
        createMove(0,1);
        assertTrue(subject.isValid());
    }

    @Test
    public void itShouldAllowNotAllowMovesWithMoreThanTwoElements() throws Exception {
        createMove(0,1,1);
        assertFalse(subject.isValid());
    }

    @Test
    public void itShouldAllowNotAllowMovesWithLessThanTwoElements() throws Exception {
        createMove(1);
        assertFalse(subject.isValid());

        createMove();
        assertFalse(subject.isValid());
    }

    @Test
    public void itShouldSetCorrectRowAndCol() throws Exception {
        final Move move = new Move(0, 4);

        assertEquals(0, move.getRow());
        assertEquals(4, move.getCol());
    }

    @Test
    public void itShouldOutputValidJson() throws Exception {
        createMove(2, 4);
        assertEquals("[2,4]", subject.toString());

    }

    void createMove(@NonNull Integer... values){
        subject = new Move();
        for(final int value : values) {
            subject.add(value);
        }
    }

}