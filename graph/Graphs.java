package graph;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;

/** Assorted graph algorithms.
 *  @author Conrad Shiao
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, V1) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> fScores =
                new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> closed =
                new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openList = new
            PriorityQueue<Graph<VLabel, ELabel>.Vertex>(
                    G.vertexSize(), helper(fScores));
        HashMap<Graph<VLabel, ELabel>.Vertex,
        Graph<VLabel, ELabel>.Edge> path =
               new HashMap<Graph<VLabel, ELabel>.Vertex,
               Graph<VLabel, ELabel>.Edge>();
        initializeVertexValues(G, vweighter);
        openList.add(V0);
        vweighter.setWeight(V0.getLabel(), 0.0);
        double fScoreOfV0 = h.dist(V0.getLabel(), V1.getLabel());
        fScores.put(V0, fScoreOfV0);
        while (!openList.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex curr = openList.element();
            if (curr == V1) {
                return reconstructPath(path, V1);
            }
            openList.remove();
            closed.add(curr);
            for (Graph<VLabel, ELabel>.Edge e : G.isDirected()
                    ? G.outEdges(curr) : G.edges(curr)) {
                Graph<VLabel, ELabel>.Vertex neighbor = e.getV(curr);
                double tempGScore = vweighter.weight(curr.getLabel())
                        + eweighter.weight(e.getLabel());
                double tempFScore = tempGScore
                        + h.dist(neighbor.getLabel(), V1.getLabel());
                if (closed.contains(neighbor)
                        && tempFScore >= fScores.get(neighbor)) {
                    continue;
                } else if (!openList.contains(neighbor)
                        || tempFScore < fScores.get(neighbor)) {
                    path.put(neighbor, e);
                    fScores.put(neighbor,  tempFScore);
                    vweighter.setWeight(neighbor.getLabel(), tempGScore);
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    /** Initializes all the vertices of graph G of with edge labels of type
     *  ELABEL and vertices of type VLABEL to have a weight of
     *  Double.POSITIVE_INFINITY using VWEIGHTER. */
    private static <VLabel, ELabel> void initializeVertexValues(
            Graph<VLabel, ELabel> G, Weighter<? super VLabel> vweighter) {
        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            vweighter.setWeight(v.getLabel(), Double.POSITIVE_INFINITY);
        }
    }

    /** Returns a list of edges that correspond to the minimum and best path
     *  to END for the shortestPath method, grabbing edges from PATH
     *  in a Graph whose vertices' labels are of type VLABEL and whose
     *  edges' labels are of type ELABEL. */
    private static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    reconstructPath(Map<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> path,
            Graph<VLabel, ELabel>.Vertex end) {
        List<Graph<VLabel, ELabel>.Edge> best =
                new ArrayList<Graph<VLabel, ELabel>.Edge>();
        Graph<VLabel, ELabel>.Edge from = path.get(end);
        Graph<VLabel, ELabel>.Vertex prev = from.getV(end);
        while (true) {
            best.add(from);
            from = path.get(prev);
            if (from == null) {
                break;
            }
            prev = from.getV(prev);
        }
        Collections.reverse(best);
        return best;
    }

    /** Returns a comparator used for the priority queue in shortestPath to
     *  help find the next element, which will have the lowest F score on a
     *  VLABEL, according to the formula F = G + H, where G is the actual
     *  distance traveled using an edge's ELABEL and H is
     *  the heuristic we use. This method references the mappings of FSCORES
     *  to find the appropriate F score values for a given vertex of the graph.
     *  Assumes that all vertices used as arguments will be keys in FSCORES. */
    private static <VLabel, ELabel> Comparator<Graph<VLabel, ELabel>.Vertex>
    helper(final Map<Graph<VLabel, ELabel>.Vertex, Double> fScores) {
        return new Comparator<Graph<VLabel, ELabel>.Vertex>() {
            @Override
            public int compare(Graph<VLabel, ELabel>.Vertex x,
                    Graph<VLabel, ELabel>.Vertex y) {
                return Double.compare(fScores.get(x), fScores.get(y));
            }
        };
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, V1) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> fScores =
                new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openList =
                new PriorityQueue<Graph<VLabel, ELabel>.Vertex>
            (G.vertexSize(), helper(fScores));
        List<Graph<VLabel, ELabel>.Vertex> closed =
                new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex,
        Graph<VLabel, ELabel>.Edge> path =
                new HashMap<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge>();
        initializeVertexValues(G);
        openList.add(V0);
        double gScore = 0.0;
        double fScore = gScore + h.dist(V0.getLabel(), V1.getLabel());
        V0.getLabel().setWeight(gScore);
        fScores.put(V0, fScore);
        while (!openList.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex curr = openList.element();
            if (curr == V1) {
                return reconstructPath(path, V1);
            }
            openList.remove();
            closed.add(curr);
            for (Graph<VLabel, ELabel>.Edge e : G.outEdges(curr)) {
                Graph<VLabel, ELabel>.Vertex neighbor = e.getV(curr);
                double tempGScore = curr.getLabel().weight()
                        + e.getLabel().weight();
                double tempFScore = tempGScore + h.dist(neighbor.getLabel(),
                        V1.getLabel());
                if (closed.contains(neighbor)
                        && tempFScore >= fScores.get(neighbor)) {
                    continue;
                } else if (!openList.contains(neighbor)
                        || tempFScore < fScores.get(neighbor)) {
                    path.put(neighbor, e);
                    fScores.put(neighbor, tempFScore);
                    neighbor.getLabel().setWeight(tempGScore);
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    /** Initializes all the vertices of graph G with vertex labels of type
     *  VLABEL and edge labels of type ELABEL to have weight labels of
     *  Double.POSITIVE_INFINITY. */
    private static <VLabel extends Weightable, ELabel extends Weighted>
    void initializeVertexValues(Graph<VLabel, ELabel> G) {
        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            v.getLabel().setWeight(Double.POSITIVE_INFINITY);
        }
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
