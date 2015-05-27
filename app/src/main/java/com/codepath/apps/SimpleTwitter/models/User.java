package com.codepath.apps.SimpleTwitter.models;

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

    public static User fromJSON(JSONObject object){
        User user = new User();
        try {
            user.name = object.getString("name");
            user.uid = object.getLong("id");
            user.screenName = object.getString("screen_name");
            user.profileImageURL = object.getString("profile_image_url");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
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
}
