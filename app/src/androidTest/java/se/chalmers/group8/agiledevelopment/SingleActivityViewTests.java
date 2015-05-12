package se.chalmers.group8.agiledevelopment;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Matthias on 5/7/2015.
 */
public class SingleActivityViewTests
        extends ActivityInstrumentationTestCase2<SingleActivityView> {

    private SingleActivityView singleActivityView;
    private TextView descriptionText;

    public SingleActivityViewTests() {
        super(SingleActivityView.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        singleActivityView = getActivity();
        descriptionText = (TextView)singleActivityView.findViewById(R.id.storyDescription);
    }

    public void testMyFirstTestTextView_labelText() {
        final String expected = "description";
        final String actual = descriptionText.getText().toString();
        assertEquals(expected, actual);
    }
}