package domini.controladors;

import domini.controladors.graf.grafgenerator.Criteris.Criteri;
import domini.controladors.graf.grafgenerator.GrafGenerator;
import domini.modeldades.ConjuntComunitatWiki;
import domini.modeldades.InformacioCjtComunitats;
import domini.modeldades.TipusAlgorisme;
import domini.modeldades.graf.GrafWikipedia;
import domini.modeldades.graf.NodeCategoria;
import prop.classescompartides.algorismes.AlgorismeLouvain;
import prop.classescompartides.algorismes.CtrlGirvanBron;
import prop.classescompartides.algorismes.grupclique.CtrlAlgoritmoClique;
import prop.classescompartides.graf.Algoritme;
import prop.classescompartides.graf.Graf;

import java.util.ArrayList;

/**
 * Grup 3: Wikipedia
 * Usuari: aleix.paris
 * Data: 02/05/2015
 */


/**
 * Controlador d'Algorisme
 */


public class CtrlAlgorisme{
    private GrafWikipedia grafWikipedia;
    private TipusAlgorisme tipusAlgorisme;
    private double par1;
    private ArrayList<Criteri> criteris;

    /**
     * Constructora per defecte de la classe
     * @param grafWikipedia    .
     * @param tipusAlgorisme  .
     * @param par1           .
     * @param criteris      .
     */
    public CtrlAlgorisme(GrafWikipedia grafWikipedia, TipusAlgorisme tipusAlgorisme, double par1, ArrayList<Criteri> criteris){
        this.grafWikipedia = grafWikipedia;
        this.tipusAlgorisme = tipusAlgorisme;
        this.par1 = par1;
        this.criteris = criteris;
    }

    /**
     * Cerca comunitats en un graf seguint un dels 3 algorismes definits
     * @return comunitats en un graf seguint un dels 3 algorismes definits
     */
    public ConjuntComunitatWiki cercarComunitats(){
        Algoritme<NodeCategoria> algorisme;
        ConjuntComunitatWiki conjunt = new ConjuntComunitatWiki();

        if(tipusAlgorisme == TipusAlgorisme.LOUVAIN) {
            algorisme = new AlgorismeLouvain<NodeCategoria>();
        }
        else if(tipusAlgorisme == TipusAlgorisme.GIRVAN){
            algorisme = new CtrlGirvanBron<>();
        }
        else{ // Clique
            algorisme = new CtrlAlgoritmoClique<>();
        }

        long startTime = System.currentTimeMillis();
        GrafGenerator generator = new GrafGenerator();
        System.err.println("Algoritme triat: " + String.valueOf(tipusAlgorisme));
        System.err.println("Aplicant criteris...");
        Graf<NodeCategoria> graf = generator.generate(grafWikipedia, criteris);
        long generatorTime = System.currentTimeMillis() - startTime;
        System.err.println("Temps en aplicar criteris: " + String.valueOf(generatorTime) + "ms");
        System.err.println("Cercant comunitats...");
        conjunt.setCjtComunitats(algorisme.cercarComunitats(graf, par1));

        long elapsedTime = System.currentTimeMillis() - startTime - generatorTime;
        System.err.println("Temps en cercar comunitats: " + String.valueOf(elapsedTime) + "ms");
        System.out.println("Temps en cercar comunitats: " + elapsedTime + "ms");

        int nComunitats = conjunt.getCjtComunitats().getNumComunitats();
        System.err.println("Nombre de comunitats generades: " + String.valueOf(nComunitats));

        conjunt.setInformacio(new InformacioCjtComunitats(generatorTime, elapsedTime, nComunitats, tipusAlgorisme, criteris.toString(), graf.ordre()/(double)conjunt.getCjtComunitats().getNumComunitats()));


        return conjunt;
    }

}
