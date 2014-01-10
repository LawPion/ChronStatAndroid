package com.chron_stat_android;

import java.util.ArrayList;

import com.chron_stat_android.model.Championship;
import com.chron_stat_android.model.Gym;
import com.chron_stat_android.model.Match;
import com.chron_stat_android.model.Player;
import com.chron_stat_android.model.Team;
import com.chron_stat_android.tasks.GetJSONTask;
import com.google.gson.Gson;

import android.os.Bundle;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddMatchFragment extends ListFragment implements
		GetJSONTask.CallBackListener {

	// L'adapteur de la liste
	private MatchAdapter adapter;

	// Listener pour le callback gérant le click sur un élément de la liste
	private OnItemClickListener interfaceItemClickListener;

	// Objet Gson simplifiant la manipulation de chaînes JSON
	private static Gson gson;

	private Team currentTeam;
	private Match[] matches;
	private int dependencyCounter = 0;
	private boolean hasDependencies = false;

	/***************************************************************************
	 * A la création du fragment on crée un objet Gson et le nouvel adapteur de
	 * liste.
	 * 
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 **************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gson = new Gson();

		currentTeam = ((Team) getActivity().getIntent().getExtras().getSerializable("team"));

		adapter = new MatchAdapter(getActivity());
	}

	/***************************************************************************
	 * Rafraichit la liste en téléchargeant un nouveau JSON depuis le serveur.
	 **************************************************************************/
	public void refreshList() {
		// Efface le contenu de la liste
		adapter.clear();

		try {
			// Préférences contenant le token utilisé pour l'authentification
			SharedPreferences preferences = getActivity().getSharedPreferences(
					"CurrentUser", MainActivity.MODE_PRIVATE);

			String matchesURL = getString(R.string.SERVER_URL) + "matches"
					+ getString(R.string.JSON_EXT);

			/*
			 * Nouvelle GetJSONTask gérant la récupération d'un JSON depuis une
			 * URL.
			 */
			GetJSONTask task = new GetJSONTask();
			task.setListener(this);
			task.execute(preferences.getString("AuthCookie", "false"),
					matchesURL);
			Log.d("DEBUG - add", "first launching getjsontask");
		} catch (Exception e) {
			Log.e("GetJSON", "GetJSONTask: " + e.getMessage());
		}
	}

	/***************************************************************************
	 * L'interface servant à définir le callback pour la sélection d'un élément
	 * de la liste.
	 **************************************************************************/
	public interface OnItemClickListener {
		/***********************************************************************
		 * Méthode callback à implémenter. Appelée quand un élément de la liste
		 * est sélectionné.
		 * 
		 * @param match
		 *            L'équipe sélectionnée dans la liste.
		 **********************************************************************/
		void onItemClick(Match match);
	}

	/***************************************************************************
	 * Enregistre le listener à l'interface OnItemClickListener.
	 * 
	 * @param listener
	 *            La classe implémentant OnItemClickListener
	 **************************************************************************/
	public void setListener(OnItemClickListener listener) {
		this.interfaceItemClickListener = listener;
	}

	/***************************************************************************
	 * Callback appelé quand la récupération du JSON est terminée. Rempli la
	 * liste du fragment avec celle retournée par le serveur.
	 * 
	 * @see com.chron_stats_android_prototype.tasks.GetJSONTask.CallBackListener#callback(java.lang.String)
	 **************************************************************************/
	@Override
	public synchronized void callback(String[] jsons) {
		if (!hasDependencies) {
			matches = gson.fromJson(jsons[0], Match[].class);
			Log.d("DEBUG - add", "downloading dependencies: "+matches.length);
			getDependencies(matches);
			hasDependencies = true;
		} else {
			Match match = gson.fromJson(jsons[0], Match.class);
			Log.d("DEBUG - add", "bla gotten: "+match.getChampionship_id());
			match.setTeam1(gson.fromJson(jsons[1], Team.class));
			match.setTeam2(gson.fromJson(jsons[2], Team.class));
			match.getTeam1()
					.setPlayers(gson.fromJson(jsons[3], Player[].class));
			match.getTeam2()
					.setPlayers(gson.fromJson(jsons[4], Player[].class));
			match.setChampionship(gson.fromJson(jsons[5], Championship.class));
			match.setGym(gson.fromJson(jsons[6], Gym.class));
			matches[dependencyCounter] = match;
			dependencyCounter++;
			Log.d("DEBUG - add", "number of dependencies gotten: "+dependencyCounter);
		}

		if (dependencyCounter == matches.length) {

			Log.d("DEBUG - add", "populating list");
			populateList(matches);
		}
	}

	private void getDependencies(Match[] matches) {
		// Préférences contenant le token utilisé pour l'authentification
		SharedPreferences preferences = getActivity().getSharedPreferences(
				"CurrentUser", MainActivity.MODE_PRIVATE);

		for (int i = 0; i < matches.length; i++) {
			try {
				String matchURL = getString(R.string.SERVER_URL) + "matches/"
						+ matches[i].getId() + getString(R.string.JSON_EXT);
				String team1URL = getString(R.string.SERVER_URL) + "teams/"
						+ matches[i].getTeam_id1_id()
						+ getString(R.string.JSON_EXT);
				String team2URL = getString(R.string.SERVER_URL) + "teams/"
						+ +matches[i].getTeam_id2_id()
						+ getString(R.string.JSON_EXT);
				String players1URL = getString(R.string.SERVER_URL) + "teams/"
						+ matches[i].getTeam_id1_id() + "/getPlayers"
						+ getString(R.string.JSON_EXT);
				String players2URL = getString(R.string.SERVER_URL) + "teams/"
						+ +matches[i].getTeam_id2_id() + "/getPlayers"
						+ getString(R.string.JSON_EXT);
				String championshipURL = getString(R.string.SERVER_URL)
						+ "championships/" + matches[i].getChampionship_id()
						+ getString(R.string.JSON_EXT);
				String gymURL = getString(R.string.SERVER_URL) + "gyms/"
						+ matches[i].getGym_id() + getString(R.string.JSON_EXT);

				/*
				 * Nouvelle GetJSONTask gérant la récupération d'un JSON depuis
				 * une URL.
				 */
				GetJSONTask task = new GetJSONTask();
				task.setListener(this);
				task.execute(preferences.getString("AuthCookie", "false"),
						matchURL, team1URL, team2URL, players1URL, players2URL,
						championshipURL, gymURL);
			} catch (Exception e) {
				Log.e("GetJSON", "GetJSONTask: " + e.getMessage());
			}
		}
	}

	/***************************************************************************
	 * Appelle l'implementation de la méthode onListFragmentItemClick dans
	 * l'activité contenant ce fragment.
	 * 
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView,
	 *      android.view.View, int, long)
	 **************************************************************************/
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		interfaceItemClickListener.onItemClick(adapter.getItem(position));
	}

	/***************************************************************************
	 * Peuple la liste du fragment avec les utilisateurs contenus dans le
	 * tableau passé en paramètre.
	 * 
	 * La méthode efface d'abord le contenu de l'adapteur, puis lui ajoute les
	 * utilisateurs une à une.
	 * 
	 * @param matches
	 *            Le tableau d'équipes avec lequel remplir la liste
	 **************************************************************************/
	public synchronized void populateList(Match[] matches) {
		// Efface le contenu de l'adapteur de liste
		adapter.clear();

		// Ajoute chaque utilisateur du tableau passé en paramètre
		for (int i = 0; i < matches.length; i++) {
			adapter.addItem(matches[i]);
		}

		// Lie au nouvel adapter
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/***************************************************************************
	 * Fonction statique pour convertir un JSON en équipe.
	 * 
	 * @param matchJSON
	 *            Le JSON à convertir en équipe.
	 * @return L'objet Match créé à partir du JSON.
	 **************************************************************************/
	public static Match JSONToMatch(String matchJSON) {
		return gson.fromJson(matchJSON, Match.class);
	}

	/***************************************************************************
	 * Fonction statique pour convertir un JSON en tableau d'équipes.
	 * 
	 * @param matchesJSON
	 *            Le JSON à convertir en tableau d'équipes.
	 * @return Le tableau de Match créé à partir du JSON.
	 **************************************************************************/
	public static Match[] JSONToMatchArray(String matchesJSON) {
		return gson.fromJson(matchesJSON, Match[].class);
	}

	/***************************************************************************
	 * @author Crescenzio Fabio
	 * @author Fresco Alain
	 * @author Therisod Romain
	 * @author Triki Mohamed
	 * @author Walpen Laurian
	 * 
	 * @goal Adapteur de liste pour le fragment MatchListFragment.
	 * 
	 *       Gère les interactions avec la liste d'équipes contenue dans ce
	 *       fragment.
	 * 
	 * @notes -
	 **************************************************************************/
	protected class MatchAdapter extends BaseAdapter {
		/*
		 * Numéro représentant le type de la classe Match. Le fragment ne
		 * prenant pas en charge le polymorphisme, seule la classe Match est
		 * représentée ici.
		 */
		private static final int TYPE_MATCH = 0;

		/*
		 * Nombre de types d'objets différents pouvant être contenu dans la
		 * liste. Utilisé pour représenter chaque type différemment.
		 */
		private static final int TYPE_COUNT = 1;

		// ArrayList contenant les utilisateurs à afficher dans la liste
		private ArrayList<Match> list = new ArrayList<Match>();

		// Inflater pour construire la vue de la liste
		private LayoutInflater inflater;

		/***********************************************************************
		 * Constructeur de l'adapteur de liste. Récupère le LayoutInflater mis à
		 * disposition par le contexte donné.
		 * 
		 * @param context
		 *            Contexte englobant la liste.
		 **********************************************************************/
		public MatchAdapter(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/***********************************************************************
		 * Ajoute un match à la liste.
		 * 
		 * @param match
		 *            Le match à ajouter à la liste.
		 **********************************************************************/
		public void addItem(Match match) {
			list.add(match);
			notifyDataSetChanged();
		}

		/***********************************************************************
		 * Efface le contenu de la liste.
		 **********************************************************************/
		public void clear() {
			list.clear();
			notifyDataSetChanged();
		}

		/***********************************************************************
		 * Retourne le nombre d'éléments dans la liste.
		 * 
		 * @return le nombre d'éléments dans la liste.
		 * 
		 * @see android.widget.Adapter#getCount()
		 **********************************************************************/
		@Override
		public int getCount() {
			return list.size();
		}

		/***********************************************************************
		 * Retourne l'utilisateur situé à la position donnée dans la liste.
		 * 
		 * @param position
		 *            La position de l'utilisateur voulu dans la liste.
		 * @return L'utilisateur situé à la position donnée dans la liste.
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 **********************************************************************/
		@Override
		public Match getItem(int position) {
			return list.get(position);
		}

		/***********************************************************************
		 * Retourne l'identificateur de l'élément à la position donnée dans la
		 * liste. Ici retourne simplement la position comme identificateur.
		 * 
		 * @param position
		 *            La position de l'élément.
		 * @return L'identificateur de l'élément.
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 **********************************************************************/
		@Override
		public long getItemId(int position) {
			return position;
		}

		/***********************************************************************
		 * Retourne le type de l'élément à la position donnée dans la liste.
		 * Utilisé pour savoir quelle vue utiliser pour chaque élément.
		 * 
		 * @param position
		 *            La position de l'élément.
		 * @return Le numéro du type de la vue de l'élément.
		 * 
		 * @see android.widget.BaseAdapter#getItemViewType(int)
		 **********************************************************************/
		@Override
		public int getItemViewType(int position) {
			int type = -1;
			Match match = list.get(position);

			if (match instanceof Match) {
				type = 0;
			}

			return type;
		}

		/***********************************************************************
		 * Retourne le nombre de types de vues différentes.
		 * 
		 * @return Le nombre de types de vues différentes.
		 * 
		 * @see android.widget.BaseAdapter#getViewTypeCount()
		 **********************************************************************/
		public int getViewTypeCount() {
			return TYPE_COUNT;
		}

		/***********************************************************************
		 * Construit puis retourne la vue de l'élément à la position donnée dans
		 * la liste. Utilise la méthode getItemViewType pour déterminer la vue à
		 * utiliser pour l'élément. Ici, seule la vue pour un élément de type
		 * User est implémentée.
		 * 
		 * @param position
		 *            La position de l'élément.
		 * @param convertView
		 *            L'ancienne vue à réutiliser, si possible.
		 * @param parent
		 *            Le parent auquel cette vue va être attaché.
		 * @return La vue
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 **********************************************************************/
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			switch (type) {
			case TYPE_MATCH:
				// Match edition logic
				convertView = inflater.inflate(R.layout.list_item_match,
						parent, false);
				Match match = (Match) list.get(position);
				String label;
				Log.d("DEBUG - add", "currentTeam: "+(currentTeam==null));
				if (currentTeam.getId() == match.getTeam1().getId()) {
					label = match.getDate().substring(0,
							match.getDate().indexOf('T'))
							+ " - " + match.getTeam1().getName();
				} else {
					label = match.getDate().substring(0,
							match.getDate().indexOf('T'))
							+ " - " + match.getTeam2().getName();
				}
				((TextView) convertView.findViewById(R.id.textView_matchItem))
						.setText(label);
			default:
				break;
			}

			return convertView;
		}
	}
}
