package com.pigredorou.jeuxenvisio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pigredorou.jeuxenvisio.objets.Jeu;
import com.pigredorou.jeuxenvisio.objets.Joueur;
import com.pigredorou.jeuxenvisio.objets.Salon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 1.02 : Version finale The Crew
     * 1.10 : Ajout du choix d'un jeu (seul jeu dispo : The Crew)
     * 1.11 : Gestion de plusieurs jeux en // avec la notion de partie
     * 1.12 : The Crew : amélioration ergonomie - Belote : début implémentation
     * 1.13 : The Crew : correction bug double clic + ajout double clic pour sélection tache
     * 1.14 : Implémentation échange jeu ou 1 carte + Belote : affichage carte atout à choisir
     * 1.15 : Correction double clic
     * 1.16 : Correction distribution si nombre de carte pas équitable + ajout de plus de salons + correction double clic + The Crew : implémentation Zone de silence
     * 2.00 : The Crew - Implementation XML + refresh complet toutes les secondes
     * 2.01 : The Crew - Correction affichage tâches réalisées + blocage communication avant le pli autorisé
     * 2.02 : Belote : Implémentation XML + Gestion du chois de l'atout (1er et 2ème tour)
     * 2.03 : Belote : Ajout de l'affichage de l'historique des plis + score + joueur qui remporte le pli
     * 2.04 : Belote : Ajout de l'affichage des scores
     * 2.05 : The Crew : Annulation dernière carte posée sur double clic (uniquement par le joueur qui l'a posé)
     * 2.06 : Belote : Gestion tour suivant (changement premier joueur + maj score)
     * 2.07 : Belote : Correction bug refresh histo + couleur choix 2ème tour + The Crew : début implémentation jeton détresse
     * 2.08 : Belote : Annulation dernière carte posée sur double clic (uniquement par le joueur qui l'a posé) + seul le joueur qui remporte le pli peut jouer le pli suivant
     * 2.09 : Belote : Gestion de la "coupe" pour les distributions et de la "goutière" pour la fin de distribution (on ne mélange pas les cartes entre les manches)
     * 2.10 : Fiesta de los muertos : début implémentation
     * 2.11 : Fiesta de los muertos : implémentation lecture XML + saisie mot (4x)
     * 2.12 : Fiesta de los muertos : implémentation v1 terminée
     * 2.13 : Fiesta de los muertos : ajout initialisation du jeu + Main : option admin affichées selon le jeu
     * 2.14 : Fiesta de los muertos : amélioration de l'affichage des résultats
     * 2.15 : Fiesta de los muertos : ajout du nom des joueurs précédent et suivants pour la première phase
     * 2.16 : Fiesta de los muertos : Correction bugs sur l'affichage des résultats
     */
    public static final String url = "http://julie.et.pierre.free.fr/Salon/";
    public static final String urlGetJoueurs = url + "getJoueurs.php?salon=";
    public static final String urlDistribueCartes = url + "distribueCartes.php?partie=";
    public static final String urlAnnulCarte = MainActivity.url + "annulCarte.php?partie=";
    // Variables statiques
    private static final String mNumVersion = "2.16";
    private static final String urlGetSalons = url + "getSalons.php";
    private static final String urlGetJeux = url + "getJeux.php?salon=";
    private static final String urlRAZDistribution = url + "RAZDistribution.php?partie=";
    private static final String urlDistribueTaches = url + "distribueTaches.php?partie=";
    private static final String urlMAJNumMission = url + "majNumeroMission.php?partie=";
    private static final String urlEchangeCarte = url + "echangeCarte.php?partie=";
    public static final String VALEUR_PSEUDO = "Pseudo";
    public static final String VALEUR_ID_SALON = "idSalon";
    public static final String VALEUR_ID_PARTIE = "idPartie";
    public static final String VALEUR_NOM_SALON = "NomSalon";
    public static final int THE_CREW_ACTIVITY_REQUEST_CODE=11;
    public static final int FIESTA_MUERTOS_ACTIVITY_REQUEST_CODE=12;
    public static final int ROI_NAINS_ACTIVITY_REQUEST_CODE=13;
    public static final int MANCHOTS_BARJOTS_ACTIVITY_REQUEST_CODE=14;
    public static final int BELOTE_ACTIVITY_REQUEST_CODE=15;
    private static final int[] tableIdLignePseudo = {R.id.ligne_pseudo_j1, R.id.ligne_pseudo_j2, R.id.ligne_pseudo_j3, R.id.ligne_pseudo_j4, R.id.ligne_pseudo_j5, R.id.ligne_pseudo_j6, R.id.ligne_pseudo_j7, R.id.ligne_pseudo_j8};
    private static final int[] tableIdImagePseudo = {R.id.image_pseudo_joueur1, R.id.image_pseudo_joueur2, R.id.image_pseudo_joueur3, R.id.image_pseudo_joueur4, R.id.image_pseudo_joueur5, R.id.image_pseudo_joueur6, R.id.image_pseudo_joueur7, R.id.image_pseudo_joueur8};
    private static final int[] tableIdPseudo = {R.id.pseudo_text_joueur1, R.id.pseudo_text_joueur2, R.id.pseudo_text_joueur3, R.id.pseudo_text_joueur4, R.id.pseudo_text_joueur5, R.id.pseudo_text_joueur6, R.id.pseudo_text_joueur7, R.id.pseudo_text_joueur8};
    private static final int[] tableIdLigneSalon = {R.id.ligne_salon1, R.id.ligne_salon2, R.id.ligne_salon3, R.id.ligne_salon4, R.id.ligne_salon5, R.id.ligne_salon6, R.id.ligne_salon7, R.id.ligne_salon8};
    private static final int[] tableIdImageSalon = {R.id.image_salon1, R.id.image_salon2, R.id.image_salon3, R.id.image_salon4, R.id.image_salon5, R.id.image_salon6, R.id.image_salon7, R.id.image_salon8};
    private static final int[] tableIdNomSalon = {R.id.salon_text_1, R.id.salon_text_2, R.id.salon_text_3, R.id.salon_text_4, R.id.salon_text_5, R.id.salon_text_6, R.id.salon_text_7, R.id.salon_text_8};
    private static final int[] tableIdImageJeux = {R.id.jeu_1, R.id.jeu_2, R.id.jeu_3, R.id.jeu_4, R.id.jeu_5, R.id.jeu_6};
    private static final int[] tableIdResourceImageJeux = {0, R.drawable.the_crew, R.drawable.fiesta_de_los_muertos, R.drawable.le_roi_des_nains, R.drawable.manchots_barjots, R.drawable.belote};
    private static final int mIdTheCrew = 1;
    private static final int mIdFiestaDeLosMuertos = 2;
    private static final int mIdLeRoiDesNains = 3;
    private static final int mIdManchotsBarjots = 4;
    private static final int mIdBelote = 5;

    // Variables globales - contexte
    private int mIdSalon; // Id du salon (en BDD)
    private int mIdPartie; // Id de la partie (en BDD)
    private int mIdJeu; // Id du jeu (en BDD)
    private int mIndexSalon; // Index du salon (numéro de la liste)
    private String mNomSalon;
    private String mPseudo;
    private boolean mJoueurChoisi = false;
    private boolean mJeuChoisi = false;
    // Administration
    private Button mBoutonRAZ;
    private Button mBoutonDistribueCartes;
    private Button mBoutonDistribueTache;
    private TextView mNbTaches;
    private TableLayout mOptionTaches;
    private LinearLayout mLigneNbTaches;
    private Button mBoutonOptionTache1;
    private Button mBoutonOptionTache2;
    private Button mBoutonOptionTache3;
    private Button mBoutonOptionTache4;
    private Button mBoutonOptionTache5;
    private Button mBoutonOptionTache6;
    private Button mBoutonOptionTache7;
    private Button mBoutonOptionTache8;
    private Button mBoutonOptionTache9;
    private Button mBoutonOptionTache10;
    // Mission
    private LinearLayout mLigneNumMission;
    private TextView mNumMission;
    private Button mBoutonMissionSuivante;
    // Autres options
    private Button mBoutonEchangeCarte;
    private Button mBoutonEchangeJeu;

    // Listes
    private SharedPreferences mPreferences;
    private ArrayList<Salon> mListeSalons = new ArrayList<>();
    private ArrayList<Joueur> mListeJoueurs = new ArrayList<>();
    private ArrayList<Jeu> mListeJeux = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Masque le bar de titre de l'activité
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Charge le layout
        setContentView(R.layout.activity_main);

        // On recupère les préférences du joueur
        mPreferences = getPreferences(MODE_PRIVATE);
        mIdSalon = mPreferences.getInt(VALEUR_ID_SALON, 1);
        mNomSalon = mPreferences.getString(VALEUR_NOM_SALON, "");
        mPseudo = mPreferences.getString(VALEUR_PSEUDO, "");

        // Entete
        TextView version1 = findViewById(R.id.version);
        String version = version1.getText()+" "+mNumVersion;
        version1.setText(version);
        // Entête
        ImageView boutonQuitter = findViewById(R.id.bouton_quitter);
        boutonQuitter.setOnClickListener(this);

        // Liste des salons de jeu
        new TacheGetSalons().execute(urlGetSalons);

        // Liste des joueurs du salon
        new TacheGetJoueursSalon().execute(urlGetJoueurs+mIdSalon);

        // Liste des jeux du salon
        new TacheGetJeuxSalon().execute(urlGetJeux+mIdSalon);
        // Jeux
        ImageView jeu1 = findViewById(R.id.jeu_1);
        ImageView jeu2 = findViewById(R.id.jeu_2);
        ImageView jeu3 = findViewById(R.id.jeu_3);
        ImageView jeu4 = findViewById(R.id.jeu_4);
        ImageView jeu5 = findViewById(R.id.jeu_5);
        jeu1.setOnClickListener(this);
        jeu2.setOnClickListener(this);
        jeu3.setOnClickListener(this);
        jeu4.setOnClickListener(this);
        jeu5.setOnClickListener(this);
        jeu1.setTag("NO");
        jeu2.setTag("NO");
        jeu3.setTag("NO");
        jeu4.setTag("NO");
        jeu5.setTag("NO");

        // Bouton valider
        Button boutonValider = findViewById(R.id.boutonValider);
        boutonValider.setOnClickListener(this);

        // Gestion de la distribution des cartes
        chargementBoutonsDistribution();

        // Distributions des tâches
        chargementDesTaches();

        // Mission
        chargementMissions();

        // Autre option
        mBoutonEchangeCarte = findViewById(R.id.boutonEchangeCarte);
        mBoutonEchangeJeu = findViewById(R.id.boutonEchangeJeu);
        mBoutonEchangeCarte.setOnClickListener(this);
        mBoutonEchangeJeu.setOnClickListener(this);
        mBoutonEchangeCarte.setVisibility(View.GONE);
        mBoutonEchangeJeu.setVisibility(View.GONE);

    }

    private void chargementBoutonsDistribution() {
        mBoutonRAZ = findViewById(R.id.boutonRAZ);
        mBoutonDistribueCartes = findViewById(R.id.boutonDistribue);
        mBoutonRAZ.setOnClickListener(this);
        mBoutonDistribueCartes.setOnClickListener(this);
        mBoutonRAZ.setVisibility(View.GONE);
        mBoutonDistribueCartes.setVisibility(View.GONE);
    }

    private void chargementDesTaches() {
        mBoutonDistribueTache = findViewById(R.id.boutonDistribueTache);
        // Taches
        Button boutonTachesMoins = findViewById(R.id.boutonNbTacheMoins);
        Button boutonTachesPlus = findViewById(R.id.boutonNbTachePlus);
        mOptionTaches = findViewById(R.id.option_taches);
        mNbTaches = findViewById(R.id.nbTache);
        mLigneNbTaches = findViewById(R.id.ligne_nb_taches);
        mBoutonOptionTache1 = findViewById(R.id.option_tache_1);
        mBoutonOptionTache2 = findViewById(R.id.option_tache_2);
        mBoutonOptionTache3 = findViewById(R.id.option_tache_3);
        mBoutonOptionTache4 = findViewById(R.id.option_tache_4);
        mBoutonOptionTache5 = findViewById(R.id.option_tache_5);
        mBoutonOptionTache6 = findViewById(R.id.option_tache_6);
        mBoutonOptionTache7 = findViewById(R.id.option_tache_7);
        mBoutonOptionTache8 = findViewById(R.id.option_tache_8);
        mBoutonOptionTache9 = findViewById(R.id.option_tache_9);
        mBoutonOptionTache10 = findViewById(R.id.option_tache_10);
        mBoutonDistribueTache.setOnClickListener(this);
        boutonTachesMoins.setOnClickListener(this);
        boutonTachesPlus.setOnClickListener(this);
        mBoutonOptionTache1.setOnClickListener(this);
        mBoutonOptionTache2.setOnClickListener(this);
        mBoutonOptionTache3.setOnClickListener(this);
        mBoutonOptionTache4.setOnClickListener(this);
        mBoutonOptionTache5.setOnClickListener(this);
        mBoutonOptionTache6.setOnClickListener(this);
        mBoutonOptionTache7.setOnClickListener(this);
        mBoutonOptionTache8.setOnClickListener(this);
        mBoutonOptionTache9.setOnClickListener(this);
        mBoutonOptionTache10.setOnClickListener(this);
        mBoutonDistribueTache.setVisibility(View.GONE);
        mLigneNbTaches.setVisibility(View.GONE);
        mOptionTaches.setVisibility(View.GONE);
    }

    private void chargementMissions() {
        mLigneNumMission = findViewById(R.id.ligne_num_mission);
        mBoutonMissionSuivante = findViewById(R.id.boutonValiderNumMission);
        mNumMission = findViewById(R.id.numMission);
        mBoutonMissionSuivante.setOnClickListener(this);
        mLigneNumMission.setVisibility(View.GONE);
        mBoutonMissionSuivante.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        String nbCarte="";
        switch (v.getId()) {
            // Quitte l'application
            case R.id.bouton_quitter:
                finish();
                break;

            // Sélection d'un salon
            case R.id.ligne_salon1 :
            case R.id.ligne_salon2 :
            case R.id.ligne_salon3 :
            case R.id.ligne_salon4 :
            case R.id.ligne_salon5 :
            case R.id.ligne_salon6 :
            case R.id.ligne_salon7 :
            case R.id.ligne_salon8 :
                afficheSalonEnBlanc(v.getId());
                // Chargement des joueurs de ce salon
                new TacheGetJoueursSalon().execute(urlGetJoueurs + mIdSalon);
                mJoueurChoisi=false;
                afficheOptionAdmin();
                // Chargement des jeux de ce salon
                new TacheGetJeuxSalon().execute(urlGetJeux + mIdSalon);
                mJeuChoisi=false;
                break;

            // Sélection d'un joueur
            case R.id.ligne_pseudo_j1 :
            case R.id.ligne_pseudo_j2 :
            case R.id.ligne_pseudo_j3 :
            case R.id.ligne_pseudo_j4 :
            case R.id.ligne_pseudo_j5 :
            case R.id.ligne_pseudo_j6 :
            case R.id.ligne_pseudo_j7 :
            case R.id.ligne_pseudo_j8 :
                affichePseudoEnBlanc(v.getId());
                afficheOptionAdmin();
                break;

            // Jeux
            case R.id.jeu_1 :
            case R.id.jeu_2 :
            case R.id.jeu_3 :
            case R.id.jeu_4 :
            case R.id.jeu_5 :
                afficheJeux(mListeJeux);
                ImageView iv = findViewById(v.getId());
                TextView titre = findViewById(R.id.titre_jeu);
                if (iv.getTag().toString().equals("NO")) {
                    iv.setBackgroundColor(getResources().getColor(R.color.blanc));
                    iv.setTag("YES");
                    int index=0;
                    for (int i=0;i<tableIdImageJeux.length;i++) {
                        if (tableIdImageJeux[i]==iv.getId()) {
                            index=i;
                            break;
                        }
                    }
                    // Mise à jour du numéro de mission
                    TextView tv = findViewById(R.id.numMission);
                    tv.setText(String.valueOf(mListeJeux.get(index).getNumMission()));
                    // Titre
                    titre.setText(mListeJeux.get(index).getNom());
                    // Id du jeu en BDD
                    mIdJeu = mListeJeux.get(index).getId();
                    mIdPartie = mListeJeux.get(index).getIdPartie();
                    mJeuChoisi=true;
                }
                else {
                    titre.setText("");
                    iv.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    iv.setTag("NO");
                    mJeuChoisi=false;
                }
                afficheOptionAdmin();
                break;

            // Lancer le jeu dans le salon pour le joueur demandé
            case R.id.boutonValider:
                if (!mJoueurChoisi) {
                    Toast.makeText(this, "Il faut choisir un joueur", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    // Si 1 seul jeu, on le sélectionne automatiquement
                    if (mListeJeux.size() == 1) {
                        mIdJeu=mListeJeux.get(0).getId();
                        mIdPartie=mListeJeux.get(0).getIdPartie();
                        mJeuChoisi=true;
                    }
                    if (!mJeuChoisi) {
                        Toast.makeText(this, "Il faut choisir un jeu", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                // Sauvegarde le pseudo
                String nom_salon = mListeSalons.get(mIndexSalon).getNom();
                // Sauvegarde des préférences
                mPreferences.edit().putString(VALEUR_PSEUDO, mPseudo).apply();
                mPreferences.edit().putInt(VALEUR_ID_SALON, mIdSalon).apply();
                mPreferences.edit().putString(VALEUR_NOM_SALON, nom_salon).apply();

                // Lance le jeu choisi
                Intent JeuActivity;
                int REQUEST_CODE;
                switch (mIdJeu) {
                    case 1 :
                    default:
                        JeuActivity = new Intent(MainActivity.this, TheCrewActivity.class);
                        REQUEST_CODE = THE_CREW_ACTIVITY_REQUEST_CODE;
                        break;
                    case 2 :
                        JeuActivity = new Intent(MainActivity.this, FiestaDeLosMuertosActivity.class);
                        REQUEST_CODE = FIESTA_MUERTOS_ACTIVITY_REQUEST_CODE;
                        break;
                    case 3 :
                        JeuActivity = new Intent(MainActivity.this, LeRoiDesNainsActivity.class);
                        REQUEST_CODE = ROI_NAINS_ACTIVITY_REQUEST_CODE;
                        break;
                    case 4 :
                        JeuActivity = new Intent(MainActivity.this, ManchotsBarjotsActivity.class);
                        REQUEST_CODE = MANCHOTS_BARJOTS_ACTIVITY_REQUEST_CODE;
                        break;
                    case 5 :
                        JeuActivity = new Intent(MainActivity.this, BeloteActivity.class);
                        REQUEST_CODE = BELOTE_ACTIVITY_REQUEST_CODE;
                        break;
                }
                // Lance l'activité du jeu demandé avec les paramètres
                JeuActivity.putExtra(VALEUR_PSEUDO, mPseudo);
                JeuActivity.putExtra(VALEUR_ID_SALON, mIdSalon);
                JeuActivity.putExtra(VALEUR_ID_PARTIE, mIdPartie);
                JeuActivity.putExtra(VALEUR_NOM_SALON, nom_salon);
                startActivityForResult(JeuActivity, REQUEST_CODE);
                break;

            // Distribue les cartes dans le salon sélectionné
            case R.id.boutonDistribue :
                int typeCarte;
                String nbCarteParJoueur;
                String belote;
                switch(mIdJeu) {
                    case 1 :
                    case 2 :
                    case 3 :
                    case 4 :
                    default:
                        typeCarte=1;
                        nbCarteParJoueur="";
                        belote="";
                        break;
                    case 5 :
                        typeCarte=4;
                        nbCarteParJoueur="5";
                        belote="Table";
                        break;
                }
                new TacheURLSansRetour().execute(urlDistribueCartes+mIdPartie+"&typeCarte="+typeCarte+"&nbCarteParJoueur="+nbCarteParJoueur+"&belote="+belote);
                Toast.makeText(this, "Distribution terminée", Toast.LENGTH_SHORT).show();
                break;

            // Remise à zéro de la dernière distribution
            case R.id.boutonRAZ :
                new TacheURLSansRetour().execute(urlRAZDistribution+mIdPartie);
                Toast.makeText(this, "Distribution réinitialisée", Toast.LENGTH_SHORT).show();
                break;

            // Distribue les n tâches dans la salon sélectionné
            case R.id.boutonDistribueTache :
                String[] optionDemandees = checkOptionsTaches();
                String url = urlDistribueTaches+mIdPartie+"&nbTache="+mNbTaches.getText();
                url+= "&option1="+optionDemandees[0]+"&option2="+optionDemandees[1]+"&option3="+optionDemandees[2]+"&option4="+optionDemandees[3]+"&option5="+optionDemandees[4];
                new TacheURLSansRetour().execute(url);
                Toast.makeText(this, "Distribution terminée", Toast.LENGTH_SHORT).show();
                break;

            case R.id.boutonNbTacheMoins :
                if (!mNbTaches.getText().toString().equals("0"))
                    mNbTaches.setText(String.valueOf(Integer.parseInt(mNbTaches.getText().toString())-1));
                break;

            case R.id.boutonNbTachePlus :
                if (Integer.parseInt(mNbTaches.getText().toString()) < 20)
                    mNbTaches.setText(String.valueOf(Integer.parseInt(mNbTaches.getText().toString()) + 1));
                break;

            // Incrémente le numéro de mission
            case R.id.boutonValiderNumMission :
                int numMission = Integer.parseInt(mNumMission.getText().toString())+1;
                mNumMission.setText(String.valueOf(numMission));
                new TacheURLSansRetour().execute(urlMAJNumMission+mIdPartie+"&numMission="+numMission);
                Toast.makeText(this, "Mission mise à jour", Toast.LENGTH_SHORT).show();
                break;

            // Sélection d'une option d'une tâche
            case R.id.option_tache_1 :
            case R.id.option_tache_2 :
            case R.id.option_tache_3 :
            case R.id.option_tache_4 :
            case R.id.option_tache_5 :
            case R.id.option_tache_6 :
            case R.id.option_tache_7 :
            case R.id.option_tache_8 :
            case R.id.option_tache_9 :
            case R.id.option_tache_10 :
                Button b = findViewById(v.getId());
                if(v.getTag().equals("NO")) {
                    b.setTextColor(getResources().getColor(R.color.blanc));
                    b.setTag("YES");
                }
                else {
                    b.setTextColor(getResources().getColor(R.color.noir));
                    b.setTag("NO");
                }
                break;

            case R.id.boutonEchangeCarte :
                nbCarte="1";
            case R.id.boutonEchangeJeu :
                new TacheURLSansRetour().execute(urlEchangeCarte+mIdPartie+"&nbCarte="+nbCarte);
                Toast.makeText(this, "Action terminée", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Verifie quels sont les tâches sélectionnées

    /**
     * Regarde parmis les 10 boutons options les 5 premiers sélectionnés
     * @return : un tableau de chaine de taille 5 avec les options
     */
    private String[] checkOptionsTaches() {
        int nbOption=0;
        String[] optionsDemandees = {"","","","",""};

        if (mBoutonOptionTache1.getTag().equals("YES"))
            optionsDemandees[nbOption++]=mBoutonOptionTache1.getText().toString();
        if (mBoutonOptionTache2.getTag().equals("YES"))
            optionsDemandees[nbOption++]=mBoutonOptionTache2.getText().toString();
        if (mBoutonOptionTache3.getTag().equals("YES"))
            optionsDemandees[nbOption++]=mBoutonOptionTache3.getText().toString();
        if (mBoutonOptionTache4.getTag().equals("YES"))
            optionsDemandees[nbOption++]=mBoutonOptionTache4.getText().toString();
        if (mBoutonOptionTache5.getTag().equals("YES"))
            optionsDemandees[nbOption++]=mBoutonOptionTache5.getText().toString();
        if (mBoutonOptionTache6.getTag().equals("YES") && nbOption<=5)
            optionsDemandees[nbOption++]=mBoutonOptionTache6.getText().toString();
        if (mBoutonOptionTache7.getTag().equals("YES") && nbOption<=5)
            optionsDemandees[nbOption++]=mBoutonOptionTache7.getText().toString();
        if (mBoutonOptionTache8.getTag().equals("YES") && nbOption<=5)
            optionsDemandees[nbOption++]=mBoutonOptionTache8.getText().toString();
        if (mBoutonOptionTache9.getTag().equals("YES") && nbOption<=5)
            optionsDemandees[nbOption++]=mBoutonOptionTache9.getText().toString();
        if (mBoutonOptionTache10.getTag().equals("YES") && nbOption<=5)
            optionsDemandees[nbOption]=mBoutonOptionTache10.getText().toString();

        return optionsDemandees;
    }

    /**
     * Affiche l'ID du salon en blanc (et les autres en noir)
     * @param ressourceIdSalon : ID ressource de la ligne voulue
     */
    private void afficheSalonEnBlanc(int ressourceIdSalon) {
        ImageView iv;
        TextView tv;

        for(int i=0;i<mListeSalons.size();i++) {
            iv = findViewById(tableIdImageSalon[i]);
            tv = findViewById(tableIdNomSalon[i]);
            if (ressourceIdSalon == tableIdLigneSalon[i]) {
                iv.setImageResource(R.drawable.icone_check_blanc);
                tv.setTextColor(getResources().getColor(R.color.blanc));
                mIdSalon = Integer.parseInt(iv.getTag().toString());
                mIndexSalon = Integer.parseInt(tv.getTag().toString());
            }
            else {
                iv.setImageResource(R.drawable.icone_check);
                tv.setTextColor(getResources().getColor(R.color.noir));
            }
        }
    }

    /**
     * Affiche l'ID du pseudo en blanc (et les autres en noir)
     * @param ressourceIdPseudo : ID ressource de la ligne voulue
     */
    private void affichePseudoEnBlanc(int ressourceIdPseudo) {
        ImageView iv;
        TextView tv;

        for(int i=0;i<mListeJoueurs.size();i++) {
            iv = findViewById(tableIdImagePseudo[i]);
            tv = findViewById(tableIdPseudo[i]);
            if (ressourceIdPseudo == tableIdLignePseudo[i]) {
                iv.setImageResource(R.drawable.icone_check_blanc);
                tv.setTextColor(getResources().getColor(R.color.blanc));
                mPseudo = iv.getTag().toString();
                mJoueurChoisi = true;
            }
            else {
                iv.setImageResource(R.drawable.icone_check);
                tv.setTextColor(getResources().getColor(R.color.noir));
            }
        }
    }

    private void afficheOptionAdmin () {
        int indexJoueur= -1;

        // Récupère l'index du joueur
        for(int i=0;i<mListeJoueurs.size();i++) {
            if(mListeJoueurs.get(i).getNomJoueur().equals(mPseudo)) {
                indexJoueur=i;
                break;
            }
        }

        if (indexJoueur != -1 && mListeJoueurs.get(indexJoueur).getAdmin() == 1) {
            switch (mIdJeu) {
                case mIdTheCrew:
                    mBoutonRAZ.setVisibility(View.VISIBLE);
                    mBoutonDistribueCartes.setVisibility(View.VISIBLE);
                    mBoutonDistribueTache.setVisibility(View.VISIBLE);
                    mLigneNbTaches.setVisibility(View.VISIBLE);
                    mOptionTaches.setVisibility(View.VISIBLE);
                    mLigneNumMission.setVisibility(View.VISIBLE);
                    mBoutonMissionSuivante.setVisibility(View.VISIBLE);
                    mBoutonEchangeCarte.setVisibility(View.VISIBLE);
                    mBoutonEchangeJeu.setVisibility(View.INVISIBLE);
                    break;
                case mIdBelote:
                    mBoutonRAZ.setVisibility(View.VISIBLE);
                    mBoutonDistribueCartes.setVisibility(View.VISIBLE);
                    mBoutonDistribueTache.setVisibility(View.GONE);
                    mLigneNbTaches.setVisibility(View.GONE);
                    mOptionTaches.setVisibility(View.GONE);
                    mLigneNumMission.setVisibility(View.GONE);
                    mBoutonMissionSuivante.setVisibility(View.GONE);
                    mBoutonEchangeCarte.setVisibility(View.GONE);
                    mBoutonEchangeJeu.setVisibility(View.GONE);
                    break;
                case mIdFiestaDeLosMuertos:
                case mIdLeRoiDesNains:
                case mIdManchotsBarjots:
                default:
                    mBoutonRAZ.setVisibility(View.GONE);
                    mBoutonDistribueCartes.setVisibility(View.GONE);
                    mBoutonDistribueTache.setVisibility(View.GONE);
                    mLigneNbTaches.setVisibility(View.GONE);
                    mOptionTaches.setVisibility(View.GONE);
                    mLigneNumMission.setVisibility(View.GONE);
                    mBoutonMissionSuivante.setVisibility(View.GONE);
                    mBoutonEchangeCarte.setVisibility(View.GONE);
                    mBoutonEchangeJeu.setVisibility(View.GONE);
                    break;
            }

        } else {
            mBoutonRAZ.setVisibility(View.GONE);
            mBoutonDistribueCartes.setVisibility(View.GONE);
            mBoutonDistribueTache.setVisibility(View.GONE);
            mLigneNbTaches.setVisibility(View.GONE);
            mOptionTaches.setVisibility(View.GONE);
            mLigneNumMission.setVisibility(View.GONE);
            mBoutonMissionSuivante.setVisibility(View.GONE);
            mBoutonEchangeCarte.setVisibility(View.GONE);
            mBoutonEchangeJeu.setVisibility(View.GONE);
        }

    }

    private void afficheJoueurs(ArrayList<Joueur> listeJoueurs) {
        TableLayout tl = findViewById(R.id.liste_joueurs_TL);
        TableRow.LayoutParams paramsRow;
        TableRow.LayoutParams paramsIV;
        TableRow.LayoutParams paramsTV;
        tl.removeAllViewsInLayout();
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 0);
        params.setMargins(0, 0, 0, 0);
        tl.setLayoutParams(params);
        tl.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tl.setVisibility(View.VISIBLE);

        // Affiche les joueurs dans la liste
        if (listeJoueurs != null) {
            String[] listePseudoJoueurs = new String[listeJoueurs.size()];
            for (int i=0; i<listeJoueurs.size(); i++) {
                listePseudoJoueurs[i] = listeJoueurs.get(i).getNomJoueur();

                // Création dynamique des joueurs dans des lignes du tableau
                TableRow tr = new TableRow(this);
                TextView tv = new CheckedTextView(this);
                ImageView iv = new ImageView(this);
                paramsRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                paramsIV = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                paramsTV = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                // Ligne
                paramsRow.setMargins(10, 10, 0, 0);
                tr.setLayoutParams(paramsRow);
                tr.setOnClickListener(this);
                tr.setId(tableIdLignePseudo[i]);
                // Image
                paramsIV.setMargins(10, 30, 30, 0);
                iv.setLayoutParams(paramsIV);
                iv.setImageResource(R.drawable.icone_check);
                iv.setTag(listePseudoJoueurs[i]);
                iv.setId(tableIdImagePseudo[i]);
                tr.addView(iv);
                // Texte
                paramsTV.setMargins(0, 30, 0, 0);
                tv.setLayoutParams(paramsTV);
                tv.setText(listePseudoJoueurs[i]);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tv.setTag(listePseudoJoueurs[i]);
                tv.setId(tableIdPseudo[i]);
                tr.addView(tv);
                // Ajout de la ligne dans la vue table
                tl.addView(tr);
            }
        }
    }

    private void afficheJeux(ArrayList<Jeu> listeJeux) {
        int index=0;
        // Affiche les jeux
        if (listeJeux != null) {
            for (int i = 0; i < listeJeux.size(); i++) {
                ImageView iv = findViewById(tableIdImageJeux[i]);
                // Image
                iv.setImageResource(tableIdResourceImageJeux[listeJeux.get(i).getId()]);
                iv.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                iv.setTag("NO");
                iv.setVisibility(View.VISIBLE);
                index++;
            }
        }

        // Masque les ImageView non utilisées
        for(int i=index;i<tableIdImageJeux.length;i++) {
            ImageView iv = findViewById(tableIdImageJeux[i]);
            // Image
            iv.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            iv.setTag("NO");
            iv.setVisibility(View.GONE);
            index=i;
        }
    }

    private void afficheSalons(ArrayList<Salon> listeSalons) {
        TableLayout tl = findViewById(R.id.liste_salons_TL);
        TableRow.LayoutParams paramsRow;
        TableRow.LayoutParams paramsIV;
        TableRow.LayoutParams paramsTV;
        tl.removeAllViewsInLayout();
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 0);
        params.setMargins(0, 0, 0, 0);
        tl.setLayoutParams(params);
        tl.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Affiche les salons dans la liste
        if (listeSalons != null) {
            String[] listeNomSalons = new String[listeSalons.size()];
            for (int i = 0; i < listeSalons.size(); i++) {
                listeNomSalons[i] = listeSalons.get(i).getNom();

                // Création dynamique des joueurs dans des lignes du tableau
                TableRow tr = new TableRow(this);
                TextView tv = new CheckedTextView(this);
                ImageView iv = new ImageView(this);
                paramsRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                paramsIV = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                paramsTV = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                // Ligne
                paramsRow.setMargins(30, 10, 0, 0);
                tr.setLayoutParams(paramsRow);
                tr.setOnClickListener(this);
                tr.setId(tableIdLigneSalon[i]);
                // Image
                paramsIV.setMargins(30, 30, 30, 0);
                iv.setLayoutParams(paramsIV);
                iv.setImageResource(R.drawable.icone_check);
                iv.setTag(listeSalons.get(i).getId()); // Id du salon en tag de l'image
                iv.setId(tableIdImageSalon[i]);
                tr.addView(iv);
                // Texte
                paramsTV.setMargins(0, 30, 0, 0);
                tv.setLayoutParams(paramsTV);
                tv.setText(listeNomSalons[i]);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tv.setTag(String.valueOf(i)); // Index salon en tag du texte
                tv.setId(tableIdNomSalon[i]);
                tr.addView(tv);
                // Ajout de la ligne dans la vue table
                tl.addView(tr);
            }
        }
    }

    /**
     * Classe qui permet de récupérer la liste des joueurs du salon et de l'afficher dans une liste déroulante
     * -> Retourne la liste des joeurs du salon
     */
    private class TacheGetSalons extends AsyncTask<String, Void, ArrayList<Salon>> {
        String result;
        @Override
        protected ArrayList<Salon> doInBackground(String... strings) {
            URL url;
            try {
                // l'URL est en paramètre donc toujours 1 seul paramètre
                url = new URL(strings[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    String[] chaine = stringBuffer.split("_");
                    // Salon : id, nom, numero de mission
                    //Salon salon = new Salon(Integer.parseInt(chaine[0]), chaine[1], Integer.parseInt(chaine[2]));
                    Salon salon = new Salon(Integer.parseInt(chaine[0]), chaine[1]);
                    mListeSalons.add(salon);
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }

            return mListeSalons;
        }

        @Override
        protected void onPostExecute(ArrayList<Salon> listeSalons) {
            afficheSalons(listeSalons);
            for(int i=0;i<mListeSalons.size();i++) {
                if (mListeSalons.get(i).getNom().equals(mNomSalon)) {
                    mIndexSalon=i;
                    break;
                }
            }
            afficheSalonEnBlanc(tableIdLigneSalon[mIndexSalon]);
            super.onPostExecute(listeSalons);
        }
    }

    /**
     * Classe qui permet de récupérer la liste des joueurs du salon et de l'afficher
     * -> Retourne la liste des joueurs du salon
     */
    private class TacheGetJoueursSalon extends AsyncTask<String, Void, ArrayList<Joueur>> {
        String result;
        @Override
        protected ArrayList<Joueur> doInBackground(String... strings) {
            URL url;
            mListeJoueurs.clear();
            try {
                // l'URL est en paramètre donc toujours 1 seul paramètre
                url = new URL(strings[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    String[] chaine = stringBuffer.split("_");
                    Joueur joueur = new Joueur(chaine[1], chaine[0], Integer.parseInt(chaine[2]));
                    mListeJoueurs.add(joueur);
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;
            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }

            return mListeJoueurs;
        }

        @Override
        protected void onPostExecute(ArrayList<Joueur> listeJoueurs) {
            afficheJoueurs(listeJoueurs);
            for(int i=0;i<mListeJoueurs.size();i++) {
                if (mListeJoueurs.get(i).getNomJoueur().equals(mPseudo)) {
                    affichePseudoEnBlanc(tableIdLignePseudo[i]);
                    break;
                }
            }
            super.onPostExecute(listeJoueurs);
        }
    }

    /**
     * Classe qui permet de récupérer la liste des jeux du salon et de l'afficher
     * -> Retourne la liste des jeux du salon (ayant une partie)
     */
    private class TacheGetJeuxSalon extends AsyncTask<String, Void, ArrayList<Jeu>> {
        @Override
        protected ArrayList<Jeu> doInBackground(String... strings) {
            URL url;
            mListeJeux.clear();
            try {
                // l'URL est en paramètre donc toujours 1 seul paramètre
                url = new URL(strings[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    String[] chaine = stringBuffer.split("_");
                    // Id BDD, id partie, nom du jeu, numéro de mission
                    Jeu jeu = new Jeu(Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]), chaine[2], Integer.parseInt(chaine[3]));
                    mListeJeux.add(jeu);
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            return mListeJeux;
        }

        @Override
        protected void onPostExecute(ArrayList<Jeu> listeJeux) {
            afficheJeux(listeJeux);
            super.onPostExecute(listeJeux);
        }
    }

    /**
     * Classe qui permet d'appeler une URL sans traitement d'information en retour
     */
    public static class TacheURLSansRetour extends AsyncTask<String, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            try {
                // l'URL est en paramètre donc toujours 1 seul paramètre
                url = new URL(strings[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null) {
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;
            } catch (IOException e) {
                e.printStackTrace();
                result = e.toString();
            }

            return null;
        }
    }
}
