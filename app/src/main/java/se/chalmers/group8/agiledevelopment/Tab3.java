package se.chalmers.group8.agiledevelopment;

/**
 * Created by nattapon on 28/04/15.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;
import se.chalmers.group8.session.PivotalSession;


public class Tab3 extends Fragment implements UpdateFinish {

    View v;
    String project;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.activity_all_stories,container,false);

        PivotalSession session = PivotalSession.getInstance();
        if(session.getStatus().equals("loggedIn") && !session.getProjectID().equals("")) {

            project = session.getProjectID();
            PivotalTracker tracker = new PivotalTracker(session.getToken(), this);
            tracker.setProjectID(session.getProjectID());
            try {
                tracker.readAllStories();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {

            LinearLayout ll = (LinearLayout) v.findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            Button newButton = new Button(getActivity());
            newButton.setText("Login");
            final Intent intent = new Intent(getActivity(), SettingsActivity.class);
            newButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
            ll.addView(newButton, lp);
        }

        return v;
    }

    @Override
    public void onUpdateFinished(int callFinish, String result) {
        try {
            JSONArray obj = new JSONArray(result);
            System.out.println(obj.length());

            LinearLayout ll = (LinearLayout) v.findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            for (int i = 0; i < obj.length(); i++) {
                JSONObject current = obj.getJSONObject(i);
                final String id = current.getString("id");
                String name = current.getString("name");

                Button newButton = new Button(getActivity());
                newButton.setText(name);
                final Intent intent = new Intent(getActivity(), SingleActivityView.class);
                newButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        intent.putExtra("intent_id", id);
                        intent.putExtra("intent_project_id", project);
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
