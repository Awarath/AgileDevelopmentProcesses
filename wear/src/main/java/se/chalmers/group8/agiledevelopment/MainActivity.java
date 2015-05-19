package se.chalmers.group8.agiledevelopment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    private TextView mTextView;
    private static final String MESSAGE_PATH = "/start_activity";

    private static final int SPEECH_REQUEST_CODE = 0;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        initApiClient(this);
    }

    private void initApiClient(Context context) {
        client = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        client.registerConnectionCallbacks(this);
        client.connect();
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            sendMessage(MESSAGE_PATH, spokenText);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    private void sendMessage(final String path, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Get connected nodes
                String nodeID = "";
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(client, node.getId(), path, text.getBytes()).await();
                    if(result.getStatus().isSuccess())
                        Log.d("Wear", "Message sent");
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //sendMessage(MESSAGE_PATH, "Hej");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onLogoClick(View view) {
        displaySpeechRecognizer();
    }
}
