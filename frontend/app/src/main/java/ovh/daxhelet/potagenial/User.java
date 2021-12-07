package ovh.daxhelet.potagenial;

public class User {
    public String username;
    public String password;
    public String email;

    public User (String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void connectUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public boolean isConnected() {
        return !this.username.isEmpty() && !this.password.isEmpty() && !this.email.isEmpty();
    }
}
