package com.chron_stat_android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
	protected SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!preferences.contains("AuthCookie")) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/***************************************************************************
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 **************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_signout:
			signOut();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void signOut() {
		// deletes every value stored in preferences
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();

		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
