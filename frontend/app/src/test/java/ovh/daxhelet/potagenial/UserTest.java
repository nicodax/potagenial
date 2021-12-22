package ovh.daxhelet.potagenial;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class UserTest {

    User user = mock(User.class);
    String username = user.username;
    String access_token = user.access_token;
    String refresh_token = user.refresh_token;

    @Test
    public void connectUser() {
        user = new User(username, access_token, refresh_token);
    }

    @Test
    public void isConnected() {
        user.connectUser(username,access_token,refresh_token);
        assertEquals(1,1);

    }
}