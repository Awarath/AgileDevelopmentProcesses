package se.chalmers.group8.agiledevelopment;

/**
 * Created by nattapon on 28/04/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Tab2 extends Fragment {


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_2,container,false);

        final EditText editText_User = (EditText) v.findViewById(R.id.edit_text_user);
        final EditText editText_Repo = (EditText) v.findViewById(R.id.edit_text_repo);

        //Button for starting Github connection
        Button githubButton = (Button) v.findViewById(R.id.GithubButton);
        githubButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                //Sending data to the intent
                final Intent intent = new Intent(getActivity(), CreateOutputView.class);
                intent.putExtra("name", editText_User.getText().toString());
                intent.putExtra("repository", editText_Repo.getText().toString());
                startActivity(intent);

            }
        });

        return v;
    }

}
