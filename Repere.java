package iut.bayonne.game3D;

import eclairage.Materiau;
import formes.Parallelepipede;
import objets3d.GroupeDeVolumes;

/**
 * Created by damien on 31/01/16.
 */
public class Repere extends GroupeDeVolumes {
    public Repere(int nbUnitees) {
        // Axes
        int tailleAxes = 20 * nbUnitees + 5; // (10 * nbUnitees) * 2 + 5

        Parallelepipede axe_x = new Parallelepipede(1, tailleAxes, 1, 1);
        Parallelepipede axe_y = new Parallelepipede(tailleAxes, 1, 1, 1);
        Parallelepipede axe_z = new Parallelepipede(1, 1, tailleAxes, 1);

        axe_x.setMateriau(Materiau.emeraude); // x : vert
        axe_y.setMateriau(Materiau.rubis);    // y : rouge
        axe_z.setMateriau(Materiau.or);       // z : jaune
        ajouteComposant(axe_x);
        ajouteComposant(axe_y);
        ajouteComposant(axe_z);

        // Unitées
        for (int i = -nbUnitees; i <= nbUnitees; i++) {
            Parallelepipede uniteeCourante_x = new Parallelepipede(2, 2, 2, 1);
            Parallelepipede uniteeCourante_y = new Parallelepipede(2, 2, 2, 1);
            Parallelepipede uniteeCourante_z = new Parallelepipede(2, 2, 2, 1);

            if (i < 0) {
                uniteeCourante_x.setMateriau(Materiau.caoutchoucNoir);
                uniteeCourante_y.setMateriau(Materiau.caoutchoucNoir);
                uniteeCourante_z.setMateriau(Materiau.caoutchoucNoir);
            } else {
                uniteeCourante_x.setMateriau(Materiau.argent);
                uniteeCourante_y.setMateriau(Materiau.argent);
                uniteeCourante_z.setMateriau(Materiau.argent);
            }



            uniteeCourante_x.setPosition(0, 10 * i - 0.5f, 0);
            ajouteComposant(uniteeCourante_x);
            uniteeCourante_y.setPosition(10 * i - 0.5f, 0, 0);
            ajouteComposant(uniteeCourante_y);
            uniteeCourante_z.setPosition(0, 0, 10 * i - 0.5f);
            ajouteComposant(uniteeCourante_z);
        }
    }

    public Repere() {
        this(5);
    }
}
