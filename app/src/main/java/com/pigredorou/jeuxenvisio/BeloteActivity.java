package com.pigredorou.jeuxenvisio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pigredorou.jeuxenvisio.objets.Carte;
import com.pigredorou.jeuxenvisio.objets.HistoriquePlis;
import com.pigredorou.jeuxenvisio.objets.Joueur;
import com.pigredorou.jeuxenvisio.objets.Pli;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BeloteActivity extends JeuEnVisioActivity implements View.OnTouchListener {

    // Constantes
    // Tableaux des resssources
    private static final int[] imagesPique = {0, 0, 0, 0, 0, 0, 0, R.drawable.pique_7, R.drawable.pique_8, R.drawable.pique_9, R.drawable.pique_10, R.drawable.pique_valet, R.drawable.pique_dame, R.drawable.pique_roi, R.drawable.pique_as};
    private static final int[] imagesCoeur = {0, 0, 0, 0, 0, 0, 0, R.drawable.coeur_7, R.drawable.coeur_8, R.drawable.coeur_9, R.drawable.coeur_10, R.drawable.coeur_valet, R.drawable.coeur_dame, R.drawable.coeur_roi, R.drawable.coeur_as};
    private static final int[] imagesTrefle = {0, 0, 0, 0, 0, 0, 0, R.drawable.trefle_7, R.drawable.trefle_8, R.drawable.trefle_9, R.drawable.trefle_10, R.drawable.trefle_valet, R.drawable.trefle_dame, R.drawable.trefle_roi, R.drawable.trefle_as};
    private static final int[] imagesCarreau = {0, 0, 0, 0, 0, 0, 0, R.drawable.carreau_7, R.drawable.carreau_8, R.drawable.carreau_9, R.drawable.carreau_10, R.drawable.carreau_valet, R.drawable.carreau_dame, R.drawable.carreau_roi, R.drawable.carreau_as};
    private static final int[] imagesCouleur = {R.id.image_2eme_tour_coeur, R.id.image_2eme_tour_pique, R.id.image_2eme_tour_carreau, R.id.image_2eme_tour_trefle};
    private static final int[] tableIdImageCarteMain = {R.id.carte_1, R.id.carte_2, R.id.carte_3, R.id.carte_4, R.id.carte_5, R.id.carte_6, R.id.carte_7, R.id.carte_8};
    private static final int[] tableIdPseudoChoixAtout = {R.id.pseudo_joueur1_choix_atout, R.id.pseudo_joueur2_choix_atout, R.id.pseudo_joueur3_choix_atout, R.id.pseudo_joueur4_choix_atout};
    private static final int[] tableIdPseudoPli = {R.id.table_pseudo_joueur1, R.id.table_pseudo_joueur2, R.id.table_pseudo_joueur3, R.id.table_pseudo_joueur4};
    private static final int[] tableIdImageCartePli = {R.id.table_carte_image_joueur1, R.id.table_carte_image_joueur2, R.id.table_carte_image_joueur3, R.id.table_carte_image_joueur4};
    private static final int[] tableIdHisto1 = {R.id.carte1_histo_pli1, R.id.carte2_histo_pli1, R.id.carte3_histo_pli1, R.id.carte4_histo_pli1};
    private static final int[] tableIdHisto2 = {R.id.carte1_histo_pli2, R.id.carte2_histo_pli2, R.id.carte3_histo_pli2, R.id.carte4_histo_pli2};
    private static final int[] tableIdHisto3 = {R.id.carte1_histo_pli3, R.id.carte2_histo_pli3, R.id.carte3_histo_pli3, R.id.carte4_histo_pli3};
    private static final int[] tableIdHisto4 = {R.id.carte1_histo_pli4, R.id.carte2_histo_pli4, R.id.carte3_histo_pli4, R.id.carte4_histo_pli4};
    private static final int[] tableIdHisto5 = {R.id.carte1_histo_pli5, R.id.carte2_histo_pli5, R.id.carte3_histo_pli5, R.id.carte4_histo_pli5};
    private static final int[] tableIdHisto6 = {R.id.carte1_histo_pli6, R.id.carte2_histo_pli6, R.id.carte3_histo_pli6, R.id.carte4_histo_pli6};
    private static final int[] tableIdHisto7 = {R.id.carte1_histo_pli7, R.id.carte2_histo_pli7, R.id.carte3_histo_pli7, R.id.carte4_histo_pli7};
    private static final int[] tableIdHisto8 = {R.id.carte1_histo_pli8, R.id.carte2_histo_pli8, R.id.carte3_histo_pli8, R.id.carte4_histo_pli8};
    private static final int[] tableIdHistoLayout = {R.id.layout_histo_pli1, R.id.layout_histo_pli2, R.id.layout_histo_pli3, R.id.layout_histo_pli4, R.id.layout_histo_pli5, R.id.layout_histo_pli6, R.id.layout_histo_pli7, R.id.layout_histo_pli8};
    private static final int[] tableIdHistoScore = {R.id.score_pli1, R.id.score_pli2, R.id.score_pli3, R.id.score_pli4, R.id.score_pli5, R.id.score_pli6, R.id.score_pli7, R.id.score_pli8};
    private static final int[] tableIdHistoJoueur = {R.id.joueur_pli1, R.id.joueur_pli2, R.id.joueur_pli3, R.id.joueur_pli4, R.id.joueur_pli5, R.id.joueur_pli6, R.id.joueur_pli7, R.id.joueur_pli8};
    // URLs des actions en base
    private static final String urlBelote = MainActivity.url + "belote.php?partie=";
    private static final String urlJoueCarte = MainActivity.url + "majTable.php?partie=";
    private static final String urlDistribueBelote = MainActivity.url + "distribueBelote.php?partie=";
    private static final String urlMAJContrat = MainActivity.url + "majContrat.php?partie=";
    private static final String urlTourSuivant = MainActivity.url + "tourSuivant.php?partie=";
    // Variables globales
    private String[] mListePseudo; // Liste des pseudos des joueurs
    private int mAdmin; // Pseudo du joueur
    private String mJoueurQuiAPris; // Pseudo du joueur qui a décidé de l'atout
    private String mJoueurQuiRemporteLePli; // Pseudo du joueur qui a remporté le pli
    private String mAtoutChoisi; // Atout de la partie
    private int mEstCeMonTourDeChoisir = 0; // 0:non, 1:premier tour, 2:2ème tour
    private int mNumeroPli=0;
    private int[] scorePartie = {0,0};
    private ArrayList<Pli> mListeCartesPliEnCours;
    private ArrayList<Joueur> mListeJoueurs;
    // Elements de la vue
    private LinearLayout mTable;
    private ImageView mCarteActive;
    private TextView mTitrePli;
    // Gestion du double clic
    int mClickCount = 0;
    int mLastViewID = 0;
    long mStartTimeClick;
    long mDurationClick;
    static final int MAX_DURATION_CLICK = 500;
    static final int MAX_DURATION_DOUBLE_CLICK = 2000;
    // Tour suivant
    Button mBoutonTourSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Affiche la vue
        setContentView(R.layout.activity_belote);

        super.onCreate(savedInstanceState);

        // ChoixAtout
        Button boutonPasser = findViewById(R.id.bouton_passer);
        boutonPasser.setOnClickListener(this);
        ImageView iv = findViewById(R.id.image_2eme_tour_coeur);
        iv.setOnClickListener(this);
        iv = findViewById(R.id.image_2eme_tour_coeur);
        iv.setOnClickListener(this);
        iv = findViewById(R.id.image_2eme_tour_trefle);
        iv.setOnClickListener(this);
        iv = findViewById(R.id.image_2eme_tour_carreau);
        iv.setOnClickListener(this);

        // Table
        mTitrePli = findViewById(R.id.titre_pli);
        mTitrePli.setOnClickListener(this);
        mTable = findViewById(R.id.table);

        // Tour suivant
        mBoutonTourSuivant = findViewById(R.id.bouton_tour_suivant);
        mBoutonTourSuivant.setOnClickListener(this);
        mBoutonTourSuivant.setVisibility(View.GONE);
    }

    private String getPseudoQuiDoitJouer() {
        String pseudoQuiDoitJouer = "";
        ImageView iv;
        TextView tv;
        // Parcours les images pour savoir si c'est mon tour
        for (int i = 0; i < tableIdImageCartePli.length; i++) {
            iv = findViewById(tableIdImageCartePli[i]);
            tv = findViewById(tableIdPseudoPli[i]);
            // On prends le pseudo de la première image invisible
            if (iv.getVisibility() == View.INVISIBLE) {
                pseudoQuiDoitJouer = tv.getText().toString();
                debug("pseudoQuiDoitJouer :" + pseudoQuiDoitJouer);
                break;
            }
        }
        return pseudoQuiDoitJouer;
    }

    private String getEquipeJoueur(String pseudo) {
        String equipe="";

        for (int i=0;i<mListeJoueurs.size();i++) {
            if (pseudo.equals(mListeJoueurs.get(i).getNomJoueur())) {
                equipe = mListeJoueurs.get(i).getNomEquipe();
                break;
            }
        }

        return equipe;
    }

    private void startRefreshAuto() {
        if (t == null || !t.isAlive()) {
            t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Mise à jour complète
                                    new TacheGetInfoBelote().execute(urlBelote + mIdPartie + "&joueur=" + mPseudo);
                                }
                            });
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };

            t.start();
            debug("start refresh");
        }
    }

    @Override
    protected void onResume() {
        startRefreshAuto();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bouton_retour:
                finish();
                break;

            case R.id.table_carte_image_atout :
                if (mEstCeMonTourDeChoisir == 2)
                    break;
            case R.id.image_2eme_tour_coeur :
            case R.id.image_2eme_tour_pique :
            case R.id.image_2eme_tour_trefle :
            case R.id.image_2eme_tour_carreau :
                if (mEstCeMonTourDeChoisir == 0) {
                    Toast.makeText(this, "Ce n'est pas ton tour !", Toast.LENGTH_SHORT).show();
                    break;
                }
                String tag;
                if (v.getId() == R.id.table_carte_image_atout) {
                    String[] chaine = v.getTag().toString().split("_");
                    tag = chaine[1];
                }
                else
                    tag = v.getTag().toString();
                afficheCouleurAtout(R.id.table_carte_image_couleur_atout, tag);
                new MainActivity.TacheURLSansRetour().execute(urlDistribueBelote + mIdPartie + "&joueur=" + mPseudo + "&couleur=" + tag);
                masqueChoixAtout();
                break;

            case R.id.bouton_passer:
                switch (mEstCeMonTourDeChoisir) {
                    case 0:
                        Toast.makeText(this, "Ce n'est pas ton tour !", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        new MainActivity.TacheURLSansRetour().execute(urlMAJContrat+mIdPartie+"&joueur="+mPseudo+"&contrat=non");
                        break;
                    case 2 :
                        new MainActivity.TacheURLSansRetour().execute(urlMAJContrat+mIdPartie+"&joueur="+mPseudo+"&contrat=deux");
                        break;
                }
                break;
            case R.id.bouton_tour_suivant:
                mBoutonTourSuivant.setOnClickListener(null);
                passerTourSuivant();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void afficheCouleurAtout(int id, String couleur) {
        ImageView iv = findViewById(id);
        switch (couleur) {
            default:
            case "coeur" :
                iv.setImageResource(R.drawable.coeur);
                break;
            case "pique" :
                iv.setImageResource(R.drawable.pique);
                break;
            case "trefle" :
                iv.setImageResource(R.drawable.trefle);
                break;
            case "carreau" :
                iv.setImageResource(R.drawable.carreau);
                break;
        }
    }

    private void masqueChoixAtout() {
        // Masque le choix de l'atout
        LinearLayout ll = findViewById(R.id.layout_choix_atout);
        ll.setVisibility(View.GONE);
        // Masque le choix des couleurs pour le 2ème tour
        ll = findViewById(R.id.layout_couleur_2eme_tour);
        ll.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        long time;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            // Appuie sur l'écran
            case MotionEvent.ACTION_DOWN:
                time = System.currentTimeMillis();

                if (mClickCount > 2 )
                    mClickCount=0;
                // Si temps entre 2 clicks trop long, on retourne à 0
                if ((time - mStartTimeClick) > MAX_DURATION_DOUBLE_CLICK)
                    mClickCount = 0;
                // Si le précédent click n'est pas sur la même vue, on retourne à 0
                if (mLastViewID != v.getId())
                    mClickCount = 0;

                mStartTimeClick = System.currentTimeMillis();
                mClickCount++;
                mLastViewID = v.getId();
                Log.d("PGR-onTouch", "ACTION_DOWN " + mClickCount + " " + v.getId() + " ");
                break;

            // Relanche l'écran
            case MotionEvent.ACTION_UP:
                // Temps en appuie et relache
                time = System.currentTimeMillis() - mStartTimeClick;
                mDurationClick = mDurationClick + time;
                if (mClickCount == 2) {
                    if (mDurationClick <= MAX_DURATION_CLICK) {
                        // On a un double clic !
                        doublicClic(v);
                        Log.d("PGR-onTouch", "ACTION_UP -> Double clic");
                    }
                    mClickCount = 0;
                    mDurationClick = 0;
                    break;
                }
                Log.d("PGR-onTouch", "ACTION_UP " + mClickCount + " " + v.getId() + " ");
                break;
            default:
                if (mClickCount > 2 )
                    mClickCount=0;
                Log.d("PGR-onTouch", "AUTRE ACTION " + mClickCount + " " + v.getId() + " evt "+event.getAction());
                break;
        }
        return true;
    }

    /**
     * Gestion des actions liées à un double clic
     * @param v : la vue sur laquelle le double clic a été réalisé
     */
    private void doublicClic(View v) {
        if (v.getTag() != null && v.getTag().toString().startsWith("carte_")) {
            mCarteActive = findViewById(v.getId());
            String[] chaine = mCarteActive.getTag().toString().split("_"); // ex : carte_bleu_2
            String couleurCarteActive = chaine[1];
            String valeurCarteActive = chaine[2];

            boolean JeJoueUneCarteAutorisee = false;
            // A qui est-ce le tour ?
            String pseudoQuiDoitJouer = getPseudoQuiDoitJouer();
            String messageErreur;
            if (pseudoQuiDoitJouer.equals(""))
                messageErreur = "C'est à " + mJoueurQuiRemporteLePli + " de jouer";
            else
                messageErreur = "C'est à " + pseudoQuiDoitJouer + " de jouer";
            // Quelle est la couleur de la première carte jouée dans ce pli ?
            if (pseudoQuiDoitJouer.equals(mPseudo)) { // Si c'est mon tour dans ce pli, on regarde la couleur jouée
                ImageView iv = findViewById(tableIdImageCartePli[0]); // Première carte jouée dans le pli en cours
                String couleurDemandee="";
                if (iv!=null && iv.getVisibility()==View.VISIBLE)
                    couleurDemandee = iv.getTag().toString().split("_")[1]; // Couleur de la première carte du pli
                // Si je joue la couleur demandée => OK
                if (couleurDemandee.equals("") || couleurDemandee.equals(couleurCarteActive))
                    JeJoueUneCarteAutorisee = true;
                else { // Sinon, on vérifie que je n'ai plus la couleur demandée dans ma main
                    // On regarde toutes les cartes de la main du joueur
                    for (int value : tableIdImageCarteMain) {
                        ImageView iv2 = findViewById(value);
                        JeJoueUneCarteAutorisee = true;
                        if (iv2 == null)
                            break;
                        // Si une carte visible est de la couleur demandée
                        if (iv2.getVisibility() == View.VISIBLE && iv2.getTag().toString().split("_")[1].equals(couleurDemandee)) {
                            // Si j'ai la couleur demandée, je ne peux pas jouer une autre carte
                            JeJoueUneCarteAutorisee = false;
                            messageErreur = "C'est " + couleurDemandee + " demandé !";
                            break;
                        }
                    }
                }
            }
            // Si je peux jouer la couleur ou si tout le monde a joué le pli et que j'ai remporté le pli
            if (JeJoueUneCarteAutorisee || (pseudoQuiDoitJouer.equals("") && mJoueurQuiRemporteLePli.equals(mPseudo))) {
                new BeloteActivity.TacheJoueCarte().execute(urlJoueCarte + mIdPartie + "&couleur_carte=" + couleurCarteActive + "&valeur_carte=" + valeurCarteActive + "&joueur=" + mPseudo);
            } else
                Toast.makeText(getBaseContext(), messageErreur, Toast.LENGTH_SHORT).show();
        } else {
            switch (v.getId()) {
                case R.id.table_carte_image_joueur1:
                case R.id.table_carte_image_joueur2:
                case R.id.table_carte_image_joueur3:
                case R.id.table_carte_image_joueur4:
                    clicAnnulCarte(v);
                    break;
            }
        }
    }

    private void clicAnnulCarte(View v) {
        ImageView iv = findViewById(v.getId());
        String[] chaine = iv.getTag().toString().split("_");
        new MainActivity.TacheURLSansRetour().execute(MainActivity.urlAnnulCarte + mIdPartie + "&couleur_carte=" + chaine[1] + "&valeur_carte=" + chaine[2]);
        Toast.makeText(this, "Carte recupérée", Toast.LENGTH_SHORT).show();
    }

    private int getImageCarte(String couleurCarte, int valeurCarte) {
        int ressource=0;
        switch (couleurCarte) {
            case "pique":
                ressource = imagesPique[valeurCarte];
                break;
            case "coeur":
                ressource = imagesCoeur[valeurCarte];
                break;
            case "trefle":
                ressource = imagesTrefle[valeurCarte];
                break;
            case "carreau":
                ressource = imagesCarreau[valeurCarte];
                break;
        }

        return ressource;
    }

    /**
     * Affiche les cartes dans la vue
     *
     * @param cartes : Liste des cartes à afficher
     */
    private void afficheCartes(ArrayList<Carte> cartes) {
        TableRow tableauCartes = findViewById(R.id.tableau_cartes);
        tableauCartes.removeAllViewsInLayout();
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        params.setMargins(5, 0, 5, 0);
        tableauCartes.setLayoutParams(params);

        // Affiche les cartes
        if (cartes != null)
            for (int i = 0; i < cartes.size(); i++) {
                ImageView carte = new ImageView(this);
                String nomCarte = cartes.get(i).getCouleur() + "_" + cartes.get(i).getValeur();
                params = new TableRow.LayoutParams(250, 450, 0);
                params.setMargins(5, 0, 0, 0);
                carte.setLayoutParams(params);
                carte.setImageResource(getImageCarte(cartes.get(i).getCouleur(), cartes.get(i).getValeur()));
                carte.setTag("carte_" + nomCarte);
                carte.setId(tableIdImageCarteMain[i]);
                carte.setOnTouchListener(this);
                tableauCartes.addView(carte);
            }
    }

    /**
     * Affiche la carte atout proposée dans la vue
     *
     * @param cartes : Liste des cartes à afficher
     */
    private void afficheCarteAtout(ArrayList<Carte> cartes) {
        // Affiche la carte
        if (cartes != null && cartes.size()>0) {
            // Affiche le choix de l'atout
            LinearLayout ll = findViewById(R.id.layout_choix_atout);
            ll.setVisibility(View.VISIBLE);
            ImageView imageCarte = findViewById(R.id.table_carte_image_atout);
            String couleur = cartes.get(0).getCouleur();
            String nomCarte = couleur + "_" + cartes.get(0).getValeur();
            imageCarte.setImageResource(getImageCarte(couleur, cartes.get(0).getValeur()));
            imageCarte.setTag("carte_" + nomCarte);
            imageCarte.setOnClickListener(this);

            // Affiche toutes les couleurs
            ImageView iv;
            for (int value : imagesCouleur) {
                iv = findViewById(value);
                iv.setVisibility(View.VISIBLE);
            }
            // Masque la couleur pour le 2ème tour
            switch (couleur) {
                default:
                case "coeur" :
                    iv = findViewById(R.id.image_2eme_tour_coeur);
                    break;
                case "pique" :
                    iv = findViewById(R.id.image_2eme_tour_pique);
                    break;
                case "trefle" :
                    iv = findViewById(R.id.image_2eme_tour_trefle);
                    break;
                case "carreau" :
                    iv = findViewById(R.id.image_2eme_tour_carreau);
                    break;
            }
            iv.setVisibility(View.GONE);
            mTable.setVisibility(View.GONE);
        }
        // Cache la zone du choix l'atout
        else {
            afficheCouleurAtout(R.id.table_carte_image_couleur_atout, mAtoutChoisi);
            masqueChoixAtout();
            mTable.setVisibility(View.VISIBLE);
        }
    }

    private void affichePseudos(ArrayList<Joueur> listeJoueurs) {
        mListePseudo = new String[listeJoueurs.size()];
        TextView tv;
        for (int i = 0; i < listeJoueurs.size(); i++) {
            mListePseudo[i] = listeJoueurs.get(i).getNomJoueur();
            debug(listeJoueurs.get(i).getNomJoueur());
            tv = findViewById(tableIdPseudoPli[i]);
            tv.setText(mListePseudo[i]);
            tv.setVisibility(View.VISIBLE);
        }
    }

    private void afficheScore() {
        TextView tv = findViewById(tableIdPseudoPli[0]);
        String pseudoPremierJoueur = tv.getText().toString();
        int posPremierJoueur = -1;

        TextView tvScoreEquipe = findViewById(R.id.score_equipe1);
        TextView tvScoreTotal = findViewById(R.id.score_total_equipe1);

        for (int i=0;i<mListeJoueurs.size();i++) {
            if (pseudoPremierJoueur.equals(mListeJoueurs.get(i).getNomJoueur())) {
                tvScoreEquipe.setText(mListeJoueurs.get(i).getNomEquipe());
                tvScoreTotal.setText(String.valueOf(mListeJoueurs.get(i).getScore()));
                posPremierJoueur = i;
                break;
            }
        }

        tvScoreEquipe = findViewById(R.id.score_equipe2);
        tvScoreTotal = findViewById(R.id.score_total_equipe2);

        tvScoreEquipe.setText(mListeJoueurs.get((posPremierJoueur + 1) % 2).getNomEquipe());
        tvScoreTotal.setText(String.valueOf(mListeJoueurs.get((posPremierJoueur + 1) % 2).getScore()));
    }

    private void affichePliEnCours(ArrayList<Pli> plis) {
        TextView pseudo;
        ImageView imageCarte;
        int nbJoueur = mListePseudo.length;
        int positionPremierJoueur = 0;
        int positionJoueur;
        // On récupère la place du premier joueur
        if (plis != null && plis.size() > 0) {
            for (int j = 0; j < nbJoueur; j++) {
                if (mListePseudo[j].equals(plis.get(0).getJoueur())) {
                    positionPremierJoueur = j;
                    debug("posPremJoueur " + positionPremierJoueur);
                    break;
                }
            }
        }
        // On parcours les id des pseudo
        for (int i = 0; i < tableIdPseudoPli.length; i++) {
            pseudo = findViewById(tableIdPseudoPli[i]);
            imageCarte = findViewById(tableIdImageCartePli[i]);
            // Affichage de tous les pseudo dans l'ordre de jeu, même si le joueur n'a pas encore joué
            positionJoueur = (positionPremierJoueur + i) % nbJoueur;
            debug("posJoueurNonJoué " + i + " positionPremierJoueur " + positionPremierJoueur + " nbJoueur " + nbJoueur + " positionJoueur" + positionJoueur);
            String pseudoTexte=mListePseudo[positionJoueur];
            pseudo.setTextColor(getResources().getColor(R.color.blanc));
            pseudo.setText(pseudoTexte);
            pseudo.setVisibility(View.VISIBLE);

            if (plis!=null && i < plis.size()) {
                imageCarte.setImageResource(getImageCarte(plis.get(i).getCarte().getCouleur(), plis.get(i).getCarte().getValeur()));
                imageCarte.setTag("pli_" + plis.get(i).getCarte().getCouleur() + "_" + plis.get(i).getCarte().getValeur());
                imageCarte.setVisibility(View.VISIBLE);
                // Rend clicable uniquement la dernière carte posée si c'est ma carte
                if ((i + 1) == plis.size() && mPseudo.equals(pseudoTexte))
                    imageCarte.setOnTouchListener(this);
                else
                    imageCarte.setOnTouchListener(null);
            } else {
                imageCarte.setOnTouchListener(null);
                imageCarte.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void afficheHistoriquePlis(ArrayList<HistoriquePlis> histoPlis) {
        int[] idImageCarte;
        scorePartie[0]=0;
        scorePartie[1]=0;
        TextView tvEquipe = findViewById(R.id.score_equipe1);
        String equipePremierJoueur = tvEquipe.getText().toString();
        // Parcours les plis
        for(int i=0; i<histoPlis.size();i++) {
            idImageCarte = getIdImageHisto(i);
            HistoriquePlis histoPli = histoPlis.get(i);
            TextView tv = findViewById(tableIdHistoScore[i]);
            tv.setText(String.valueOf(histoPli.getScore()));
            tv = findViewById(tableIdHistoJoueur[i]);
            tv.setText(histoPli.getJoueurQuiRemportePli());
            LinearLayout ll = findViewById(tableIdHistoLayout[i]);
            ll.setVisibility(View.VISIBLE);

            if (equipePremierJoueur.equals(getEquipeJoueur(histoPli.getJoueurQuiRemportePli())))
                scorePartie[0]+=histoPli.getScore();
            else
                scorePartie[1]+=histoPli.getScore();

            // Parcours les cartes du pli
            for(int j=0;j<histoPli.getPlis().size();j++) {
                ImageView iv = findViewById(idImageCarte[j]);
                iv.setImageResource(getImageCarte(histoPli.getPlis().get(j).getCarte().getCouleur(),histoPli.getPlis().get(j).getCarte().getValeur()));
            }
        }
        // Masque les pli non réalisés
        for (int j=histoPlis.size();j<8;j++) {
            LinearLayout ll = findViewById(tableIdHistoLayout[j]);
            ll.setVisibility(View.GONE);
        }
        // Affiche score de la partie
        TextView tvScore = findViewById(R.id.score_partie_equipe1);
        String scorePar = "(" + scorePartie[0] + ")";
        tvScore.setText(scorePar);
        tvScore.setTag(scorePartie[0]);
        tvScore = findViewById(R.id.score_partie_equipe2);
        scorePar = "(" + scorePartie[1] + ")";
        tvScore.setText(scorePar);
        tvScore.setTag(scorePartie[1]);
    }

    private void passerTourSuivant() {
        // On change de premier joueur
        if (mNumeroPli==0)
            new MainActivity.TacheURLSansRetour().execute(urlTourSuivant+mIdPartie);
        else
        {
            // Prise en compte des scores de la partie
            TextView tvScore1 = findViewById(R.id.score_partie_equipe1);
            TextView tvScore2 = findViewById(R.id.score_partie_equipe2);
            TextView tvEquipe1 = findViewById(R.id.score_equipe1);
            TextView tvEquipe2 = findViewById(R.id.score_equipe2);

            String urlTS = urlTourSuivant+mIdPartie+"&equipe1="+tvEquipe1.getText().toString()+"&equipe2="+ tvEquipe2.getText().toString();
            urlTS += "&score1=" + tvScore1.getTag().toString() + "&score2=" + tvScore2.getTag().toString();
            new MainActivity.TacheURLSansRetour().execute(urlTS);
        }
    }

    private int[] getIdImageHisto(int i) {
        int[] idImageCarte = new int[4];
        switch (i) {
            case 0:
                idImageCarte = tableIdHisto1;
                break;
            case 1:
                idImageCarte = tableIdHisto2;
                break;
            case 2:
                idImageCarte = tableIdHisto3;
                break;
            case 3:
                idImageCarte = tableIdHisto4;
                break;
            case 4:
                idImageCarte = tableIdHisto5;
                break;
            case 5:
                idImageCarte = tableIdHisto6;
                break;
            case 6:
                idImageCarte = tableIdHisto7;
                break;
            case 7:
                idImageCarte = tableIdHisto8;
                break;
        }

        return idImageCarte;
    }

    void parseXML(Document doc) {

        Element element = doc.getDocumentElement();
        element.normalize();

        // Cartes que le joueur a en main
        ArrayList<Carte> listeCartesMainJoueur = parseNoeudsCarte(doc, "Cartes");
        afficheCartes(listeCartesMainJoueur);

        // Joueurs
        mListeJoueurs = parseNoeudsJoueurEtEquipe(doc);
        affichePseudos(mListeJoueurs);

        // Pli en cours
        mListeCartesPliEnCours = parseNoeudsPliFromDoc(doc);
        affichePliEnCours(mListeCartesPliEnCours);
        afficheScore();

        // Atout
        ArrayList<Carte> mListeCarteAtout = parseNoeudsCarte(doc, "Atout");
        afficheCarteAtout(mListeCarteAtout);

        // Historique des plis joués
        parseEtAfficheNoeudsHisto(doc);

        // Score
        parseNoeudsEquipeFromDoc(doc);
        //afficheMonScore();
    }

    private void parseNoeudsEquipeFromDoc(Document doc) {
        Node NoeudScores = getNoeudUnique(doc, "Scores");

        for (int i = 0; i < NoeudScores.getChildNodes().getLength(); i++) { // Parcours toutes les équipes
            Node noeudEquipe = NoeudScores.getChildNodes().item(i);
            Log.d("PGR-XML-Equipe", noeudEquipe.getNodeName());
            int scorePartie = 0;
            int scoreTotal = 0;
            String nomEquipe = "";
            TextView tvEquipe = findViewById(R.id.score_equipe1);
            String equipePremierJoueur = tvEquipe.getText().toString();
            for (int j = 0; j < noeudEquipe.getAttributes().getLength(); j++) { // Parcours tous les attributs du noeud Equipe
                Log.d("PGR-XML-Equipe", noeudEquipe.getAttributes().item(j).getNodeName() + "_" + noeudEquipe.getAttributes().item(j).getNodeValue());
                switch (noeudEquipe.getAttributes().item(j).getNodeName()) {
                    case "nom":
                        nomEquipe = noeudEquipe.getAttributes().item(j).getNodeValue();
                        break;
                    case "scorePartie":
                        scorePartie = Integer.parseInt(noeudEquipe.getAttributes().item(j).getNodeValue());
                        break;
                    case "scoreTotal":
                        scoreTotal = Integer.parseInt(noeudEquipe.getAttributes().item(j).getNodeValue());
                        break;
                }
            }

            // Mets à jour le score d'équipe de chaque joueur de l'équipe
            for (int index = 0; index < mListeJoueurs.size(); index++) {
                if (mListeJoueurs.get(index).getNomEquipe().equals(nomEquipe)) {
                    mListeJoueurs.get(index).setScore(scoreTotal);
                }
            }
            // On mets à jour le score de la partie en cours
            if (mNumeroPli == 8 && mListeCartesPliEnCours.size() == 4) {
                if (equipePremierJoueur.equals(nomEquipe))
                    tvEquipe = findViewById(R.id.score_partie_equipe1);
                else
                    tvEquipe = findViewById(R.id.score_partie_equipe2);
                int scoreCumulPlis = Integer.parseInt(tvEquipe.getTag().toString());
                if (scoreCumulPlis == scorePartie) {
                    tvEquipe.setText(String.valueOf(scorePartie));
                } else {
                    // Si somme des plis n'est pas le même score, on affiche les 2
                    String texte = tvEquipe.getText().toString();
                    texte = scorePartie + " " + texte;
                    tvEquipe.setText(texte);
                }
                tvEquipe.setTag(String.valueOf(scorePartie));
            }
        }
    }

    private void parseEtAfficheNoeudsHisto(Document doc) {
        ArrayList<HistoriquePlis> mHistoriquePlis = new ArrayList<>();
        // On parcours l'historique des plis
        int scorePli = 0;
        String joueurRemportePli = "";
        Node noeudHistorique = getNoeudUnique(doc, "Historique");
        for (int i = 0; i < noeudHistorique.getChildNodes().getLength(); i++) { // Parcours toutes les plis
            Node noeudPli = noeudHistorique.getChildNodes().item(i);
            ArrayList<Pli> histoPli = parseNoeudsPlis(noeudPli); // Charge les cartes du pli

            scorePli = 0;
            joueurRemportePli = "";
            for (int j = 0; j < noeudPli.getAttributes().getLength(); j++) { // Parcours tous les attributs du noeud pli
                switch (noeudPli.getAttributes().item(j).getNodeName()) {
                    case "numero":
                        break;
                    case "score":
                        scorePli = Integer.parseInt(noeudPli.getAttributes().item(j).getNodeValue());
                        break;
                    case "joueur":
                        joueurRemportePli = noeudPli.getAttributes().item(j).getNodeValue();
                        break;
                }
            }
            HistoriquePlis histoPlis = new HistoriquePlis(histoPli, scorePli, joueurRemportePli);
            mHistoriquePlis.add(histoPlis);
        }

        // Ajout du pli en cours en dernier élément de l'historique
        if (mNumeroPli == 8) {
            //mScorePartie
            Node noeudPli = getNoeudUnique(doc, "Pli");
            for (int j = 0; j < noeudPli.getAttributes().getLength(); j++) { // Parcours tous les attributs du noeud pli
                switch (noeudPli.getAttributes().item(j).getNodeName()) {
                    case "numero":
                        break;
                    case "score":
                        scorePli = Integer.parseInt(noeudPli.getAttributes().item(j).getNodeValue());
                        break;
                    case "joueur":
                        joueurRemportePli = noeudPli.getAttributes().item(j).getNodeValue();
                        break;
                }
            }
            HistoriquePlis histoPlis = new HistoriquePlis(mListeCartesPliEnCours, scorePli, joueurRemportePli);
            mHistoriquePlis.add(histoPlis);

            // Affiche l'histotiques des plis en fin de partie
            if (mListeCartesPliEnCours.size() == 4) {
                afficheHistoriquePlis(mHistoriquePlis);
                if (mAdmin == 1) {
                    mBoutonTourSuivant.setVisibility(View.VISIBLE);
                    mBoutonTourSuivant.setOnClickListener(this);
                }
            } else {
                // Masque l'historique
                masqueHistorique();
            }
        } else {
            // Masque l'historique
            masqueHistorique();
        }
    }

    private void masqueHistorique() {
        for (int value : tableIdHistoLayout) {
            LinearLayout ll = findViewById(value);
            ll.setVisibility(View.GONE);
        }
        TextView tvScore = findViewById(R.id.score_partie_equipe1);
        tvScore.setText("");
        tvScore.setTag("");
        tvScore = findViewById(R.id.score_partie_equipe2);
        tvScore.setText("");
        tvScore.setTag("");
    }

    private ArrayList<Pli> parseNoeudsPlis(Node NoeudCartes) {
        String pseudo="";
        String couleur="";
        int valeur=0;
        String com="";
        ArrayList<Pli> listePli = new ArrayList<>();

        for (int i=0; i<NoeudCartes.getChildNodes().getLength(); i++) { // Parcours toutes les cartes
            Node noeudCarte = NoeudCartes.getChildNodes().item(i);
            Log.d("PGR-XML-Pli",noeudCarte.getNodeName());
            for (int j = 0; j < noeudCarte.getAttributes().getLength(); j++) { // Parcours tous les attributs du noeud carte
                Log.d("PGR-XML-Pli", noeudCarte.getAttributes().item(j).getNodeName() + "_" + noeudCarte.getAttributes().item(j).getNodeValue());
                switch (noeudCarte.getAttributes().item(j).getNodeName()) {
                    case "joueur" :
                        pseudo = noeudCarte.getAttributes().item(j).getNodeValue();
                        break;
                    case "couleur" :
                        couleur = noeudCarte.getAttributes().item(j).getNodeValue();
                        break;
                    case "valeur" :
                        valeur = Integer.parseInt(noeudCarte.getAttributes().item(j).getNodeValue());
                        break;
                    case "com" :
                        com = noeudCarte.getAttributes().item(j).getNodeValue();
                        break;
                }
            }
            Pli pli = new Pli(pseudo,new Carte(couleur, valeur), com);
            listePli.add(pli);
        }

        return listePli;

    }

    private ArrayList<Joueur> parseNoeudsJoueurEtEquipe(Document doc) {
        Node NoeudJoueurs = getNoeudUnique(doc, "Joueurs");

        String pseudo = "";
        String equipe = "";
        int nbJoueurQuiPassent = 0;
        int maPostion = -1;
        boolean estCeLe2EmeTour = false;
        mAtoutChoisi = "";
        mJoueurQuiAPris = "";
        TextView tv = findViewById(R.id.pseudo_joueur_qui_a_pris);
        tv.setText("");
        ArrayList<Joueur> listeJoueurs = new ArrayList<>();

        for (int i=0; i<NoeudJoueurs.getChildNodes().getLength(); i++) { // Parcours toutes les cartes
            int admin = 0;
            int scoreEquipe = 0;
            Node noeudCarte = NoeudJoueurs.getChildNodes().item(i);
            TextView tvPseudo;
            Log.d("PGR-XML-Joueur",noeudCarte.getNodeName());
            for (int j = 0; j < noeudCarte.getAttributes().getLength(); j++) { // Parcours tous les attributs du noeud carte
                Log.d("PGR-XML-Joueur", noeudCarte.getAttributes().item(j).getNodeName() + "_" + noeudCarte.getAttributes().item(j).getNodeValue());
                switch (noeudCarte.getAttributes().item(j).getNodeName()) {
                    case "pseudo":
                        pseudo = noeudCarte.getAttributes().item(j).getNodeValue();
                        tvPseudo = findViewById(tableIdPseudoChoixAtout[i]);
                        tvPseudo.setText(pseudo);
                        if (pseudo.equals(mPseudo))
                            maPostion = i;
                        break;
                    case "admin":
                        admin = Integer.parseInt(noeudCarte.getAttributes().item(j).getNodeValue());
                        if (pseudo.equals(mPseudo))
                            mAdmin = admin;
                        break;
                    case "equipe":
                        equipe = noeudCarte.getAttributes().item(j).getNodeValue();
                        break;
                    case "scoreEquipe":
                        scoreEquipe = Integer.parseInt(noeudCarte.getAttributes().item(j).getNodeValue());
                        break;
                    case "contrat":
                        String contrat = noeudCarte.getAttributes().item(j).getNodeValue();
                        switch (contrat) {
                            case "pique" :
                            case "coeur" :
                            case "trefle" :
                            case "carreau" :
                                mAtoutChoisi = contrat;
                                mJoueurQuiAPris = pseudo;
                                tv.setText(mJoueurQuiAPris);
                                break;
                            case "deux" :
                                estCeLe2EmeTour=true;
                                nbJoueurQuiPassent++;
                            case "non" :
                                tvPseudo = findViewById(tableIdPseudoChoixAtout[i]);
                                String texte = pseudo + " : " + contrat;
                                tvPseudo.setText(texte);
                                if (!estCeLe2EmeTour)
                                    nbJoueurQuiPassent++;
                                break;
                        }
                        break;
                    case "annonce1" :
                        break;
                    case "annonce2" :
                        break;
                }
            }
            Joueur joueur = new Joueur(pseudo, equipe, scoreEquipe, admin);
            listeJoueurs.add(joueur);
        }

        // Est-ce mon tour de jouer ?
        if (nbJoueurQuiPassent%4 == maPostion) {
            mEstCeMonTourDeChoisir = 1;
            if (estCeLe2EmeTour || nbJoueurQuiPassent==4)
                mEstCeMonTourDeChoisir++;
        }
        else
            mEstCeMonTourDeChoisir=0;

        if (estCeLe2EmeTour && nbJoueurQuiPassent == 4) {
            mBoutonTourSuivant.setVisibility(View.VISIBLE);
            mBoutonTourSuivant.setOnClickListener(this);
        } else
            mBoutonTourSuivant.setVisibility(View.GONE);

        // Si tout le monde a répondu pour le premier tour, affichage des couleurs pour le second tour
        LinearLayout ll = findViewById(R.id.layout_couleur_2eme_tour);
        if (mEstCeMonTourDeChoisir==2)
            ll.setVisibility(View.VISIBLE);
        else
            ll.setVisibility(View.GONE);

        return listeJoueurs;
    }

    private ArrayList<Pli> parseNoeudsPliFromDoc(Document doc) {
        Node NoeudCartes = getNoeudUnique(doc, "Pli");
        mJoueurQuiRemporteLePli = "";

        for (int i = 0; i < NoeudCartes.getAttributes().getLength(); i++) {
            switch (NoeudCartes.getAttributes().item(i).getNodeName()) {
                case "numero":
                    mNumeroPli = Integer.parseInt(NoeudCartes.getAttributes().item(i).getNodeValue());
                    String titrePli = getResources().getString(R.string.pli_en_cours) + " - numéro " + mNumeroPli;
                    mTitrePli.setText(titrePli);
                    break;
                case "score":
                    break;
                case "joueur":
                    mJoueurQuiRemporteLePli = NoeudCartes.getAttributes().item(i).getNodeValue();
                    break;
            }
        }

        return parseNoeudsPlis(NoeudCartes);
    }

    /**
     * Classe qui permet de récupérer en base toutes les informations du jeu Belote d'un joueur
     * -> Retourne l'ensemble des informations à afficher au joueurs sous forme XML
     * * - Cartes de la main du joueur
     * * - Cartes Atout sur la table
     * * - Liste des joueurs
     * * - Cartes du pli en cours
     * * - Historique des plis joués
     */
    private class TacheGetInfoBelote extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... strings) {
            URL url;
            Document doc=null;
            try {
                // l'URL est en paramètre donc toujours 1 seul paramètre
                url = new URL(strings[0]);
                // Lecture du flux
                InputStream is = url.openStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(is);
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            // Lecture des informations du jeu
            if (doc != null) {
                // Ecran de chargement
                findViewById(R.id.chargement).setVisibility(View.GONE);
                parseXML(doc);
            }
            super.onPostExecute(doc);
        }
    }

    /**
     * Classe qui permet de mettre à jour en base la main d'un joueur (update main + ajout carte sur la table)
     * -> Retourne la liste des cartes du joueur demandé
     */
    private class TacheJoueCarte extends AsyncTask<String, Void, Integer> {
        String result;

        @Override
        protected Integer doInBackground(String... strings) {
            //ArrayList<Carte> cartes = new ArrayList<>();
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

            return result.length();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer > 0) {
                Toast.makeText(getBaseContext(), "Ce n'est pas ton tour !!", Toast.LENGTH_SHORT).show();
            } else {
                // Masque la carte
                mCarteActive.setVisibility(View.GONE);
            }
            super.onPostExecute(integer);
        }
    }
}

