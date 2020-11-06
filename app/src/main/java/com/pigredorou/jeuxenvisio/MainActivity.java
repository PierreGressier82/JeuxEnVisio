package com.pigredorou.jeuxenvisio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String url = "http://julie.et.pierre.free.fr/Salon/";
    private static final String urlDistribue = url + "distribueCartes.php";
    private static final String urlRAZDistribution = url + "RAZDistribution.php";
    private static final String urlGetJoueurs = url + "getJoueurs.php?salon=";
    private static final String urlGetSalons = url + "getSalons.php";
    public static final int MAIN_JOUEUR_ACTIVITY_REQUEST_CODE=14;
    public static final String VALEUR_PSEUDO = "Pseudo";
    private Button mBoutonJ1;
    private Button mBoutonRAZ;
    private Button mBoutonDistribue;
    private SharedPreferences mPreferences;
    private Spinner mListeDeroulanteSalons;
    private Spinner mListeDeroulanteJoueurs;
    private ArrayList<Joueur> mListeJoueurs = new ArrayList<>();
    private ArrayList<Salon> mListeSalons = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapterSalons;
    private ArrayAdapter<String> mArrayAdapterJoueurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Masque le bar de titre de l'activité
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Charge le layout
        setContentView(R.layout.activity_main);
        LinearLayout layoutBoutonsJoueurs = findViewById(R.id.layout_boutons_joueurs);

        // Liste des salons de jeu
        mListeDeroulanteSalons = findViewById(R.id.liste_salons);
        mListeDeroulanteSalons.setOnItemSelectedListener(this);
        mListeDeroulanteSalons.setTag("salons");
        mArrayAdapterSalons = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        mArrayAdapterSalons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListeDeroulanteSalons.setAdapter(mArrayAdapterSalons);
        new TacheGetSalons().execute(urlGetSalons);

        // Liste des joueurs
        mListeDeroulanteJoueurs = findViewById(R.id.liste_joueurs);
        mListeDeroulanteJoueurs.setOnItemSelectedListener(this);
        mArrayAdapterJoueurs = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        mArrayAdapterJoueurs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListeDeroulanteJoueurs.setAdapter(mArrayAdapterJoueurs);

        mBoutonJ1 = findViewById(R.id.boutonJ1);
        mBoutonJ1.setOnClickListener(this);

        // Gestion de la distribution des cartes
        mBoutonRAZ = findViewById(R.id.boutonRAZ);
        mBoutonDistribue = findViewById(R.id.boutonDistribue);
        mBoutonRAZ.setOnClickListener(this);
        mBoutonDistribue.setOnClickListener(this);
        mBoutonRAZ.setVisibility(View.GONE);
        mBoutonDistribue.setVisibility(View.GONE);

        mPreferences = getPreferences(MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boutonJ1:
            case R.id.boutonJ2:
            case R.id.boutonJ3:
            case R.id.boutonJ4:
                Intent MainJoueurActivity = new Intent(MainActivity.this, MainJoueurActivity.class);
                // Sauvegarde le pseudo
                Button bouton = v.findViewById(v.getId());
                // Sauvegarde des préférences
                mPreferences.edit().putString(VALEUR_PSEUDO, bouton.getText().toString()).apply();
                // Lance l'activité "Main joueur" avec le pseudo en paramètre
                MainJoueurActivity.putExtra(VALEUR_PSEUDO, bouton.getText().toString());
                startActivityForResult(MainJoueurActivity, MAIN_JOUEUR_ACTIVITY_REQUEST_CODE);
                break;

            case R.id.boutonDistribue :
                new TacheURLSansRetour().execute(urlDistribue + "?salon="+ mListeSalons.get(mListeDeroulanteSalons.getSelectedItemPosition()).getId());
                Toast.makeText(this, "Distribution terminée", Toast.LENGTH_SHORT).show();
                break;

            case R.id.boutonRAZ :
                new TacheURLSansRetour().execute(urlRAZDistribution);
                Toast.makeText(this, "Distribution réinitialisée", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.liste_salons:
                new TacheGetJoueursSalon().execute(urlGetJoueurs + mListeSalons.get(position).getId());
                break;
            case R.id.liste_joueurs:
                mBoutonJ1.setText(mListeJoueurs.get(position).getNomJoueur());

                if (mListeJoueurs.get(position).getAdmin() == 1) {
                    mBoutonRAZ.setVisibility(View.VISIBLE);
                    mBoutonDistribue.setVisibility(View.VISIBLE);
                } else {
                    mBoutonRAZ.setVisibility(View.GONE);
                    mBoutonDistribue.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void afficheJoueurs(ArrayList<Joueur> listeJoueurs) {
        for(int i=0; i<mArrayAdapterJoueurs.getCount();)
            mArrayAdapterJoueurs.remove(mArrayAdapterJoueurs.getItem(0).toString());
        mArrayAdapterJoueurs.notifyDataSetChanged();
        // Affiche les joueurs dans la liste
        if (listeJoueurs != null) {
            String[] listePseudoJoueurs = new String[listeJoueurs.size()];
            for (int i = 0; i < listeJoueurs.size(); i++) {
                listePseudoJoueurs[i] = listeJoueurs.get(i).getNomJoueur();
                mArrayAdapterJoueurs.add(listePseudoJoueurs[i]);
                mArrayAdapterJoueurs.notifyDataSetChanged();
            }
        }
    }

    private void afficheSalons(ArrayList<Salon> listeSalons) {
        // Affiche les salons dans la liste
        if (listeSalons != null) {
            String[] listeNomSalons = new String[listeSalons.size()];
            for (int i = 0; i < listeSalons.size(); i++) {
                listeNomSalons[i] = listeSalons.get(i).getNom();
                mArrayAdapterSalons.add(listeNomSalons[i]);
                mArrayAdapterSalons.notifyDataSetChanged();
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
            super.onPostExecute(listeSalons);
        }
    }

    /**
     * Classe qui permet de récupérer la liste des joueurs du salon et de l'afficher dans une liste déroulante
     * -> Retourne la liste des joeurs du salon
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
            super.onPostExecute(listeJoueurs);
        }
    }

    /**
     * Classe qui permet d'appeler une URL sans traitement d'information en retour
     */
    class TacheURLSansRetour extends AsyncTask<String, Void, Void> {
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
