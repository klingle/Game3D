package iut.bayonne.game3D;

import android.support.v7.app.ActionBarActivity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import compatibilite.Color;
import compatibilite.GLCanvas;
import eclairage.Lumiere;
import eclairage.Materiau;
import formes.Parallelepipede;
import visionneuse.AffichageOpengl;
import visionneuse.EcouteurDeToucher;
import visionneuse.FenetreOpenGl;
import visionneuse.Scene;

/*
 * Petite game_3d simpliste d'utilisation de la bibliotheque 3D pour Android.
 * Cree un cube qui tourne autour de l'axe X alternativement dans un  sens puis dans l'autre.
 * On peut modifier la position de la camera par glissement et par pincement sur l'ecran.
 */
public class Game3D extends ActionBarActivity implements FenetreOpenGl, EcouteurDeToucher {

    // Vue initiale
    private static final float DISTANCE = 49f; // plus cette valeur augmente plus la camera est loin
    private static final float PROFONDEUR = 200f; // profondeur jusqu'ou la scene est visible
    public static final int FPS = 25; // vitesse en images/s pour une animation contrelee (non utilise ici)
    // Limites de mouvements de la camera par pincer des doigts
    private static final float DISTANCE_MIN = 48.4f; // distance minimale de la camera
    private static final float DISTANCE_MAX = 49.8f; // distance maximale de la camera
    private static final float SENSIBILITE_DISTANCE = 200f; // plus cette valeur est elevee moins la camera avance/recule vite

    private boolean resume; // Etat permettant de traiter le onResume d'Android
    private GLCanvas glSurfaceView; // canvas opengl pour Android
    private Scene scene;
    private Labyrinthe labyrinthe; // l'ensemble des objets 3d a montrer
    private float positionAnterieureX, positionAnterieureY; // pour gestion de glisser sur l'ecran
    private float ancienneDistance, distanceActuelle; // pour gestion de l'approche camera par pincer
    private static AffichageOpengl affichage; // ecouteur d'evenements opengl (statique car unique)

    // Boutons
    private Button button_gauche;
    private Button button_droite;
    private Button button_avancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Creation de l'appli
        super.onCreate(savedInstanceState);
        // Plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_3d); // mise en place de l'interface (voir fichier res/layout/game3D.xml)
        resume = false; // Pas de onResume execute
        scene = new Scene(); // creer une scene vide

        // Recuperation du canvas opengl de l'interface
        glSurfaceView = (GLCanvas)findViewById(R.id.canvasOpengl);

        // Ecouteur d'evenements du canvas opengl et mise en place de la vue et de l'eclairage initial
        // position initiale de la camera
        affichage = new AffichageOpengl(this, glSurfaceView, 0, 0, 0, DISTANCE, PROFONDEUR, FPS);
        glSurfaceView.setRenderer(affichage); // ecouteur d'evenements opengl
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); // raffraichissement en continu
        initEclairage(); // mise en place de l'eclairage d'ambiance

        // La ligne suivante ne sert que si l'on veut modifier l'affichage par toucher d'ecran
        affichage.ajouterEcouteurDeToucher(this); // ecouteur de toucher pour rotation et zoom

        // Recuperation des boutons
        button_gauche = (Button)findViewById(R.id.button_gauche);
        button_droite = (Button)findViewById(R.id.button_droite);
        button_avancer = (Button)findViewById(R.id.button_avancer);

        button_gauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labyrinthe.tournerAGauche();
            }
        });

        button_droite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labyrinthe.tournerADroite();
            }
        });

        button_avancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labyrinthe.avancer();
            }
        });

        Repere repere = new Repere(); // creer le repere
        scene.ajouterObjet("repere", repere);

        labyrinthe = new Labyrinthe(); // creer le labyrinthe
        labyrinthe.genererLabyrintheTest();
        labyrinthe.afficherGrilleSurTerminal();
        labyrinthe.afficherBlocParBloc();
        scene.ajouterObjet("labyrinthe", labyrinthe);

        /*float tailleDuCube = 15; // cote du cube
        Cube cube1 = new Cube(0, 0, 0, tailleDuCube, Materiau.plastiqueCyan);
        cube1.modifieRotation(Volume.AXE_X, 0);
        cube1.modifieRotation(Volume.AXE_Y, 0);
        cube1.modifieRotation(Volume.AXE_Z, 0);
        scene.ajouterObjet("cube1", cube1);*/
        //cube1.tournerEncontinu(5000); // faire tourner le cube (5s par tour)

        /*Parallelepipede parallelepipede = new Parallelepipede(10, 10, 10, 1);
        parallelepipede.setMateriau(Materiau.plastiqueCyan);
        scene.ajouterObjet("parallelepipede", parallelepipede);*/

        /*Cube cube2 = new Cube(25, 0, 0, tailleDuCube, Materiau.rubis);
        scene.ajouterObjet("cube2", cube2);*/
    }

    @Override
    protected void onResume() { // Reprise de l'appli
        super.onResume();
        // Lancer le rafraichissement de la vue opengl
        lancerVue();
    }

    @Override
    protected void onPause() { // Mise en pause de l'appli
        super.onPause();
        // Arreter le rafraichissement de la vue opengl
        arreterVue();
    }

    @Override
    protected void onDestroy() { // Arret de l'appli
        super.onDestroy();
        // Arreter opengl et suprimer tous les objets de la scene
        arreterVue();
        affichage.dispose();
    }

    // Methodes de gestion de l'action bar (menu general de l'interface)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Genere par Eclipse (non utilise ici)
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_3d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Genere par Eclipse (non utilise ici)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Methodes utilisees par l'ecouteur OpenGL
    @Override
    public Scene getScene() { return scene; } // recuperer la scene
    @Override
    public boolean getResume() { return resume; } // pour traiter le onResume
    @Override
    public void setResume() { resume = false; } // pour indiquer que le onResume a ete traite

    // Methodes internes de modification/initialisation de la vue et des eclairages
    private void arreterVue() {
// 		Si on utilise un animateur
//     	if (animator != null) {
//			animator.arreter();
//			animator = null;
//    	}

        // Faire un pause de la glSurface
        glSurfaceView.onPause();
    }

    // Lancer ou relancer la vue et les eclairages
    private void lancerVue() {
        resume = true;
        // Faire une reprise de la glSurface
        glSurfaceView.onResume();
        affichage.reactiverEclairages();
    }

    // Initialiser l'eclairage d'ambiance (spot 0) et enlever les autres
    private void initEclairage() {
        for (int i = 0; i < 8; i++) affichage.enleverEclairage(i); // enlever tous les eclairages
        Lumiere lumiereAmbiante = new Lumiere(new Color(180,180,180)); // lumiere blanche
        lumiereAmbiante.setPositionALInfini(0.0f, 0.0f, 1.0f); // placee en face de la scene
        lumiereAmbiante.setDirection(new float[]{0.0f, 0.0f, -1.0f}); // orientee vers l'arriere

        /*Lumiere lumiere1 = new Lumiere(new Color(180,180,180));
        lumiere1.setPositionALInfini(0.0f, 1.0f, 0.0f);
        lumiere1.setDirection(new float[]{0.0f, -1.0f, 0.0f});*/

        affichage.ajouterEclairage(0, lumiereAmbiante); // mise en place de l'eclairage d'ambiance
        //affichage.ajouterEclairage(0, lumiere1);
    }

    // Initialiser la vue (eclairages et point de vue)
    private void initialiserVue() {
        initEclairage();
        affichage.changePointDeVueDistance(DISTANCE);
        affichage.definirRotation(0, 0);
    }

    // Methodes de gestion des touchers d'ecran (facultatives)
    @Override
    public void onClick(View v, MotionEvent event) { // click => retour a la position initiale de camera
        affichage.definirRotation(0, 0);
        initialiserVue();
    }

    @Override
    public void onLongClick(View v, MotionEvent event) { // clic long => rien
    }

    @Override
    public boolean onActionDown(MotionEvent event) { // contact => on memorise la position du toucher
        positionAnterieureX = event.getX();
        positionAnterieureY = event.getY();
        return true;
    }

    @Override
    public boolean onActionUp(MotionEvent event) { // fin du contact => rien
        return false;
    }

    @Override
    public boolean onActionMove(MotionEvent event) { // bouger => rotation de la camera
        float x= event.getX();
        float y= event.getY();
        float thetaY = 360.0f * ((float)(x - positionAnterieureX) / (float)glSurfaceView.getWidth());
        float thetaX = 360.0f * ((float)(positionAnterieureY - y) / (float)glSurfaceView.getHeight());
        positionAnterieureX = x;
        positionAnterieureY = y;
        affichage.changerRotation(thetaX, thetaY);
        return true;
    }

    @Override
    public boolean onActionCancel(MotionEvent event) { // perte d'ecoute => rien
        return false;
    }

    @Override
    public boolean onActionPointerDown(MotionEvent event) { // contact a 2 doigts => memoriser la position
        float ptx = event.getX(0) - event.getX(1);
        float pty = event.getY(0) - event.getY(1);
        ancienneDistance = (float)Math.sqrt(ptx * ptx + pty * pty); // distance entre les points de contact
        distanceActuelle = affichage.getDistance();
        return true;
    }

    @Override
    public boolean onActionPointerMove(MotionEvent event) { // bouger a 2 doigts => approcher/reculer la camera
        float ptx = event.getX(0) - event.getX(1);
        float pty = event.getY(0) - event.getY(1);
        float distance = (float)Math.sqrt(ptx * ptx + pty * pty); // distance entre les points de contact
        float rapport = distance - ancienneDistance; // distance entre l'écart actuel et le precedent (vitesse du mouvement)
        ancienneDistance = distance;
        distanceActuelle = distanceActuelle-rapport / SENSIBILITE_DISTANCE; // calcul de l'approche
        if (distanceActuelle < DISTANCE_MIN) distanceActuelle = DISTANCE_MIN; // limitation de l'approche
        if (distanceActuelle > DISTANCE_MAX) distanceActuelle = DISTANCE_MAX;
        affichage.changePointDeVueDistance(distanceActuelle); // modifier la distance de la camera
        return true;
    }
}
