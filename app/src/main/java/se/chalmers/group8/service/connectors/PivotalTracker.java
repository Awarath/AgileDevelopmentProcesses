package se.chalmers.group8.service.connectors;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.communication.http.RequestPropertyPair;

/**
 * Created by patrik on 2015-04-28.
 */
public class PivotalTracker implements ConnectorResult {


    private Connector connector;

    private String baseURL;
    private String storiesURL;
    private String projectURL;

    private String token;
    private String projectID;

    private UpdateFinish updateFinish;

    private RequestPropertyPair[] rpp;


    public PivotalTracker(String token, UpdateFinish updateFinish) {
        this.token = token;
        baseURL = "https://www.pivotaltracker.com/services/v5/projects";
        projectID = "";
        storiesURL = "";
        projectURL = "";
        this.updateFinish = updateFinish;

        connector = new Connector(this);

        rpp = new RequestPropertyPair[2];
        rpp[0] = new RequestPropertyPair("X-TrackerToken", token);
        rpp[1] = new RequestPropertyPair("Content-Type", "application/json");
    }

    public void create(String name, String description) throws MalformedURLException{
        URL url = new URL(storiesURL);

        String data = "{\"name\":" + "\"" +  name + "\"" + ",\"description\":" + "\"" + description + "\"" + "}";

        connector.doHttpRequest(url, Connector.METHOD_POST, data, rpp);
    }

    public void readAllStories() throws MalformedURLException{
        readStory("");
    }



    public void readStory(String storyID) throws MalformedURLException {
        String storyURL = storiesURL + storyID;
        URL url = new URL(storyURL);

        connector.doHttpRequest(url, Connector.METHOD_GET, rpp);

    }

    public void update(String storyID, String data) throws MalformedURLException{
        URL url = new URL(storiesURL + storyID);

        connector.doHttpRequest(url, Connector.METHOD_PUT, data, rpp);
    }

    public void delete(String storyID) throws MalformedURLException{
        URL url = new URL(storiesURL + storyID);
        connector.doHttpRequest(url, Connector.METHOD_DELETE, rpp);
    }

    public void addComment(String storyID, String comment) throws MalformedURLException{
        URL url = new URL(storiesURL + storyID + "/comments");

        String data = "{\"text\":" + "\"" + comment + "\"" + "}";

        connector.doHttpRequest(url, Connector.METHOD_POST, data, rpp);
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
        projectURL = baseURL + "/" + this.projectID;
        storiesURL = projectURL + "/stories/";
    }


    @Override
    public void onConnectorResult(String result) {
        updateFinish.onUpdateFinished(result);
    }
}
