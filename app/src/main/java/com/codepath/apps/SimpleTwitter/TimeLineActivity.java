package com.codepath.apps.SimpleTwitter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ListView lvTweets;
    private ArrayList<Tweet> listOfTweets;
    private TweetsArrayAdapter tweetsAdapter;

    private int count = 25;
    private long since_id = -1;
    private long max_id = -1;

    public static int REQUEST_CODE = 1;

    private SwipeRefreshLayout swipeContainer;

    private int offline = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        listOfTweets = new ArrayList<Tweet>();

        client = TwitterApplication.getRestClient();

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeLine(count, since_id, listOfTweets.get(listOfTweets.size()-1).getUid());
                Log.d("IN onLoadMore ", String.valueOf(listOfTweets.size()));

            }
        });
        tweetsAdapter = new TweetsArrayAdapter(this, listOfTweets);
        lvTweets.setAdapter(tweetsAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(offline == 0){
                listOfTweets.clear();
                tweetsAdapter.notifyDataSetChanged();
                since_id = -1;
                max_id = -1;
                populateTimeLine(count, since_id, max_id);
                    tweetsAdapter.notifyDataSetChanged();
                Log.d("IN refresh ", String.valueOf(listOfTweets.size()));
                swipeContainer.setRefreshing(false);}
                else{
                    Intent i = new Intent(TimeLineActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        //configure refresh colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
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
            }else{
                Toast.makeText(this, "First load: no tweets yet!", Toast.LENGTH_SHORT).show();
            }
        } else{
            populateTimeLine(count, since_id, max_id); //launch initial list
        }
        Log.d("IN onCreate ", String.valueOf(listOfTweets.size()));

        Log.d("SQL: ", Tweet.getAll().toString());

    }

    //send API request to populate time and fill listview
    //JSON response starts with [ ] which means it is a JSONArray
    private void populateTimeLine(int count, long since_id, long max_id) {
        if(isOnline() == true) {
          //  if(isOnline() == true && isNetworkAvailable() == true) {
            client.getHomeTimeLine(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("DEBUG JSONARRAY Success", response.toString());
                    //deserialize JSON and create models, and load model data in listView
                    listOfTweets.addAll(Tweet.fromJSONArray(response));
                    Log.d("DEBUG JSONARRAY List", listOfTweets.toString());
                    tweetsAdapter.notifyDataSetChanged();
                    //              swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG JSONARRAY Failure", errorResponse.toString());
                }
            }, count, since_id, max_id);
            if (listOfTweets.size() > 1) {
                //sets since_id to for infinite scrolling
                this.max_id = listOfTweets.get(listOfTweets.size() - 1).getUid();
            }
            Log.d("IN method ", String.valueOf(listOfTweets.size()));
        }
        else{
            List<Tweet> old_list = Tweet.getAll();
            if(old_list.size() > 1){
                listOfTweets.addAll(old_list);
                Toast.makeText(this, "Offline mode", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No previous tweets found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Log.d("DEBUG REFRESH:", "populating new timeline");
            //clear old, relaunch time line
            listOfTweets.clear();
            since_id = -1;
            max_id = -1;
            populateTimeLine(count, since_id, max_id);
            tweetsAdapter.notifyDataSetChanged();
            Log.d("IN result ", String.valueOf(listOfTweets.size()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

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
}
