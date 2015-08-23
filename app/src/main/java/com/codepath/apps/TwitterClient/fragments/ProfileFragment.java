package com.codepath.apps.TwitterClient.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TwitterApplication;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.apps.TwitterClient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Fragment that displays the user's twitter profile
 */
public class ProfileFragment extends Fragment {

    protected TwitterClient client;
    protected User user;
    protected ImageView ivUserProfile;
    protected ImageView ivBanner;
    protected TextView tvUserName;
    protected TextView tvUserTag;
    protected TextView tvUserFollowers;
    protected TextView tvUserFollowing;
    // To change actionbar title
    private OnScreenNameAvailable listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screen_name = getArguments().getString("screen_name");
        // Initiate REST client to make API request to obtain user details
        client = TwitterApplication.getRestClient();
        client.getUserDetails(screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // Create rounded transformation for user pic
                Transformation transformation;
                transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.WHITE)
                        .borderWidthDp(2)
                        .cornerRadiusDp(5)
                        .oval(false)
                        .build();
                // Load user profile pic
                Picasso.with(getActivity().getApplicationContext()).load(user.getProfileImageURL()).fit().transform(transformation).into(ivUserProfile);
                if(user.getBackground() != null){
                    ivBanner.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    Picasso.with(getActivity().getApplicationContext()).load(user.getBackground()).into(ivBanner);
                }
                tvUserName.setText(user.getName());
                tvUserTag.setText(user.getTagline());
                tvUserFollowers.setText(user.getFollowers() + " FOLLOWERS");
                tvUserFollowing.setText(user.getFollowing() + " FOLLOWING");
                listener.onRssItemSelected(user.getScreenName());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("getUserDetails: ", errorResponse.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ivUserProfile = (ImageView) v.findViewById(R.id.ivUserProfile);
        tvUserName  = (TextView) v.findViewById(R.id.tvUserName);
        tvUserTag = (TextView) v.findViewById(R.id.tvUserTag);
        tvUserFollowers = (TextView) v.findViewById(R.id.tvUserFollowers);
        tvUserFollowing = (TextView) v.findViewById(R.id.tvUserFollowing);
        ivBanner = (ImageView) v.findViewById(R.id.ivBanner);
        return v;
    }

    /**
     * Instantiate fragment
     *
     * @param screen_name
     * @return ProfileFragment
     */
    public static ProfileFragment newInstance(String screen_name) {
        ProfileFragment fragmentDemo = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    /**
     * Define the events that the fragment will use to communicate
     */
    public interface OnScreenNameAvailable {
        public void onRssItemSelected(String link);
    }

    /**
     *  Store the listener (activity) that will have events fired once the fragment is attached
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnScreenNameAvailable) {
            listener = (OnScreenNameAvailable) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ProfileFragment.OnScreenNameAvailable");
        }
    }
}
