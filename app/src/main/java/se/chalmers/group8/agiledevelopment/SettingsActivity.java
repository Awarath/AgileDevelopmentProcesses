package se.chalmers.group8.agiledevelopment;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import se.chalmers.group8.service.connectors.PivotalTracker;
import se.chalmers.group8.service.connectors.UpdateFinish;


public class SettingsActivity extends ActionBarActivity implements UpdateFinish {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] savedData = readFromFile().split(";");
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

        System.out.println("Hallo Welt!" + getFilesDir());
        EditText userNameText = (EditText) findViewById(R.id.userName);
        EditText passwordText = (EditText) findViewById(R.id.password);

        //PivotalTracker tracker = new PivotalTracker();

        String filename = "gitTracker";
        String string = "Hello world!";

        writeToFile(userNameText.getText() + ";" + passwordText.getText());
    }

        @Override
    public void onUpdateFinished(int callFunction, String result) {

    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");

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
