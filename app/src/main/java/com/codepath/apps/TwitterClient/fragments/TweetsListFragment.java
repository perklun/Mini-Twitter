package com.codepath.apps.TwitterClient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TweetsArrayAdapter;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.apps.TwitterClient.activities.ProfileActivity;
import com.codepath.apps.TwitterClient.models.Tweet;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent Fragment that list tweets in a list view
 * Extended by HomeTimeLineFragment, MentionsTimeLineFragment and UserTimelineFragment
 */
public class TweetsListFragment extends Fragment{

    protected TwitterClient client;
    protected long max_id = -1;
    public ListView lvTweets;
    public ArrayList<Tweet> listOfTweets;
    public TweetsArrayAdapter tweetsAdapter;
    protected SwipeRefreshLayout swipeContainer;
    public ProgressBar progressBarFooter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfTweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(), listOfTweets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);
        // Opens a new profile activity whenever an item is clicked
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Tweet tw = tweetsAdapter.getItem(position);
                String user_screen_name = tw.getUser().getScreenName();
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("screen_name", user_screen_name);
                startActivity(i);
            }
        });
        // Refreshes tweets when swipe
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewTweets();
                swipeContainer.setRefreshing(false);
            }
        });
        ListView lvItems = (ListView) v.findViewById(R.id.lvTweets);
        // Inflate the footer
        View footer = inflater.inflate(
                R.layout.footer_progress, null);
        // Find the progressbar within footer
        progressBarFooter = (ProgressBar)
                footer.findViewById(R.id.pbFooterLoading);
        // Add footer to ListView before setting adapter
        lvItems.addFooterView(footer);
        // Set the adapter AFTER adding footer
        lvItems.setAdapter(tweetsAdapter);
        return v;
    }

    /**
     * Show progress bar
     */
    public void showProgressBar() {
        progressBarFooter.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progress bar
     */
    public void hideProgressBar() {
        progressBarFooter.setVisibility(View.GONE);
    }

    /**
     * Parent method to populate tweets time line
     *
     * @param max_id
     */
    public void populateTimeLine(long max_id){}

    public void loadNewTweets(){
        tweetsAdapter.clear();
        populateTimeLine(-1);
        tweetsAdapter.notifyDataSetChanged();
    }

    /**
     * Add a list of tweets to existing list
     *
     * @param tweets
     */
    public void addAll(List<Tweet> tweets){
        tweetsAdapter.addAll(tweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    /**
     * Adds footer to the list default hidden progress
     */
    public void setupListWithFooter() {
        // Find the ListView
    }
}