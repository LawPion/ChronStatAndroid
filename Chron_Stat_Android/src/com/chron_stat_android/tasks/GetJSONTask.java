package com.chron_stat_android.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/*******************************************************************************
 * GetJSONTask.java
 * 
 * @author Crescenzio Fabio
 * @author Fresco Alain
 * @author Therisod Romain
 * @author Triki Mohamed
 * @author Walpen Laurian
 * 
 * @goal Tache de requête de JSON depuis une URL.
 * 
 *       Télécharge un fichier JSON présent sur une certaine URL et le retourne
 *       une chaîne de caractères contenant le JSON désiré en résultat pour
 *       traitement.
 * 
 *       Utilisé pour peupler la liste d'utilisateur dans UserListFragment,
 *       ainsi que dans le fragment d'édition pour récupérer et afficher les
 *       informations d'un utilisateur.
 * 
 * @notes -
 ******************************************************************************/
public class GetJSONTask extends AsyncTask<String, Void, String[]> {

	// Listener du callback pour le traitement ultérieur du JSON.
	private CallBackListener cbl;

	/***************************************************************************
	 * L'interface servant à définir le callback appelé après l'obtention du
	 * JSON désiré.
	 **************************************************************************/
	public interface CallBackListener {
		/***********************************************************************
		 * Méthode callback à implémenter. Appelée une fois le JSON récupéré.
		 * 
		 * @param jsons
		 *            La chaîne de caractère contenant le JSON.
		 **********************************************************************/
		public void callback(String[] jsons);
	}

	/***************************************************************************
	 * Enregistre le listener à l'interface CallBackListener.
	 * 
	 * @param listener
	 *            La classe implémentant CallBackListener.
	 **************************************************************************/
	public void setListener(CallBackListener cbl) {
		this.cbl = cbl;
	}

	/***************************************************************************
	 * Méthode de la classe AsyncTask se chargeant de traiter une tâche de fond.
	 * Redéfinie ici pour récupérer un JSON depuis une URL.
	 * 
	 * @param urls
	 *            Les urls à traiter. Pour le prototype, seulement la première
	 *            est traitée.
	 * 
	 * @return La chaîne de caractères qui contient le JSON. Sera passée à la
	 *         méthode onPostExecute() pour traitement.
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 **************************************************************************/
	@Override
	protected String[] doInBackground(String... params) {
		String[] jsons = new String[params.length - 1];

		// Si le cookie n'existe pas dans les préférences, il sera remplacé par
		// la valeur par défaut "false", on gère donc ce cas.
		if (params[0].equals("false")) {
			jsons[0] = "[{\"name\":\"No Entry\"}]";
			Log.d("RETRIEVED JSON", jsons[0]);
			return jsons;
		}

		DefaultHttpClient client = new DefaultHttpClient();

		for (int i = 1; i < params.length; i++) {
			HttpGet httpget = new HttpGet(params[i]);
			BufferedReader reader = null;

			try {
				httpget.setHeader("Cookie", "remember_token=" + params[0]);
				Log.d("DEBUG - Cookie GET", "remember_token=" + params[0]);
				HttpResponse HTTPResponse = client.execute(httpget);
				HttpEntity entity = HTTPResponse.getEntity();
				InputStream is = entity.getContent();
				reader = new BufferedReader(new InputStreamReader(is, "utf-8"),
						8);
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					Log.d("RETRIEVED JSON", line);
					builder.append(line);
				}
				is.close();
				jsons[i - 1] = builder.toString();
			} catch (Exception e) {
				Log.e("GetJSON", "GetJSONTask: " + e.getMessage());
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						Log.e("GetJSON", "GetJSONTask: " + e.getMessage());
					}
				}
			}
		}

		for (int i = 0; i < jsons.length; i++) {
			Log.d("RETRIEVED JSON", jsons[i]);
		}
		return jsons;
	}

	/***************************************************************************
	 * Méthode de la classe AsyncTask appelée après que doInBackground ait fini
	 * son traitement. Passe le JSON récupérer à la méthode callback implémentée
	 * par la classe appelante.
	 * 
	 * @param result
	 *            Le JSON à traiter (retourné par la méthode doInBackground()).
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 **************************************************************************/
	@Override
	protected void onPostExecute(String[] result) {
		cbl.callback(result);
	}
}
