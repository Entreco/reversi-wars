package nl.entreco.reversi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class StoneTest {

    @InjectMocks private Stone subject;

    @Test
    public void value() throws Exception {
        assertEquals(Stone.EMPTY, subject.color());
    }

    @Test
    public void row() throws Exception {
        assertEquals(0, subject.row());
    }

    @Test
    public void col() throws Exception {
        assertEquals(0, subject.col());
    }

    @Test
    public void setEmpty() throws Exception {
        subject.set(Stone.EMPTY);
        assertEquals(Stone.EMPTY, subject.color());
    }
    @Test
    public void setBlack() throws Exception {
        subject.set(Stone.BLACK);
        assertEquals(Stone.BLACK, subject.color());
    }
    @Test
    public void setWhite() throws Exception {
        subject.set(Stone.WHITE);
        assertEquals(Stone.WHITE, subject.color());
    }

    @Test
    public void itShouldNotFlipWhenEmpty() throws Exception {
        @Stone.Color int initial = subject.color();
        subject.flip();
        assertEquals(initial, subject.color());
    }

    @Test
    public void itShouldFlipBlackToWhite() throws Exception {
        subject.set(Stone.BLACK);
        subject.flip();
        assertEquals(Stone.WHITE, subject.color());
    }

    @Test
    public void itShouldFlipWhiteToBlack() throws Exception {
        subject.set(Stone.WHITE);
        subject.flip();
        assertEquals(Stone.BLACK, subject.color());
    }

    @Test
    public void itShouldPrintBlackString() throws Exception {
        subject.set(Stone.BLACK);
        assertEquals("1", subject.toString());
    }

    @Test
    public void itShouldPrintWhiteString() throws Exception {
        subject.set(Stone.WHITE);
        assertEquals("-1", subject.toString());
    }

    @Test
    public void itShouldPrintEmptyString() throws Exception {
        assertEquals("0", subject.toString());
    }
}