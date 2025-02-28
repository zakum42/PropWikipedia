package domini.controladors;

import domini.modeldades.graf.GrafWikipedia;
import domini.modeldades.graf.NodeCategoria;
import domini.modeldades.graf.NodePagina;
import prop.classescompartides.graf.Arc;
import prop.classescompartides.graf.Comunitat;
import prop.classescompartides.graf.ConjuntComunitats;

import java.util.ArrayList;

/**
 * Grup 3: Wikipedia
 * Usuari: eduard.casellas
 * Data: 21/05/15
 */
public class CtrlCatPag {
    private static CtrlCatPag INSTANCE;

    private GrafWikipedia grafWiki;
    private ConjuntComunitats<NodeCategoria> conjuntComunitats;

    /**
     * Retorna una instancia de CtrlCatPag
     */
    public static CtrlCatPag getInstance() {
        if(INSTANCE == null) INSTANCE = new CtrlCatPag();
        return INSTANCE;
    }

    /**
     * Constructora per defecte
     */
    private CtrlCatPag(){
        grafWiki = CtrlWikipedia.getInstance().getGrafWiki();
        conjuntComunitats = CtrlWikipedia.getInstance().getConjuntsGenerats().getCjtComunitats();
    }

    /**
     * Es reseteja la informacio de tots els atributs
     */
    public void reset() {
        INSTANCE = null;
    }

    /**
     * S'obte si existeix la categoria amb nom nom
     */
    public boolean existeixCategoria(String nom) { return grafWiki.existeixNodeCat(nom);}

    /**
     * S'obte si existeix la pagina amb nom nom
     */
    public boolean existeixPagina(String nom) {return grafWiki.existeixNodePag(nom);}

    /**
     * Cas d'us Modificar categoria. Canviar nom
     */
    public void ModificarNomCat(String nomantic, String nounom){
        //obtenir el node
        NodeCategoria c = grafWiki.getCategoriesMap().get(nomantic);
        //obtenir els arcs
        ArrayList<NodeCategoria> nodesb = new ArrayList<>();
        ArrayList<Double> pesos = new ArrayList<Double>();
        ArrayList<Arc<NodeCategoria>> arcs = new ArrayList<>();
        for(Arc<NodeCategoria> a: grafWiki.getArcs()){
            if (a.getNodeA().equals(c) && !nodesb.contains(a.getNodeB())){
                nodesb.add(a.getNodeB());
                pesos.add(a.getPes());
                arcs.add(a);
            }
        }
        //esborrar els arcs
        for(Arc<NodeCategoria> a: arcs){
            grafWiki.eliminarArc(a);
        }
        //esborrar el node
        grafWiki.getCategoriesMap().remove(nomantic);
        grafWiki.eliminarCategoria(c);
        //canviar el nom
        c.setNom(nounom);
        //tornar a crear el node
        grafWiki.getCategoriesMap().put(nounom, c);
        grafWiki.afegirCategoria(c);
        //tornar a afegir els arcs
        int i =0;
        for(NodeCategoria n: nodesb){
            if (pesos.get(i) < 0 ){
                grafWiki.afegirArcCsupC(n,c);
            }
            else {
               grafWiki.afegirArcCsupC(c, n);
            }
            ++i;
        }
    }

    /**
     * Cas d'us Modificar pagina. Canviar nom
     */
    public void ModificarNomPag(String nomantic, String nounom){
        NodePagina p = grafWiki.getPaginesMap().get(nomantic);
        grafWiki.getPaginesMap().remove(nomantic);
        p.setNom(nounom);
        grafWiki.getPaginesMap().put(nounom,p);
    }

    /**
     * Cas d'us Relacionar categories i pagines. Crea un arc CsubC
     */
    public void RelCsubC(String superC, String subC){
        grafWiki.afegirArcCsubC(grafWiki.getNodeCat(superC), grafWiki.getNodeCat(subC));
    }

    /**
     * Cas d'us Relacionar categories i pagines. Crea un arc CsupC
     */
    public void RelCsupC(String superC, String subC){
        grafWiki.afegirArcCsupC(grafWiki.getNodeCat(subC), grafWiki.getNodeCat(superC));
    }

    /**
     * Cas d'us Relacionar categories i pagines. Crea un arc PC
     */
    public void RelPC(String pag, String cat){
        grafWiki.afegirArcPC(grafWiki.getNodePag(pag), grafWiki.getNodeCat(cat));
    }

    /**
     * Cas d'us Relacionar categories i pagines. Eliminar arc
     */
    public void esborrarArc(String nom1, String nom2){
        grafWiki.eliminarArc(grafWiki.getArcEntre(grafWiki.getNodeCat(nom1), grafWiki.getNodeCat(nom2)));
        grafWiki.eliminarArc(grafWiki.getArcEntre(grafWiki.getNodeCat(nom2), grafWiki.getNodeCat(nom1)));
    }

    /**
     * Cas d'us Relacionar categories i pagines. Eliminar arc PC
     */
    public void esborrarArcPC(String cat, String pag){
        grafWiki.eliminarArcPC(cat, pag, grafWiki.getNodeCat(cat), grafWiki.getNodePag(pag));
    }

    /**
     * Obtenir les Pagines relacionades d'una categoria amb nom categoria
     */
    public ArrayList<String> getPagines(String categoria){
        ArrayList<String> pagines = new ArrayList<String>();
        for(NodePagina pagina: grafWiki.getNodeCat(categoria).getPagines()){
            pagines.add(pagina.getNom());
        }
        return pagines;
    }

    /**
     * Obtenir les Super Categories relacionades d'una categoria amb nom categoria
     */
    public ArrayList<String> getSuperCategories(String categoria){
        ArrayList<String> result = new ArrayList<String>();
        for (NodeCategoria superC: grafWiki.getSupCategories(grafWiki.getNodeCat(categoria))){
            result.add(superC.getNom());
        }
        return result;
    }

    /**
     * Obtenir les Sub Categories relacionades d'una categoria amb nom categoria
     */
    public ArrayList<String> getSubCategories(String categoria){
        ArrayList<String> result = new ArrayList<String>();
        for (NodeCategoria superC: grafWiki.getSubCategories(grafWiki.getNodeCat(categoria))){
            result.add(superC.getNom());
        }
        return result;
    }

    /**
     * Obtenir les Categories relacionades mitjan�ant un tema d'una categoria amb nom categoria
     */
    public ArrayList<String> getCategoriesTema(String categoria){
        ArrayList<String> result = new ArrayList<String>();
        if (conjuntComunitats.getComunitats().isEmpty())
                return result;
        for (Comunitat<NodeCategoria> comunitat : conjuntComunitats.getComunitats()){
            if (comunitat.teNode(grafWiki.getNodeCat(categoria))){
                for(NodeCategoria cat: comunitat.getNodes()){
                    if (! cat.equals(grafWiki.getNodeCat(categoria)))
                        result.add(cat.getNom());
                }
            }
        }
        return result;
    }

    /**
     * Obtenir les Categories relacionades d'una pagina amb nom pagina
     */
    public ArrayList<String> getCategoriesPagina(String pagina){
        ArrayList<String> categories = new ArrayList<String>();
        for(NodeCategoria cat: grafWiki.getNodePag(pagina).getCategories()){
            categories.add(cat.getNom());
        }
        return categories;
    }

    public String getUrlPag(String nomPag){
        return grafWiki.getNodePag(nomPag).getUrl();
    }
    public String getUrlCat(String nomCat){
        return grafWiki.getNodeCat(nomCat).getUrl();
    }
}
