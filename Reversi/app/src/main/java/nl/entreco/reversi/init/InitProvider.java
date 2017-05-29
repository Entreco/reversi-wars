package nl.entreco.reversi.init;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InitProvider extends ContentProvider {

    private static final String MAIL = "referee@reversi.entreco.nl";
    private static final String PASS = "*:^vTseN`_Q*!8rjnT]nYy%)<T%2$k@kvs2Dzfn$3W@H4'svMm";

    @Override
    public boolean onCreate() {

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            auth.signInWithEmailAndPassword(MAIL, PASS).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.i("Bingo", "Bingo");
                            } else {
                                Log.e("Bingo", "No bingo has");
                            }
                        }
                    });
        }

        return true;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        if (providerInfo == null) {
            throw new NullPointerException("InitProvider ProviderInfo cannot be null.");
        }
        // So if the authorities equal the library internal ones, the developer forgot to set his
        // applicationId
        if ("nl.entreco.reversi.init.InitProvider".equals(providerInfo.authority)) {
            throw new IllegalStateException(
                    "Incorrect provider authority in manifest. Most likely due to a "
                            + "missing applicationId variable in application\'s build.gradle.");
        }
        super.attachInfo(context, providerInfo);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
