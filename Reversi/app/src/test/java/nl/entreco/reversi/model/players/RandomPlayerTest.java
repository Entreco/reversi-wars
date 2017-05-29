package nl.entreco.reversi.model.players;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RandomPlayerTest {

    private RandomPlayer subject;

    @Mock private GameCallback mockGameCallback;

    @Captor private ArgumentCaptor<Runnable> runnable;
    @Captor private ArgumentCaptor<Runnable> commandCaptor;

    @Before
    public void setUp() throws Exception {
        subject = new RandomPlayer();
        subject.setStoneColor(Stone.BLACK);
        subject.setCallback(mockGameCallback);
    }

    @Test
    public void itShouldReturnCorrectStoneColor() throws Exception {
        assertEquals(Stone.BLACK, subject.getStoneColor());
    }

    @Test
    public void itShouldNotSubmitMoveIfNoCallbackIsSet() throws Exception {
        subject.setCallback(null);

        simulateTurn("");

        verify(mockGameCallback, never()).submitMove(any(Player.class), any(Move.class));
    }

    @Test
    public void itShouldForwardMoveToGameCallback() throws Exception {
        simulateTurn("");
        verify(mockGameCallback).submitMove(eq(subject), any(Move.class));
    }

    @Test
    public void itShouldNotifyGameCallbackItsOurTurn() throws Exception {
        simulateTurn("");
        verify(mockGameCallback).setCurrentPlayer(subject);
    }

    @Test
    public void itShouldNotifyGameCallbackWhenMoveRejected() throws Exception {
        simulateRejected("");
        verify(mockGameCallback).onMoveRejected(subject);
    }

    @Test
    public void itShouldTryAgainWhenMoveRejected() throws Exception {
        simulateRejected("");
        verify(mockGameCallback).onMoveRejected(subject);
        verify(mockGameCallback).setCurrentPlayer(subject);
    }

    @Test
    public void isHuman() throws Exception {
        assertFalse(subject.isHuman());
    }

    private void simulateTurn(final String board) {
        subject.yourTurn(board);
    }

    private void simulateRejected(final String board) {
        subject.onMoveRejected(board);
    }

}