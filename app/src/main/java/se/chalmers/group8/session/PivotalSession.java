package se.chalmers.group8.session;

/**
 * Created by Matthias on 5/12/2015.
 */
public class PivotalSession {
    private static PivotalSession ourInstance = new PivotalSession();

    private String userName;
    private String token;
    private String status;
    private String projectID;

    public static PivotalSession getInstance() {
        return ourInstance;
    }

    private PivotalSession() {
        destroySession();
    }

    public void destroySession(){
        setUserName("");
        setToken("");
        setStatus("notLoggedIn");
        setProjectID("");
    }

    public void setSession(String userName, String token) {
        setUserName(userName);
        setToken(token);
        setStatus("loggedIn");
        setProjectID("");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
}
