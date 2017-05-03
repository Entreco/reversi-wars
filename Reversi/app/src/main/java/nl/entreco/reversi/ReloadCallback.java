package nl.entreco.reversi;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

class ReloadCallback extends BottomSheetBehavior.BottomSheetCallback {

    @NonNull private final ReversiViewModel viewModel;

    ReloadCallback(@NonNull ReversiViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if(newState == BottomSheetBehavior.STATE_EXPANDED){
            viewModel.fetchPlayers();
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }
}
