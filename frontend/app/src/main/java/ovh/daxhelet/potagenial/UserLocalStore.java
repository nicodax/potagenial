package ovh.daxhelet.potagenial;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserLocalStore {

    public static final String SP_USER = "user details";
    private SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            userLocalDatabase = EncryptedSharedPreferences.create(
                    context,
                    SP_USER,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("accessToken", user.access_token);
        spEditor.putString("refreshToken", user.refresh_token);
        spEditor.apply();
    }

    public void setAccessToken(String accessToken) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("accessToken", accessToken);
        spEditor.apply();
    }

    public User getLoggedInUser() {
        String username = userLocalDatabase.getString("username", "");
        String access_token = userLocalDatabase.getString("accessToken", "");
        String refresh_token = userLocalDatabase.getString("refreshToken", "");

        return new User(username, access_token, refresh_token);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", "");
        spEditor.putString("accessToken", "");
        spEditor.putString("refreshToken", "");
        spEditor.apply();
    }
}
