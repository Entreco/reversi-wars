package nl.entreco.reversi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import nl.entreco.reversi.data.FetchPlayersUsecase;
import nl.entreco.reversi.data.RemoteUsecase;
import nl.entreco.reversi.game.Game;
import nl.entreco.reversi.model.Player;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReversiViewModelTest {

    private static final String GAME_UID = UUID.randomUUID().toString();

    @InjectMocks ReversiViewModel subject;
    @Mock Game mockGame;
    @Mock RemoteUsecase mockRemoteUsecase;
    @Mock FetchPlayersUsecase mockFetchPlayersUsecase;

    @Mock private Player mockPlayer1;
    @Mock private Player mockPlayer2;

    @Test
    public void itShouldFetchPlayers() throws Exception {
        subject.fetchPlayers();

        verify(mockRemoteUsecase).fetchPlayers(subject);
    }

    @Test
    public void itShouldClearEverythingWhenFetchingPlayers() throws Exception {
        subject.fetchPlayers();

        assertNull(subject.player1.get());
        assertNull(subject.player2.get());
        assertTrue(subject.players.isEmpty());
        assertNull(subject.game.get());

        verify(mockGame).clear();
    }

    @Test
    public void itShouldAddPlayerToPlayerListWhenRetrieved() throws Exception {
        subject.onPlayerRetrieved(mockPlayer1);

        assertTrue(subject.players.contains(mockPlayer1));
    }

    @Test
    public void itShouldStoreFirstSelectedPlayerAsPlayer1() throws Exception {
        subject.onPlayerSelected(mockPlayer1);
        assertTrue(subject.player1.get().equals(mockPlayer1));
    }

    @Test
    public void itShouldStoreSecondSelectedPlayerAsPlayer2() throws Exception {
        subject.onPlayerSelected(mockPlayer1);
        assertTrue(subject.player1.get().equals(mockPlayer1));

        subject.onPlayerSelected(mockPlayer2);
        assertTrue(subject.player2.get().equals(mockPlayer2));
    }

    @Test
    public void itShouldStartNewGameAfterSecondPlayerSelected() throws Exception {
        subject.onPlayerSelected(mockPlayer1);
        subject.onPlayerSelected(mockPlayer2);

        verify(mockRemoteUsecase).createMatch(subject, mockPlayer1, mockPlayer2);
    }

    @Test
    public void itShouldRemovePlayerFromTheListWhenSelected() throws Exception {
        subject.onPlayerSelected(mockPlayer1);

        assertFalse(subject.players.contains(mockPlayer1));
    }

    @Test
    public void itShouldStoreSelectedPlayersInFieldAndNotInTheGameItself() throws Exception {
        subject.onPlayerSelected(mockPlayer1);
        assertTrue(subject.player1.get().equals(mockPlayer1));
        assertNull(subject.game.get());

        subject.onPlayerSelected(mockPlayer2);
        assertTrue(subject.player2.get().equals(mockPlayer2));
    }
}