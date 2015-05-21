package se.chalmers.group8.service.connectors;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.net.MalformedURLException;

import se.chalmers.group8.session.PivotalSession;

/**
 * Created by patrik on 2015-05-18.
 */
public class WearMessageListener extends WearableListenerService{

    private static final String MESSAGE_PATH = "/start_activity";

    private String token;
    private String projectID;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(MESSAGE_PATH)){

            PivotalSession pivotalSession = PivotalSession.getInstance();
            token = pivotalSession.getToken();
            projectID = pivotalSession.getProjectID();

            String message = new String(messageEvent.getData());
            String name = parseBetween(message, "name ", " and");
            String description = parseBetween(message, "description ", "");

            if(pivotalSession.getStatus().equals("loggedIn") && name.length() > 0
                    && description.length() > 0) // If input was said correctly
                createNewStory(name, description);
            else
                Toast.makeText(this, "Please say the phrase in a correct way", Toast.LENGTH_LONG).show();
        } else
            super.onMessageReceived(messageEvent);
    }

    private void createNewStory(String name, String description) {
        PivotalTracker pt = new PivotalTracker(token, new UpdateFinish() {
            @Override
            public void onUpdateFinished(int callFunction, String result) {

            }
        });

        pt.setProjectID(projectID);

        try {
            pt.create(name, description);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static String parseBetween(String text, String startWord, String endWord) {

        int beginIndex = 0;
        int endIndex = text.length();

        if(startWord.length() > 0)
            beginIndex = text.indexOf(startWord);
        if(beginIndex != -1)
            beginIndex += startWord.length();

        if(endWord.length() > 0)
            endIndex = text.indexOf(endWord);

        String result = "";
        if(beginIndex >= 0 && beginIndex < text.length() && endIndex > 0 && endIndex <= text.length())
            result = text.substring(beginIndex, endIndex);
        return result;
    }
}
