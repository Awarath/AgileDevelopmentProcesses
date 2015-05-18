package se.chalmers.group8.github.connector;

import java.util.TimerTask;

/**
 * This class start get data from Github.
 */

public class NewTimerTask extends TimerTask{

    public void run(){

        Github github = new Github();
        github.getRequest();

    }
}
