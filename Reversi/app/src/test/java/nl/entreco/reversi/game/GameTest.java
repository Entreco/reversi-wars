package nl.entreco.reversi.game;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private static final String GAME_UID = UUID.randomUUID().toString();

    Game subject;

    @Mock private BoardAdapter mockAdapter;
    @Mock private Arbiter mockArbiter;
    @Mock private CreateMatchUsecase mockCreateMatchUsecase;
    @Mock private DatabaseReference mockDatabaseReference;
    @Mock private Player mockP1;
    @Mock private Player mockP2;

    @Mock private Move mockMove;
    @Mock private Handler mockHandler;
    @Mock private ScheduledExecutorService mockScheduler;

    @Captor private ArgumentCaptor<Runnable> mainCaptor;

    @Before
    public void setUp() throws Exception {
        when(mockCreateMatchUsecase.createRemoteMatch(any(Player.class), any(Player.class))).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.getKey()).thenReturn(GAME_UID);

        subject = new Game(mockAdapter, mockArbiter, mockCreateMatchUsecase){
            @NonNull
            @Override
            Handler setupHandler() {
                return mockHandler;
            }

            @NonNull
            @Override
            ScheduledExecutorService setupScheduler() {
                return mockScheduler;
            }
        };
    }

    @Test
    public void itShouldSetGameCallbackOnPlayersOnStart() throws Exception {
        subject.setWhitePlayer(mockP1);
        subject.setBlackPlayer(mockP2);
        subject.startGame();

        verify(mockP1).setCallback(subject);
        verify(mockP2).setCallback(subject);
    }

    @Test
    public void itShouldRestartArbiterOnStart() throws Exception {
        subject.startGame();

        verify(mockArbiter).start(subject);
    }

    @Test
    public void itShouldNotifyAdapterChangedOnStart() throws Exception {
        subject.startGame();

        verify(mockAdapter).start();
    }

    @Test
    public void submitMove() throws Exception {
        simulateValidMove(
                Arrays.asList(new Stone(0, 1, Stone.BLACK), new Stone(0, 2, Stone.BLACK)), mockP1, mockMove);

        verify(mockAdapter).update(mockMove, mockP1.getStoneColor());
        verify(mockArbiter).notifyNextPlayer(mockP1);
    }

    @Test
    public void currentPlayer() throws Exception {
        simulateSetCurrentPlayer(mockP1);

        assertEquals(mockP1, subject.current.get());
        verify(mockAdapter).setCurrentPlayer(mockP1, subject);
    }

    @Test
    public void itShouldSetRejectedPlayer() throws Exception {
        simulateMoveRejected(mockP1);

        assertEquals(mockP1, subject.rejected.get());

    }

    private void simulateMoveRejected(Player player) {
        subject.onMoveRejected(player);
        verify(mockHandler).post(mainCaptor.capture());
        mainCaptor.getValue().run();
    }

    private void simulateValidMove(List<Stone> stones, Player player, Move move) {
        when(mockArbiter.onMoveReceived(any(Player.class), anyString())).thenReturn(stones);
        simulateMove(player, move);
    }

    private void simulateMove(Player player, Move move) {
        subject.submitMove(player, move);
        verify(mockHandler).post(mainCaptor.capture());
        mainCaptor.getValue().run();
    }

    private void simulateSetCurrentPlayer(Player mockP1) {
        subject.setCurrentPlayer(mockP1);
        verify(mockHandler).post(mainCaptor.capture());
        mainCaptor.getValue().run();
    }

}