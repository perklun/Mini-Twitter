package com.codepath.apps.SimpleTwitter;

import org.scribe.builder.api.Api;

import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "H6Bi3Iq57DgPMKgmTZzpHtWrN";       // Change this
	public static final String REST_CONSUMER_SECRET = "1JdoUwCubxtvOfZ5arP0Fd52cZVEpQDplBzvLqmmzgNNP4xBcR"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpSimpleTwitter"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
/**	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}
**/
	//Obtain home timeline from twitter, with count and since ID, success and failure handled by handler
	public void getHomeTimeLine(AsyncHttpResponseHandler handler, int count, long since_id, long max_id){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", count);
		//params.put("since_id", since_id);
		if(max_id > 0){
			params.put("max_id", max_id);
		}
		Log.d("DEBUG MAX_ID: ", String.valueOf(max_id));
		//Execute the request
		getClient().get(apiUrl, params, handler);
		Log.d("Get completed: ", apiUrl);

	}

	//Composing a tweet
	public void composeTweet(AsyncHttpResponseHandler handler, String tweet_text){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet_text);
		Log.d("DEBUG STATUS POST: ", tweet_text);
		//Execute the request
		getClient().post(apiUrl, params, handler);
	}

	//Obtain user details
	public void getUserDetails(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		//Execute the request
		Log.d("Getting user: ", apiUrl);
		getClient().get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	*/

}