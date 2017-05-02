package nl.entreco.reversi.model.players;

import org.junit.Before;
import org.junit.Test;

import nl.entreco.reversi.model.Stone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserPlayerTest {

    private UserPlayer subject;

    @Before
    public void setUp() throws Exception {
        subject = new UserPlayer();
        subject.setStoneColor(Stone.WHITE);
    }

    @Test
    public void itShouldReturnCorrectStoneColor() throws Exception {
        assertEquals(Stone.WHITE, subject.getStoneColor());
    }

    @Test
    public void isHuman() throws Exception {
        assertTrue(subject.isHuman());
    }

}