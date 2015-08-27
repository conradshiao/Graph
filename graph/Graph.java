package graph;

import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make methods in Graph abstract, if you want
 * different implementations in DirectedGraph and UndirectedGraph.  You may
 * add bodies to abstract methods, modify existing bodies, or override
 * inherited methods. */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Conrad Shiao
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label on this vertex. */
        private final VLabel _label;

    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV0(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _mappings.keySet().size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        if (!contains(v)) {
            System.err.printf("Vertex %s is not in my graph", v.toString());
            return 0;
        } else {
            int count = 0;
            for (Edge e : _mappings.get(v)) {
                if (isDirected()) {
                    if (e.getV0() == v) {
                        count += 1;
                    }
                } else {
                    count += 1;
                    if (e.getV0() == v && e.getV1() == v) {
                        count += 1;
                    }
                }
            }
            return count;
        }
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        if (!contains(v)) {
            System.err.printf("Vertex %s is not in my graph", v.toString());
            return 0;
        } else {
            int count = 0;
            for (Edge e : _mappings.get(v)) {
                if (isDirected()) {
                    if (e.getV1() == v) {
                        count += 1;
                    }
                } else {
                    count += 1;
                    if (e.getV0() == v && e.getV1() == v) {
                        count += 1;
                    }
                }
            }
            return count;
        }
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e : _mappings.get(u)) {
            Vertex from = e.getV0(), to = e.getV1();
            if (from == u && to == v) {
                return true;
            } else if (!isDirected() && from == v && to == u) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        for (Edge e : _mappings.get(u)) {
            Vertex from = e.getV0(), to = e.getV1();
            ELabel get = e.getLabel();
            if (from == u && to == v && get == label) {
                return true;
            } else if (!isDirected() && from == v && to == u && get == label) {
                return true;
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex answer = new Vertex(label);
        _mappings.put(answer, new HashSet<Edge>());
        return answer;
    }

    /** Returns true iff I already contain vertex V in myself. */
    private boolean contains(Vertex v) {
        return _mappings.containsKey(v);
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO).
     *  Returns null if either FROM or TO are not in the graph. */
    public Edge add(Vertex from, Vertex to, ELabel label) {
        if (contains(from) && contains(to)) {
            Edge answer = new Edge(from, to, label);
            _mappings.get(from).add(answer);
            _edges.add(answer);
            if (to != from) {
                _mappings.get(to).add(answer);
            }
            return answer;
        } else {
            System.err.printf("given vertex %s not found in graph",
                    !contains(from) ? from.toString() : to.toString());
            return null;
        }
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from, Vertex to) {
        return this.add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        _mappings.remove(v);
        for (Set<Edge> edges : _mappings.values()) {
            Iterator<Edge> temp = edges.iterator();
            while (temp.hasNext()) {
                Edge edge = temp.next();
                if (edge.getV0() == v || edge.getV1() == v) {
                    temp.remove();
                    _edges.remove(edge);
                }
            }
        }
    }

    /** Remove E from me, if present. E must be between my vertices,
     *  or the result is undefined. */
    public void remove(Edge e) {
        Vertex from = e.getV0(), to = e.getV1();
        if (contains(from) && contains(to)) {
            _mappings.get(from).remove(e);
            _mappings.get(to).remove(e);
            _edges.remove(e);
        } else {
            System.err.printf("Edge %s is not between my vertices, as"
                + "Vertex %s is not in my graph.", e.toString(),
                !contains(from) ? from.toString() : to.toString());
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        if (contains(v1) && contains(v2)) {
            Set<Edge> alternates = _mappings.get(v1);
            Iterator<Edge> edges = _mappings.get(v2).iterator();
            while (edges.hasNext()) {
                Edge temp = edges.next();
                Vertex from = temp.getV0(), to = temp.getV1();
                if ((from == v1 && to == v2)
                        || (!isDirected() && from == v2 && to == v1)) {
                    edges.remove();
                    _edges.remove(temp);
                    alternates.remove(temp);
                }
            }
        } else {
            System.err.printf("Vertex %s is not in my graph",
                    !contains(v1) ? v1.toString() : v2.toString());
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(_mappings.keySet());
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        List<Vertex> successors = new ArrayList<Vertex>();
        for (Edge e : _mappings.get(v)) {
            Vertex from = e.getV0(), to = e.getV1();
            if (from == v) {
                successors.add(to);
            } else if (!isDirected() && to == v) {
                successors.add(from);
            }
        }
        return Iteration.iteration(successors);
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        List<Vertex> predecessors = new ArrayList<Vertex>();
        for (Edge e : _mappings.get(v)) {
            Vertex from = e.getV0(), to = e.getV1();
            if (to == v) {
                predecessors.add(from);
            } else if (!isDirected() && from == v) {
                predecessors.add(to);
            }
        }
        return Iteration.iteration(predecessors);
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(_edges);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        List<Edge> outgoing = new ArrayList<Edge>();
        for (Edge e : _mappings.get(v)) {
            if ((e.getV0() == v) || (!isDirected() && e.getV1() == v)) {
                outgoing.add(e);
            }
        }
        return Iteration.iteration(outgoing);
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        List<Edge> incomingEdges = new ArrayList<Edge>();
        for (Edge e : _mappings.get(v)) {
            if ((e.getV1() == v) || (!isDirected() && e.getV0() == v)) {
                incomingEdges.add(e);
            }
        }
        return Iteration.iteration(incomingEdges);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if intComp = Graph.<Integer>naturalOrder(), then
     *  intComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Cause subsequent traversals and calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        final Comparator<ELabel> temp = comparator;
        Comparator<Edge> edgeComparator = new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return temp.compare(e1.getLabel(), e2.getLabel());
            }
        };
        Collections.sort(_edges, edgeComparator);
    }

    /** A Map that maps all the vertices in me to a set of all edges that
     *  are incident to their associated key.
     *  Note that in this structure, the same edge, as long as it is not a
     *  self-edge, will appear twice, each time in separate key values.
     *  If the same edge is somehow added to me, I will only keep one copy
     *  in the set. */
    private HashMap<Vertex, Set<Edge>> _mappings =
            new HashMap<Vertex, Set<Edge>>();

    /** The list of edges that I contain. */
    private List<Edge> _edges = new ArrayList<Edge>();
}
