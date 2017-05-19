package nl.entreco.reversi.model;

import android.os.Handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTimerTest {

    private GameTimer subject;

    @Mock GameTimer.Callback mockCallback;
    @Mock ScheduledExecutorService mockExecutor;
    @Mock Handler mockHandler;
    @Mock private ScheduledFuture<?> mockScheduledFuture;
    @Mock private Player mockPlayer;

    @Captor ArgumentCaptor<Runnable> runnableCaptor;

    @Test
    public void itShouldStartTimerOnStart() throws Exception {
        simulateStart(4);

        verify(mockExecutor).schedule(subject, 4, TimeUnit.MILLISECONDS);
        assertNotNull(subject.scheduledFuture);
    }

    @Test
    public void itShouldNotifyCallbackAfterTimeout() throws Exception {
        simulateStart(1L);

        subject.onTimedout();

        verify(mockHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(mockCallback).onTimedOut(mockPlayer);
    }

    @Test
    public void itShouldClearCallbackOnStop() throws Exception {
        simulateStart(1L);

        simulateStopped();
    }

    @Test
    public void itShouldRestartAfterBeingStopped() throws Exception {
        simulateStart(1L);
        simulateStopped();

        subject.start(mockCallback, mockPlayer, 1L);
        verify(mockExecutor, times(2)).schedule(subject, 1L, TimeUnit.MILLISECONDS);
    }

    private void simulateStart(long timeout) {
        when(mockExecutor.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenAnswer(
                new Answer<ScheduledFuture>() {
                    @Override
                    public ScheduledFuture answer(InvocationOnMock invocation) throws Throwable {
                        return mockScheduledFuture;
                    }
                });

        subject = new GameTimer(mockExecutor, mockHandler);
        subject.start(mockCallback, mockPlayer, timeout);
        assertNotNull(subject.callback);
    }

    private void simulateStopped() {
        subject.stop();
        verifyStopped();
    }

    private void verifyStopped() {
        verify(mockScheduledFuture).cancel(true);
        assertNull(subject.scheduledFuture);

        verify(mockCallback, never()).onTimedOut(any(Player.class));
        assertNull(subject.callback);
    }

}