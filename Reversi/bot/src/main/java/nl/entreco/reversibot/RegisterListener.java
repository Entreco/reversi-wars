package nl.entreco.reversibot;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class RegisterListener implements
        OnCompleteListener<AuthResult> {
    private final FirebaseAuth auth;
    private final FirebaseBot bot;
    private final String botName;
    private DatabaseReference playersReference;

    public <T extends FirebaseBot> RegisterListener(FirebaseAuth auth, T bot, String botName) {
        this.auth = auth;
        this.bot = bot;
        this.botName = botName;
        playersReference = FirebaseDatabase.getInstance().getReference("players");
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            @SuppressWarnings("ConstantConditions")
            final DatabaseReference push =
                    playersReference.child(auth.getCurrentUser().getUid()).push();
            final String playerUid = push.getKey();
            initBot(push, playerUid);
        } else {
            Log.e("RegisterListener", "unable to signin");
        }
    }

    private void initBot(DatabaseReference push, String playerUid) {
        Log.i("RegisterListener", "initBot:" + push + " playerUuid:" + playerUid);
        bot.init(push, playerUid, botName);
        push.setValue(bot);
    }

    public void go() {
        final FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            auth.signInAnonymously().addOnCompleteListener(this);
        } else {
            final DatabaseReference push = playersReference.child(user.getUid()).getRef();
            initBot(push, push.getKey());
        }
    }
}
