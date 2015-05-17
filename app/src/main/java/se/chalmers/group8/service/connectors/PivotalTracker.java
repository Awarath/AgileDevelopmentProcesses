package se.chalmers.group8.service.connectors;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.communication.http.RequestPropertyPair;
import se.chalmers.group8.service.connectors.UpdateFinish;


/**
 * Created by patrik on 2015-04-28.
 */
public class PivotalTracker implements ConnectorResult {


    public static final int FUNCTION_CREATE = 0x00;
    public static final int FUNCTION_READ = 0x01;
    public static final int FUNCTION_UPDATE = 0x02;
    public static final int FUNCTION_DELETE = 0x03;
    public static final int FUNCTION_ADD_COMMENT = 0x04;
    public static final int FUNCTION_GET_MEMBERS = 0x05;
    public static final int FUNCTION_CREATE_TASK = 0x06;

    private Connector connector;

    private String baseURL;
    private String storiesURL;
    private String projectURL;

    private String token;
    private String projectID;
    private String userID;

    private UpdateFinish updateFinish;

    private RequestPropertyPair[] rpp;

    private int callFunction;


    /**
     * Class for using the Pivotal Tracker API. Can handle CRUD requests to Pivotal Tracker.
     * Classes who uses the PivotalTracker class should implement the UpdateFinish interface to handle CRUD results.
     * <p/>
     * UpdateFinish contains the method doOnUpdateFinish(callFunction, result).
     * callFunction specifies the method called in PivotalTracker.
     * result contains the result of the method called.
     *
     * @param token        user Pivotal Tracker token
     * @param updateFinish class which will handle the CRUD results
     */
    public PivotalTracker(String token, UpdateFinish updateFinish) {
        this.token = token;
        baseURL = "https://www.pivotaltracker.com/services/v5/projects";
        projectID = "";
        storiesURL = "";
        projectURL = "";
        callFunction = -1;
        this.updateFinish = updateFinish;

        connector = new Connector(this);

        rpp = new RequestPropertyPair[2];
        rpp[0] = new RequestPropertyPair("X-TrackerToken", token);
        rpp[1] = new RequestPropertyPair("Content-Type", "application/json");

        initUserProfile();
    }

    private void initUserProfile() {
        Connector initUser = new Connector(new ConnectorResult() {
            @Override
            public void onConnectorResult(String result) {
                JSONObject userProfile = null;
                try {
                    userProfile = new JSONObject(result);
                    userID = userProfile.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String profileURL = "https://www.pivotaltracker.com/services/v5/me";
        try {
            URL url = new URL(profileURL);
            initUser.doHttpRequest(url, Connector.METHOD_GET, rpp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new story with the specified name and description.
     *
     * @param name        the name of the new story
     * @param description the description of the new story
     * @throws MalformedURLException
     */
    public void create(String name, String description) throws MalformedURLException {
        callFunction = FUNCTION_CREATE;

        URL url = new URL(storiesURL);

        String data = "{\"name\":" + "\"" + name + "\"" + ",\"description\":" + "\"" + description + "\"" + "}";

        connector.doHttpRequest(url, Connector.METHOD_POST, data, rpp);
    }

    /**
     * Create a new task with the in the specified story with a description.
     * @param storyID the story ID to which the task belong
     * @param description the description of the task
     * @throws MalformedURLException
     */
    public void createTask(String storyID, String description) throws MalformedURLException{
        callFunction = FUNCTION_CREATE_TASK;
        String tasksURL = storiesURL + storyID + "/tasks";
        URL url = new URL(tasksURL);

        String data = "{\"description\":" + "\"" + description + "\"" + "}";

        connector.doHttpRequest(url, Connector.METHOD_POST, data, rpp);
    }

    /**
     * Reads all stories and gets the result in JSON format.
     *
     * @throws MalformedURLException
     */
    public void readAllStories() throws MalformedURLException {
        readStory("");
    }

    /**
     * Reads the specified fields of all stories. Gets the result in JSON format.
     * Example: readStory("name,description,story_type,comments")
     *
     * @param fields the fields to be read (i.e. "comments" for only comments)
     */
    public void readAllStories(String fields) throws MalformedURLException {
        callFunction = FUNCTION_READ;

        String storyURL = storiesURL.substring(0, storiesURL.length() - 1) + "?fields=" + fields;
        URL url = new URL(storyURL);

        connector.doHttpRequest(url, Connector.METHOD_GET, rpp);
    }


    /**
     * Reads the default fields of the specified story and gets the result in JSON format.
     *
     * @param storyID the story ID
     * @throws MalformedURLException
     */
    public void readStory(String storyID) throws MalformedURLException {
        callFunction = FUNCTION_READ;

        String storyURL = storiesURL + storyID;
        URL url = new URL(storyURL);

        connector.doHttpRequest(url, Connector.METHOD_GET, rpp);

    }

    /**
     * Reads the specified fields of the specified story. Gets the result in JSON format.
     * Example: readStory("id", "name,description,story_type,comments")
     *
     * @param storyID the story ID
     * @param fields  the fields to be read (i.e. "comments" for only comments)
     */
    public void readStory(String storyID, String fields) throws MalformedURLException {
        callFunction = FUNCTION_READ;

        String fieldURL = storiesURL + storyID + "?fields=" + fields;
        URL url = new URL(fieldURL);

        connector.doHttpRequest(url, Connector.METHOD_GET, rpp);
    }


    /**
     * Updates the specified story's fields using the specified data.
     * Example: update("id", "{\"description\":\"This is an updated description\"}"
     *
     * @param storyID the story ID
     * @param data    specify both field and data that the field should be updated with
     * @throws MalformedURLException
     */
    public void update(String storyID, String data) throws MalformedURLException {
        callFunction = FUNCTION_UPDATE;

        URL url = new URL(storiesURL + storyID);

        connector.doHttpRequest(url, Connector.METHOD_PUT, data, rpp);
    }

    /**
     * Updates the specified task's fields of the specified story using the specified data.
     * Example: update("storyID", "taskID", "{\"description\":\"This is an updated description\"}"
     *
     * @param storyID the story ID
     * @param taskID  the task ID
     * @param data    specify both field and data that the field should be updated with
     * @throws MalformedURLException
     */
    public void update(String storyID, String taskID, String data) throws MalformedURLException{
        storyID += "/tasks/" + taskID;
        update(storyID, data);
    }

    /**
     * Deletes the specified story.
     *
     * @param storyID the story ID
     * @throws MalformedURLException
     */
    public void delete(String storyID) throws MalformedURLException {
        callFunction = FUNCTION_DELETE;

        URL url = new URL(storiesURL + storyID);
        connector.doHttpRequest(url, Connector.METHOD_DELETE, rpp);
    }

    /**
     * Adds a comment to a story.
     *
     * @param storyID the story ID
     * @param comment the comment to be added
     * @throws MalformedURLException
     */
    public void addComment(String storyID, String comment) throws MalformedURLException {
        callFunction = FUNCTION_ADD_COMMENT;

        URL url = new URL(storiesURL + storyID + "/comments");

        String data = "{\"text\":" + "\"" + comment + "\"" + "}";

        connector.doHttpRequest(url, Connector.METHOD_POST, data, rpp);
    }

    public void getMembers() throws MalformedURLException {
        callFunction = FUNCTION_GET_MEMBERS;

        String membersURL = projectURL + "/memberships";
        URL url = new URL(membersURL);

        connector.doHttpRequest(url, Connector.METHOD_GET, rpp);
    }

    /**
     * Sets the current project ID.
     *
     * @param projectID the project ID
     */
    /**
     * Sets the current project ID.
     * @param projectID the project ID
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
        projectURL = baseURL + "/" + this.projectID;
        storiesURL = projectURL + "/stories/";
    }

    public String getUserID() {
        return userID;
    }


    @Override
    public void onConnectorResult(String result) {
        // A connector can only be used once because of the AsyncTask
        connector = new Connector(this);

        updateFinish.onUpdateFinished(callFunction, result);
    }
}
