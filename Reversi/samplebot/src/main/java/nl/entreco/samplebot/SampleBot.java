package nl.entreco.samplebot;

import android.support.annotation.NonNull;
import android.util.Log;

import nl.entreco.reversibot.FirebaseBot;

class SampleBot extends FirebaseBot {

    SampleBot() {
        Log.i("SampleBot", "created");
    }

    @Override
    public void onStartMatch(@NonNull String matchUid, int yourStoneColor) {
        // Store which color your are: -1 == White, 1 == Black
        Log.i("SampleBot", "onStartMatch:" + matchUid + " stone:" + yourStoneColor);
    }

    @Override
    public void onMatchFinished() {
        // Pass if we won, or lost
    }

    @Override
    public void onRejected(@NonNull String move, @NonNull String board) {
        /* Format is (0 = empty,  1 = white , -1 = black):
            { "size":8,
              "board":[
                        [0,0,0, 0, 0,0,0,0],
                        [0,0,0, 0, 0,0,0,0],
                        [0,0,0, 0, 0,0,0,0],
                        [0,0,0, 1,-1,0,0,0],
                        [0,0,0,-1, 1,0,0,0],
                        [0,0,0, 0, 0,0,0,0],
                        [0,0,0, 0, 0,0,0,0],
                        [0,0,0, 0, 0,0,0,0]
                      ]
            }
         */
        Log.i("SampleBot", "onRejected move:" + move + " board:"+ board);
        onCalculateMove(board);
    }

    @Override
    public void onCalculateMove(@NonNull String board) {
        Log.i("SampleBot", "onCalculateMove:" + board);
        int row = (int) (Math.random() * 8);
        int col = (int) (Math.random() * 8);
        submitMove(String.format("[%s,%s]", row, col));
    }
}
