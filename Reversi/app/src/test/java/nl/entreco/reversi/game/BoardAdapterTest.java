package nl.entreco.reversi.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoardAdapterTest {

    private BoardAdapter subject;

    @Mock private Arbiter mockArbiter;
    @Mock private Stone mockStone;
    @Mock private Board mockBoard;
    @Mock private GameCallback mockCallback;
    @Mock private Player mockHumanPlayer;
    @Mock private Player mockRandomPlayer;
    @Mock private Move mockMove;

    @Before
    public void setUp() throws Exception {
        when(mockHumanPlayer.isHuman()).thenReturn(true);
        when(mockRandomPlayer.isHuman()).thenReturn(false);

        simulateBoard(mockBoard);
    }

    @Test
    public void itShouldIgnoreClicksWhenNoGameCallbackIsSet() throws Exception {
        subject.onStoneClicked(mockStone);

        verifyZeroInteractions(mockArbiter, mockStone);
    }

    @Test
    public void itShouldDelegateClicksIfCurrentPlayerIsHuman() throws Exception {
        subject.setCurrentPlayer(mockHumanPlayer, mockCallback);

        subject.onStoneClicked(mockStone);

        verify(mockCallback).submitMove(eq(mockHumanPlayer), any(Move.class));
    }

    @Test
    public void itShouldNotDelegateClicksIfCurrentPlayerIsNotHuman() throws Exception {
        subject.setCurrentPlayer(mockRandomPlayer, mockCallback);

        subject.onStoneClicked(mockStone);

        verifyZeroInteractions(mockArbiter, mockStone);
    }

    @Test
    public void itShouldApplyUpdatesOnTheBoard() throws Exception {
        subject.update(mockMove, Stone.BLACK);
        verify(mockBoard).apply(mockMove, Stone.BLACK);
    }

    @Test
    public void itShouldClearGameCallbackWhenNonHumanPlayer() throws Exception {
        subject.setCurrentPlayer(mockRandomPlayer, mockCallback);
        assertNull(subject.getGameCallback());
    }

    @Test
    public void itShouldSetGameCallbackWhenNonHumanPlayer() throws Exception {
        subject.setCurrentPlayer(mockHumanPlayer, mockCallback);
        assertEquals(mockCallback, subject.getGameCallback());
    }

    private void simulateBoard(Board board) {
        when(mockArbiter.getBoard()).thenReturn(board);
        subject = new BoardAdapter(mockArbiter){
            @Override
            boolean shouldAnimate() {
                return false;
            }
        };

        verify(mockArbiter).getBoard();
    }

}