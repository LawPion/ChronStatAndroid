package com.chron_stat_android;

import java.io.File;

import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.Team;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchDetailsActivity extends Activity {
	private Match match;
	private Team currentTeam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_details);
		
		match = (Match)getIntent().getSerializableExtra("match");
		String date = match.getDate().replace("T", " / ");
		date.replace("Z", "");
		date.replace("-", ".");
		date = date.substring(0, date.lastIndexOf(':'));

		((TextView)findViewById(R.id.match_sheet_title)).setText("Feuille de match - "+currentTeam.getName());
		((TextView)findViewById(R.id.match_sheet_opponent_team)).setText(match.getTeam_id1_id() == currentTeam.getId() ? match.getTeam2().getName() : match.getTeam1().getName());
		((TextView)findViewById(R.id.match_sheet_date)).setText(date);
		((TextView)findViewById(R.id.match_sheet_gym)).setText(match.getGym().getName());
		((TextView)findViewById(R.id.match_sheet_status)).setText("TODO STATUS");

		((Button)findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new File(getFilesDir(), "match_" + match.getId()
						+ getString(R.string.JSON_EXT)).delete();
				Intent intent = new Intent(getApplicationContext(),
						MatchListActivity.class);
				intent.putExtra("team", currentTeam);
				startActivity(intent);
			}
		});
		((Button)findViewById(R.id.send_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sendMatch();
				Intent intent = new Intent(getApplicationContext(),
						TeamListActivity.class);
				startActivity(intent);
			}
		});
		((Button)findViewById(R.id.launch_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(),
						TimeKeepingActivity.class);
				intent.putExtra("match", match);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_details, menu);
		return true;
	}
	
	public void sendMatch() {
		// TODO
	}
}
