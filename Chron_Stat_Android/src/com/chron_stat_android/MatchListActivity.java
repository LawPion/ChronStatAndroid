package com.chron_stat_android;

import com.chron_stat_android.model.Team;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MatchListActivity extends MainActivity {
	
	private Team currentTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matches_list);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		
		currentTeam = (Team) getIntent().getExtras().getSerializable("team");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.matches_list, menu);
		return true;
	}

	public Team getTeam() {
		return currentTeam;
	}

}
