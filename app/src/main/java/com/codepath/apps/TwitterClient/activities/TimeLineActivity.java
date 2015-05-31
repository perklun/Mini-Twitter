package com.codepath.apps.TwitterClient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.fragments.HomeTimelineFragment;
import com.codepath.apps.TwitterClient.fragments.MentionsTimelineFragment;

public class TimeLineActivity extends ActionBarActivity {

    public static int REQUEST_CODE = 1;
    private SwipeRefreshLayout swipeContainer;
    private int offline = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        //get view pager
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        //set the viewpager adapter for the pager
        viewpager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewpager);

      //  swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
      /*  swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(offline == 0){
                    listOfTweets.clear();
                    since_id = -1;
                    max_id = -1;
                    populateTimeLine(count, since_id, max_id);
                    Log.d("IN refresh ", String.valueOf(listOfTweets.size()));

                }
                else{
                    Intent i = new Intent(TimeLineActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        }
        ); */
        //configure refresh colors
/*        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //if in offline mode, load from sql
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

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        //when you want to add a screenname: i.putExtra("screen_name")
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Log.d("DEBUG REFRESH:", "populating new timeline");
            //clear old, relaunch time line
       //     listOfTweets.clear();
        //    tweetsAdapter.notifyDataSetChanged();
            since_id = -1;
            max_id = -1;
            populateTimeLine(count, since_id, max_id);
        //    Log.d("IN result ", String.valueOf(listOfTweets.size()));
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

    //launch compose activity
    public void onComposeAction(MenuItem mi){
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    //return oder of fragments in viewpager
    public class TweetsPagerAdapter extends FragmentPagerAdapter{
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
            } else{
                return new MentionsTimelineFragment();
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
