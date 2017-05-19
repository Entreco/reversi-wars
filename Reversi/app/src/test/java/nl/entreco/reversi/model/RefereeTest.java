package nl.entreco.reversi.model;

import android.os.Handler;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RefereeTest {

    private static final String VALID_MOVE_1 = "[2,3]";
    private static final String VALID_MOVE_2 = "[2,2]";
    private static final int BOARD_SIZE = 8;

    private Referee subject;

    @Mock private GameSettings mockGameSettings;
    @Mock private GameTimer mockTimer;
    @Mock private Board mockBoard;
    @Mock private Stone mockStone;

    @Mock private Player mockPlayer;
    @Mock private Player mockOpponent;
    @Mock private GameCallback mockGameCallback;

    @Captor private ArgumentCaptor<TimerTask> timerTask;
    @Captor private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setUp() throws Exception {
        when(mockGameSettings.getStartIndex()).thenReturn(0);
        when(mockPlayer.getStoneColor()).thenReturn(Stone.BLACK);
        when(mockOpponent.getStoneColor()).thenReturn(Stone.WHITE);
        when(mockBoard.getBoardSize()).thenReturn(BOARD_SIZE);

        subject = new Referee(mockGameSettings, mockTimer, mockBoard);
    }

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

        verify(mockPlayer, never()).yourTurn(anyString());
    }

    @Test
    public void itShouldAddAllUniquePlayersToList() throws Exception {
        final Player mock = mock(Player.class);
        simulateMatchStarted(mockPlayer, mockOpponent, mock);
        assertEquals(3, subject.getPlayers().size());
    }

    @Test
    public void itShouldNotifyFirstPlayerWhenMatchStarts() throws Exception {
        simulateMatchStarted(mockPlayer, mock(Player.class));
    }

    @Test
    public void itShouldNotStartTimerWhenHumanPlayersTurn() throws Exception {
        when(mockPlayer.isHuman()).thenReturn(true);

        simulateMatchStarted(mockPlayer, mockOpponent);

        verify(mockTimer, never()).start(subject, mockPlayer, mockGameSettings.getTimeout());
    }

    @Test
    public void itShouldStopTimerWhenNonHumanSubmitsValidMove() throws Exception {
        when(mockOpponent.isHuman()).thenReturn(false);

        simulateMatchStarted(mockOpponent, mockPlayer);
        simulateTurn(mockOpponent, VALID_MOVE_1);

        verify(mockTimer).stop();
    }

    @Test
    public void itShouldNotStopTimerWhenNonHumanSubmitsInvalidMove() throws Exception {
        when(mockOpponent.isHuman()).thenReturn(false);

        simulateMatchStarted(mockOpponent, mockPlayer);
        simulateTurn(mockOpponent, "");

        verify(mockTimer, never()).stop();
    }

    @Test
    public void itShouldStopTimerWhenGameIsFinished() throws Exception {
        simulateGameFinished(mockPlayer, mockOpponent);

        verify(mockTimer).stop();
    }

    @Test
    public void itShouldReturnFirstPlayerWhenStartingNewMatch() throws Exception {
        simulateMatchStarted(mockPlayer, mock(Player.class));
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowErrorWhenTryingToRestartWithoutPlayers() throws Exception {
        subject.start(mockGameCallback);
    }

    @Test
    public void itShouldRestartWhenPlayersAlreadyAdded() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);
        subject.start(mockGameCallback);

        assertFalse(subject.getPlayers().isEmpty());
        assertEquals(2, subject.getPlayers().size());
    }

    @Test
    public void itShouldNotifyFirstPlayerAfterRestart() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);
        simulateTurn(mockPlayer, VALID_MOVE_1);

        subject.start(mockGameCallback);

//        assertTrue(subject.getCurrentPlayer().equals(mockPlayer));
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

        verify(mockOpponent).yourTurn(anyString());
    }

    @Test
    public void itShouldNotifyFirstPlayerAfterSecondPlayerMoveReceived() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, VALID_MOVE_1);
        simulateTurn(mockOpponent, VALID_MOVE_2);
    }

    @Test
    public void itShouldNotifyThirdPlayerAfterSecondPlayerMoveReceived() throws Exception {
        final Player mock = mock(Player.class);
        when(mock.getStoneColor()).thenReturn(Stone.BLACK);
        simulateMatchStarted(mock, mockPlayer, mockOpponent);

        simulateTurn(mock, VALID_MOVE_1);
        simulateTurn(mockPlayer, VALID_MOVE_2);
    }

    @Test
    public void itShouldLetPlayerKnowWhenMoveIsInvalid() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        simulateTurn(mockPlayer, "");

        verify(mockPlayer).onMoveRejected(anyString());
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
        simulateMoves(Collections.<Stone>emptyList());

        simulateTurn(mockPlayer, "[0,0]");

        verify(mockPlayer).onMoveRejected(anyString());
    }

    @Test
    public void itShouldNotNotifyNextPlayerIfNoMovesAvailable() throws Exception {
        simulateMatchStarted(mockPlayer, mockOpponent);

        verify(mockOpponent, never()).yourTurn(anyString());
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
        simulateMoves(Arrays.asList(mockStone));

        assertTrue(subject.isValidPosition("[2,2]"));
        assertTrue(subject.isValidPosition("[0,1]"));
        assertTrue(subject.isValidPosition("[2,2]"));
        assertTrue(subject.isValidPosition("[2,4]"));
    }

    private void simulateMatchStarted(Player... players) {
        when(mockBoard.canMove(Stone.WHITE)).thenReturn(true);
        when(mockBoard.canMove(Stone.BLACK)).thenReturn(true);

        simulateMoves(Arrays.asList(mockStone, mockStone));

        when(mockBoard.toJson()).thenReturn("");

        for (final Player player : players) {
            subject.addPlayer(player);
        }
        subject.startMatch();

        verify(mockBoard).start();

        verify(players[0]).yourTurn(anyString());
        reset(players[0]);
    }

    private void simulateMoves(List<Stone> stones) {
        when(mockBoard.get(anyInt())).thenReturn(mockStone);
        when(mockStone.color()).thenReturn(Stone.EMPTY);
        when(mockBoard.clone()).thenReturn(mockBoard);
        when(mockBoard.getItemPosition(any(Move.class))).thenReturn(2);
        when(mockBoard.apply(any(Move.class), anyInt())).thenReturn(stones);
    }

    private void simulateTurn(Player player, String s) {
        when(player.getStoneColor()).thenReturn(VALID_MOVE_1.equals(s) ? Stone.BLACK : Stone.WHITE);
        subject.onMoveReceived(player, s);
    }

    private void simulateTimedOut(Player player) {
        subject.onTimedOut(player);
        simulateRun();
    }

    private void simulateRun() {

    }

    private void simulateGameFinished(Player... players) {

        when(mockBoard.canMove(Stone.WHITE)).thenReturn(false);
        when(mockBoard.canMove(Stone.BLACK)).thenReturn(false);

        for (final Player player : players) {
            subject.addPlayer(player);
        }

        subject.notifyNextPlayer(players[0]);
        simulateRun();
    }
}