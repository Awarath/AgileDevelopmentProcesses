package se.chalmers.group8.agiledevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;


public class SingleActivityView extends ActionBarActivity implements UpdateFinish {

    String storyID;
    String projID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        storyID = intent.getStringExtra("intent_id");
        projID = intent.getStringExtra("intent_project_id");

        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

        PivotalTracker tracker = new PivotalTracker(token, this);
        tracker.setProjectID("1330222");
        try {
            tracker.readStory(storyID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_activity_view, menu);
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

    @Override
    public void onUpdateFinished(String result) {
        try {
            JSONObject object = new JSONObject(result);

            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            final String id = object.getString("id");
            String name = object.getString("name");
            System.out.println(name);
            String description = object.getString("description");

            // Create the text view
            TextView textView = new TextView(this);
            textView.setTextSize(40);
            textView.setText(name + ";" + description);

            setContentView(textView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
