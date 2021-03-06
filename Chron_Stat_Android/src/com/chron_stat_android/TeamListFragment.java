package com.chron_stat_android;

import java.util.ArrayList;

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

public class TeamListFragment extends ListFragment implements
		GetJSONTask.CallBackListener {

	// L'adapteur de la liste
	private TeamAdapter adapter;

	// Listener pour le callback gérant le click sur un élément de la liste
	private OnItemClickListener interfaceItemClickListener;

	// Objet Gson simplifiant la manipulation de chaînes JSON
	private static Gson gson;

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

		adapter = new TeamAdapter(getActivity());
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

			String teamsURL = getString(R.string.SERVER_URL) + "teams"
					+ getString(R.string.JSON_EXT);
			
			/*
			 * Nouvelle GetJSONTask gérant la récupération d'un JSON depuis une
			 * URL.
			 */
			GetJSONTask task = new GetJSONTask();
			task.setListener(this);
			task.execute(preferences.getString("AuthCookie", "false"), teamsURL);
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
		 * @param team
		 *            L'équipe sélectionnée dans la liste.
		 **********************************************************************/
		void onItemClick(Team team);
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
	public void callback(String[] json) {
		populateList(json[0]);
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
	 * @param teams
	 *            Le tableau d'équipes avec lequel remplir la liste
	 **************************************************************************/
	public synchronized void populateList(Team[] teams) {
		// Efface le contenu de l'adapteur de liste
		adapter.clear();

		// Ajoute chaque utilisateur du tableau passé en paramètre
		for (int i = 0; i < teams.length; i++) {
			Log.d("DEBUG - adding teams", ((Team) teams[i]).toString());
			adapter.addItem(teams[i]);
		}

		// Lie au nouvel adapter
		setListAdapter(adapter);
	}

	/***************************************************************************
	 * Surcharge de la méthode populateList prenant une chaîne de caractère
	 * représentant un JSON.
	 * 
	 * La méthode transforme d'abord le JSON en tableau d'utilisateurs par un
	 * appel à la fonction statique JSONToTeamArray, puis passe ce dernier à la
	 * méthode populateList.
	 * 
	 * @param teamsJSON
	 **************************************************************************/
	public void populateList(String teamsJSON) {
		Team[] teams = JSONToTeamArray(teamsJSON);
		populateList(teams);
	}

	/***************************************************************************
	 * Fonction statique pour convertir un JSON en équipe.
	 * 
	 * @param teamJSON
	 *            Le JSON à convertir en équipe.
	 * @return L'objet Team créé à partir du JSON.
	 **************************************************************************/
	public static Team JSONToTeam(String teamJSON) {
		return gson.fromJson(teamJSON, Team.class);
	}

	/***************************************************************************
	 * Fonction statique pour convertir un JSON en tableau d'équipes.
	 * 
	 * @param teamsJSON
	 *            Le JSON à convertir en tableau d'équipes.
	 * @return Le talbeau de Team créé à partir du JSON.
	 **************************************************************************/
	public static Team[] JSONToTeamArray(String teamsJSON) {
		return gson.fromJson(teamsJSON, Team[].class);
	}

	/***************************************************************************
	 * @author Crescenzio Fabio
	 * @author Fresco Alain
	 * @author Therisod Romain
	 * @author Triki Mohamed
	 * @author Walpen Laurian
	 * 
	 * @goal Adapteur de liste pour le fragment TeamListFragment.
	 * 
	 *       Gère les interactions avec la liste d'équipes contenue dans ce
	 *       fragment.
	 * 
	 * @notes -
	 **************************************************************************/
	protected class TeamAdapter extends BaseAdapter {
		/*
		 * Numéro représentant le type de la classe Team. Le fragment ne prenant
		 * pas en charge le polymorphisme, seule la classe Team est représentée
		 * ici.
		 */
		private static final int TYPE_TEAM = 0;

		/*
		 * Nombre de types d'objets différents pouvant être contenu dans la
		 * liste. Utilisé pour représenter chaque type différemment.
		 */
		private static final int TYPE_COUNT = 1;

		// ArrayList contenant les utilisateurs à afficher dans la liste
		private ArrayList<Team> list = new ArrayList<Team>();

		// Inflater pour construire la vue de la liste
		private LayoutInflater inflater;

		/***********************************************************************
		 * Constructeur de l'adapteur de liste. Récupère le LayoutInflater mis à
		 * disposition par le contexte donné.
		 * 
		 * @param context
		 *            Contexte englobant la liste.
		 **********************************************************************/
		public TeamAdapter(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/***********************************************************************
		 * Ajoute un utilisateur à la liste.
		 * 
		 * @param user
		 *            L'utilisateur à ajouter à la liste.
		 **********************************************************************/
		public void addItem(Team team) {
			list.add(team);
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
		public Team getItem(int position) {
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
			Team team = list.get(position);

			if (team instanceof Team) {
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
			case TYPE_TEAM:
				// Team edition logic
				convertView = inflater.inflate(R.layout.list_item_team, parent, false);
				String name = ((Team) list.get(position)).getName();
				((TextView) convertView.findViewById(R.id.textView_teamItem))
						.setText(name);
			default:
				break;
			}

			return convertView;
		}
	}
}
