package graf.grafgenerator.Criteris;

import graf.GrafWikipedia;
import graf.NodeWiki;
import org.grupwiki.graf.Arc;
import org.grupwiki.graf.Graf;

import java.util.HashSet;

/**
 * Grup 3: Wikipedia
 * Usuari: agusti.bau
 * Data: 2/5/15
 *
 */
public class CriteriParesComuns extends Criteri{

    public CriteriParesComuns(double ponderacio) {
        super(ponderacio);
    }

    @Override
    public double getPes(NodeWiki n1, NodeWiki n2, GrafWikipedia graf) {
        HashSet<Arc<NodeWiki>> arcs1 = graf.getNodesAdjacents(n1);
        double paresComuns = 0;
        for(Arc<NodeWiki> arc : arcs1){
            if(arc.getPes() > 0) {
                NodeWiki pare = Graf.getNodeOposat(n1, arc);
                if(graf.existeixArc(pare,n2)){
                    paresComuns++;
                }
            }
        }
        return paresComuns;
    }


    }