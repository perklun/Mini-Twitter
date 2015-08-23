package com.codepath.apps.TwitterClient.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model that represents a Twitter User
 * Build for ORM
 * Deserialize the user json to an User object
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

    /**
     * Construct a user from a JSON object
     *
     * @param object
     * @return user
     */
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

    /**
     * Getter for user name
     *
     * @return name
     */
    public String getName() {return name;
    }

    /**
     * Getter for user uid
     *
     * @return uid
     */
    public long getUid() {
        return uid;
    }

    /**
     * Getter for user screenname
     *
     * @return screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Getter for user profile photo url
     *
     * @return profileImageURL
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * Getter for user tagline
     *
     * @return tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * Getter for nunber of user follows as a string
     *
     * @return String.valueOf(followers)
     */
    public String getFollowers() {
        return String.valueOf(followers);
    }

    /**
     * Getter for nunber of user following as a string
     *
     * @return String.valueOf(following)
     */
    public String getFollowing() {
        return String.valueOf(following);
    }

    /**
     * Getter for return background URL as a string
     *
     * @return background
     */
    public String getBackground() { return background;    }
}
