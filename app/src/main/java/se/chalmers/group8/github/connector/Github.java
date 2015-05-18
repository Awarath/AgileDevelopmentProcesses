package se.chalmers.group8.github.connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;

/**
 * This class get data from Github.
 */

public class Github implements ConnectorResult {

    private Connector connector;

    public Github() {

        connector = new Connector(this);

    }

    public void getRequest() {

        try {

            //URL branchesURL = new URL("https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/branches");
            URL branchesURL = new URL(DataProcessor.createdURL);

            connector.doHttpRequest(branchesURL, Connector.METHOD_GET);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectorResult(String result) {

        //For parsing the branches
        try {

            JSONArray array = new JSONArray(result);

            List shaList = new ArrayList();
            List nameList = new ArrayList();

            //Parsing branch name and its sha
            for(int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                JSONObject commit = obj.getJSONObject("commit");
                String sha = commit.getString("sha");
                String name = obj.getString("name");
                shaList.add(i, sha);
                nameList.add(i, name);

            }

            //Parsing details of each sha
            for (int j = 0; j < shaList.size(); j++) {

                //Store sha into a list
                DataProcessor.setSha(shaList.get(j).toString());

                //Store branch into a list
                DataProcessor.setBranchName(nameList.get(j).toString());

                //Parsing details of a sha
                ParseSha parseSha = new ParseSha();
                parseSha.getRequest();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
