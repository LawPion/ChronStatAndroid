package com.chron_stat_android;

import android.os.Bundle;
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
}
