package nl.entreco.samplebot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.entreco.reversibot.ReversiBot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReversiBot.registerBot(new SampleBot(), "Entrecodelaco");
    }
}
