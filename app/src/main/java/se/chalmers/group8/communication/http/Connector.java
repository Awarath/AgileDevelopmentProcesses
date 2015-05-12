package se.chalmers.group8.communication.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handles connections to URLs and uses HTTP methods to query HTTP requests.
 */
public class Connector extends AsyncTask<RequestPropertyPair, Void, String>{

    //Http methods
    public static final String METHOD_GET   =    "GET";
    public static final String METHOD_POST  =    "POST";
    public static final String METHOD_PUT   =    "PUT";
    public static final String METHOD_PATCH =    "PATCH";
    public static final String METHOD_DELETE=    "DELETE";

    private URL url;
    private ConnectorResult connectorResult;

    private String httpMethod;
    private String data;
    private boolean sendData;


    /**
     * The constructor takes a ConnectorResult as parameter that specifies what should happened when a query has returned a result.
     * @param connectorResult the class that handles the results of a request.
     */
    public Connector(ConnectorResult connectorResult) {
        this.url = null;
        this.connectorResult = connectorResult;
    }

    /**
     * Queries an HTTP request with data to send. Usually used with HTTP methods "POST", "PUT", "PATCH".
     * @param url the URL object containing the url to query
     * @param method choose an http method (available as static constants in Connector)
     * @param data the data to send
     * @param requestPropertyPairs the header/value pair
     */
    public void doHttpRequest(URL url, String method, String data, RequestPropertyPair... requestPropertyPairs) {
        this.url = url;
        sendData = true;
        this.data = data;
        httpMethod = method;
        this.execute(requestPropertyPairs);
    }

    /**
     * Queries an HTTP request without sending data. Usually used with HTTP methods "GET", "DELETE".
     * @param url the URL object containing the url to query
     * @param method choose an http method (available as static constants in Connector)
     * @param requestPropertyPairs the header/value pair
     */
    public void doHttpRequest(URL url, String method, RequestPropertyPair... requestPropertyPairs) {
        this.url = url;
        sendData = false;
        httpMethod = method;
        this.execute(requestPropertyPairs);
    }

    @Override
    protected String doInBackground(RequestPropertyPair... requestPropertyPairs) {
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            urlConnection = (HttpURLConnection)url.openConnection();


            //Set the request method
            urlConnection.setRequestMethod(httpMethod);

            //Add the properties
            for(int i = 0; i < requestPropertyPairs.length; i++) {
                String field = requestPropertyPairs[i].getField();
                String newValue = requestPropertyPairs[i].getNewValue();
                urlConnection.addRequestProperty(field, newValue);
            }

            if(sendData) {
                urlConnection.setDoOutput(true);

                //Write the data
                urlConnection.getOutputStream().write(data.getBytes("UTF-8"));
            }

            //Read the result
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null)
                result += line;

            br.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        connectorResult.onConnectorResult(result);
    }
}
