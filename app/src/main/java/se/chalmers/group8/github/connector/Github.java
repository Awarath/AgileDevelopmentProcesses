package se.chalmers.group8.github.connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.communication.http.RequestPropertyPair;

/**
 * Created by patrik on 2015-05-12.
 */
public class Github implements ConnectorResult {

    private Connector connector;

    private String githubToken;

    public Github() {
        connector = new Connector(this);
        githubToken = "6e147c4e65becda449822491241aab2d31cc1bdf";
    }

    public void getRequest() {
        //RequestPropertyPair rpp = new RequestPropertyPair()

        try {
            URL branchesURL = new URL("https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/branches");
            URL commitsURL = new URL("https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/commits/fbfcfc1fe4080528228a37eb52ed491f58ce3b6c");
            connector.doHttpRequest(commitsURL, Connector.METHOD_GET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectorResult(String result) {

        /*try { For parsing the branches
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                JSONObject commit = obj.getJSONObject("commit");
                String sha = commit.getString("sha");
                System.out.println(sha);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        // For parsing the commits
        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONObject commit = jsonResult.getJSONObject("commit");
            JSONObject committer = commit.getJSONObject("committer");
            String name = committer.getString("name");
            String time = committer.getString("date");
            String message = commit.getString("message");

            System.out.println("Name:" + name);
            System.out.println("Time:" + time);
            System.out.println("Message:" + message);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
