package iut.bayonne.game3D;

import java.util.Vector;

import eclairage.Materiau;
import formes.Parallelepipede;
import formes.PlaquePolygonale;
import objets3d.ElementMobile;

/**
 * Created by damien on 31/01/16.
 */
public class Labyrinthe extends ElementMobile {
    public static final int ESPACE = 1;
    public static final int MUR = 2;
    public static final int DEPART = 3;
    public static final int ARRIVE = 4;
    public static final int PIECE = 5;

    private float angle;

    private Vector < Vector < Vector <Integer> > > grille;

    public Labyrinthe() {
        super(AXE_Y, 1);
        grille = new Vector < Vector < Vector <Integer> > >();
        angle = 0;

        this.setLimites(0, 360);
    }

    public void avancer() {
        System.out.println("avancer");
        this.bouger(0, 0, 0, 5, 500);
    }

    public void tournerAGauche() {
        System.out.println("gauche");
        this.modifieRotation(AXE_Y, -10);
        angle -= 10;
        if (angle < 0) angle += 360;
        //this.tourner(0, angle, 500);
    }

    public void tournerADroite() {
        System.out.println("droite");
        this.modifieRotation(AXE_Y, 10);
        angle += 10;
        if (angle > 360) angle -= 360;
        //this.tourner(0, angle, 500);
    }

    public void afficher() { // affichage optimisé
        int tailleMur_x = 5;
        int tailleMur_y = 15;
        int tailleMur_z = 5;
        int posCourante_x = tailleMur_x / 2;
        int posCourante_y = tailleMur_y / 2;
        int posCourante_z = tailleMur_z / 2;

        //PlaquePolygonale;

        Parallelepipede sol = new Parallelepipede(1, grille.elementAt(0).elementAt(0).size() * tailleMur_x, grille.elementAt(0).size() * tailleMur_z, 1);
        sol.setPosition(sol.getLargeur() / 2, -sol.getLongueur() / 2, sol.getHauteur() / 2);
        sol.setMateriau(Materiau.plastiqueJaune);
        this.ajouteComposant(sol);

        for (Vector < Vector <Integer> > sousgrille : grille) { // on parcours les étages (si le labyrinthe a plusieurs étages)
            for(Vector <Integer> ligne : sousgrille) { // on parcours la grille ligne par ligne
                for(Integer element : ligne) { // on parcours la ligne case par case
                    if (element == MUR) {
                        Parallelepipede mur = new Parallelepipede(tailleMur_y, tailleMur_x, tailleMur_z, 1);
                        mur.setMateriau(Materiau.caoutchoucCyan);
                        mur.setPosition(posCourante_x, posCourante_y, posCourante_z);
                        this.ajouteComposant(mur);
                    }
                    posCourante_x += tailleMur_x;

                }
                posCourante_x = tailleMur_x / 2;
                posCourante_z += tailleMur_z;
            }
        }
    }

    public void afficherBlocParBloc() {
        int tailleMur_x = 5;
        int tailleMur_y = 15;
        int tailleMur_z = 5;
        int posCourante_x = tailleMur_x / 2;
        int posCourante_y = tailleMur_y / 2;
        int posCourante_z = tailleMur_z / 2;

        Parallelepipede sol = new Parallelepipede(1, grille.elementAt(0).elementAt(0).size() * tailleMur_x, grille.elementAt(0).size() * tailleMur_z, 1);
        sol.setPosition(sol.getLargeur() / 2, -sol.getLongueur() / 2, sol.getHauteur() / 2);
        sol.setMateriau(Materiau.plastiqueJaune);
        this.ajouteComposant(sol);

        for (Vector < Vector <Integer> > sousgrille : grille) { // on parcours les étages (si le labyrinthe a plusieurs étages)
            for(Vector <Integer> ligne : sousgrille) { // on parcours la grille ligne par ligne
                for(Integer element : ligne) { // on parcours la ligne case par case
                    if (element == MUR) {
                        Parallelepipede mur = new Parallelepipede(tailleMur_y, tailleMur_x, tailleMur_z, 1);
                        mur.setMateriau(Materiau.caoutchoucCyan);
                        mur.setPosition(posCourante_x, posCourante_y, posCourante_z);
                        this.ajouteComposant(mur);
                    }
                    posCourante_x += tailleMur_x;

                }
                posCourante_x = tailleMur_x / 2;
                posCourante_z += tailleMur_z;
            }
        }
    }

    public void afficherGrilleSurTerminal() {
        for(Vector < Vector <Integer> > sousgrille : grille) { // on parcours les étages (si le labyrinthe a plusieurs étages)
            for(Vector <Integer> ligne : sousgrille) { // on parcours la grille ligne par ligne
                for(Integer element : ligne) { // on parcours la ligne case par case
                    System.out.print(element + " ");
                }
                System.out.println();
            }
            System.out.println("======================== taille : " + sousgrille.size());
        }
    }

    public void genererLabyrintheTest() {
        Vector < Vector <Integer> > grilleTmp = new Vector < Vector <Integer> >();

        Vector <Integer> ligne1 = new Vector <Integer>();
        ligne1.addElement(MUR);ligne1.addElement(MUR);ligne1.addElement(MUR);ligne1.addElement(MUR);ligne1.addElement(MUR);
        grilleTmp.addElement(ligne1);

        Vector <Integer> ligne2 = new Vector <Integer>();
        ligne2.addElement(ARRIVE);ligne2.addElement(ESPACE);ligne2.addElement(ESPACE);ligne2.addElement(ESPACE);ligne2.addElement(MUR);
        grilleTmp.addElement(ligne2);

        Vector <Integer> ligne3 = new Vector <Integer>();
        ligne3.addElement(MUR);ligne3.addElement(MUR);ligne3.addElement(MUR);ligne3.addElement(ESPACE);ligne3.addElement(MUR);
        grilleTmp.addElement(ligne3);

        Vector <Integer> ligne4 = new Vector <Integer>();
        ligne4.addElement(MUR);ligne4.addElement(ESPACE);ligne4.addElement(ESPACE);ligne4.addElement(ESPACE);ligne4.addElement(MUR);
        grilleTmp.addElement(ligne4);

        Vector <Integer> ligne5 = new Vector <Integer>();
        ligne5.addElement(MUR);ligne5.addElement(PIECE);ligne5.addElement(MUR);ligne5.addElement(MUR);ligne5.addElement(MUR);
        grilleTmp.addElement(ligne5);

        Vector <Integer> ligne6 = new Vector <Integer>();
        ligne6.addElement(MUR);ligne6.addElement(ESPACE);ligne6.addElement(MUR);ligne6.addElement(ESPACE);ligne6.addElement(ESPACE);
        grilleTmp.addElement(ligne6);

        Vector <Integer> ligne7 = new Vector <Integer>();
        ligne7.addElement(MUR);ligne7.addElement(ESPACE);ligne7.addElement(MUR);ligne7.addElement(ESPACE);ligne7.addElement(MUR);
        grilleTmp.addElement(ligne7);

        Vector <Integer> ligne8 = new Vector <Integer>();
        ligne8.addElement(DEPART);ligne8.addElement(ESPACE);ligne8.addElement(MUR);ligne8.addElement(ESPACE);ligne8.addElement(ESPACE);
        grilleTmp.addElement(ligne8);

        Vector <Integer> ligne9 = new Vector <Integer>();
        ligne9.addElement(MUR);ligne9.addElement(MUR);ligne9.addElement(MUR);ligne9.addElement(MUR);ligne9.addElement(MUR);
        grilleTmp.addElement(ligne9);

        grille.addElement(grilleTmp);
    }
}
