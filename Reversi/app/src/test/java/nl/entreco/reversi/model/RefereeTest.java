package nl.entreco.reversi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RefereeTest {

    private static final String VALID_MOVE = "[1,2]";
    @InjectMocks private Referee subject;
    @Mock private Player mockPlayer;
    @Mock private Player mockOpponent;

    @Test
    public void itShouldInitializeEmptyPlayersList() throws Exception {
        assertNotNull(subject.getPlayers());
    }

    @Test
    public void itShouldAddPlayerToPlayersList() throws Exception {
        subject.addPlayer(mockPlayer);

        assertArrayEquals(Collections.singletonList(mockPlayer).toArray(),
                subject.getPlayers().toArray());
    }

    @Test
    public void itShouldAddPlayersOnlyOnce() throws Exception {
        subject.addPlayer(mockPlayer);
        subject.addPlayer(mockPlayer);

        assertEquals(1, subject.getPlayers().size());
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldNotStartWhenNumberOfPlayersLessThan2() throws Exception {
        subject.addPlayer(mockPlayer);
        subject.startMatch();

        verify(mockPlayer, never()).yourTurn();
    }

    @Test
    public void itShouldAddAllUniquePlayersToList() throws Exception {
        simulateMatchStarted(mock(Player.class), mock(Player.class), mock(Player.class));
        assertEquals(3, subject.getPlayers().size());
    }

    @Test
    public void itShouldNotifyFirstPlayerWhenMatchStarts() throws Exception {
        simulateMatchStarted(mockPlayer, mock(Player.class));
    }

    @Test
    public void itShouldNotifySecondPlayerOnMoveReceived() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, VALID_MOVE);
        verify(mockOpponent).yourTurn();
    }

    @Test
    public void itShouldNotifySecondPlayerOnTimeout() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTimedOut(mockPlayer);
        verify(mockOpponent).yourTurn();
    }

    @Test
    public void itShouldNotifyFirstPlayerAfterSecondPlayerMoveReceived() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, VALID_MOVE);
        verify(mockOpponent).yourTurn();

        simulateTurn(mockOpponent, VALID_MOVE);
        verify(mockPlayer).yourTurn();
    }

    @Test
    public void itShouldNotifyThirdPlayerAfterSecondPlayerMoveReceived() throws Exception {
        simulateMatchStarted(mock(Player.class), mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, VALID_MOVE);
        verify(mockOpponent).yourTurn();
    }

    @Test
    public void itShouldLetPlayerKnowWhenMoveIsInvalid() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, "");

        verify(mockPlayer).onMoveRejected();
    }

    @Test
    public void itShouldRejectNullMoves() throws Exception {
        assertFalse(subject.isValidPosition(null));
        assertFalse(subject.isValidPosition(""));
        assertFalse(subject.isValidPosition("[]"));
        assertFalse(subject.isValidPosition("[1]"));
        assertFalse(subject.isValidPosition("[1,2,3]"));
        assertFalse(subject.isValidPosition("{}"));
        assertFalse(subject.isValidPosition("{1}"));
        assertFalse(subject.isValidPosition("{1,2,3}"));
    }

    @Test
    public void itShouldRejectMovesWithOffTheBoard() throws Exception {
        assertFalse(subject.isValidPosition("[9,8]"));
        assertFalse(subject.isValidPosition("[-1,0]"));
    }

    @Test
    public void itShouldNotRejectValidMoves() throws Exception {
        assertTrue(subject.isValidPosition("[2,2]"));
        assertTrue(subject.isValidPosition("[0,1]"));
        assertTrue(subject.isValidPosition("[2,2]"));
        assertTrue(subject.isValidPosition("[2,4]"));
    }

    private void simulateTimedOut(Player player) {
        subject.onTimedOut(player);
    }

    private void simulateTurn(Player player, String s) {
        subject.onMoveReceived(player, s);
    }

    private void simulateMatchStarted(Player... players) {
        for (final Player player : players) {
            subject.addPlayer(player);
        }
        subject.startMatch();
        verify(players[0]).yourTurn();
        reset(players[0]);
    }
}