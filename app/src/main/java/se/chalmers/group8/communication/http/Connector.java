package se.chalmers.group8.communication.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handles connection to URLs and uses HTTP methods to do HTTP requests.
 */
public class Connector extends AsyncTask<RequestPropertyPair, Void, String>{

    //Http methods
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private URL url;
    private ConnectorResult connectorResult;

    private String httpMethod;
    private String data;
    private boolean sendData;


    public Connector(URL url, ConnectorResult connectorResult) {
        this.url = url;
        this.connectorResult = connectorResult;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void doHttpRequest(String method, String data, RequestPropertyPair... requestPropertyPairs) {
        sendData = true;
        this.data = data;
        httpMethod = method;
        this.execute(requestPropertyPairs);
    }

    public void doHttpRequest(String method, RequestPropertyPair... requestPropertyPairs) {
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
