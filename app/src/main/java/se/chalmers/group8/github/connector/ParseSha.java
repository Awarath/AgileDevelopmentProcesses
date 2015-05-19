package se.chalmers.group8.github.connector;

import java.net.MalformedURLException;
import java.net.URL;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.service.connectors.UpdateFinish;

/**
 * This class parse details of sha.
 */

public class ParseSha implements ConnectorResult {

    private Connector connector;
    private UpdateFinish updateFinish;

    public ParseSha (UpdateFinish updateFinish) {

        connector = new Connector(this);
        this.updateFinish = updateFinish;

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

        updateFinish.onUpdateFinished(1, result);

    }

}
