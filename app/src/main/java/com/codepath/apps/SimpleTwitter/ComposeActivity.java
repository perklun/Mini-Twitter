package com.codepath.apps.SimpleTwitter;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitter.R;
import com.codepath.apps.SimpleTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.widget.Toast.*;

public class ComposeActivity extends ActionBarActivity {

    public EditText etComposeTweet;
    public TextView tvCount;
    public TextView tvUser_compose;
    public ImageView ivProfile_compose;
    public String Profile_image_url;
    public String name;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvUser_compose = (TextView) findViewById(R.id.tvUser_compose);
        ivProfile_compose = (ImageView) findViewById(R.id.ivProfile_compose);

        populateUserDetails();
        Log.d("DEBUG compose ", Profile_image_url + " " + name);


        //keeps track of the number characters typed in the line
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

    //send API request to populate time and fill listview
    //JSON response starts with [ ] which means it is a JSONArray
    private void populateUserDetails(){
        Log.d("DEBUG compose ", "Loading details...");
        if(isNetworkAvailable() == true) {
            //  if(isOnline() == true && isNetworkAvailable() == true) {
            client.getUserDetails(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG compose json:", response.toString());
                    try {
                        Profile_image_url = (String) response.get("profile_image_url");
                        name = (String) response.get("name");
                        tvUser_compose.setText(name);
                        Picasso.with(getApplicationContext()).load(Profile_image_url).into(ivProfile_compose);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG compose ", errorResponse.toString());
                }
            });
        }
        else{
                Toast.makeText(this, "Offline mode", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    public void onSubmit(View v){
        //etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        if(etComposeTweet.getText().toString() != null && etComposeTweet.getText().length() > 0){
        String status = etComposeTweet.getText().toString();
            Log.d("DEBUG TWEET: ", status);
            TwitterClient client = TwitterApplication.getRestClient();
            client.composeTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG TWEET SUCCESS: ", response.toString());
                    Toast.makeText(getApplicationContext(), "Status Posted", LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG TWEET FAILURE: ", errorResponse.toString());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
