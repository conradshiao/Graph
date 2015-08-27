package graph;


/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** A directed graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Conrad Shiao
 */
public class DirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public DirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return true;
    }

}
