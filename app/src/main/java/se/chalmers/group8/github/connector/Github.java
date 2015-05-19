package se.chalmers.group8.github.connector;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.service.connectors.UpdateFinish;

/**
 * This class get data from Github.
 */

public class Github implements ConnectorResult {

    private Connector connector;
    private UpdateFinish updateFinish;

    public Github(UpdateFinish updateFinish) {

        this.updateFinish = updateFinish;
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
        updateFinish.onUpdateFinished(0, result);



    }

}
