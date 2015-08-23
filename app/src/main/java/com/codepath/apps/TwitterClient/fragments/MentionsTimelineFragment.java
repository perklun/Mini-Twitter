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
 * Fragment that displays all the tweet in the Mention timeline
 */
public class MentionsTimelineFragment extends TweetsListFragment {

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
     * Handler to send API request to twitter to retrieve home timeline
     *
     * @param max_id max id to retrieve tweets
     */
    @Override
    public void populateTimeLine(long max_id) {
        client.getMentionsTimeLine(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize JSON and create models, and load model data in listView
                addAll(Tweet.fromJSONArray(response));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG JSONObject Fail:", errorResponse.toString());
            }
        });
    }
}
