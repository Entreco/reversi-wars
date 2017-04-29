package nl.entreco.reversi.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.TimerTask;

import nl.entreco.reversi.model.players.NotStartedPlayer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RefereeTest {

    private static final String VALID_MOVE_1 = "[2,3]";
    private static final String VALID_MOVE_2 = "[2,2]";

    private Referee subject;

    @Mock private GameSettings mockGameSettings;

    @Mock private Player mockPlayer;
    @Mock private Player mockOpponent;

    @Captor private ArgumentCaptor<TimerTask> timerTask;

    @Before
    public void setUp() throws Exception {
        when(mockGameSettings.getStartIndex()).thenReturn(0);
        when(mockGameSettings.getBoardSize()).thenReturn(8);

        subject = new Referee(mockGameSettings);
    }

    @Test
    public void itShouldInitializeEmptyPlayersList() throws Exception {
        assertNotNull(subject.getPlayers());
    }

    @Test
    public void itShouldReturnDummyPlayerWhenGameNotStartedYet() throws Exception {
        assertNotNull(subject.getCurrentPlayer());
        assertTrue(subject.getCurrentPlayer() instanceof NotStartedPlayer);
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
    public void itShouldReturnFirstPlayerWhenStartingNewMatch() throws Exception {
        simulateMatchStarted(mockPlayer, mock(Player.class));
        assertEquals(mockPlayer, subject.getCurrentPlayer());
    }

    @Test
    public void itShouldClearPlayersOnRestart() throws Exception {
        subject.restart();
        assertTrue(subject.getPlayers().isEmpty());
    }

    @Test
    public void itShouldNotifyFirstPlayerAfterRestart() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);
        subject.restart();
        subject.addPlayer(mockPlayer);
        subject.addPlayer(mockOpponent);
        subject.startMatch();

        verify(mockPlayer).yourTurn();
    }


    @Test
    public void itShouldRevertAfterInvalidMove() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);
        String startBoardLayout = subject.getBoard().toJson();

        simulateTurn(mockPlayer, "[\"invalid\":\"move\"]");
        assertEquals(startBoardLayout, subject.getBoard().toJson());
    }

    @Test
    public void itShouldNotifySecondPlayerOnMoveReceived() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, VALID_MOVE_1);
        verify(mockOpponent).yourTurn();
    }

    @Test
    public void itShouldNotifyNextPlayerWhenTimedOut() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);
        simulateTimedOut(mockPlayer);

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

        simulateTurn(mockPlayer, VALID_MOVE_1);
        verify(mockOpponent).yourTurn();

        simulateTurn(mockOpponent, VALID_MOVE_2);
        verify(mockPlayer).yourTurn();
    }

    @Test
    public void itShouldNotifyThirdPlayerAfterSecondPlayerMoveReceived() throws Exception {
        final Player mock = mock(Player.class);
        simulateMatchStarted(mock, mockPlayer, mockOpponent);

        simulateTurn(mock, VALID_MOVE_1);
        verify(mockPlayer).yourTurn();

        simulateTurn(mockPlayer, VALID_MOVE_2);
        verify(mockOpponent).yourTurn();
    }

    @Test
    public void itShouldLetPlayerKnowWhenMoveIsInvalid() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, "");

        verify(mockPlayer).onMoveRejected();
    }

    @Test
    public void itShouldRejectMoveWhenNotPlayersTurn() throws Exception {

        assertFalse(subject.isPlayersTurn(mockPlayer));
        assertFalse(subject.isPlayersTurn(mockOpponent));

        simulateMatchStarted(mockPlayer, mockOpponent);
        assertTrue(subject.isPlayersTurn(mockPlayer));
        assertFalse(subject.isPlayersTurn(mockOpponent));
    }

    @Test
    public void itShouldRejectMoveWhenNoStonesAreFlipped() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, "[0,0]");

        verify(mockPlayer).onMoveRejected();
    }


    @Test
    public void itShouldNotNotifyNextPlayerIfNoMovesAvailable() throws Exception {

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
        when(player.getStoneColor()).thenReturn(VALID_MOVE_1.equals(s) ? Stone.BLACK: Stone.WHITE);
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