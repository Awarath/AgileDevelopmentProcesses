package se.chalmers.group8.github.connector;

import java.util.TimerTask;

import se.chalmers.group8.service.connectors.UpdateFinish;

/**
 * This class start get data from Github.
 */

public class NewTimerTask extends TimerTask{

    UpdateFinish updateFinish;

    public NewTimerTask(UpdateFinish updateFinish) {
        this.updateFinish = updateFinish;
    }

    public void run(){

        Github github = new Github(updateFinish);
        github.getRequest();

    }
}
