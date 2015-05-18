package se.chalmers.group8.github.connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;

/**
 * This class parse details of sha.
 */

public class ParseSha implements ConnectorResult {

    private Connector connector;

    public ParseSha () {

        connector = new Connector(this);

    }

    public void getRequest() {

        try {

            URL commitsURL = new URL(DataProcessor.createdSha);

            connector.doHttpRequest(commitsURL, Connector.METHOD_GET);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectorResult(String result) {

        // For parsing the sha
        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONObject commit = jsonResult.getJSONObject("commit");
            JSONObject committer = commit.getJSONObject("committer");
            String name = committer.getString("name");
            String time = committer.getString("date");
            String message = commit.getString("message");

            //Store the sha into a list for further checking update
            DataProcessor.setShaList(message.toString());

            System.out.println("Branch: " + DataProcessor.nameList.get(DataProcessor.branchIndex));

            //Check if the sha have changed, which means the branch is updated
            DataProcessor.compareList(message.toString());

            //For processing next sha
            DataProcessor.setBranchIndex();

            System.out.println("Author: " + name);
            System.out.println("Time: " + time);
            System.out.println("Message :" + message);
            System.out.println("------------------------------------");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
