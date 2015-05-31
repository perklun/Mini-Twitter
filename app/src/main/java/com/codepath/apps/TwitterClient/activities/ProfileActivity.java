package com.codepath.apps.TwitterClient.activities;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TwitterApplication;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.apps.TwitterClient.fragments.UserTimelineFragment;
import com.codepath.apps.TwitterClient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    public TwitterClient client;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        client.getUserDetails(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("getUserDetails: ", errorResponse.toString());
            }
        });
        //get screename from timeline activity
        String screen_name = getIntent().getStringExtra("screen_name");
        if(savedInstanceState == null){
            //create usertimeline fragment and pass information over
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);
            //Display user fragment within this activity
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            // Complete the changes added above
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        ImageView ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
        Picasso.with(this).load(user.getProfileImageURL()).into(ivUserProfile);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(user.getName());
        TextView tvUserTag = (TextView) findViewById(R.id.tvUserTag);
        tvUserTag.setText(user.getTagline());
        TextView tvUserFollowers = (TextView) findViewById(R.id.tvUserFollowers);
        tvUserFollowers.setText(user.getFollowers() + " followers");
        TextView tvUserFollowing = (TextView) findViewById(R.id.tvUserFollowing);
        tvUserFollowing.setText(user.getFollowing()+ " following");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
