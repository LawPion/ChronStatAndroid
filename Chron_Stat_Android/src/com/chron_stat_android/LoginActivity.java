package com.chron_stat_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/*******************************************************************************
 * LoginActivity.java
 * 
 * @author Crescenzio Fabio
 * @author Fresco Alain
 * @author Therisod Romain
 * @author Triki Mohamed
 * @author Walpen Laurian
 * 
 * @goal Activité gérant l'authentification d'un utilisateur avant que celui-ci
 *       puisse utiliser les fonctionnalités de l'application.
 * 
 * @notes -
 ******************************************************************************/
public class LoginActivity extends Activity {
	private final static String LOGIN_API_ENDPOINT_URL = "http://chron-stats.herokuapp.com/sessions";
	private SharedPreferences preferences;
	private String username;
	private String password;

	/***************************************************************************
	 * Ajoute un OnClickListener sur le bouton d'envoi du clavier afin de gérer
	 * l'authentification.
	 * 
	 * Ajoute un OnClickListener sur le bouton d'envoi afin de gérer
	 * l'authentification.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 **************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

		// Enregistre l'action listener pour le bouton d'envoi du clavier
		EditText editText = (EditText) findViewById(R.id.editText_password);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					sendLogin();
					handled = true;
				}
				return handled;
			}
		});

		// Enregistre l'action listener pour le bouton Envoyer...
		((Button) findViewById(R.id.button_sendLogin))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						sendLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/***************************************************************************
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 **************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_timekeeping:
			timeKeep();
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/***************************************************************************
	 * Gère l'envoi de l'authentification au serveur
	 * 
	 * Ici, contrôle seulement que le mot de passe est correct et passe à
	 * l'activité principale, sinon affiche un Toast informant que le mot de
	 * passe est erroné
	 **************************************************************************/
	private void sendLogin() {
		EditText usernameView = (EditText) findViewById(R.id.editText_username);
		username = usernameView.getText().toString();
		EditText passwordView = (EditText) findViewById(R.id.editText_password);
		password = passwordView.getText().toString();

		if (username.length() == 0 || password.length() == 0) {
			// input fields are empty
			Toast.makeText(this, "Veuillez remplir les champs",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			LoginTask loginTask = new LoginTask(LoginActivity.this);
			loginTask.execute(LOGIN_API_ENDPOINT_URL);
		}
	}

	class LoginTask extends AsyncTask<String, Void, String> {
		private static final String TAG = "LoginTask";
		private static final String MESSAGE_BUSY = "Server is busy. Please try again.";
		private static final String MESSAGE_ERROR = "There was an error processing your request. Please try again.";
		private static final String JSON_SUCCESS = "succès";
		private static final String JSON_INFO = "info";

		protected Context context = null;
		private String messageBusy;
		private String messageError;
		private String jsonSuccess;
		private String jsonInfo;

		public LoginTask(Context context) {
			this.context = context;
			this.messageBusy = MESSAGE_BUSY;
			this.messageError = MESSAGE_ERROR;
			this.jsonSuccess = JSON_SUCCESS;
			this.jsonInfo = JSON_INFO;
		}

		@Override
		protected String doInBackground(String... urls) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(urls[0]);
			JSONObject holder = new JSONObject();
			String response = null;

			try {
				try {
					// add the user email and password to
					// the params
					holder.put("login", username);
					holder.put("password", password);
					StringEntity se = new StringEntity(holder.toString());
					post.setEntity(se);

					// setup the request headers
					post.setHeader("Accept", "application/json");
					post.setHeader("Content-Type", "application/json");

					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					response = client.execute(post, responseHandler);
					Log.d("HTTP RESPONSE", response);

				} catch (HttpResponseException e) {
					e.printStackTrace();
					Log.e("ClientProtocol", "" + e);
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("IO", "" + e);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("JSON", "" + e);
			}

			return response;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (!result.equals("false") && result != null) {
					// everything is ok
					SharedPreferences.Editor editor = preferences.edit();
					// save the returned auth_token into
					// the SharedPreferences
					editor.putString("AuthCookie", result);
					editor.commit();

					// launch the HomeActivity and close this one
					Intent intent = new Intent(getApplicationContext(),
							TeamListActivity.class);
					startActivity(intent);
					finish();
					Toast.makeText(context, "Authentification réussie",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "L'authentification a échoué.",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// something went wrong: show a Toast
				// with the exception message
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
						.show();
			} finally {
				super.onPostExecute(result);
			}
		}

		protected JSONObject queryUrlForJson(String url) {
			JSONObject json = new JSONObject();

			try {
				try {
					json.put(this.jsonSuccess, false);
					json = new JSONObject(getStringFromUrl(url, 0, 0));
				} catch (SocketTimeoutException e) {
					json.put(this.jsonInfo, this.messageBusy);
				} catch (Exception e) {
					StringWriter sWriter = new StringWriter();
					e.printStackTrace(new PrintWriter(sWriter));
					Log.e(TAG, sWriter.getBuffer().toString());
					json.put(this.jsonInfo, this.messageError);
				}
			} catch (JSONException e) {
				StringWriter sWriter = new StringWriter();
				e.printStackTrace(new PrintWriter(sWriter));
				Log.e(TAG, sWriter.getBuffer().toString());
				return null;
			}

			return json;
		}

		private String getStringFromInputStream(InputStream is)
				throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}

		private String getStringFromUrl(String url, int connectTimeout,
				int readTimeout) throws MalformedURLException, JSONException,
				IOException {
			URL urlObject = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) urlObject
					.openConnection();
			String jsonString = "";

			if (connectTimeout != 0) {
				urlConn.setConnectTimeout(connectTimeout);
			}
			if (readTimeout != 0) {
				urlConn.setReadTimeout(readTimeout);
			}

			try {
				jsonString = getStringFromInputStream(urlConn.getInputStream());
			} finally {
				urlConn.disconnect();
			}
			return jsonString;
		}
	}
	
	public void timeKeep() {
		Intent intent = new Intent(getApplicationContext(),
				TimeKeepingActivity.class);
		startActivity(intent);
	}
}
