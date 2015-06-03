package com.codepath.apps.TwitterClient.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PerkLun on 5/23/2015.
 * deserialize the user json to an User object
 */
@Table(name = "User")
public class User extends Model{
    @Column(name = "name")
    private String name;
    @Column(name = "uid")
    private long uid;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageURL")
    private String profileImageURL;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followers")
    private int followers;
    @Column(name = "following")
    private int following;
    @Column(name = "background")
    private String background;

    public static User fromJSON(JSONObject object){
        User user = new User();
        try {
            user.name = object.getString("name");
            user.uid = object.getLong("id");
            user.screenName = object.getString("screen_name");
            user.profileImageURL = object.getString("profile_image_url");
            user.tagline = object.getString("description");
            user.followers = object.getInt("followers_count");
            user.following = object.getInt("friends_count");
            try{
                user.background = object.getString("profile_banner_url");
            }catch (JSONException e){
                user.background = null;
            }
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public String getTagline() {
        return tagline;
    }

    public String getFollowers() {
        return String.valueOf(followers);
    }

    public String getFollowing() {
        return String.valueOf(following);
    }

    public String getBackground() { return background;    }
}
