package com.chron_stat_android;

import com.chron_stat_android.model.Team;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class TeamListActivity extends MainActivity implements
		TeamListFragment.OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_list);
		
		TeamListFragment teamListFragment = (TeamListFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_teamList);
		teamListFragment.setListener(this);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_list, menu);
		return true;
	}

	@Override
	public void onItemClick(Team team) {
		/*
		 * FrameLayout ne se trouve pas dans le layout ou que l'orientation est
		 * verticale, il faut alors démarrer l'activité EditActivity en lui
		 * passant l'utilisateur à éditer
		 */
		Intent intent = new Intent(this, MatchListActivity.class);
		intent.putExtra("team", team);
		startActivity(intent);
	}
}
