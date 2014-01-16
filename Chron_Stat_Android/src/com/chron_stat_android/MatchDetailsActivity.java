package com.chron_stat_android;

import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.MatchSheet;
import com.chron_stat_android.model.Team;
import com.chron_stat_android.tasks.*;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MatchDetailsActivity extends MainActivity implements
		SendJSONTask.CallBackListener {
	private Match match;
	private Team currentTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_details);

		match = (Match) getIntent().getSerializableExtra("match");
		currentTeam = (Team) getIntent().getSerializableExtra("team");
		String date = match.getDate().replace("T", " / ");
		date.replace("Z", "");
		date.replace("-", ".");
		date = date.substring(0, date.lastIndexOf(':'));

		((TextView) findViewById(R.id.match_sheet_title))
				.setText("Feuille de match - " + currentTeam.getName());
		((TextView) findViewById(R.id.match_sheet_opponent_team)).setText(match
				.getTeam_id1_id() == currentTeam.getId() ? match.getTeam2()
				.getName() : match.getTeam1().getName());
		((TextView) findViewById(R.id.match_sheet_date)).setText(date);
		((TextView) findViewById(R.id.match_sheet_gym)).setText(match.getGym()
				.getName());

		((Button) findViewById(R.id.delete_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						match.deleteFromStorage(MatchDetailsActivity.this);
						Intent intent = new Intent(getApplicationContext(),
								MatchListActivity.class);
						intent.putExtra("team", currentTeam);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.send_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						sendMatch();
						match.deleteFromStorage(MatchDetailsActivity.this);
						Intent intent = new Intent(getApplicationContext(),
								TeamListActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.launch_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(getApplicationContext(),
								TimeKeepingActivity.class);
						intent.putExtra("match", match);
						intent.putExtra("team", currentTeam);
						startActivity(intent);
					}
				});

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
	}

	@Override
	protected void onResume() {
		if (match.isOver()) {
			((TextView) findViewById(R.id.match_sheet_status))
					.setText("Terminé");
			((TextView) findViewById(R.id.match_sheet_status))
					.setTextColor(Color.RED);
			((Button) findViewById(R.id.launch_button)).setEnabled(false);
		} else {
			((TextView) findViewById(R.id.match_sheet_status))
					.setText("Non commencé");
			((TextView) findViewById(R.id.match_sheet_status))
					.setTextColor(Color.GREEN);
			((Button) findViewById(R.id.send_button)).setEnabled(false);
		}
		
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_details, menu);
		return true;
	}

	public void sendMatch() {
		MatchSheet sheet = new MatchSheet(match);

		// Définition de l'adresse du serveur
		String url = getString(R.string.SERVER_URL) + "matches/matchsheet/"
				+ match.getId();

		// Méthode de la requête HTTP à envoyer (selon CRUD: Create = POST)
		String method = "POST";

		// Création de la SendJSONTask
		SendJSONTask task = new SendJSONTask();
		task.setListener(this);
		task.execute(new Request(preferences.getString("AuthCookie", "false"),
				url, method, sheet, MatchSheet.class));
	}

	/****************************************************************************
	 * Callback appelé par la SendJSONTask quand le traitement des requêtes est
	 * fini.
	 * 
	 * @see com.chron_stats_android_prototype.tasks.SendJSONTask.CallBackListener#callback(java.lang.String)
	 ***************************************************************************/
	@Override
	public void callback(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(),
				TeamListActivity.class);
		startActivity(intent);
	}
}
