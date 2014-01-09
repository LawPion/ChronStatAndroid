package com.chron_stat_android;

import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.Team;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class AddMatchActivity extends MainActivity {
	
	private Team currentTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_list);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		
		currentTeam = (Team) getIntent().getExtras().getSerializable("team");
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
		case R.id.action_add:
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***************************************************************************
	 * @see android.app.Activity#onResume()
	 **************************************************************************/
	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}

	/***************************************************************************
	 * Used to refresh UserListFragment in onResume and from add and edit
	 * fragments
	 **************************************************************************/
	public void refreshList() {
		MatchListFragment plf = (MatchListFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_matchesList);
		plf.refreshList();
	}

	public Team getTeam() {
		return currentTeam;
	}

}
