package se.chalmers.group8.agiledevelopment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.MalformedURLException;

import se.chalmers.group8.github.connector.Authenticating;
import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;



public class MainActivity extends ActionBarActivity implements UpdateFinish {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                //setContentView(R.layout.webview);

                //Authenticating at = new Authenticating();
                try {
                    Authenticating.GetAuthentication();

                }
               catch (IOException e){
                    System.out.println("Error--------------------------------------------------------------");
                    System.out.println(e.getMessage());

                }

            }
        });
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

        /*try {
            URL url = new URL("https://www.pivotaltracker.com/services/v5/projects/1330222/stories/93103212");
            String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";
            URL postURL = new URL("https://www.pivotaltracker.com/services/v5/projects/1330222/stories");


            Connector connector = new Connector(postURL, this);
            RequestPropertyPair rpp[] = new RequestPropertyPair[2];
            rpp[0] = new RequestPropertyPair("X-TrackerToken", token);
            rpp[1] = new RequestPropertyPair("Content-Type", "application/json");

            String data = "{\"current_state\":\"started\",\"estimate\":1,\"name\":\"Exhaust ports are ray shielded\"}";



            //connector.doHttpRequest(Connector.METHOD_POST, data, rpp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/

        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";
        String url = "https://www.pivotaltracker.com/services/v5/projects/1330222/stories/93109016";


        PivotalTracker pt = new PivotalTracker(token, this);
        pt.setProjectID("1330222");
        try {
            String data = "{\"description\":" + "\"" + "This description should now be updated!" + "\"" + "}";
            //pt.update("93421486", data);
            pt.readAllStories();
            //pt.delete("93421486");
           //pt.addComment("93103212", "This is a comment.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpdateFinished(String result) {
        System.out.println(result);
    }
}
