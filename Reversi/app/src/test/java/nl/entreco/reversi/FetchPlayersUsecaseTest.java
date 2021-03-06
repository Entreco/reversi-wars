package nl.entreco.reversi;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import nl.entreco.reversi.api.PlayerData;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.players.RemotePlayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchPlayersUsecaseTest {

    FetchPlayersUsecase subject;

    @Mock private FirebaseDatabase mockDatabase;
    @Mock private DatabaseReference mockDatabaseReference;
    @Mock private FetchPlayersUsecase.Callback mockCallback;
    @Mock private PlayerData mockPlayerData;
    @Mock private DataSnapshot mockSnapShot;
    @Mock private RemotePlayer mockRemotePlayer;
    @Captor private ArgumentCaptor<ChildEventListener> childEventCaptor;
    @Captor private ArgumentCaptor<ValueEventListener> valueEventCaptor;

    @Before
    public void setUp() throws Exception {

        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);

        subject = new FetchPlayersUsecase(mockDatabase){
            @NonNull
            @Override
            DatabaseReference getDbRef() {
                return mockDatabaseReference;
            }
        };
        verify(mockDatabase).getReference("players");
    }

    @Test
    public void itShouldAddEventListenerWhenFetching() throws Exception {
        subject.registerCallback(mockCallback);

        verify(mockDatabaseReference).addChildEventListener(childEventCaptor.capture());
        assertNotNull(childEventCaptor.getValue());
    }

    @Test
    public void itShouldFetchSevenLocalPlayersWhenCallbackIsRegistered() throws Exception {
        subject.registerCallback(mockCallback);

        verify(mockCallback, times(5)).onPlayerRetrieved(any(Player.class));
    }

    @Test
    public void itShouldFetchNoLocalPlayersWhenCallbackIsNotRegistered() throws Exception {
        subject.registerCallback(null);

        verify(mockCallback, never()).onPlayerRetrieved(any(Player.class));
    }

    @Test
    public void itShouldPingPlayerWhenRetrieved() throws Exception {
        simulateRemotePlayers(Arrays.asList(mockPlayerData));

        verify(mockDatabaseReference, atLeast(2)).child(anyString());
    }

    @Test
    public void itShouldFetchRemotePlayersWhenCallbackIsRegistered() throws Exception {
        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);
        when(mockSnapShot.exists()).thenReturn(true);

        subject.registerCallback(mockCallback);
        subject.pingPlayer(mockRemotePlayer, "uid", "name");

        verify(mockDatabaseReference).addValueEventListener(valueEventCaptor.capture());
        valueEventCaptor.getValue().onDataChange(mockSnapShot);

        verify(mockCallback).onPlayerRetrieved(any(RemotePlayer.class));
    }

    @Test
    public void itShouldRegisterCallback() throws Exception {
        assertNull(subject.callback);

        subject.registerCallback(mockCallback);

        assertNotNull(subject.callback);
    }

    @Test
    public void itShouldBeAbleToHandleNulls() throws Exception {
        subject.registerCallback(null);

        assertNull(subject.callback);
    }

    @Test
    public void itShouldUnregisterCallback() throws Exception {
        assertNull(subject.callback);
        subject.unregisterCallback();
        assertNull(subject.callback);
    }

    @Test
    public void itShouldRemoveChildEventHandlerWhenUnregistering() throws Exception {
        subject.unregisterCallback();

        verify(mockDatabaseReference).removeEventListener(any(ChildEventListener.class));
    }

    @Test
    public void itShouldHandleOnChildAdded() throws Exception {
        subject.onChildAdded(mockSnapShot, "");

        verify(mockSnapShot).getValue(PlayerData.class);
    }

    @Test
    public void itShouldHandleGetValueWithInvalidData() throws Exception {
        when(mockSnapShot.getValue(PlayerData.class)).thenThrow(JsonSyntaxException.class);

        subject.onChildAdded(mockSnapShot, "");

        verify(mockCallback, never()).onPlayerRetrieved(any(Player.class));
    }

    private void simulateRemotePlayers(List<PlayerData> remotePlayers) {
        subject.registerCallback(mockCallback);

        verify(mockDatabaseReference).addChildEventListener(childEventCaptor.capture());

        int playerCounter = 0;
        for(final PlayerData data : remotePlayers) {
            final DataSnapshot dataSnapShot = mock(DataSnapshot.class);
            when(dataSnapShot.getValue(PlayerData.class)).thenReturn(data);
            when(dataSnapShot.getKey()).thenReturn("key");
            childEventCaptor.getValue().onChildAdded(dataSnapShot, String.valueOf(playerCounter));
            playerCounter++;
        }
    }
}