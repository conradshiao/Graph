package graph;

import java.util.Iterator;
import java.util.Comparator;

import org.junit.Test;
import static org.junit.Assert.*;


/** Unit Tests of the graph.java class.
 *  @author Conrad Shiao
 */
public class GraphTesting {

    private String outDegreeMessage(String vertex) {
        return String.format("outDegree of vertex '%s' is wrong", vertex);
    }

    @Test
    public void Olivia() {
        Graph<String, Integer> G = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex a = G.add("a");
        G.add(a, a);
        assertEquals("degree method wrong", 2, G.degree(a));
    }

    @Test
    public void testoutDegree() {
        Graph<String, Integer> graph = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph.add("first"),
                second = graph.add("second"), third = graph.add("third");
        assertEquals(outDegreeMessage("first"), 0, graph.outDegree(first));
        graph.add(first, second);
        assertEquals(outDegreeMessage("first"), 1, graph.outDegree(first));
        assertEquals(outDegreeMessage("third"), 0, graph.outDegree(third));
        graph.add(third, second, 4); graph.add(second, third);
        graph.add(third, first);
        assertEquals(outDegreeMessage("first"), 2, graph.outDegree(first));
        assertEquals(outDegreeMessage("second"), 3, graph.outDegree(second));
        assertEquals(outDegreeMessage("third"), 3, graph.outDegree(third));
        Graph<String, Integer> graph1 = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"),
                two = graph1.add("two"), three = graph1.add("three"),
                four = graph1.add("four"), five = graph1.add("five");
        graph1.add(one, two); graph1.add(two, three); graph1.add(three, four);
        graph1.add(four, five); graph1.add(three, five); graph1.add(two, one);
        assertEquals(outDegreeMessage("one"), 1, graph1.outDegree(one));
        assertEquals(outDegreeMessage("two"), 2, graph1.outDegree(two));
        assertEquals(outDegreeMessage("three"), 2, graph1.outDegree(three));
        assertEquals(outDegreeMessage("four"), 1, graph1.outDegree(four));
        assertEquals(outDegreeMessage("five"), 0, graph1.outDegree(five));
    }

    @Test
    public void testSelfEdges() {
        Graph<String, Integer> graph = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph.add("one"),
                two = graph.add("two");
        graph.add(one, one);
        assertEquals("inDegree method wrong", 1, graph.inDegree(one));
        assertEquals("outDegree method wrong", 1, graph.outDegree(one));
        graph.add(two, one); graph.add(two, two);
        assertEquals("inDegree method wrong", 1, graph.inDegree(two));
        assertEquals("outDegree method wrong", 2, graph.outDegree(two));
        assertEquals("outDegree method wrong", 1, graph.outDegree(one));
        assertEquals("inDegree method wrong", 2, graph.inDegree(one));
        assertEquals("inDegree wrong", 2, graph.inDegree(one));
        Graph<String, Integer> G = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = G.add("first"),
                second = G.add("second");
        G.add(first, first);
        assertEquals("degree method wrong", 2, G.degree(first));
        G.add(second, second); G.add(first, second);
        assertEquals("degree method wrong", 3, G.degree(first));
        assertEquals("degree method wrong", 3, G.degree(second));
    }

    private String inDegreeMessage(String vertex) {
        return String.format("inDegree of vertex '%s' is wrong", vertex);
    }

    @Test
    public void testinDegree() {
        Graph<String, Integer> graph = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph.add("first"),
                second = graph.add("second"), third = graph.add("third");
        assertEquals(inDegreeMessage("first"), 0, graph.inDegree(first));
        graph.add(first, second);
        assertEquals(inDegreeMessage("first"), 1, graph.inDegree(first));
        assertEquals(inDegreeMessage("second"), 1, graph.inDegree(second));
        graph.add(third, second, 4); graph.add(second, third);
        graph.add(third, first);
        assertEquals(inDegreeMessage("first"), 2, graph.inDegree(first));
        assertEquals(inDegreeMessage("second"), 3, graph.inDegree(second));
        assertEquals(inDegreeMessage("third"), 3, graph.inDegree(third));
        Graph<String, Integer> graph1 = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"),
                two = graph1.add("two"), three = graph1.add("three"),
                four = graph1.add("four"), five = graph1.add("five");
        graph1.add(one, two); graph1.add(two, three); graph1.add(three, four);
        graph1.add(four, five); graph1.add(three, five); graph1.add(two, one);
        assertEquals(inDegreeMessage("one"), 1, graph1.inDegree(one));
        assertEquals(inDegreeMessage("two"), 1, graph1.inDegree(two));
        assertEquals(inDegreeMessage("three"), 1, graph1.inDegree(three));
        assertEquals(inDegreeMessage("four"), 1, graph1.inDegree(four));
        assertEquals(inDegreeMessage("five"), 2, graph1.inDegree(five));
    }

    @Test
    public void testcontains() {
        DirectedGraph<String, Integer> graph1 =
                new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"), two =
                graph1.add("two"), three = graph1.add("three"), four =
        graph1.add("four");
        graph1.add(one, two); graph1.add(three, four, null);
        graph1.add(three, two); graph1.add(four, one);
        assertTrue("contains method is wrong", graph1.contains(one, two));
        assertTrue("contains method is wrong", graph1.contains(three, four));
        assertTrue("contains method is wrong", graph1.contains(three, two));
        assertTrue("contains method is wrong", graph1.contains(four, one));
        assertTrue("contains method is wrong", !graph1.contains(two, one));
        assertTrue("contains method is wrong", !graph1.contains(four, three));
        Graph<String, Integer> graph2 = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph2.add("one"), second =
            graph2.add("two"), third = graph2.add("three"), fourth =
        graph2.add("four");
        graph2.add(first, second); graph2.add(third, fourth, null);
        graph2.add(third, second); graph2.add(fourth, first);
        assertTrue("contains method is wrong", graph2.contains(first, second));
        assertTrue("contains method is wrong", graph2.contains(third, fourth));
        assertTrue("contains method is wrong", graph2.contains(third, second));
        assertTrue("contains method is wrong", graph2.contains(fourth, first));
        assertTrue("contains method is wrong", graph2.contains(second, first));
        assertTrue("contains method is wrong", graph2.contains(fourth, third));
        assertTrue("contains method is wrong",
                !graph2.contains(second, fourth));
        assertTrue("contains method is wrong", !graph2.contains(first, third));
    }

    @Test
    public void testcontainsLabel() {
        DirectedGraph<String, Integer> graph1 =
                new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"), two =
                graph1.add("two"), three = graph1.add("three"), four =
        graph1.add("four");
        graph1.add(one, two, 3); graph1.add(three, four, null);
        graph1.add(three, two, -24); graph1.add(four, one, -5);
        String errorMessage = "contains method is wrong";
        assertTrue(errorMessage, graph1.contains(one, two, 3));
        assertTrue(errorMessage, graph1.contains(three, four, null));
        assertTrue(errorMessage, graph1.contains(three, two, -24));
        assertTrue(errorMessage, !graph1.contains(four, one, -7));
        assertTrue(errorMessage, !graph1.contains(one, two, 2));
        assertTrue(errorMessage, !graph1.contains(three, four, 10));
        Graph<String, Integer> graph2 = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph2.add("one"), second =
            graph2.add("two"), third = graph2.add("three"), fourth =
        graph2.add("four");
        graph2.add(first, second, 5); graph2.add(third, fourth, null);
        graph2.add(third, second, 97); graph2.add(fourth, first, 6);
        assertTrue(errorMessage, graph2.contains(first, second, 5));
        assertTrue(errorMessage, graph2.contains(third, fourth, null));
        assertTrue(errorMessage, graph2.contains(third, second, 97));
        assertTrue(errorMessage, !graph2.contains(fourth, first, 8));
        assertTrue(errorMessage, graph2.contains(second, first, 5));
        assertTrue(errorMessage, graph2.contains(fourth, third, null));
        assertTrue(errorMessage, !graph2.contains(first, second, -1));
        assertTrue(errorMessage, !graph2.contains(fourth, third, 0));
    }

    @Test
    public void testremoveVertex() {
        Graph<String, Integer> graph1 = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"), two =
        graph1.add("two"); graph1.add(one, two); graph1.add(two, one);
        graph1.remove(two);
        String errorMessage = "remove method for a single vertex erroneous";
        assertEquals(errorMessage, 0, graph1.outDegree(one));
        assertEquals(errorMessage, 0, graph1.inDegree(one));
        Iterator<Graph<String, Integer>.Edge> edgeIterator = graph1.edges();
        int count = 0;
        while (edgeIterator.hasNext()) {
            edgeIterator.next(); count += 1;
        }
        assertEquals(errorMessage, 0, count);
        Graph<String, Integer> graph2 = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph2.add("one"), second =
                graph2.add("two"), third = graph2.add("three");
        graph2.add(first, third); graph2.add(second, first);
        graph2.add(second, third);
        graph2.remove(second);
        assertEquals(errorMessage, 1, graph2.degree(first));
        assertEquals(errorMessage, 1, graph2.degree(third));
        Iterator<Graph<String, Integer>.Edge> temp = graph2.edges();
        int counter = 0;
        while (temp.hasNext()) {
            temp.next(); counter++;
        }
        assertEquals(errorMessage, 1, counter);
    }

    @Test
    public void testremoveEdge() {
        Graph<String, Integer> graph1 = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph1.add("one"), two =
                graph1.add("two");
        Graph<String, Integer>.Edge eOne = graph1.add(one, two),
            eTwo = graph1.add(two, one, 3);
        graph1.remove(eOne);
        String errorMessage = "remove method for a single vertex erroneous";
        assertEquals(errorMessage, 0, graph1.outDegree(one));
        assertEquals(errorMessage, 1, graph1.inDegree(one));
        assertEquals(errorMessage, 1, graph1.outDegree(two));
        Iterator<Graph<String, Integer>.Edge> edgeIterator = graph1.edges();
        int count = 0;
        while (edgeIterator.hasNext()) {
            edgeIterator.next(); count += 1;
        }
        assertEquals(errorMessage, 1, count);
        graph1.add(one, two); graph1.add(one, two);
        graph1.remove(one, two);
        assertEquals(errorMessage, 1, graph1.outDegree(two));
        graph1.remove(two, one);
        assertEquals(errorMessage, 0, graph1.outDegree(two));
        Graph<String, Integer> graph2 = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex first = graph2.add("one"), second =
                graph2.add("two"), third = graph2.add("three");
        Graph<String, Integer>.Edge eFirst = graph2.add(first, third),
            eSecond = graph2.add(second, first, 3),
            eThird = graph2.add(second, third),
            eFourth = graph2.add(third, first),
            eFifth = graph2.add(third, first);
        assertTrue(errorMessage, graph2.contains(second, first, 3));
        graph2.remove(eSecond);
        assertEquals(errorMessage, 3, graph2.degree(first));
        assertEquals(errorMessage, 1, graph2.degree(second));
        graph2.remove(eFourth);
        assertEquals(errorMessage, 2, graph2.degree(first));
        assertEquals(errorMessage, 1, graph2.degree(second));
        graph2.remove(third, first);
        assertEquals(errorMessage, 0, graph2.degree(first));
        assertEquals(errorMessage, 1, graph2.degree(third));
        assertEquals(errorMessage, 1, graph2.degree(second));
        Iterator<Graph<String, Integer>.Edge> temp = graph2.edges();
        int counter = 0;
        while (temp.hasNext()) {
            temp.next(); counter++;
        }
        assertEquals(errorMessage, 1, counter);
    }

    @Test
    public void testVertexSize() {
        Graph<String, Integer> graph = new DirectedGraph<String, Integer>();
        for (int i = 1; i <= 35; i++) {
            graph.add("hey");
        }
        String errorMessage = "wrong number of vertices";
        assertEquals(errorMessage, 35, graph.vertexSize());
        Graph<String, Integer>.Vertex temp = graph.add("conrad");
        assertEquals(errorMessage, 36, graph.vertexSize());
        graph.add("conrad");
        assertEquals(errorMessage, 37, graph.vertexSize());
        graph.remove(temp);
        assertEquals(errorMessage, 36, graph.vertexSize());
    }

    @Test
    public void testedgeSizewithDirectedGraphs() {
        String errorMessage = "wrong number of edges";
        Graph<String, Integer> graph = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph.add("one"),
                two = graph.add("two"), three = graph.add("three");
        Graph<String, Integer>.Edge first = graph.add(one, two),
                second = graph.add(two, three),
                third = graph.add(three, one);
        graph.add(three, one); graph.add(three, one, -10);
        graph.add(one, three);
        assertEquals(errorMessage, 6, graph.edgeSize());
        graph.remove(three, one);
        assertEquals(errorMessage, 3, graph.edgeSize());
        graph.remove(first);
        assertEquals(errorMessage, 2, graph.edgeSize());
        graph.remove(first);
        assertEquals(errorMessage, 2, graph.edgeSize());
        graph.add(three, one);
        assertEquals(errorMessage, 3, graph.edgeSize());
    }

    @Test
    public void testedgeSizewithUndirectedGraphs() {
        String errorMessage = "wrong number of edges";
        Graph<String, Integer> graph = new UndirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex one = graph.add("one"),
                two = graph.add("two"), three = graph.add("three");
        Graph<String, Integer>.Edge first = graph.add(one, two),
                second = graph.add(two, three),
                third = graph.add(three, one);
        graph.add(three, one); graph.add(three, one, -10);
        graph.add(one, three);
        assertEquals(errorMessage, 6, graph.edgeSize());
        graph.remove(three, one);
        assertEquals(errorMessage, 2, graph.edgeSize());
        graph.remove(first);
        assertEquals(errorMessage, 1, graph.edgeSize());
        graph.remove(third);
        assertEquals(errorMessage, 1, graph.edgeSize());
    }

    private Comparator<Integer> testorderEdgesHelper() {
        return new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                return x.compareTo(y);
            }
        };
    }

    @Test
    public void testorderEdges() {
        String errorMessage = "orderEdges method erroneous";
        Graph<String, Integer> graph = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Edge one = graph.add(graph.add("one"),
                graph.add("two"), 10),
        two = graph.add(graph.add("three"), graph.add("four"), 5);
        graph.orderEdges(testorderEdgesHelper());
        Iterator temp = graph.edges();
        assertEquals(errorMessage, two, temp.next());
        assertEquals(errorMessage, one, temp.next());
    }
}
