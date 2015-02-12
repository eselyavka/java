package tv.okko.hive.auth;

import org.apache.hive.service.auth.PasswdAuthenticationProvider;
import java.util.*;
import javax.security.sasl.AuthenticationException;

public class SampleAuthenticator implements PasswdAuthenticationProvider {
    private static final Map<String, String> CREDENTIALS = new HashMap<>();

    static {
        CREDENTIALS.put("user1", "passwd1");
        CREDENTIALS.put("user2", "passwd2");
    }

    public SampleAuthenticator() {
    }

    @Override
    public void Authenticate(String user, String password) throws AuthenticationException {
        String pswd = CREDENTIALS.get(user);
        if (pswd == null || !pswd.equals(password)) {
            throw new AuthenticationException("Authentication failed for user " + user);
        }
    }
}
