package se.chalmers.group8.agiledevelopment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import se.chalmers.group8.github.connector.DataProcessor;
import se.chalmers.group8.github.connector.NewTimerTask;
import se.chalmers.group8.github.connector.ParseSha;
import se.chalmers.group8.service.connectors.UpdateFinish;

/**
 * This class handle get and parse date from Github.
 */
public class CreateOutputView extends Activity implements UpdateFinish {

    String userName;
    String repositoryName;
    //String userName = "Awarath";
    //String repositoryName = "AgileDevelopmentProcesses";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = getIntent().getStringExtra("name");
        repositoryName = getIntent().getStringExtra("repository");

        //Handle empty input
        if (userName == null || userName.equals("") || userName.length() == 0
                || repositoryName.equals("") || repositoryName.equals("") || repositoryName.length() == 0) {

            //Need to build a warning here later
            userName = "Awarath";
            repositoryName = "AgileDevelopmentProcesses";

        } //else {

            //The link to parse

            DataProcessor.setBranchURL("https://api.github.com/repos/" + userName + "/" + repositoryName + "/branches");

            //Initial the index for get branch name
            DataProcessor.branchIndex = 0;

            //Start
            run();

            setContentView(R.layout.github_output);
        //}

    }


    //This function handle the timer for continually get data
    public void run(){

        Timer timer = new Timer();
        NewTimerTask timerTask = new NewTimerTask(this);

        //Update every 20 minutes (1200s)
        timer.schedule(timerTask, 0, 1200000);

    }

    @Override
    public void onUpdateFinished(int callFinish, String result) {

        if (callFinish == 0) {

            //For parsing the branch status
            try {

                JSONArray array = new JSONArray(result);

                List shaList = new ArrayList();
                List nameList = new ArrayList();

                //Parsing branch name and its sha
                for(int i = 0; i < array.length(); i++) {

                    JSONObject obj = array.getJSONObject(i);
                    JSONObject commit = obj.getJSONObject("commit");
                    String sha = commit.getString("sha");
                    String name = obj.getString("name");
                    shaList.add(i, sha);
                    nameList.add(i, name);

                }

                //Parsing details of each sha
                for (int j = 0; j < shaList.size(); j++) {

                    //Store sha into a list
                    DataProcessor.setSha(shaList.get(j).toString());

                    //Store branch into a list
                    DataProcessor.setBranchName(nameList.get(j).toString());

                    //Parsing details of a sha
                    ParseSha parseSha = new ParseSha(this);
                    parseSha.getRequest();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // For parsing the sha
        } else if (callFinish == 1) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                JSONObject commit = jsonResult.getJSONObject("commit");
                JSONObject committer = commit.getJSONObject("committer");
                String name = committer.getString("name");
                String time = committer.getString("date");
                String message = commit.getString("message");

                //Store the sha into a list for further checking update
                DataProcessor.setShaList(message.toString());

                //Check if the sha have changed, which means the branch is updated
                DataProcessor.compareList(message.toString());

                //Store branch name for the output
                //System.out.println("Branch: " + DataProcessor.nameList.get(DataProcessor.branchIndex));
                DataProcessor.setTempBranch(DataProcessor.nameList.get(DataProcessor.branchIndex).toString());

                //Store author name for the output
                //System.out.println("Author: " + name);
                DataProcessor.setTempName(name.toString());

                //Store branch change time for the output
                //System.out.println("Time: " + time);
                DataProcessor.setTempTime(time.toString());

                //Store comment for the output
                //System.out.println("Comment :" + message);
                DataProcessor.setTempComment(message.toString());

                //System.out.println("------------------------------------");

                //Create TextView for showing output
                TextView ntxt = new TextView(this);

                //Create output string
                String s = "Branch:" + DataProcessor.tempBranchList.get(DataProcessor.branchIndex) + "\n" +
                        "Author:" + DataProcessor.tempNanmeList.get(DataProcessor.branchIndex) + "\n" +
                        "Time:" + DataProcessor.tempTimeList.get(DataProcessor.branchIndex) + "\n" +
                        "Comment:" + DataProcessor.tempCommentList.get(DataProcessor.branchIndex) + "\n";

                //For processing next sha
                DataProcessor.setBranchIndex();

                //Processing wrap
                s = s.replace("\\n", "\n");
                ntxt.setText(s);

                LinearLayout l1 = (LinearLayout) findViewById(R.id.githubOutput);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                l1.addView(ntxt, lp);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
