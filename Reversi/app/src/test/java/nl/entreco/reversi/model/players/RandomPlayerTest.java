package nl.entreco.reversi.model.players;

import android.os.Handler;
import android.support.annotation.NonNull;

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
    @Mock private Handler mockHandler;

    @Captor private ArgumentCaptor<Runnable> runnable;

    @Before
    public void setUp() throws Exception {
        subject = new RandomPlayer() {
            @NonNull
            @Override
            protected Handler getMainLooper() {
                return mockHandler;
            }
        };
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

        subject.yourTurn("");

        verify(mockGameCallback, never()).submitMove(any(Player.class), any(Move.class));
    }

    @Test
    public void itShouldForwardMoveToGameCallback() throws Exception {
        subject.yourTurn("");
        verify(mockGameCallback).submitMove(eq(subject), any(Move.class));
    }

    @Test
    public void itShouldNotifyGameCallbackItsOurTurn() throws Exception {
        subject.yourTurn("");

        verify(mockGameCallback).setCurrentPlayer(subject);
    }

    @Test
    public void itShouldRetryAfterSmallDelayToAvoidStackOverflowException() throws Exception {
        subject.onMoveRejected("");
        verify(mockHandler).postDelayed(any(Runnable.class), eq(10L));
    }

    @Test
    public void itShouldTryAgainWhenMoveRejected() throws Exception {
        subject.onMoveRejected("");

        verify(mockHandler).postDelayed(runnable.capture(), eq(10L));
        runnable.getValue().run();

        verify(mockGameCallback).submitMove(eq(subject), any(Move.class));
    }

    @Test
    public void isHuman() throws Exception {
        assertFalse(subject.isHuman());
    }

}