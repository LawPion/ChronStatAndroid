package com.chron_stat_android.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

/*******************************************************************************
 * SendJSONTask.java
 * 
 * @author Crescenzio Fabio
 * @author Fresco Alain
 * @author Therisod Romain
 * @author Triki Mohamed
 * @author Walpen Laurian
 * 
 * @goal Tache d'envoi de JSON à une URL.
 * 
 *       Traite des objets Request. Envoie pour chaque requête passée le JSON de
 *       la requête à l'URL donnée selon la méthode voulue.
 * 
 *       Utilisé afin d'éditer, d'ajouter ou de supprimer un élément dans le
 *       fragment d'édition d'élément et dans l'action d'ajout de MainActivity.
 * 
 * @notes Le choix des méthodes HTTP est basé sur l'implémentation CRUD (Create,
 *        Read, Update et Delete, les quatres opérations de base) pour Ruby on
 *        Rails. Ainsi Create se traduit par la méthode POST, Update par la
 *        méthode PUT et Delete par la méthode DELETE.
 ******************************************************************************/
public class SendJSONTask extends AsyncTask<Request, Void, Void> {
	private CallBackListener cbl = null;
	private int editNumber = 0;
	private int createNumber = 0;
	private int deleteNumber = 0;

	/***************************************************************************
	 * L'interface servant à définir le callback appelé après envoi de la
	 * requête.
	 **************************************************************************/
	public interface CallBackListener {
		/***********************************************************************
		 * Méthode callback à implémenter. Appelé une fois la requête envoyée.
		 * 
		 * @param message
		 *            Le message contenant l'état des requêtes traitées par la
		 *            tâche.
		 **********************************************************************/
		public void callback(String message);
	}

	/***************************************************************************
	 * Enregistre le listener à l'interface CallBackListener.
	 * 
	 * @param cbl
	 *            La classe implémentant CallBackListener à enregistrer.
	 **************************************************************************/
	public void setListener(CallBackListener cbl) {
		this.cbl = cbl;
	}

	/***************************************************************************
	 * Méthode de la classe AsyncTask se chargeant de traiter une tâche de fond.
	 * Redéfinie ici pour traiter différentes requêtes (ajout, édition et
	 * suppression d'éléments).
	 * 
	 * @param requests
	 *            Les requêtes à traiter.
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 **************************************************************************/
	@Override
	protected Void doInBackground(Request... requests) {
		for (int i = 0; i < requests.length; i++) {
			BufferedReader reader = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpRequestBase request = null;
				try {
					if (requests[i].getMethod().equals("PUT")) {
						// Edition d'élément.
						request = new HttpPut(requests[i].getURI());
						((HttpPut) request).setEntity(new ByteArrayEntity(
								requests[i].getTargetJSON().getBytes("UTF-8")));
						Log.d("DEBUG - PUT",
								"PUTing json: " + requests[i].getTargetJSON());
						editNumber++;
					} else if (requests[i].getMethod().equals("POST")) {
						// Ajout d'élément.
						request = new HttpPost(requests[i].getURI());
						((HttpPost) request).setEntity(new ByteArrayEntity(
								requests[i].getTargetJSON().getBytes("UTF-8")));
						Log.d("DEBUG - POST",
								"POSTing json: " + requests[i].getTargetJSON());
						createNumber++;
					} else if (requests[i].getMethod().equals("DELETE")) {
						// Suppression d'élément.
						request = new HttpDelete(requests[i].getURI());
						Log.d("DEBUG - DELETE", "DELETEing json: "
								+ requests[i].getURI());
						deleteNumber++;
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				request.addHeader("Accept", "application/json");
				request.addHeader("Content-Type", "application/json");
				request.addHeader("Cookie",
						"remember_token=" + requests[i].getCookie());
				Log.d("DEBUG - Cookie POST", "remember_token=" + requests[i].getCookie());
				HttpResponse HTTPResponse = client.execute(request);
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
			} catch (Exception e) {
				Log.e("SendJSON", "SendJSONTask: " + e.getMessage());
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
		return null;
	}

	/***************************************************************************
	 * Méthode de la classe AsyncTask appelée après que doInBackground ait fini
	 * son traitement. Construit le message contenant le nombre d'éléments
	 * modifiés, ajoutés ou supprimés avant de le passer à la méthode callback
	 * redéfinie dans la classe appelante.
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 **************************************************************************/
	@Override
	protected void onPostExecute(Void result) {
		String message = "";
		if (createNumber == 1)
			message += "Elément créé\n";
		else if (createNumber != 0)
			message += createNumber + " éléments créés\n";
		if (editNumber == 1)
			message += "Elément modifié\n";
		else if (editNumber != 0)
			message += editNumber + " éléments modifiés\n";
		if (deleteNumber == 1)
			message += "Elément supprimé\n";
		else if (deleteNumber != 0)
			message += deleteNumber + " éléments supprimés\n";
		cbl.callback(message.trim());
	}
}
