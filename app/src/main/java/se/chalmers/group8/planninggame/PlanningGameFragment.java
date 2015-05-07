package se.chalmers.group8.planninggame;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import se.chalmers.group8.agiledevelopment.R;
import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;

/**
 * Created by patrik on 2015-05-04.
 */
public class PlanningGameFragment extends Fragment implements UpdateFinish {

    private PivotalTracker pt;
    private PivotalTracker ptCommenter;

    private ArrayList<GlowButton> voteButtons;
    private ArrayList<GlowButton> storyButtons;

    private PopupWindow storyInfoWindow;

    private View fragmentView;
    private View popupView;

    private String  selectedStoryID = "";
    private int     selectedPriority = -1;
    private int     storyVoteTotal;
    private int     storyNumberOfVotes;

    private String userPivotalID = "1656672";
    private String token = "b33f5efe7f296d2bf724f2d3a20bb8b1";
    private String projectID = "1330222";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.planning_game, container, false);
        initVoteButtons(fragmentView);
        initPopup();

        storyButtons = new ArrayList<GlowButton>();

        pt = new PivotalTracker(token, this);
        pt.setProjectID(projectID);

        ptCommenter = new PivotalTracker(token, this);
        ptCommenter.setProjectID(projectID);

        createStoryNameButtons();

        return fragmentView;

    }

    private void createStoryNameButtons() {
        try {
            pt.readAllStories("id,name,description,comments");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void addStoryNameButtons(final String id, final String storyName, final String storyDescription, final int totalVoteValue, final int numberOfVotes) {
        final LinearLayout storyNameButtonLayout = (LinearLayout) fragmentView.findViewById(R.id.storyNameButtonLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button button = new Button(getActivity());
        button.setText(storyName);
        button.setBackgroundColor(Color.TRANSPARENT);

        final GlowButton storyButton = new GlowButton(button, R.drawable.pg_buttonframe, 0);
        storyButtons.add(storyButton);

        storyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storyButton.isGlowing()) {
                    selectedStoryID = id;
                    storyVoteTotal = totalVoteValue;
                    storyNumberOfVotes = numberOfVotes;

                    showPopup(storyName, storyDescription);
                } else
                    selectedStoryID = "";

                updateGlow(storyButtons, storyButton);
            }
        });
        storyNameButtonLayout.addView(button, layoutParams);

    }

    private void updateGlow(ArrayList<GlowButton> buttons, GlowButton buttonPressed) {
        if(!buttonPressed.isGlowing()) {
            buttonPressed.setGlowing(true);

            for(int i = 0; i < buttons.size(); i++) {
                if(!buttons.get(i).equals(buttonPressed) && buttons.get(i).isGlowing()) {
                    buttons.get(i).setGlowing(false);
                }
            }
        }else {
            buttonPressed.setGlowing(false);
        }
    }

    private void initVoteButtons(View view) {

        /****************** Priority Buttons ******************/

        ImageView vBt1 = (ImageView) view.findViewById(R.id.button1);
        ImageView vBt2 = (ImageView) view.findViewById(R.id.button2);
        ImageView vBt3 = (ImageView) view.findViewById(R.id.button3);


        voteButtons = new ArrayList<GlowButton>();
        voteButtons.add(new GlowButton(vBt1, R.drawable.pg_button1glow, R.drawable.pg_button1));
        voteButtons.add(new GlowButton(vBt2, R.drawable.pg_button2glow, R.drawable.pg_button2));
        voteButtons.add(new GlowButton(vBt3, R.drawable.pg_button3glow, R.drawable.pg_button3));

        voteButtons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGlow(voteButtons, voteButtons.get(0));
                selectedPriority = 1;
            }
        });


        voteButtons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGlow(voteButtons, voteButtons.get(1));
                selectedPriority = 2;
            }
        });



        voteButtons.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGlow(voteButtons, voteButtons.get(2));
                selectedPriority = 3;
            }
        });

        /******************************************************************/

        /********** Vote Button ***********/
        Button sendVoteButton = (Button) view.findViewById(R.id.sendVoteButton);
        sendVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStoryID.equals("") || selectedPriority == -1) {
                    Toast.makeText(getActivity(), "Please select a story and the priority you want it to have.", Toast.LENGTH_LONG).show();
                } else {
                    int newPriority = (storyVoteTotal + selectedPriority) / (storyNumberOfVotes + 1);
                    String priorityStr = Integer.toString(newPriority);
                    // Update the story priority on Pivotal Tracker
                    try {
                        pt.update(selectedStoryID, "{\"estimate\":" + priorityStr + "}");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    // Add the VOTE comment
                    try {
                        ptCommenter.addComment(selectedStoryID, "VOTE:" + Integer.toString(selectedPriority));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /**********************************/
    }

    public void initPopup() {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_layout, null);
        storyInfoWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button dismiss = (Button)popupView.findViewById(R.id.popupCloseButton);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyInfoWindow.dismiss();
            }
        });
    }

    private void showPopup(String name, String description) {
        TextView nameTv = (TextView)popupView.findViewById(R.id.popupStoryName);
        TextView descriptionTv = (TextView)popupView.findViewById(R.id.popupStoryDescription);
        nameTv.setText(name);
        descriptionTv.setText(description);
        storyInfoWindow.showAtLocation(voteButtons.get(0).getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onUpdateFinished(int callFunction, String result) {
        switch (callFunction) {
            case PivotalTracker.FUNCTION_READ:
                try {
                    JSONArray obj = new JSONArray(result);

                    for (int i = 0; i < obj.length(); i++){
                        JSONObject current = obj.getJSONObject(i);
                        String storyID = current.getString("id");
                        String name = current.getString("name");
                        String description = current.getString("description");

                        boolean hasVoted = false;
                        int numberOfVotes = 0;
                        int totalVoteValue = 0;
                        // Get comments
                        JSONArray comments = current.getJSONArray("comments");
                        for(int j = 0; j < comments.length(); j++) {
                            JSONObject comment = comments.getJSONObject(j);
                            String text = comment.getString("text");
                            String personID = comment.getString("person_id");

                            // Votes look like i.e "VOTE:1"
                            if(text.substring(0, text.length()-1).equals("VOTE:")) {
                                // A user can only vote once on a story!
                                if(personID.equals(userPivotalID)) {
                                   hasVoted = true;
                                  break;
                                } else {
                                // Votes look like i.e "VOTE:1"
                                int voteValue = Integer.parseInt(text.substring(text.length()-1));
                                totalVoteValue += voteValue;
                                numberOfVotes++;
                                }

                            }

                        }

                        if(!hasVoted)
                            addStoryNameButtons(storyID, name, description, totalVoteValue, numberOfVotes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case PivotalTracker.FUNCTION_UPDATE:
                Toast.makeText(getActivity(), "Vote sent!", Toast.LENGTH_SHORT).show();

                // Update Buttons!
                LinearLayout storyNameButtonLayout = (LinearLayout)fragmentView.findViewById(R.id.storyNameButtonLayout);
                storyNameButtonLayout.removeAllViews();
                createStoryNameButtons();
                updateGlow(voteButtons, voteButtons.get(selectedPriority-1));

                selectedPriority = -1;
                selectedStoryID = "";
                break;
        }

    }
}
