package ovh.daxhelet.potagenial;

public class User {
    public String username;
    public String access_token;
    public String refresh_token;

    public User (String username, String access_token, String refresh_token) {
        this.username = username;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public void connectUser(String username, String access_token, String refresh_token) {
        this.username = username;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public boolean isConnected() {
        return !this.username.isEmpty() && !this.access_token.isEmpty() && !this.refresh_token.isEmpty();
    }
}
