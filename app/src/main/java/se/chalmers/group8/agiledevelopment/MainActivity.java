package se.chalmers.group8.agiledevelopment;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.net.MalformedURLException;
import java.util.Timer;

<<<<<<< Updated upstream
=======
import se.chalmers.group8.github.connector.DataProcessor;
import se.chalmers.group8.github.connector.NewTimerTask;
>>>>>>> Stashed changes
import se.chalmers.group8.service.connectors.PivotalTracker;


public class MainActivity extends ActionBarActivity {

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Home", "Progress", "Tasks", "Planning Game"};
    int NumbOfTabs = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Need to take input later, need to take input from user
        final String userName = "Awarath";
        final String repositoryName = "AgileDevelopmentProcesses";

<<<<<<< Updated upstream
        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,NumbOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
=======
        //Button for starting Github connection
        Button githubButton = (Button) findViewById(R.id.button2);
        githubButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                //The link to parse
                //CreateURL.setBranchURL("https://api.github.com/repos/Awarath/AgileDevelopmentProcesses/branches");
                DataProcessor.setBranchURL("https://api.github.com/repos/" + userName + "/" + repositoryName + "/branches");

                //Initial the index for get branch name
                DataProcessor.branchIndex = 0;

                //This part can be removed
                //Github github = new Github();
                //github.getRequest();

                //Start
                run();
>>>>>>> Stashed changes

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
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
}
