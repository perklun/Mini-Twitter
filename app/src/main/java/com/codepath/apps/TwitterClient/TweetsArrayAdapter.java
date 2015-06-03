package com.codepath.apps.TwitterClient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwitterClient.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by PerkLun on 5/23/2015.
 * taking the tweet objects and turning them into views
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    //consider implementing viewholder pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        //find or inflate the template, if convertView is null, it means it is not being recycled, and therefore you need to inflate it
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet, parent, false); //false so you don't update first
        }
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
        TextView tvRelativeTime  = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        tvUserName.setText(tweet.getUser().getScreenName());
        tvTweet.setText(tweet.getBody());
        tvRelativeTime.setText(tweet.getCreatedAt());
        ivProfilePic.setImageResource(android.R.color.transparent); //clear out old image
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(2)
                .cornerRadiusDp(5)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageURL()).fit().transform(transformation).into(ivProfilePic);
        return convertView;
    }
}
