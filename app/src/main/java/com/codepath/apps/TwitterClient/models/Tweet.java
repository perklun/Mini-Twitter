package com.codepath.apps.TwitterClient.models;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Model that Represents attributes of tweet
 * Parse the JSON from twitter, encapsulate state logic or display logic\
 *Build to support ORM
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

    /**
     * default constructor for ActiveAndroid Model
     */
    public Tweet(){
        super();
    }
    // Extract values from json and extract them
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

    /**
     * Parse JSON array and create an arraylist of tweets
     *
     * @param array
     * @return listOfTweets arraylist of tweets
     */
    public static ArrayList<Tweet> fromJSONArray(JSONArray array){
        ArrayList<Tweet> listOfTweets = new ArrayList<Tweet>();
        // Iterate through JSON array
        for(int i=0; i<array.length();i++){
            try {
                JSONObject tweetJSONObject = array.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSONObject);
                if(tweet != null){
                    listOfTweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue; // Even if one tweet fails;
            }
        }
        return listOfTweets;
    }

    /**
     * Formats raw JSON date to relative date
     * e.g. 1d, 10m
     *
     * @param rawJsonDate
     * @return relativeDate
     */
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = getDateDifferenceForDisplay(new Date(dateMillis)); //shorter version
            //relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    /**
     * Helper method to get relative date
     *
     * @param inputdate
     * @return date difference as a String
     */
    public static String getDateDifferenceForDisplay(Date inputdate) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(inputdate);
        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();
        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;
        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffMinutes < 60) {
            return diffMinutes + "m";
        } else if (diffHours < 24) {
            return diffHours + "h";
        } else if (diffDays < 7) {
            return diffDays + "d";
        } else {
            SimpleDateFormat todate = new SimpleDateFormat("MMM dd",
                    Locale.ENGLISH);
            return todate.format(inputdate);
        }
    }

    /**
     * Getter for tweet body
     *
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Getter for tweet Uid
     *
     * @return uid
     */
    public long getUid() {
        return uid;
    }

    /**
     * Getter for tweet creation time
     *
     * @return createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter for user object who created the tweet
     *
     * @return user
     */
    public User getUser() {
        return user;
    }


}
