package se.chalmers.group8.service.connectors;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by patrik on 2015-05-18.
 */
public class WearMessageListener extends WearableListenerService {

    private static final String MESSAGE_PATH = "/start_activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("Mobile", "Message received");
        if(messageEvent.getPath().equals(MESSAGE_PATH)){
            String msg = new String(messageEvent.getData());
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }else
            super.onMessageReceived(messageEvent);
    }
}
