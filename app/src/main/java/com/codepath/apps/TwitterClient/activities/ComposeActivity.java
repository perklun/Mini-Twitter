package com.codepath.apps.TwitterClient.activities;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TwitterApplication;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.apps.TwitterClient.fragments.ProfileFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;
import static android.widget.Toast.*;

/**
 * Activity that allows user to compose Twitter messages and post them
 */
public class ComposeActivity extends MyActionBar implements ProfileFragment.OnScreenNameAvailable {

    public EditText etComposeTweet;
    public TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        // Retrieve screename from timeline activity to update actionbar
        String screen_name = getIntent().getStringExtra("screen_name");
        if(savedInstanceState == null){
            //create profile fragment and pass information over to fragment
            ProfileFragment fragmentProfile = ProfileFragment.newInstance(screen_name);
            //Display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfile_compose, fragmentProfile);
            // Complete the changes added above
            ft.commit();
        }
        etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        tvCount = (TextView) findViewById(R.id.tvCount);
        // Keeps track of the number characters typed in the line
        TextWatcher TextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvCount.setText("140");
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvCount.setText(String.valueOf(140 - s.length()));
            }
            public void afterTextChanged(Editable s) {
            }
        };
        etComposeTweet.addTextChangedListener(TextEditorWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    /**
     * Method assigned to btnSubmit to post tweet and finish activity
     *
     * @param v
     */
    public void onSubmit(View v){
        if(etComposeTweet.getText().toString() != null && etComposeTweet.getText().length() > 0){
        String status = etComposeTweet.getText().toString();
            TwitterClient client = TwitterApplication.getRestClient();
            client.composeTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Status Posted", LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getApplicationContext(), "Tweet Failed", LENGTH_SHORT).show();
                }
            }, status);
            setResult(RESULT_OK, null);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRssItemSelected(String screen_name) {}
}
