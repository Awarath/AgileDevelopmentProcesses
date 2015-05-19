package se.chalmers.group8.agiledevelopment;

import android.widget.Button;
import android.widget.EditText;

import junit.framework.TestCase;

import java.util.Set;

import se.chalmers.group8.session.PivotalSession;

/**
 * Created by Matthias on 5/12/2015.
 */
public class LoginTest extends TestCase {

    private SettingsActivity activity;
    private PivotalSession session;
    private EditText uName;
    private EditText pwd;
    private Button loginButton;

    public void setUp() throws Exception {
        super.setUp();

        activity = new SettingsActivity();
        session = PivotalSession.getInstance();
        session.destroySession();
        uName = (EditText)activity.findViewById(R.id.userName);
        pwd = (EditText)activity.findViewById(R.id.password);
        loginButton = (Button)activity.findViewById(R.id.loginButton);
    }

    public void testInvalidLogin() {
        uName.setText("NotAUser");
        pwd.setText("NotThePassword");
        activity.loginButtonClicked(loginButton);
        assertEquals(session.getStatus(), "notLoggedIn");
        assertEquals(session.getToken(), "");
        assertEquals(session.getUserName(), "");
    }

    public void testValidLogin() {

        String pass = "*****"; // To be changed when running tests
        String tok = "*13123123123123123";

        uName.setText("matthias.pernerstorfer@gmx.at");
        pwd.setText(pass);
        activity.loginButtonClicked(loginButton);
        assertEquals(session.getStatus(), "loggedIn");
        assertEquals(session.getToken(), tok);
        assertEquals(session.getUserName(), "Awarath");
    }
}