package nl.entreco.reversi;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

class ReloadCallback extends BottomSheetBehavior.BottomSheetCallback {

    @NonNull private final BottomSheetBehavior<? extends View> behavior;
    @NonNull private final ReversiViewModel viewModel;

    private boolean lockDraggingDown;

    ReloadCallback(@NonNull BottomSheetBehavior<? extends View> behavior,
                   @NonNull ReversiViewModel viewModel) {
        this.behavior = behavior;
        this.viewModel = viewModel;
        this.lockDraggingDown = true;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if(newState == BottomSheetBehavior.STATE_EXPANDED){
            lockDraggingDown = true;
            viewModel.fetchPlayers();
        }
        else if (newState == BottomSheetBehavior.STATE_DRAGGING && lockDraggingDown) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {
            lockDraggingDown = false;
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }
}
