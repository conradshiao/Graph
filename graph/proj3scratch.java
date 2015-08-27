package graph;

import graph.Graph.Edge;
import graph.Graph.Vertex;

import java.util.Stack;
import java.util.*;
import java.util.Map.Entry;


import static java.lang.System.*;

public class proj3scratch {

    /**
     * @param args
     */
    public static void main(String[] args) {
       String x = "100  300   \n\n ";
       Scanner scanner = new Scanner(x);
       System.out.println("here: " + scanner.nextLine().matches("\\s*"));
       System.out.println("here: " + scanner.nextLine().matches("\\s*"));
       System.out.println("here: " + scanner.nextLine().matches("\\s*"));
       System.out.println("".length());
       System.out.println("test hereee");
       Stack<Integer> a = new Stack<Integer>();
       a.add(3); a.add(4);
       List<Integer> z = new ArrayList<Integer>();
       z.add(10); z.add(11); z.add(12);
       System.out.println(z);
       a.addAll(z);
       System.out.println(a);
       System.out.println(a.pop());
       System.out.println("wut");
       ArrayList<Integer> y = new ArrayList<>(); y.add(1);
       List<Integer> b = new ArrayList<>(); b.add(5); b.add(6); b.add(7);
       y.addAll(b);
       System.out.println(y);
       ArrayList<Integer> j = (ArrayList<Integer>) y.clone();
       System.out.println(j);
       y.remove(0);
       System.out.println(y);
       System.out.println(j);
       y.clear();
       System.out.println(y);
       System.out.println(j);
       
  //     String[] line = scanner.nextLine().trim().split("\\s+");
    //   String[] line1 = scanner.nextLine().trim().split("\\s+");
      // System.out.println(Arrays.toString(line1));
        
       /** _mapping.put("testing", new ArrayList<Integer>());
        List<Integer> a = new ArrayList<Integer>();
        a.add(33);
        _mapping.put("3", a);
        List<Integer> b = new ArrayList<Integer>(); b.add(55);
        _mapping.put("5", b);        
        out.println("beginning: " + _mapping);
        Iterator<String> vertices = _mapping.keySet().iterator();
        while (vertices.hasNext()) {
            String vertex = vertices.next();
            if (vertex.equals("5")) {
                vertices.remove();
                // not completely sure, but should work
            } else if (vertex == "3") {
                vertices.remove();
            }
        }
        out.println(_mapping);
        //Comparator<String> test = Graph.naturalOrder();
        out.println("passed");
        String aa= "test";
        String bb = "te" + "st";
        out.println(new String("test") == "test"); */
        
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if stringComp = Graph.<Integer>naturalOrder(), then
     *  stringComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
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
    static public void orderEdges(Comparator<Integer> comparator) {
        final Comparator<Integer> temp = comparator;
        Comparator<Integer> edgeComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                return temp.compare(e1, e2);
            }
        };
        Integer[] hi = new Integer[] {3,1,7,-5};
        List<Integer> _edges = new ArrayList<Integer>(Arrays.asList(hi));
        Collections.sort(_edges, edgeComparator);
        System.out.println(_edges);
    }

}
  
