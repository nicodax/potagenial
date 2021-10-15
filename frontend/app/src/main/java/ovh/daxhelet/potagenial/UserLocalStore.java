package ovh.daxhelet.potagenial;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_USER = "user details";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_USER, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.putString("email", user.email);
        spEditor.commit();
    }

    public User getLoggedInUser() {
        String username = userLocalDatabase.getString("name", "");
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email", "");

        User storedUser = new User(username, password, email);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if (userLocalDatabase.getBoolean("loggedIn", false)){
            return true;
        }
        return false;
    }
}
