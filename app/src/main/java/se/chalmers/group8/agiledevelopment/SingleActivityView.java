package se.chalmers.group8.agiledevelopment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import se.chalmers.group8.entities.Member;
import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;


public class SingleActivityView extends ActionBarActivity implements UpdateFinish {

    String storyID;
    String projID;
    String owners;
    ArrayList<Member> members;
    PivotalTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        storyID = intent.getStringExtra("intent_id");
        projID = intent.getStringExtra("intent_project_id");

        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

        tracker = new PivotalTracker(token, this);
        tracker.setProjectID("1330222");
        try {
            tracker.readStory(storyID, "id,name,description,owner_ids,labels,current_state,story_type,created_at,updated_at,estimate,tasks");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_single_activity_view);
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
    public void onUpdateFinished(int callFinish, String result) {
        try {
            if (callFinish == 0x01) {
                JSONObject object = new JSONObject(result);

                LinearLayout ll = (LinearLayout) findViewById(R.id.buttonLayout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                System.out.println(result);

                final String id = object.getString("id");
                String name = object.getString("name");
                String description = object.getString("description");
                owners = object.getString("owner_ids");
                String labels = object.getString("labels");
                String status = object.getString("current_state");
                String type = object.getString("story_type");
                String createdAt = object.getString("created_at");
                String lastUpdated = object.getString("updated_at");
                String estimate = "0";
                if (object.has("estimate"))
                    estimate = object.getString("estimate");

                EditText eText = (EditText) findViewById(R.id.storyNameText);
                eText.setText(name);

                eText = (EditText) findViewById(R.id.storyDescriptionText);
                eText.setText(description);

                TextView tView = (TextView) findViewById(R.id.createdAtText);
                tView.setText("created at: " + createdAt);

                tView = (TextView) findViewById(R.id.updatedAtText);
                tView.setText("updated at: " + lastUpdated);

                tView = (TextView) findViewById(R.id.labels);
                tView.setText("labels: " + generateLabels(labels));

                Spinner spinner = (Spinner) findViewById(R.id.statusText);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.current_state, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(adapter.getPosition(status));

                spinner = (Spinner) findViewById(R.id.storyTypeText);
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.story_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(adapter.getPosition(type));

                spinner = (Spinner) findViewById(R.id.pointsText);
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.points, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(adapter.getPosition(estimate));

                // New request for members
                String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

                PivotalTracker tracker = new PivotalTracker(token, this);
                tracker.setProjectID("1330222");
                try {
                    tracker.getMembers();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Read the tasks of the story
                readTasks(result);

            } else if(callFinish == PivotalTracker.FUNCTION_GET_MEMBERS)  {

                members = new ArrayList<Member>();
                JSONArray obj = new JSONArray(result);

                for (int i = 0; i < obj.length(); i++) {
                    JSONObject current = obj.getJSONObject(i);
                    JSONObject person = current.getJSONObject("person");

                    String id = person.getString("id");
                    String name = person.getString("name");
                    String initials = person.getString("initials");

                    Member member = new Member(id, name, initials);
                    members.add(member);
                }

                TextView textView = (TextView) findViewById(R.id.owners);
                textView.setText("owners: " + generateMembers(owners));
            }

            Toast.makeText(getApplicationContext(), "Data loaded", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String generateLabels(String labels) throws JSONException {
        if (labels.length() == 2)
            return "";
        String toReturn = "";
        JSONArray object = new JSONArray(labels);
        for (int i = 0; i < object.length(); i++) {
            JSONObject current = object.getJSONObject(i);
            toReturn += current.getString("name") + ", ";
        }

        return toReturn.substring(0, toReturn.length() - 2);
    }

    public void submitChanges(View view) {
        String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";

        PivotalTracker tracker = new PivotalTracker(token, this);
        tracker.setProjectID(projID);
        try {
            tracker.update(storyID, updateData());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Can't update: Invalid data", Toast.LENGTH_LONG).show();
        }
    }

    private void readTasks(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray tasks = obj.getJSONArray("tasks");

            for(int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String id = task.getString("id");
                String description = task.getString("description");
                boolean isComplete = task.getBoolean("complete");

                addTaskCheckBox(id, description, isComplete);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addTaskCheckBox(final String taskID, final String taskDescription, final boolean isComplete) {
        LinearLayout taskLayout = (LinearLayout) findViewById(R.id.subTaskLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CheckBox taskCheckBox = new CheckBox(this);

        taskCheckBox.setLayoutParams(layoutParams);
        taskCheckBox.setText(taskDescription);
        if(isComplete)
            taskCheckBox.setChecked(true);

        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatePivotalTrackerTask(storyID, taskID, isChecked);
            }
        });

        taskLayout.addView(taskCheckBox);
    }

    private void updatePivotalTrackerTask(String storyID, String taskID, boolean isChecked) {
        try {
            if(isChecked)
                tracker.update(storyID, taskID, "{\"complete\":true}");
            else
                tracker.update(storyID, taskID, "{\"complete\":false}");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String generateMembers(String memberList) {
        if (memberList.length() == 2)
            return "";
        String toReturn = "";

        memberList = memberList.replace("[","");
        memberList = memberList.replace("]","");
        String[] owners = memberList.split(",");

        for(int i = 0; i < owners.length; i++){
            for(int j = 0; j < members.size(); j++) {
                Member m = members.get(j);
                if (m.getId().equals(owners[i]))
                    toReturn += m.getName() + ", ";

            }
        }
        return  toReturn.substring(0, toReturn.length() - 2);
    }

    private String updateData(){
        String toReturn = "{";

        toReturn += "\"name\":\"" + ((EditText)findViewById(R.id.storyNameText)).getText() + "\",";
        toReturn += "\"description\":\"" + ((EditText)findViewById(R.id.storyDescriptionText)).getText() + "\",";
        toReturn += "\"current_state\":\"" + ((Spinner)findViewById(R.id.statusText)).getSelectedItem() + "\",";
        toReturn += "\"story_type\":\"" + ((Spinner)findViewById(R.id.storyTypeText)).getSelectedItem() + "\",";
        toReturn += "\"estimate\":" + ((Spinner)findViewById(R.id.pointsText)).getSelectedItem() + "}";

        System.out.println(toReturn);

        return toReturn;
    }
}
