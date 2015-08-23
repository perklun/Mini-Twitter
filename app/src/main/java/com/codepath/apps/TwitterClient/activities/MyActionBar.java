package com.codepath.apps.TwitterClient.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Custom Action Bar, displays three buttons for different view
 */
public class MyActionBar extends ActionBarActivity {
    public static int REQUEST_CODE = 1;

    /**
     * Launches the ProfileActivity for user profile
     *
     * @param mi
     */
    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", "");
        startActivity(i);
    }

    /**
     * Launches the ComposeActivity for composing tweet
     *
     * @param item
     */
    public void onComposeView(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("screen_name", "");
        startActivityForResult(i, REQUEST_CODE);
    }

    /**
     * Launches the TimeLineActivity for timeline
     *
     * @param item
     */
    public void onTimeLineView(MenuItem item) {
        Intent i = new Intent(this, TimeLineActivity.class);
        startActivity(i);
    }
}
