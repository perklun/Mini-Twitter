package com.codepath.apps.SimpleTwitter.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by PerkLun on 5/23/2015.
 * represents attributes of tweet
 * Parse the JSON from twitter, encapsulate state logic or display logic
 */
@Table(name = "Tweet")
public class Tweet extends Model {

    @Column(name = "body")
    private String body;
    @Column(name = "user")
    private User user;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; //unique ID of tweet
    @Column (name = "createdAt")
    private String createdAt;

    //default constructor for ActiveAndroid Model
    public Tweet(){
        super();
    }
    //extract values from json and extract them
    public static Tweet fromJSON(JSONObject object){
        Tweet tweet = new Tweet();
        try {
            tweet.body = object.getString("text");
            tweet.uid = object.getLong("id");
            tweet.createdAt = getRelativeTimeAgo(object.getString("created_at"));
            tweet.user = User.fromJSON(object.getJSONObject("user"));
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array){
        ArrayList<Tweet> listOfTweets = new ArrayList<Tweet>();
        //iterate array
        for(int i=0; i<array.length();i++){
            try {
                JSONObject tweetJSONObject = array.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSONObject);
                if(tweet != null){
                    listOfTweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue; //even if one tweet fails;
            }
        }
        return listOfTweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    public static List<Tweet> getAll() {
        // This is how you execute a query
     //   return new Select().from(Tweet.class).orderBy("Name ASC").executeSingle();
        return new Select().from(Tweet.class).orderBy("uid DESC").execute();
    }


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
