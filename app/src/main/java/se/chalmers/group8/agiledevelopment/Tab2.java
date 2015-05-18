package se.chalmers.group8.agiledevelopment;

/**
 * Created by nattapon on 28/04/15.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;

import se.chalmers.group8.github.connector.DataProcessor;
import se.chalmers.group8.github.connector.NewTimerTask;


public class Tab2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_2,container,false);

        //Need to take input later, need to take input from user
        //final String userName = "Awarath";
        //final String repositoryName = "AgileDevelopmentProcesses";
        final EditText editText_User = (EditText) v.findViewById(R.id.edit_text_user);
        final EditText editText_Repo = (EditText) v.findViewById(R.id.edit_text_repo);

        //Button for starting Github connection
        Button githubButton = (Button) v.findViewById(R.id.GithubButton);
        githubButton.setOnClickListener(new Button.OnClickListener() {


            public void onClick(View v) {

                String userName = editText_User.getText().toString();
                String repositoryName = editText_Repo.getText().toString();


                //The link to parse
                //CreateURL.setBranchURL("https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/branches");
                DataProcessor.setBranchURL("https://api.github.com/repos/" + userName + "/" + repositoryName + "/branches");

                //Initial the index for get branch name
                DataProcessor.branchIndex = 0;

                //Start
                run();

            }
        });

        return v;
    }

    //This function handle the timer for continually get data
    public static void run(){

        Timer timer = new Timer();
        NewTimerTask timerTask = new NewTimerTask();
        //10s for update testing
        //timer.schedule(timerTask, 0, 10000);
        //600s for later
        //timer.schedule(timerTask, 0, 600000);
        //1000s for single function testing
        timer.schedule(timerTask, 0, 1000000);

    }
}
