package se.chalmers.group8.agiledevelopment;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import se.chalmers.group8.session.PivotalSession;

/**
 * Created by Matthias on 13.05.2015.
 */
public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {

    private SettingsActivity activity;
    private PivotalSession session;
    private EditText uName;
    private EditText pwd;
    private Button loginButton;

    public SettingsActivityTest() {
        super(SettingsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);
        activity = getActivity();
        session = PivotalSession.getInstance();
        session.destroySession();
    }

    public void testInvalidLogin_shouldFail() {

        activity.handleLogin("NotAUsername", "NotAPassword");

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(session.getStatus(), "notLoggedIn");
        assertEquals(session.getToken(), "");
        assertEquals(session.getUserName(), "");
    }

    public void testValidLogin_shouldSucceed() {

        String username = "matthias.pernerstorfer@gmx.at";
        String password = "Chalmers123";
        String token = "a42dbb028513ae2978d848ec01bb3f2d";

        activity.handleLogin(username, password);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(session.getToken(), token);
        assertEquals(session.getUserName(), "matthiaspernerstorfer");
        assertEquals(session.getStatus(), "loggedIn");
    }

    public void testWriteFile_shouldExist(){
        activity.writeToFile("Hello World", "test.txt");
        File f = new File("test.txt");
        assertTrue(f.exists());
    }

    public void testWriteReadFile_ShouldBeEqual(){
        activity.writeToFile("Hello World!", "test2.txt)");
        String read = activity.readFromFile("test2.txt");
        assertEquals(read, "Hello World!");
    }
}
