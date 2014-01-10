package com.chron_stat_android;

import java.io.File;

import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.Team;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class AddMatchActivity extends MainActivity implements
		AddMatchFragment.OnItemClickListener {

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
	public void onItemClick(final Match match) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (new File(getFilesDir(), "match_" + match.getId()
				+ getString(R.string.JSON_EXT)).exists()) {
			// Add the buttons
			builder.setPositiveButton(R.string.ok_button,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					}).setTitle(R.string.already_created_dialog_title)
					.setMessage(R.string.already_created_dialog_message);

			// Create the AlertDialog
			dialog = builder.create();
		} else {
			// Add the buttons
			builder.setPositiveButton(R.string.ok_button,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							match.writeToStorage(AddMatchActivity.this);
							Intent intent = new Intent(AddMatchActivity.this, MatchDetailsActivity.class);
							intent.putExtra("match", match);
							intent.putExtra("team", currentTeam);
							startActivity(intent);
						}
					})
					.setNegativeButton(R.string.cancel_button,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							}).setTitle(R.string.already_created_dialog_title)
					.setMessage(R.string.already_created_dialog_message);

			// Create the AlertDialog
			dialog = builder.create();
		}
		dialog.show();
	}
}
