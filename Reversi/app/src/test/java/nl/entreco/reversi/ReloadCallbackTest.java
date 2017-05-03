package nl.entreco.reversi;

import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReloadCallbackTest {

    @InjectMocks ReloadCallback subject;
    @Mock ReversiViewModel mockViewModel;
    @Mock private View mockBottomSheet;

    @Test
    public void itShouldFetchPlayersWhenExpanded() throws Exception {
        simulateStateChange(BottomSheetBehavior.STATE_EXPANDED);

        verify(mockViewModel).fetchPlayers();
    }

    @Test
    public void itShouldNotFetchPlayersWhenCollapsed() throws Exception {
        simulateStateChange(BottomSheetBehavior.STATE_COLLAPSED);

        verify(mockViewModel, never()).fetchPlayers();
    }

    private void simulateStateChange(int state) {
        subject.onStateChanged(mockBottomSheet, state);
    }

}