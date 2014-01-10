package com.chron_stat_android;

import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.Team;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MatchListActivity extends MainActivity implements
		MatchListFragment.OnItemClickListener {

	private Team currentTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_list);
		
		MatchListFragment plf = (MatchListFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_matchesList);
		plf.setListener(this);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

		currentTeam = (Team) getIntent().getExtras().getSerializable("team");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.matches_list, menu);
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
			addAction();
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***************************************************************************
	 * La fonction utilisée quand le bouton d'ajout de la barre d'action est
	 * clické
	 * <ul>
	 * <li>Si on trouve un frameLayout, on se trouve en mobile view:
	 * <ul>
	 * <li>on démarre l'activité d'ajout d'utilisateurs</li>
	 * </ul>
	 * </li>
	 * <li>Sinon, c'est qu'on se trouve en tablet view:
	 * <ul>
	 * <li>Si un AddFragment existe déjà, on le réutilise en effaçant son
	 * contenu.</li>
	 * <li>Si un EditFragment existe déjà, on le remplace par un nouveau
	 * AddFragment</li>
	 * <li>Sinon on crée tout simplement un nouveau AddFragment</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @see com.chron_stats_android_prototype.UserListFragment.OnItemClickListener#onItemClick(com.chron_stats_android_prototype.model.User)
	 **************************************************************************/
	public void addAction() {
		Intent intent = new Intent(this, AddMatchActivity.class);
		intent.putExtra("team", currentTeam);
		startActivity(intent);
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

	@Override
	public void onItemClick(Match match) {
		Intent intent = new Intent(this, MatchDetailsActivity.class);
		intent.putExtra("match", match);
		intent.putExtra("team", currentTeam);
		startActivity(intent);
	}

}
