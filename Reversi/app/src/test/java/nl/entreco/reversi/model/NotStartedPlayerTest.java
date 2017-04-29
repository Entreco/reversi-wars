package nl.entreco.reversi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import nl.entreco.reversi.model.players.NotStartedPlayer;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NotStartedPlayerTest {

    @InjectMocks NotStartedPlayer subject;

    @Test
    public void yourTurn() throws Exception {
        subject.yourTurn();
    }

    @Test
    public void onMoveRejected() throws Exception {
        subject.onMoveRejected();
    }

    @Test
    public void getStoneColor() throws Exception {
        assertEquals(Stone.EMPTY, subject.getStoneColor());
    }

}