package com.codepath.apps.TwitterClient.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.fragments.HomeTimelineFragment;
import com.codepath.apps.TwitterClient.fragments.MentionsTimelineFragment;


public class TimeLineActivity extends MyActionBar {

    private int offline = 0;
    private ViewPager viewpager;
    private SmartFragmentStatePagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        //get view pager
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        //set the viewpager adapter for the pager
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapterViewPager);
        //find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setIndicatorColor(Color.rgb(0, 172, 237));
        tabStrip.setTextColor(Color.rgb(0, 172, 237));
        //attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewpager);

/*
        if in offline mode, load from sql
        offline = getIntent().getIntExtra("offline", 0);
        if(offline == 1){
            //retrieve old list
            List<Tweet> old_list = Tweet.getAll();
            if(old_list.size() > 1){
                listOfTweets.addAll(old_list);
                tweetsAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, "First load: no tweets yet!", Toast.LENGTH_SHORT).show();
            }
        } else{
            populateTimeLine(count, since_id, max_id); //launch initial list
        } */
        //    Log.d("IN onCreate ", String.valueOf(listOfTweets.size()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
           Log.d("onActivityResult: ", "update new fragments");
           HomeTimelineFragment fg_home_timeline = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
           fg_home_timeline.loadNewTweets();
           MentionsTimelineFragment fg_mention_timeline = (MentionsTimelineFragment) adapterViewPager.getRegisteredFragment(1);
           fg_mention_timeline.loadNewTweets();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
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

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //return order of fragments in viewpager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};
        //adapter gets manager to insert or remove from activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }
        //controls order and creation of fragments within pager
        @Override
        public Fragment getItem(int position) {
            //automatically cached
            if(position == 0){
                return new HomeTimelineFragment();
            } else if(position == 1){
                return new MentionsTimelineFragment();
            }
            else{
                return null;
            }
        }
        //return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        //how many items
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
