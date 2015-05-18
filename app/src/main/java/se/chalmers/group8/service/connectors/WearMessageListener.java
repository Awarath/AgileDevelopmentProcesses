package se.chalmers.group8.service.connectors;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by patrik on 2015-05-18.
 */
public class WearMessageListener extends WearableListenerService implements UpdateFinish{

    private static final String MESSAGE_PATH = "/start_activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(MESSAGE_PATH)){
            String message = new String(messageEvent.getData());
            String name = parseBetween(message, "name", " and");
            String description = parseBetween(message, "description", "");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Log.d("Mobile", message);

            if(name.length() > 0 && description.length() > 0) // If input was said correctly
                createNewStory(name, description);
        } else
            super.onMessageReceived(messageEvent);
    }

    private void createNewStory(String name, String description) {
        Log.d("Mobile", "Name: " + name);
        Log.d("Mobile", "Description: " + description);


    }

    private String parseBetween(String text, String startWord, String endWord) {

        int beginIndex = 0;
        int endIndex = text.length();

        if(startWord.length() > 0)
            beginIndex = text.indexOf(startWord)+startWord.length();
        if(endWord.length() > 0)
            endIndex = text.indexOf(endWord);

        String result = "";
        if(beginIndex >= 0 && endIndex > 0)
            result = text.substring(beginIndex, endIndex);
        return result;
    }

    @Override
    public void onUpdateFinished(int callFunction, String result) {

    }
}
