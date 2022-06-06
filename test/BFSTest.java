package test;

import org.junit.Test;
import sol.BFS;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly,
 * but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes,
 * but you will not be graded on those.
 * <p>
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;

    private TravelGraph graph1;
    private TravelController localController1;
    private TravelGraph graph2;
    private TravelController localController2;
    private TravelGraph graph3;
    private TravelController localController3;
    private TravelGraph graph4;
    private TravelController localController4;
    private TravelGraph graph5;
    private TravelController localController5;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may
     * require a different setup,
     * we manually call the setup method at the top of the test.
     * <p>
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    /**
     * Create graph using data from cities1.csv and transport1.csv
     */
    public void createGraph1() {
        TravelController controller1 = new TravelController();
        // localGraph1 is a new field that does the same thing as controller1
        this.localController1 = new TravelController();
        controller1.load("data/cities1.csv", "data/transport1.csv");
        this.localController1.load("data/cities1.csv", "data/transport1.csv");
        this.graph1 = controller1.getGraph();
    }

    /**
     * Create graph using data from cities2.csv and transport2.csv
     */
    public void createGraph2() {
        TravelController controller2 = new TravelController();
        this.localController2 = new TravelController();
        controller2.load("data/cities2.csv", "data/transport2.csv");
        this.localController2.load("data/cities2.csv", "data/transport2.csv");
        this.graph2 = controller2.getGraph();
    }

    /**
     * Create graph using data from test-graph-cities-1.csv and
     * test-graph-transport-1.csv
     */
    public void createGraph3() {
        TravelController controller3 = new TravelController();
        this.localController3 = new TravelController();
        controller3.load("data/test-graph-cities-1.csv",
                "data/test-graph-transport-1.csv");
        this.localController3.load("data/test-graph-cities-1.csv",
                "data/test-graph-transport-1.csv");
        this.graph3 = controller3.getGraph();
    }

    /**
     * Create graph using data from test-graph-cities-2.csv and
     * test-graph-transport-2.csv
     */
    public void createGraph4() {
        TravelController controller4 = new TravelController();
        this.localController4 = new TravelController();
        controller4.load("data/test-graph-cities-2.csv",
                "data/test-graph-transport-2.csv");
        this.localController4.load("data/test-graph-cities-2.csv",
                "data/test-graph-transport-2.csv");
        this.graph4 = controller4.getGraph();
    }

    /**
     * Create graph using data from test-graph-cities-3.csv and
     * test-graph-transport-3.csv
     */
    public void createGraph5() {
        TravelController controller5 = new TravelController();
        this.localController5 = new TravelController();
        controller5.load("data/test-graph-cities-3.csv",
                "data/test-graph-transport-3.csv");
        this.localController5.load("data/test-graph-cities-3.csv",
                "data/test-graph-transport-3.csv");
        this.graph5 = controller5.getGraph();
    }

    /**
     * Test the simple classes
     */
    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    /**
     * Test BFS using data from cities1.csv and transport1.csv
     */
    @Test
    public void testGraph1() {
        this.createGraph1();
        BFS<City, Transport> bfs = new BFS<>();

        /*
          New York City to Providence: Should be a list of a single edge;
          the shortest path is the bus ($40, 220 min). This test ensures that
          paths from Boston to Providence do not override the most direct path.
         */
        List<Transport> path = bfs.getPath(this.graph1,
                this.graph1.getCity("New York City"),
                this.graph1.getCity("Providence"));
        Set<Transport> correctEdges = new HashSet<>();
        for (Transport transport : this.graph1.getOutgoingEdges(
                this.graph1.getCity("New York City"))) {
            if (this.graph1.getEdgeTarget(transport).equals(
                    this.graph1.getCity("Providence"))) {
                correctEdges.add(transport);
            }
        }
        assertTrue(correctEdges.contains(path.get(0)));
        assertEquals(TravelGraph.getTotalCost(path), 40.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 225.0, DELTA);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> localPath = this.localController1.mostDirectRoute("New York City", "Providence");
        assertEquals(localPath.toString(), path.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(localPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(localPath), DELTA);
        /*
          Boston to New York City: There is only one correct path:
          Plane ($267, 50 min). Checks correct output.
         */
        path = bfs.getPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("New York City"));
        correctEdges = this.graph1.getOutgoingEdges(
                this.graph1.getCity("Boston"));
        assertTrue(correctEdges.contains(path.get(0)));
        assertEquals(TravelGraph.getTotalCost(path), 267.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 50.0, DELTA);
        assertEquals(path.size(), 1);
        /*
          Boston to Providence: There are two possible correct edges here.
          Train or bus. Since getOutgoingEdges() provides a Set<Edge>,
          which is an unordered collection, the expected path is just the edge
          arbitrarily selected by Java. The path from Boston -> New York City
          -> Providence is not correct.
         */
        path = bfs.getPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("Providence"));
        correctEdges = this.graph1.getOutgoingEdges(
                this.graph1.getCity("Boston"));
        assertTrue(correctEdges.contains(path.get(0)));
        assertTrue(TravelGraph.getTotalCost(path) == 13.0 ||
                TravelGraph.getTotalCost(path) == 7.0);
        assertTrue(TravelGraph.getTotalTime(path) == 150.0 ||
                TravelGraph.getTotalTime(path) == 80.0);
        assertEquals(path.size(), 1);
        /*
          Providence to Boston: Check that bidirectional edges generate the same
          path regardless if source and target are switched.
         */
        path = bfs.getPath(this.graph1, this.graph1.getCity("Providence"),
                this.graph1.getCity("Boston"));
        correctEdges = this.graph1.getOutgoingEdges(
                this.graph1.getCity("Providence"));
        assertTrue(correctEdges.contains(path.get(0)));
        assertTrue(TravelGraph.getTotalCost(path) == 13.0 ||
                TravelGraph.getTotalCost(path) == 7.0);
        assertTrue(TravelGraph.getTotalTime(path) == 150.0 ||
                TravelGraph.getTotalTime(path) == 80.0);
        assertEquals(path.size(), 1);
        /*
          Providence to New York City: All the previous tests have a path of
          size 1. This path has size 2.
         */
        path = bfs.getPath(this.graph1, this.graph1.getCity("Providence"),
                this.graph1.getCity("New York City"));
        Set<Transport> correctEdgesProvidence = this.graph1.getOutgoingEdges(
                this.graph1.getCity("Providence"));
        assertTrue(correctEdgesProvidence.contains(path.get(0)));
        Set<Transport> correctEdgesBoston = this.graph1.getOutgoingEdges(
                this.graph1.getCity("Boston"));
        assertTrue(correctEdgesBoston.contains(path.get(1)));
        assertTrue(TravelGraph.getTotalCost(path) == 280.0 ||
                TravelGraph.getTotalCost(path) == 274.0);
        assertTrue(TravelGraph.getTotalTime(path) == 200.0 ||
                TravelGraph.getTotalTime(path) == 130.0);
        assertEquals(path.size(), 2);
    }

    /**
     * Test BFS using data from cities2.csv and transport2.csv
     */
    @Test
    public void testGraph2() {
        this.createGraph2();
        BFS<City, Transport> bfs = new BFS<>();
        /*
          Boston to Durham: Should be a single edge: (train, $3, 1 min)
         */
        List<Transport> path = bfs.getPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Durham"));
        Set<Transport> correctEdges = this.graph2.getOutgoingEdges(
                this.graph2.getCity("Boston"));
        assertTrue(correctEdges.contains(path.get(0)));
        assertEquals(TravelGraph.getTotalCost(path), 3.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);
        /*
          Durham to Boston: Not symmetrical! There does not exist a path from
          Durham to Boston; should be empty list.
         */
        path = bfs.getPath(this.graph2,
                this.graph2.getCity("Durham"),
                this.graph2.getCity("Boston"));
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        /*
          Boston to Chicago: Two correct paths: Boston -> Washington -> Chicago
          or Boston -> Durham -> Chicago. The path is determined by which vertex
          (Washington or Durham) is evaluated in BFS first.
         */
        path = bfs.getPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Chicago"));
        // there are two valid paths from Boston to Chicago:
        // Bos -> Durham -> Chicago
        List<Transport> validPath1 = new LinkedList<>();
        // Bos -> Washington -> Chicago
        List<Transport> validPath2 = new LinkedList<>();
        for (Transport transport :
                this.graph2.getCity("Boston").getOutgoing()) {
            if (this.graph2.getEdgeTarget(transport).equals(
                    this.graph2.getCity("Durham"))) {
                validPath1.add(transport);
            } else if (this.graph2.getEdgeTarget(transport).equals(
                    this.graph2.getCity("Washington"))) {
                validPath2.add(transport);
            }
        }
        for (Transport transport : this.graph2.getCity("Durham").getOutgoing()) {
            if (this.graph2.getEdgeTarget(transport).equals(
                    this.graph2.getCity("Chicago")))
                validPath1.add(transport);
        }
        validPath2.addAll(this.graph2.getCity("Washington").getOutgoing());
        // path just needs to be either one
        assertTrue(path.equals(validPath1) || path.equals(validPath2));
        assertTrue(correctEdges.contains(path.get(0)));
        assertTrue(TravelGraph.getTotalCost(path) == 5.0 ||
                TravelGraph.getTotalCost(path) == 6.0);
        assertEquals(2.0, TravelGraph.getTotalTime(path), DELTA);
        assertEquals(path.size(), 2);
        // Using TravelController methods:
        List<Transport> localPath = this.localController2.mostDirectRoute("Boston", "Chicago");
        assertEquals(localPath.toString(), path.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(localPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(localPath), DELTA);
    }

    /**
     * Test BFS using data from test-graph-cities-1.csv and
     * test-graph-transport-1.csv
     */
    @Test
    public void testGraph3() {
        this.createGraph3();
        BFS<City, Transport> bfs = new BFS<>();
        /*
          Seattle to Boston: No path; empty list
         */
        List<Transport> path = bfs.getPath(this.graph3,
                this.graph3.getCity("Seattle"),
                this.graph3.getCity("Boston"));
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> localPath = this.localController3.mostDirectRoute("Seattle", "Boston");
        assertEquals(localPath.toString(), path.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(localPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(localPath), DELTA);
        /*
          Boston to Boston: reflexivity; empty list
         */
        path = bfs.getPath(this.graph3,
                this.graph3.getCity("Boston"),
                this.graph3.getCity("Boston"));
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        /*
          Miami to Houston and Houston to Miami: Symmetrical, both paths should
          have the same edge
         */
        path = bfs.getPath(this.graph3,
                this.graph3.getCity("Miami"),
                this.graph3.getCity("Houston"));
        List<Transport> validPath = new LinkedList<>();
        for (Transport transport : this.graph3.getCity("Miami").getOutgoing()) {
            if (this.graph3.getEdgeTarget(transport).equals(
                    this.graph3.getCity("Houston"))) {
                validPath.add(transport);
            }
        }
        assertEquals(validPath, path);
        assertEquals(TravelGraph.getTotalCost(path), 10.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 3.0, DELTA);
        assertEquals(path.size(), 1);
        path = bfs.getPath(this.graph3,
                this.graph3.getCity("Houston"),
                this.graph3.getCity("Miami"));
        validPath = new LinkedList<>();
        for (Transport transport :
                this.graph3.getCity("Houston").getOutgoing()) {
            if (this.graph3.getEdgeTarget(transport).equals(
                    this.graph3.getCity("Miami"))) {
                validPath.add(transport);
            }
        }
        assertEquals(validPath, path);
        assertEquals(TravelGraph.getTotalCost(path), 10.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 3.0, DELTA);
        assertEquals(path.size(), 1);
    }

    /**
     * Test BFS using data from test-graph-cities-2.csv and
     * test-graph-transport-2.csv
     */
    @Test
    public void testGraph4() {
        this.createGraph4();
        BFS<City, Transport> bfs = new BFS<>();
        /*
          Ithaca to Albany: two identical modes of transportation; either is
          correct
         */
        List<Transport> path = bfs.getPath(this.graph4,
                this.graph4.getCity("Ithaca"),
                this.graph4.getCity("Albany"));
        Set<Transport> correctEdges = this.graph4.getOutgoingEdges(
                this.graph4.getCity("Ithaca"));
        assertEquals(1.0, TravelGraph.getTotalCost(path), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);
        /*
          Albany to Rochester: more than two edges, two identical; correct edge
          doesn't matter
         */
        path = bfs.getPath(this.graph4,
                this.graph4.getCity("Albany"),
                this.graph4.getCity("Rochester"));
        correctEdges = this.graph4.getOutgoingEdges(
                this.graph4.getCity("Albany"));
        assertTrue(correctEdges.contains(path.get(0)));
        assertTrue(TravelGraph.getTotalCost(path) == 10.0 ||
                TravelGraph.getTotalCost(path) == 2.0);
        assertTrue(TravelGraph.getTotalTime(path) == 10.0 ||
                TravelGraph.getTotalTime(path) == 3.0);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> localPath = this.localController4.mostDirectRoute("Albany", "Rochester");
        assertEquals(localPath.toString(), path.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(localPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(localPath), DELTA);
        /*
          Buffalo to Potsdam: long path (3 edges) with several potential detours;
          note that there are multiple correct paths. BFS finds train -> bus ->
          train
         */
        path = bfs.getPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Potsdam"));
        Set<Transport> correctEdgeBuffalo = new HashSet<>();
        for (Transport transport : this.graph4.getOutgoingEdges(
                this.graph4.getCity("Buffalo"))) {
            if (transport.getTarget().equals(this.graph4.getCity("Albany"))) {
                correctEdgeBuffalo.add(transport);
            }
        }
        Set<Transport> correctEdgesAlbany = this.graph4.getOutgoingEdges(
                this.graph4.getCity("Albany"));
        Set<Transport> correctEdgeRochester = this.graph4.getOutgoingEdges(
                this.graph4.getCity("Rochester"));
        assertTrue(correctEdgeBuffalo.contains(path.get(0)));
        assertTrue(correctEdgesAlbany.contains(path.get(1)));
        assertTrue(correctEdgeRochester.contains(path.get(2)));
        assertTrue(TravelGraph.getTotalCost(path) == 558.0 ||
                TravelGraph.getTotalCost(path) == 550.0);
        assertTrue(TravelGraph.getTotalTime(path) == 533.0 ||
                TravelGraph.getTotalTime(path) == 526.0);
        assertEquals(path.size(), 3);
        /*
            Buffalo to Buffalo: reflexivity again; this time there is a path to
            Buffalo that is through the other vertices
         */
        path = bfs.getPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Buffalo"));
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
    }

    /**
     * Test BFS using data from test-graph-cities-3.csv and
     * test-graph-transport-3.csv
     */
    @Test
    public void testGraph5() {
        this.createGraph5();
        BFS<City, Transport> bfs = new BFS<>();
        /*
          Rome to rome: single node with no edges should just return empty list
         */
        List<Transport> path = bfs.getPath(this.graph5,
                this.graph5.getCity("Rome"),
                this.graph5.getCity("Rome"));
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> localPath = this.localController5.mostDirectRoute("Rome", "Rome");
        assertEquals(localPath.toString(), path.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(localPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(localPath), DELTA);
    }
}
