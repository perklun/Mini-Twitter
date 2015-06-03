package com.codepath.apps.TwitterClient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.TwitterClient.R;
import com.codepath.apps.TwitterClient.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, TimeLineActivity.class);
		i.putExtra("offline", 0);
		startActivity(i);
		Toast.makeText(this, "Login Successful.", Toast.LENGTH_SHORT).show();
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		Toast.makeText(this, "Login Failed.", Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		if(isNetworkAvailable() == true && isOnline() == true ){
			getClient().connect();
		} else{
			onLoginFailure(new Exception());
			Intent i = new Intent(this, TimeLineActivity.class);
			i.putExtra("offline", 1);
			startActivity(i);
			Toast.makeText(this, "Going to offline mode...", Toast.LENGTH_SHORT).show();
		}
	}

	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public Boolean isOnline() {
		try {
			Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
			int returnVal = p1.waitFor();
			boolean reachable = (returnVal==0);
			return reachable;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


}
