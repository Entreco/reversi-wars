package nl.entreco.reversibot;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.entreco.reversi.ReversiActivity;
import nl.entreco.reversibot.databinding.ActivityMatchBinding;

public class MatchActivity extends AppCompatActivity {

    private MatchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMatchBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_match);

        viewModel = new MatchViewModel(new FetchMatchesUsecase());
        binding.setViewModel(viewModel);

        initToolbar(binding);
        initFab(binding);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.registerForMatches();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unregisterForMatches();
    }

    private void initFab(@NonNull final ActivityMatchBinding binding) {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MatchActivity.this, ReversiActivity.class));
            }
        });
    }

    private void initToolbar(@NonNull final ActivityMatchBinding binding) {
        setSupportActionBar(binding.toolbar);
    }
}
