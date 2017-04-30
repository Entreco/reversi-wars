package nl.entreco.reversi.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.model.players.BasePlayer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    Game subject;

    @Mock private BoardAdapter mockAdapter;
    @Mock private Arbiter mockArbiter;
    @Mock private BasePlayer mockP1;
    @Mock private BasePlayer mockP2;

    @Mock private Move mockMove;

    @Before
    public void setUp() throws Exception {
        subject = new Game(mockAdapter, mockArbiter, mockP1, mockP2);
    }

    @Test
    public void itShouldSetGameCallbackOnPlayersOnStart() throws Exception {
        subject.startGame();

        verify(mockP1).setCallback(subject);
        verify(mockP2).setCallback(subject);
    }

    @Test
    public void itShouldRestartArbiterOnStart() throws Exception {
        subject.startGame();

        verify(mockArbiter).restart();
    }

    @Test
    public void itShouldNotifyAdapterChangedOnStart() throws Exception {
        subject.startGame();

        verify(mockAdapter).start();
    }

    @Test
    public void submitMove() throws Exception {
        simulateValidMove(
                Arrays.asList(new Stone(0, 1, Stone.BLACK), new Stone(0, 2, Stone.BLACK)));
        subject.submitMove(mockP1, mockMove);

        verify(mockAdapter).update(mockMove, mockP1.getStoneColor());
        verify(mockArbiter).notifyNextPlayer(mockP1);
    }

    @Test
    public void currentPlayer() throws Exception {
        subject.setCurrentPlayer(mockP1);

        assertEquals(mockP1, subject.current.get());
        verify(mockAdapter).setCurrentPlayer(mockP1, subject);
    }

    private void simulateValidMove(List<Stone> stones) {
        when(mockArbiter.onMoveReceived(any(Player.class), anyString())).thenReturn(stones);
    }

}