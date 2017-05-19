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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RandomPlayerTest {

    private RandomPlayer subject;

    @Mock private GameCallback mockGameCallback;
    @Mock private Handler mockHandler;
    @Mock private ScheduledExecutorService mockExecutor;

    @Captor private ArgumentCaptor<Runnable> runnable;
    @Captor private ArgumentCaptor<Runnable> commandCaptor;

    @Before
    public void setUp() throws Exception {
        subject = new RandomPlayer() {
            @NonNull
            @Override
            protected Handler getMainLooper() {
                return mockHandler;
            }

            @NonNull
            @Override
            ScheduledExecutorService getExecutor() {
                return mockExecutor;
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
        reset(mockGameCallback, mockHandler, mockExecutor);
        simulateTurn("");
        verify(mockGameCallback).setCurrentPlayer(subject);
    }

    @Test
    public void isHuman() throws Exception {
        assertFalse(subject.isHuman());
    }

    private void simulateTurn(final String board) {
        subject.yourTurn(board);
        verify(mockExecutor).schedule(commandCaptor.capture(), eq(50L), eq(TimeUnit.MILLISECONDS));
        commandCaptor.getValue().run();
        verify(mockHandler, times(2)).post(runnable.capture());
        for(final Runnable r : runnable.getAllValues()){
            r.run();
        }
    }

    private void simulateRejected(final String board) {
        subject.onMoveRejected(board);
        verify(mockExecutor).schedule(commandCaptor.capture(), eq(10L), eq(TimeUnit.MILLISECONDS));
        commandCaptor.getValue().run();
        verify(mockHandler).post(runnable.capture());
        runnable.getValue().run();
    }

}