package se.chalmers.group8.agiledevelopment;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;

import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;
import se.chalmers.group8.session.PivotalSession;


public class SettingsActivity extends ActionBarActivity implements UpdateFinish {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] savedData = readFromFile("config.txt").split(";");
        if (savedData.length == 2) {
            EditText userNameText = (EditText) findViewById(R.id.userName);
            EditText passwordText = (EditText) findViewById(R.id.password);
            userNameText.setText(savedData[0]);
            passwordText.setText(savedData[1]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void loginButtonClicked(View view) {

        EditText userNameText = (EditText) findViewById(R.id.userName);
        EditText passwordText = (EditText) findViewById(R.id.password);

        handleLogin(userNameText.getText().toString(), passwordText.getText().toString());
    }

    public void handleLogin(String username, String password) {
        writeToFile(username + ";" + password, "config.txt");

        PivotalTracker tracker = new PivotalTracker(this);
        try {
            tracker.login(username, password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

        @Override
    public void onUpdateFinished(int callFunction, String result) {
        System.out.println(result);

        try {

            JSONObject object = new JSONObject(result);

            PivotalSession pivotalSession = PivotalSession.getInstance();
            pivotalSession.setSession(object.getString("username"), object.getString("api_token"));

            Toast.makeText(getApplicationContext(), "successfully logged in as " + object.getString("username"), Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not log in", Toast.LENGTH_SHORT).show();
        }
    }

    public void writeToFile(String data, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }

        return ret;
    }
}
