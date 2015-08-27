package graph;


import java.util.Comparator;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Conrad Shiao
 */
public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        _graph = G;
        _comparator = order;
        _whichTraversal = 1;
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> fringe = new PriorityQueue<
                Graph<VLabel, ELabel>.Vertex>(G.vertexSize(),
                        makeComparator(order));
        fringe.add(v);
        while (!fringe.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex vertex = fringe.remove();
            Graph<VLabel, ELabel>.Edge edge = null;
            Graph<VLabel, ELabel>.Vertex child;
            try {
                if (!_marked.contains(vertex)) {
                    try {
                        visit(vertex);
                        _marked.add(vertex);
                    } catch (RejectException e) {
                        continue;
                    }
                }
                Iteration<Graph<VLabel, ELabel>.Edge> edges = G.isDirected()
                        ? G.outEdges(vertex) : G.edges(vertex);
                while (edges.hasNext()) {
                    try {
                        edge = edges.next();
                        child = edge.getV(vertex);
                        preVisit(edge, child);
                    } catch (RejectException e) {
                        continue;
                    }
                    fringe.add(child);
                }
            } catch (StopException e) {
                _finalEdge = edge;
                _finalVertex = vertex;
            }
        }
    }

    /** Returns a comparator of graph vertices based on ORDER. */
    private Comparator<Graph<VLabel, ELabel>.Vertex> makeComparator(
            final Comparator<VLabel> order) {
        return new Comparator<Graph<VLabel, ELabel>.Vertex>() {
            @Override
            public int compare(Graph<VLabel, ELabel>.Vertex v1,
                    Graph<VLabel, ELabel>.Vertex v2) {
                return order.compare(v1.getLabel(), v2.getLabel());
            }
        };
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        Stack<Graph<VLabel, ELabel>.Vertex> fringe =
                new Stack<Graph<VLabel, ELabel>.Vertex>();
        HashSet<Graph<VLabel, ELabel>.Vertex> preVisited =
                new HashSet<Graph<VLabel, ELabel>.Vertex>();
        HashSet<Graph<VLabel, ELabel>.Vertex> postVisited =
                new HashSet<Graph<VLabel, ELabel>.Vertex>();
        _graph = G;
        _whichTraversal = 2;
        fringe.add(v);
        preVisited.add(v);
        while (!fringe.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex currV = fringe.pop(), neighbor;
            Graph<VLabel, ELabel>.Edge currentEdge = null;
            try {
                if (_marked.contains(currV) && !postVisited.contains(currV)) {
                    postVisit(currV);
                    postVisited.add(currV);
                } else {
                    try {
                        if (!_marked.contains(currV)) {
                            visit(currV);
                            _marked.add(currV);
                        }
                    } catch (RejectException e) {
                        continue;
                    }
                    if (!postVisited.contains(currV)) {
                        fringe.add(currV);
                        List<Graph<VLabel, ELabel>.Vertex> children =
                                new ArrayList<Graph<VLabel, ELabel>.Vertex>();
                        for (Graph<VLabel, ELabel>.Edge e : G.isDirected()
                                ? G.outEdges(currV) : G.edges(currV)) {
                            try {
                                currentEdge = e;
                                neighbor = e.getV(currV);
                                if (!preVisited.contains(neighbor)) {
                                    preVisit(currentEdge, currV);
                                    preVisited.add(neighbor);
                                }
                            } catch (RejectException rejected) {
                                continue;
                            }
                            children.add(neighbor);
                        }
                        Collections.reverse(children);
                        fringe.addAll(children);
                        children.clear();
                    }
                }
            } catch (StopException e) {
                _finalEdge = currentEdge;
                _finalVertex = currV;
                break;
            }
        }
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
            Graph<VLabel, ELabel>.Vertex v) {
        Queue<Graph<VLabel, ELabel>.Vertex> fringe =
                new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        HashSet<Graph<VLabel, ELabel>.Vertex> preVisited =
                new HashSet<Graph<VLabel, ELabel>.Vertex>();
        HashSet<Graph<VLabel, ELabel>.Vertex> postVisited =
                new HashSet<Graph<VLabel, ELabel>.Vertex>();
        _graph = G;
        _whichTraversal = 3;
        fringe.add(v);
        preVisited.add(v);
        while (!fringe.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex curr = fringe.remove(), neighbor;
            Graph<VLabel, ELabel>.Edge edge = null;
            try {
                if (!postVisited.contains(curr) && _marked.contains(curr)) {
                    postVisit(curr);
                    postVisited.add(curr);
                } else {
                    try {
                        visit(curr);
                        _marked.add(curr);
                    } catch (RejectException ignore) {
                        continue;
                    }
                    for (Graph<VLabel, ELabel>.Edge e : G.isDirected()
                            ? G.outEdges(curr) : G.edges(curr)) {
                        try {
                            edge = e;
                            neighbor = e.getV(curr);
                            if (!preVisited.contains(neighbor)) {
                                preVisit(e, curr);
                                fringe.add(neighbor);
                                preVisited.add(neighbor);
                            }
                        } catch (RejectException seriouslyIgnoreThis) {
                            continue;
                        }
                    }
                    fringe.add(curr);
                }
            } catch (StopException e) {
                _finalEdge = edge;
                _finalVertex = curr;
                break;
            }
        }
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (_whichTraversal == 1) {
            traverse(_graph, v, _comparator);
        } else if (_whichTraversal == 2) {
            depthFirstTraverse(_graph, v);
        } else if (_whichTraversal == 3) {
            breadthFirstTraverse(_graph, v);
        }
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit that caused a Visit routine to return false.  Otherwise,
     *  returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to throw a StopException. If it
     *  was not an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the graph currently being traversed.  Undefined if no traversal
     *  is in progress. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
        System.out.println(v.getLabel().toString());
    }

    /** The Vertex (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The graph currently being traversed. */
    protected Graph<VLabel, ELabel> _graph;
    /** The set of marked vertices in the graph that is currently being
     *  traversed. */
    private HashSet<Graph<VLabel, ELabel>.Vertex> _marked =
            new HashSet<Graph<VLabel, ELabel>.Vertex>();
    /** Records the current comparator I am using. Will only have a value
     *  if I have prematurely ended the general traverse method with a
     *  StopException. */
    private Comparator<VLabel> _comparator;
    /** Records which traversal I am on, in case I prematurely end a
     *  traversal with a StopException. A value of 1 will correspond
     *  to the general traversal, 2 to the depth-first traversal, and
     *  3 to the breadth-first traversal. */
    private int _whichTraversal;
}
