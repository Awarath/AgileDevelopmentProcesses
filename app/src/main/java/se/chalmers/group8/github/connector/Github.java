package se.chalmers.group8.github.connector;

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
            URL url = new URL("https://api.github.com/");
            connector.doHttpRequest(url, Connector.METHOD_GET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectorResult(String result) {
        System.out.println(result);
    }
}
