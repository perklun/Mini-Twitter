package com.codepath.apps.TwitterClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.TwitterClient.EndlessScrollListener;
import com.codepath.apps.TwitterClient.TwitterApplication;
import com.codepath.apps.TwitterClient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by PerkLun on 5/30/2015.
 */
public class UserTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeLine(max_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeLine(listOfTweets.get(listOfTweets.size() - 1).getUid()-1);
                Log.d("onLoadMore - User: ", String.valueOf(listOfTweets.size()));
            }
        });
        return v;
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    //send API request to populate time and fill listview
    //JSON response starts with [ ] which means it is a JSONArray
    private void populateTimeLine(long max_id) {
        String screen_name = getArguments().getString("screen_name");
        //       if(isNetworkAvailable() == true) {
        //  if(isOnline() == true && isNetworkAvailable() == true) {
        client.getUserTimeLine(screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("populate - User:", response.toString());
                //deserialize JSON and create models, and load model data in listView
                addAll(Tweet.fromJSONArray(response));
                tweetsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG JSONObject Fail:", errorResponse.toString());
            }
        }, max_id);
       /*     if (listOfTweets.size() > 1) {
                //sets max_id to for infinite scrolling
                this.max_id = listOfTweets.get(listOfTweets.size() - 1).getUid();
                tweetsAdapter.notifyDataSetChanged();
            }
            Log.d("IN method ", String.valueOf(listOfTweets.size()));
            swipeContainer.setRefreshing(false);
        }
        else{
           /* List<Tweet> old_list = Tweet.getAll();
            if(old_list.size() > 1){
                listOfTweets.addAll(old_list);
                Toast.makeText(this, "Offline mode", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "No previous tweets found", Toast.LENGTH_SHORT).show();
            }*/
//        }
    }
}
