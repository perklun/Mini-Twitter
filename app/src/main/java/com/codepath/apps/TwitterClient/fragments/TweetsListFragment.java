package com.codepath.apps.TwitterClient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TweetsArrayAdapter;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.apps.TwitterClient.activities.ProfileActivity;
import com.codepath.apps.TwitterClient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PerkLun on 5/30/2015.
 */
public class TweetsListFragment extends Fragment{

    protected TwitterClient client;
    protected long max_id = -1;

    protected ListView lvTweets;
    protected ArrayList<Tweet> listOfTweets;
    protected TweetsArrayAdapter tweetsAdapter;

    //create lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfTweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(), listOfTweets);
    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);
        //opens a new profile activity whenever an item is clicked
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
        return v;
    }

    public void addAll(List<Tweet> tweets){
        tweetsAdapter.addAll(tweets);
    }

}
