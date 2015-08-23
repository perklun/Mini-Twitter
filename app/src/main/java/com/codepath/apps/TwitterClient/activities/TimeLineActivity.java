package com.codepath.apps.TwitterClient.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.fragments.HomeTimelineFragment;
import com.codepath.apps.TwitterClient.fragments.MentionsTimelineFragment;

/**
 * Activity that displays 2 fragments: Home and Mention timeline
 */
public class TimeLineActivity extends MyActionBar {

    private ViewPager viewpager;
    private SmartFragmentStatePagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        // Get view pager
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter for the pager
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapterViewPager);
        // Find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setIndicatorColor(Color.rgb(0, 172, 237));
        tabStrip.setTextColor(Color.rgb(0, 172, 237));
        // Attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewpager);
    }

    /**
     * When previous activity completes, reload both timelines in fragments
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * SmartFragment Adapter to manage tabs
     */
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};
        //adapter gets manager to insert or remove from activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        /**
         * Controls order and creation of fragments within pager
         *
         * @param position
         * @return
         */
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

        /**
         * Return tab title as CharSequence
         *
         * @param position position to retrieve
         * @return tabTitles[position]
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        /**
         * Returns int count of number of tabs
         *
         * @return tabTitles.length
         */
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
