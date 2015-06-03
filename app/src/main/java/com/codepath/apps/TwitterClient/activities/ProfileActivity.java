package com.codepath.apps.TwitterClient.activities;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.fragments.HomeTimelineFragment;
import com.codepath.apps.TwitterClient.fragments.MentionsTimelineFragment;
import com.codepath.apps.TwitterClient.fragments.ProfileFragment;
import com.codepath.apps.TwitterClient.fragments.UserTimelineFragment;

public class ProfileActivity extends MyActionBar implements ProfileFragment.OnScreenNameAvailable {

    UserTimelineFragment fragmentUserTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //get screename from previous activity
        String screen_name = getIntent().getStringExtra("screen_name");
        getSupportActionBar().setTitle(screen_name);
        if(savedInstanceState == null){
            //create usertimeline fragment and pass information over
            fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);
            ProfileFragment fragmentProfile = ProfileFragment.newInstance(screen_name);
            //Display user fragment within this activity
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.replace(R.id.flProfile, fragmentProfile);
            // Complete the changes added above
            ft.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            fragmentUserTimeline.loadNewTweets();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRssItemSelected(String screen_name) {
        getSupportActionBar().setTitle("@" + screen_name);
    }
}
