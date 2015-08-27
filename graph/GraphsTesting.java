package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

/** Unit Tests for the graphs.java class.
 * @author Conrad Shiao
 */
public class GraphsTesting {

    @SuppressWarnings("hiding")
    public class Vweighter<String> implements Weighter<String> {
        @Override
        public double weight(String x) {
            return _mapping.get(x);
        }

        @Override
        public void setWeight(String x, double num) {
            _mapping.put(x, num);
        }

        private HashMap<String, Double> _mapping =
                new HashMap<String, Double>();
    }

    @SuppressWarnings("hiding")
    public class Eweighter<Double> implements Weighting<Double> {
        @Override
        public double weight(Double x) {
            return (java.lang.Double) x;
        }
    }

    @SuppressWarnings("hiding")
    public class TestHeuristic<String> implements Distancer<String> {
        @Override
        public double dist(String v0, String v1) {
            if (v0.equals("a")) {
                return 4.0;
            } else if (v0.equals("b")) {
                return 2.0;
            } else if (v0.equals("c")) {
                return 4.0;
            } else if (v0.equals("d")) {
                return 4.5;
            } else if (v0.equals("e")) {
                return 2.0;
            } else if (v0.equals("goal")) {
                return 0.0;
            } else {
                return 1e3;
            }
        }
    }

    @Test
    public void testShortestPath() {
        Eweighter<Double> eweighter = new Eweighter<Double>();
        final Vweighter<String> vweighter = new Vweighter<String>();
        TestHeuristic<String> heuristic = new TestHeuristic<String>();
        Graph<String, Double> G = new UndirectedGraph<String, Double>();
        Graph<String, Double>.Vertex a = G.add("a"), b = G.add("b"),
                c = G.add("c"), d = G.add("d"), e = G.add("e"),
                goal = G.add("goal"), start = G.add("start"), z = G.add("z");
        Graph<String, Double>.Edge startToA = G.add(start, a, 1.5),
                startToD = G.add(start, d, 2.0), aToB = G.add(a, b, 2.0),
                bToC = G.add(b, c, 3.0), cToGoal = G.add(c, goal, 4.0),
                eToGoal = G.add(e, goal, 2.0), dToE = G.add(d, e, 3.0);
        List<Graph<String, Double>.Edge> res = Graphs.shortestPath(G, start,
                goal, heuristic, vweighter, eweighter);
        assertEquals("shortestPath has wrong answer", 3, res.size());
        assertEquals("shortestPath has wrong answer", startToD, res.get(0));
        assertEquals("shortestPath has wrong answer", dToE, res.get(1));
        assertEquals("shortestPath has wrong answer", eToGoal, res.get(2));
        String sideEffectError = "shortestPath has wrong side effect";
        boolean test1; test1 = vweighter.weight("a") == 1.5;
        assertTrue(sideEffectError, test1);
        boolean test2; test2 = vweighter.weight("b") == 3.5;
        assertTrue(sideEffectError, test2);
        boolean test3; test3 = vweighter.weight("c") == 6.5;
        assertTrue(sideEffectError, test3);
        boolean test4; test4 = vweighter.weight("d") == 2;
        assertTrue(sideEffectError, test4);
        boolean test5; test5 = vweighter.weight("e") == 5;
        assertTrue(sideEffectError, test5);
        boolean test6; test6 = vweighter.weight("goal") == 7;
        assertTrue(sideEffectError, test6);
        boolean test7; test7 = vweighter.weight("start") == 0;
        assertTrue(sideEffectError, test7);
        boolean test8;
        test8 = vweighter.weight("z") == Double.POSITIVE_INFINITY;
        assertTrue(sideEffectError, test8);
    }

}
