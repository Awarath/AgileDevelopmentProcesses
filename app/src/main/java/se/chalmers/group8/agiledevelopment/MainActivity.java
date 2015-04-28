package se.chalmers.group8.agiledevelopment;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

import se.chalmers.group8.communication.http.Connector;
import se.chalmers.group8.communication.http.ConnectorResult;
import se.chalmers.group8.communication.http.RequestPropertyPair;


public class MainActivity extends ActionBarActivity implements ConnectorResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClicked(View view) {
       /* Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();


        class HttpGET extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... urls) {

                try {


                    String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";
                    String url = "https://www.pivotaltracker.com/services/v5/projects/1330222/stories/93109016";


                    URL pivotal = new URL(url);
                    HttpURLConnection urlConn = (HttpURLConnection)pivotal.openConnection();

                    urlConn.setRequestMethod("GET");
                    urlConn.setRequestProperty("X-TrackerToken", token);

                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    String result;
                    while ((result = br.readLine()) != null)
                        System.out.println(result);
                    br.close();

                    return result;



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;

            }
        }

        new HttpGET().execute("null");*/

        try {
            URL url = new URL("https://www.pivotaltracker.com/services/v5/projects/1330222/stories/93103212");
            String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";
            URL postURL = new URL("https://www.pivotaltracker.com/services/v5/projects/1330222/stories");


            Connector connector = new Connector(postURL, this);
            RequestPropertyPair rpp[] = new RequestPropertyPair[2];
            rpp[0] = new RequestPropertyPair("X-TrackerToken", token);
            rpp[1] = new RequestPropertyPair("Content-Type", "application/json");

            String data = "{\"current_state\":\"started\",\"estimate\":1,\"name\":\"Exhaust ports are ray shielded\"}";



            connector.doHttpRequest(Connector.METHOD_POST, data, rpp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectorResult(String result) {
        System.out.println(result);
    }
}
