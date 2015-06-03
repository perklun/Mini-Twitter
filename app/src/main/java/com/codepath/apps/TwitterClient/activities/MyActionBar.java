package com.codepath.apps.TwitterClient.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by PerkLun on 5/30/2015.
 */
public class MyActionBar extends ActionBarActivity {
    public static int REQUEST_CODE = 1;

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", "");
        startActivity(i);
    }

    public void onComposeView(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("screen_name", "");
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onTimeLineView(MenuItem item) {
        Intent i = new Intent(this, TimeLineActivity.class);
        startActivity(i);
    }
}
