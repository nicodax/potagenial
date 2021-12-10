package ovh.daxhelet.potagenial;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserLocalStore {

    public static final String SP_USER = "user details";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_USER, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("accessToken", user.access_token);
        spEditor.apply();
    }

    public User getLoggedInUser() {
        String username = userLocalDatabase.getString("username", "");
        String access_token = userLocalDatabase.getString("accessToken", "");

        return new User(username, access_token);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }
}
