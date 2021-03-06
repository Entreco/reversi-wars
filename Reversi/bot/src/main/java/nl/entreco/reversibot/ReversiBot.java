package nl.entreco.reversibot;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ReversiBot extends ContentProvider {

    private static final String API_KEY = "AIzaSyA7ruSb75KcdYojOy4UDX-JNC_vagp01no";
    private static final String APPLICATION_ID = "reversi-wars";
    private static final String DATABASE_URL = "https://reversi-wars.firebaseio.com";

    @Override
    public boolean onCreate() {
        FirebaseApp.initializeApp(getContext(), new FirebaseOptions.Builder()
                .setApiKey(API_KEY)
                .setApplicationId(APPLICATION_ID)
                .setDatabaseUrl(DATABASE_URL)
                .build());

        return true;
    }

    public static <T extends FirebaseBot> void registerBot(@NonNull final T bot,
                                                           @NonNull final String botName) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        new RegisterListener(auth, bot, botName).go();
    }

    @Override
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        if (providerInfo == null) {
            throw new NullPointerException("ReversiBot ProviderInfo cannot be null.");
        }
        // So if the authorities equal the library internal ones, the developer forgot to set his
        // applicationId
        if ("nl.entreco.reversibot.ReversiBot".equals(providerInfo.authority)) {
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
