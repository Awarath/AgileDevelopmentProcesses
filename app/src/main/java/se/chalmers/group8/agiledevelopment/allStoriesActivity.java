package se.chalmers.group8.agiledevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;


public class allStoriesActivity extends ActionBarActivity implements UpdateFinish {

    ArrayList<HashMap<String, String>> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

        PivotalTracker tracker = new PivotalTracker(token, this);
        tracker.setProjectID("1330222");
        try {
            tracker.readAllStories();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_all_stories);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_stories, menu);
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

    public void buttonClicked(View view) throws MalformedURLException {

        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

        PivotalTracker tracker = new PivotalTracker(token, this);
        tracker.setProjectID("1330222");
        tracker.readAllStories();
    }

    @Override
    public void onUpdateFinished(String result) {

        try {
            JSONArray obj = new JSONArray(result);
            System.out.println(obj.length());

            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            for (int i = 0; i < obj.length(); i++){
                JSONObject current = obj.getJSONObject(i);
                final String id = current.getString("id");
                String name = current.getString("name");

                Button newButton = new Button(this);
                newButton.setText(name);
                final Intent intent = new Intent(this, SingleActivityView.class);
                newButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        intent.putExtra("intent_id", id);
                        intent.putExtra("intent_project_id", "1330222");
                        startActivity(intent);
                    }
                });

                ll.addView(newButton, lp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
