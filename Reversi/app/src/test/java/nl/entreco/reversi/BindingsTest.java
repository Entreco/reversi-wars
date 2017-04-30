package nl.entreco.reversi;

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
public class BindingsTest {

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

        Bindings.doPlayerRelatedAnimations(mockView, Stone.BLACK, mockPlayer, null);

        verify(mockView).animate();
    }
}