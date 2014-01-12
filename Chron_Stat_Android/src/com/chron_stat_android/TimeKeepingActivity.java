package com.chron_stat_android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chron_stat_android.model.*;

public class TimeKeepingActivity extends Activity  {

	final int DURATION_MATCH = 3600000;			// Duree total d'un match
	final int DURATION_2MIN = 120000;			// Duree d'un 2min
	final int INTERVAL_DURATION = 1000;			// Temps de rafraichissement des timer
	
	// Information sur le match
	private Match match;
	
	// Liste des faits de match
	private ArrayList<Fact> facts = new ArrayList<Fact>();
 		
	// Liste des joueurs des 2 equipes
	private ArrayList<Player> playersTeam1;
	private ArrayList<Player> playersTeam2;
	
	// Elements de l'interface graphique
	private ListView listResumeTeam1, listResumeTeam2, list2MinTeam1, list2MinTeam2;
	private Button btnGoalTeam1, btn2MinTeam1, btnGoalTeam2, btn2MinTeam2, btnPenaltyTeam1, btnPenaltyTeam2, btnPlayPause;
	private TextView lblScore, lblTps;
	
	// Liste des informations d�finit dans les listView
	ArrayList<HashMap<String, String>> listItemResumeTeam1 = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> listItemResumeTeam2 = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> listItem2MinTeam1 = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> listItem2MinTeam2 = new ArrayList<HashMap<String,String>>();
    HashMap<String, String> map;
    
    // Adapter permettant la liason entre le model et l'affichage graphique
    SimpleAdapter adapterResumeTeam1, adapterResumeTeam2, adapter2MinTeam1, adapter2MinTeam2;
    
    // Score des 2 equipes
    int scoreTeam1 = 0, scoreTeam2 = 0;
    
    // Timer principal du match
    CountDownTimerPausable mainTimer;
    
    // Liste de l'ensemble des timers de l'application
    ArrayList<CountDownTimerPausable> timers = new ArrayList<CountDownTimerPausable>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);
        
        // R�cup�ration du match
        match = (Match)getIntent().getExtras().get("match");
        
        // R�cup�ration des 2 �quipes
        playersTeam1 = new ArrayList<Player>(Arrays.asList(match.getTeam1().getPlayers()));
        playersTeam2 = new ArrayList<Player>(Arrays.asList(match.getTeam2().getPlayers()));
        
        // D�finition des listView
    	listResumeTeam1 = (ListView) findViewById(R.id.listResumeTeam1);
    	listResumeTeam2 = (ListView) findViewById(R.id.listResumeTeam2);
    	list2MinTeam1 = (ListView) findViewById(R.id.list2MinTeam1);
    	list2MinTeam2 = (ListView) findViewById(R.id.list2MinTeam2);
    	
    	// D�finition des boutons
    	btnGoalTeam1 = (Button) findViewById(R.id.btnGoalTeam1);
    	btn2MinTeam1 = (Button) findViewById(R.id.btn2MinTeam1);
    	btnPenaltyTeam1 = (Button) findViewById(R.id.btnPenaltyTeam1);
    	btnGoalTeam2 = (Button) findViewById(R.id.btnGoalTeam2);
    	btn2MinTeam2 = (Button) findViewById(R.id.btn2MinTeam2);
    	btnPlayPause = (Button) findViewById(R.id.btnPlayPause);
    	btnPenaltyTeam2 = (Button) findViewById(R.id.btnPenaltyTeam2);
    	
    	Button btnCorrection = (Button) findViewById(R.id.btnCorrection);
    	
    	// D�finition des affichage texte
    	lblScore = (TextView) findViewById(R.id.lblScore);
    	lblTps = (TextView) findViewById(R.id.lblTps);
                
    	// Initiatialisaiton des affichage des listes et du score
        initListView();
        lblScore.setText(scoreTeam1 + " - " + scoreTeam2);
        
        // D�fintion du timer principal du match
        mainTimer = new CountDownTimerPausable(DURATION_MATCH,INTERVAL_DURATION ) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				lblTps.setText(""+millisUntilFinished / INTERVAL_DURATION  / 60 +"."+millisUntilFinished / INTERVAL_DURATION   % 60);
			}
			
			@Override
			public void onFinish() {
				lblTps.setText("Match terminé");
			}
		};
		
		// Ajout du timer a la liste des timers
		timers.add(mainTimer);
		                
		
		btnCorrection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String txt = "";
				
        		AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("Feuille du match");
        		
        		for(Fact fact : facts)
        			txt = txt + " " + fact.getType() + " " + fact.getPlayer().getName() + " " + fact.getTime() + " \n";
        			
        		adb.setMessage(txt);
        		adb.setNeutralButton("annuler", null);
        		adb.show();
			}
		});
		
        // Ecouteur sur le bouton but team 1
        btnGoalTeam1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		        		
        		AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("But pour l'equipe 1");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam1.toArray(new String[playersTeam1.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout du but dans la liste r�sum�e de l'�quipe
                    	playersTeam1.get(arg).AddGoal();
                    	listItemResumeTeam1.get(arg).put("But", ""+(playersTeam1.get(arg).getNbrGoal()));
                        adapterResumeTeam1.notifyDataSetChanged();
                        
                        // Modification du score
                        lblScore.setText(++scoreTeam1 + " - " + scoreTeam2);
                        
                        // Ajout dans la liste ds faits match
                        facts.add(new Fact(TypeFact.GOAL, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam1.get(arg), playersTeam1.get(arg).getId()));
                    }
                });
        		adb.setNeutralButton("annuler", null);
        		adb.show();
        	}
        });
        
        // Ecouteur sur le bouton 2min team 1
        btn2MinTeam1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		        		
        		AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("2min pour l'equipe 1");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam1.toArray(new String[playersTeam1.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout des 2min dans la liste r�sum�e de l'�quipe
                    	playersTeam1.get(arg).Add2Min();
                    	listItemResumeTeam1.get(arg).put("DeuxMin", ""+playersTeam1.get(arg).getNbr2Min());
                        adapterResumeTeam1.notifyDataSetChanged();
                        
                        // Ajout d'un nouvelle entr�e dans la liste des 2min en cours
                        map = new HashMap<String, String>();
                        map.put("Numero", ""+playersTeam1.get(arg).getNoDossard());
                        map.put("Nom", ""+playersTeam1.get(arg).getName());
                        map.put("Tps", ""+DURATION_2MIN /INTERVAL_DURATION);
                        listItem2MinTeam1.add(map);
                        adapter2MinTeam1.notifyDataSetChanged();
                        
                        // Ajout dans la liste ds faits match
                        facts.add(new Fact(TypeFact.TWO_MIN, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam1.get(arg), playersTeam1.get(arg).getId()));

                        // Cr�ation du timer pour les 2min
                        CountDownTimerPausable timer = new CountDownTimerPausable(DURATION_2MIN ,INTERVAL_DURATION ) {
                        	
                        	HashMap<String, String> temp = map;
                			
                			@Override
                			public void onTick(long millisUntilFinished) {

                		        for(int i =0 ; i<listItem2MinTeam1.size() ; i++)  
                		            if(listItem2MinTeam1.get(i).equals(temp))  
                		            {  
                        				listItem2MinTeam1.get(i).put("Tps", ""+millisUntilFinished / INTERVAL_DURATION );
                        				adapter2MinTeam1.notifyDataSetChanged();
                		            }  
                			}
                			
                			@Override
                			public void onFinish() {
                				                				
                                listItem2MinTeam1.remove(temp);
                                adapter2MinTeam1.notifyDataSetChanged();
                			}
                		};
                		
	            		// Si le timer principal n'est pas en pause on start le timer
                		if(!mainTimer.isPaused)
                			timer.start();
                		
                		// Ajout du timer a la liste des timer
                		timers.add(timer);
                                             
                    }
                });
        		       		
        		adb.setNeutralButton("annuler", null);
        		adb.show();
        	}
        });
       
     // Ecouteur sur le bouton Penalty team 1
        btnPenaltyTeam1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("Penalty pour l'equipe 1");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam1.toArray(new String[playersTeam1.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout du but dans la liste r�sum�e de l'�quipe
                    	playersTeam1.get(arg).AddGoal();
                    	listItemResumeTeam1.get(arg).put("But", ""+playersTeam1.get(arg).getNbrGoal());
                        adapterResumeTeam1.notifyDataSetChanged();

                        // Modification du score
                        lblScore.setText(++scoreTeam1 + " - " + scoreTeam2);
                        
                        // Ajout dans la liste des faits match
                        facts.add(new Fact(TypeFact.PENALTY, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam1.get(arg), playersTeam1.get(arg).getId()));
                    }
                });
        		
        		adb.setNeutralButton("annuler", null);
        		adb.show();
			}
		});
        
        // Ecouteur sur le bouton but team 1
        btnGoalTeam2.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		        		
        		AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("But pour l'equipe 2");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam2.toArray(new String[playersTeam2.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout du but dans la liste r�sum�e de l'�quipe
                    	playersTeam2.get(arg).AddGoal();
                    	listItemResumeTeam2.get(arg).put("But", ""+playersTeam2.get(arg).getNbrGoal());
                        adapterResumeTeam2.notifyDataSetChanged();

                        // Modification du score
                        lblScore.setText(scoreTeam1 + " - " + ++scoreTeam2);
                        
                        // Ajout dans la liste des faits match
                        facts.add(new Fact(TypeFact.GOAL, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam2.get(arg), playersTeam2.get(arg).getId()));

                    }
                });
        		
        		adb.setNeutralButton("annuler", null);
        		adb.show();
        	}
        });
        
        // Ecouteur sur le bouton 2min team 2
        btn2MinTeam2.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		        		
        		AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("2min pour l'equipe 2");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam2.toArray(new String[playersTeam2.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout des 2min dans la liste r�sum�e de l'�quipe
                    	playersTeam2.get(arg).Add2Min();
                    	listItemResumeTeam2.get(arg).put("DeuxMin", ""+playersTeam2.get(arg).getNbr2Min());
                        adapterResumeTeam2.notifyDataSetChanged();
                        
                        // Ajout d'un nouvelle entr�e dans la liste des 2min en cours
                        map = new HashMap<String, String>();
                        map.put("Numero", ""+playersTeam2.get(arg).getNoDossard());
                        map.put("Nom", ""+playersTeam2.get(arg).getName());
                        map.put("Tps", ""+DURATION_2MIN / INTERVAL_DURATION);
                        listItem2MinTeam2.add(map);
                        adapter2MinTeam2.notifyDataSetChanged();
                   
                        // Ajout dans la liste ds faits match
                        facts.add(new Fact(TypeFact.TWO_MIN, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam1.get(arg), playersTeam2.get(arg).getId()));
                    
                        // Cr�ation du timer pour les 2min
	                    CountDownTimerPausable timer = new CountDownTimerPausable(DURATION_2MIN ,INTERVAL_DURATION ) {
	                    	
	                    	HashMap<String, String> temp = map;
	            			
	            			@Override
	            			public void onTick(long millisUntilFinished) {
	
	            		        for(int i =0 ; i<listItem2MinTeam2.size() ; i++)  
	            		            if(listItem2MinTeam2.get(i).equals(temp))  
	            		            {  
	                    				listItem2MinTeam2.get(i).put("Tps", ""+millisUntilFinished / INTERVAL_DURATION );
	                    				adapter2MinTeam2.notifyDataSetChanged();
	            		            }  
	            			}
	            			
	            			@Override
	            			public void onFinish() {
	            				                				
	                            listItem2MinTeam2.remove(temp);
	                            adapter2MinTeam2.notifyDataSetChanged();
	            			}
	            		};
	            		
	            		// Si le timer principal n'est pas en pause on start le timer
                		if(!mainTimer.isPaused)
                			timer.start();
	            		
	            		// Ajout du timer a la liste des timer
	            		timers.add(timer);
                    }
                });
        		
        		adb.setNeutralButton("annuler", null);
        		adb.show();
        	}
        });
                
        // Ecouteur sur le bouton Penalty team 2
        btnPenaltyTeam2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				AlertDialog.Builder adb = new AlertDialog.Builder(TimeKeepingActivity.this);
        		adb.setTitle("Penalty pour l'equipe 2");
        		        		
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setItems(playersTeam2.toArray(new String[playersTeam2.size()] ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int arg) {
                    	
                    	// Ajout du but dans la liste r�sum�e de l'�quipe
                    	playersTeam2.get(arg).AddGoal();
                    	listItemResumeTeam2.get(arg).put("But", ""+playersTeam2.get(arg).getNbrGoal());
                        adapterResumeTeam2.notifyDataSetChanged();

                        // Modification du score
                        lblScore.setText(scoreTeam1 + " - " + ++scoreTeam2);
                        
                        // Ajout dans la liste des faits match
                        facts.add(new Fact(TypeFact.PENALTY, (DURATION_MATCH - (int)mainTimer.millisRemaining) / INTERVAL_DURATION, playersTeam2.get(arg),playersTeam2.get(arg).getId()));
                    }
                });
        		
        		adb.setNeutralButton("annuler", null);
        		adb.show();
			}
		});
        
        // Ecouteur sur le bouton PlayPause
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		        		
        		if(timers.get(0).isPaused){
        			for(CountDownTimerPausable timer : timers)
        				timer.start();
        		}
        		else{
        			lblTps.setText("Pause\n"+timers.get(0).millisRemaining / INTERVAL_DURATION  / 60 +"."+timers.get(0).millisRemaining / INTERVAL_DURATION % 60);
        			for(CountDownTimerPausable timer : timers)
        				timer.pause();
        		}
        	}
        });
    }

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_terminate:
			terminate();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_keeping, menu);
        return true;
    }
    
	private void terminate(){
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
	}
	
    private void initListView(){

        // Remplissage liste resume team 2 --------------------------------------------------

		for(Player player : playersTeam1){
	        map = new HashMap<String, String>();
	        map.put("Numero", ""+player.getNoDossard());
	        map.put("Nom", player.getName());
	        map.put("But", ""+player.getNbrGoal());
	        map.put("DeuxMin", ""+player.getNbr2Min());
	        listItemResumeTeam1.add(map);
		}
    	
        adapterResumeTeam1 = new SimpleAdapter(this.getBaseContext(), listItemResumeTeam1, R.layout.item_resume, 
        		new String[] {"Numero", "Nom", "But","DeuxMin"}, 
        		new int[] {R.id.itemNumResume, R.id.itemNameResume, R.id.itemButResume,R.id.item2MinResume});
        
        listResumeTeam1.setAdapter(adapterResumeTeam1);

        
        // Remplissage liste resume team 2 --------------------------------------------------
        
		for(Player player : playersTeam2){
	        map = new HashMap<String, String>();
	        map.put("Numero", ""+player.getNoDossard());
	        map.put("Nom", player.getName());
	        map.put("But", ""+player.getNbrGoal());
	        map.put("DeuxMin", ""+player.getNbr2Min());
	        listItemResumeTeam2.add(map);
		}
                
        adapterResumeTeam2 = new SimpleAdapter(this.getBaseContext(), listItemResumeTeam2, R.layout.item_resume, 
        		new String[] {"Numero", "Nom", "But","DeuxMin"}, 
        		new int[] {R.id.itemNumResume, R.id.itemNameResume, R.id.itemButResume,R.id.item2MinResume});
        
        listResumeTeam2.setAdapter(adapterResumeTeam2);
        
        // Remplissage liste 2min team 1 --------------------------------------------------
                
        adapter2MinTeam1 = new SimpleAdapter(this.getBaseContext(), listItem2MinTeam1, R.layout.item_2min, 
        		new String[] {"Numero", "Nom", "Tps"}, 
        		new int[] {R.id.itemNum2Min, R.id.itemName2Min, R.id.itemTps2Min});
        
        list2MinTeam1.setAdapter(adapter2MinTeam1);
        
        // Remplissage 2min resume team 2 --------------------------------------------------
                
        adapter2MinTeam2 = new SimpleAdapter(this.getBaseContext(), listItem2MinTeam2, R.layout.item_2min, 
        		new String[] {"Numero", "Nom", "Tps"}, 
        		new int[] {R.id.itemNum2Min, R.id.itemName2Min, R.id.itemTps2Min});
        
        list2MinTeam2.setAdapter(adapter2MinTeam2);
    }
}
