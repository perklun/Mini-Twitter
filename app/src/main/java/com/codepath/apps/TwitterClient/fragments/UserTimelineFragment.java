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
 * Fragment that displays only the user's tweets
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
                showProgressBar();
                if (listOfTweets.size() == 0){
                    populateTimeLine(-1);
                } else {
                    populateTimeLine(listOfTweets.get(listOfTweets.size() - 1).getUid() - 1);
                }
                hideProgressBar();
            }
        });
        return v;
    }

    /**
     * Create fragment
     *
     * @param screen_name
     * @return UserTimelineFragment
     */
    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    /**
     * Handler to send API request to twitter to retrieve home timeline
     * @param max_id
     */
    @Override
    public void populateTimeLine(long max_id) {
        String screen_name = getArguments().getString("screen_name");
        client.getUserTimeLine(screen_name, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize JSON and create models, and load model data in listView
                addAll(Tweet.fromJSONArray(response));
                tweetsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG JSONObject Fail:", errorResponse.toString());
            }
        });
    }
}
