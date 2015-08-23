package com.codepath.apps.TwitterClient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.fragments.ProfileFragment;
import com.codepath.apps.TwitterClient.fragments.UserTimelineFragment;

/**
 * Activity that displays the user details and also the user timeline
 */
public class ProfileActivity extends MyActionBar implements ProfileFragment.OnScreenNameAvailable {

    UserTimelineFragment fragmentUserTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Retrieve screen name from previous activity to display in actionbar
        String screen_name = getIntent().getStringExtra("screen_name");
        getSupportActionBar().setTitle(screen_name);
        if(savedInstanceState == null){
            // Create usertimeline fragment and pass information over
            fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);
            ProfileFragment fragmentProfile = ProfileFragment.newInstance(screen_name);
            //Display user fragment within this activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.replace(R.id.flProfile, fragmentProfile);
            ft.commit();
        }
    }

    /**
     * Refreshes timeline when activity before (i.e. compose activity) completes
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Changes activity title to screen name
     *
     * @param screen_name
     */
    @Override
    public void onRssItemSelected(String screen_name) {
        getSupportActionBar().setTitle("@" + screen_name);
    }
}
