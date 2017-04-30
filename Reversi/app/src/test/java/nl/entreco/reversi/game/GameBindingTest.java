package nl.entreco.reversi.game;

import android.view.View;
import android.view.ViewPropertyAnimator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameBindingTest {

    @Mock private View mockView;
    @Mock private Player mockPlayer;
    @Mock private ViewPropertyAnimator mockProperyAnimator;

    @Test
    public void itShouldStartAnimationWhenCurrentPlayerChanges() throws Exception {

        when(mockPlayer.getStoneColor()).thenReturn(Stone.WHITE);
        when(mockView.animate()).thenReturn(mockProperyAnimator);
        when(mockProperyAnimator.scaleX(anyFloat())).thenReturn(mockProperyAnimator);
        when(mockProperyAnimator.scaleY(anyFloat())).thenReturn(mockProperyAnimator);
        when(mockProperyAnimator.alpha(anyFloat())).thenReturn(mockProperyAnimator);

        Game.setPlayerHere(mockView, Stone.BLACK, mockPlayer);

        verify(mockView).animate();
    }
}