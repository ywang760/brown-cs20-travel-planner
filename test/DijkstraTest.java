package test;

import org.junit.Test;
import sol.BFS;
import sol.Dijkstra;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.IDijkstra;
import src.Transport;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly,
 * but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will
 * not be graded on those.
 *
 */
public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

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
     *
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    /**
     * Create graph using data from cities1.csv and transport1.csv
     */
    public void createGraph1() {
        TravelController controller1 = new TravelController();
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
     * Create graph using data from test-graph-cities-3.csv and test-graph-transport-3.csv
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
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
            dijkstra.getShortestPath(this.graph, this.a, this.b,
                    edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b,
                edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }


    /**
     * Test Dijkstra using data from cities1.csv and transport1.csv
     */
    @Test
    public void testGraph1() {
        this.createGraph1();
        Dijkstra<City, Transport> dij = new Dijkstra<>();

        /*
          New York City to Providence: The cheapest price should be the direct
          bus ($40, 220 min). This test ensures that paths from Boston to
          Providence do not override the most direct path.
         */
        List<Transport> path = dij.getShortestPath(this.graph1,
                this.graph1.getCity("New York City"),
                this.graph1.getCity("Providence"), Transport::getPrice);
        String cheapestPath = "[New York City -> Providence, Type: bus, " +
                "Cost: $40.0, Duration: 225.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 40.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 225.0, DELTA);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> cheapestLocalPath = this.localController1.cheapestRoute(
                "New York City", "Providence");
        assertEquals(cheapestLocalPath.toString(), cheapestLocalPath.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                cheapestLocalPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                cheapestLocalPath), DELTA);

        /*
          New York City to Providence: The least time-consuming path should be
          a plane to Boston first and then a train ride (130min total).
          This test ensures that the direct bus ride, which takes 220 minutes,
          is not selected.
         */
        path = dij.getShortestPath(this.graph1,
                this.graph1.getCity("New York City"),
                this.graph1.getCity("Providence"), Transport::getMinutes);
        String fastestPath = "[New York City -> Boston, Type: plane, " +
                "Cost: $267.0, Duration: 50.0 minutes, Boston -> Providence, " +
                "Type: train, Cost: $13.0, Duration: 80.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 280.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 130.0, DELTA);
        assertEquals(path.size(), 2);
        // Using TravelController methods:
        List<Transport> fastestLocalPath = this.localController1.fastestRoute(
                "New York City", "Providence");
        assertEquals(path.toString(), fastestLocalPath.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                fastestLocalPath), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                fastestLocalPath), DELTA);

        /*
          Boston to New York City: There is only one correct path:
          Plane ($267, 50 min). The cheapest path is this path.
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("New York City"), Transport::getPrice);
        cheapestPath = "[Boston -> New York City, Type: plane," +
                " Cost: $267.0, Duration: 50.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 267.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 50.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Boston to New York City: There is only one correct path:
          Plane ($267, 50 min). The least time-consuming path is this path.
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("New York City"), Transport::getMinutes);
        fastestPath = "[Boston -> New York City, Type: plane, " +
                "Cost: $267.0, Duration: 50.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 267.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 50.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Boston to Providence: The cheapest route is bus ($7, 150min).
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("Providence"), Transport::getPrice);
        cheapestPath = "[Boston -> Providence, Type: bus, Cost: $7.0, " +
                "Duration: 150.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 7.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 150.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Boston to Providence: The least time-consuming route is train
          ($13, 80min).
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Boston"),
                this.graph1.getCity("Providence"), Transport::getMinutes);
        fastestPath = "[Boston -> Providence, Type: train, Cost: $13.0, " +
                "Duration: 80.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 13.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 80.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Providence to Boston: The cheapest route is bus ($7, 150min).
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Providence"),
                this.graph1.getCity("Boston"), Transport::getPrice);
        cheapestPath = "[Providence -> Boston, Type: bus, Cost: $7.0, " +
                "Duration: 150.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 7.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 150.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Providence to Boston: The least time-consuming route is train
          ($13, 80min).
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Providence"),
                this.graph1.getCity("Boston"), Transport::getMinutes);
        fastestPath = "[Providence -> Boston, Type: train, Cost: $13.0, " +
                "Duration: 80.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 13.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 80.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Providence to New York City: The cheapest route is bus to Boston ($7)
          and plane to New York City ($267).
         */
        path = dij.getShortestPath(this.graph1,
                this.graph1.getCity("Providence"),
                this.graph1.getCity("New York City"), Transport::getPrice);
        cheapestPath = "[Providence -> Boston, Type: bus, Cost: $7.0, " +
                "Duration: 150.0 minutes, Boston -> New York City, " +
                "Type: plane, Cost: $267.0, Duration: 50.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 274.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 200.0, DELTA);
        assertEquals(path.size(), 2);

        /*
          Providence to New York City: The least time-consuming route is train
          to Boston (80min) and plane to New York City (50min).
         */
        path = dij.getShortestPath(this.graph1, this.graph1.getCity("Providence"),
                this.graph1.getCity("New York City"), Transport::getMinutes);
        fastestPath = "[Providence -> Boston, Type: train, Cost: $13.0, " +
                "Duration: 80.0 minutes, Boston -> New York City, Type: plane," +
                " Cost: $267.0, Duration: 50.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 280.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 130.0, DELTA);
        assertEquals(path.size(), 2);
    }

    /**
     * Test BFS using data from cities2.csv and transport2.csv
     */
    @Test
    public void testGraph2() {
        this.createGraph2();
        Dijkstra<City, Transport> dij = new Dijkstra<>();

        /*
          Boston to Providence: The cheapest is train to Durham, Chicago,
          then Providence (total price $7).
         */
        List<Transport> path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Providence"), Transport::getPrice);
        String cheapestPath = "[Boston -> Durham, Type: train, Cost: $3.0, " +
                "Duration: 1.0 minutes, Durham -> Chicago, Type: train, " +
                "Cost: $2.0, Duration: 1.0 minutes, Chicago -> Providence, " +
                "Type: train, Cost: $1.0, Duration: 1.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 6.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 3.0, DELTA);
        assertEquals(path.size(), 3);
        // Using TravelController methods:
        List<Transport> cheapestRoute = this.localController2.cheapestRoute(
                "Boston", "Providence");
        assertEquals(path.toString(), cheapestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                cheapestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                cheapestRoute), DELTA);

       /*
          Boston to Providence: The fastest path is direct train ($100, 1min).
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Providence"), Transport::getMinutes);
        String fastestPath = "[Boston -> Providence, Type: train, " +
                "Cost: $100.0, Duration: 1.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 100.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> fastestRoute = this.localController2.fastestRoute(
                "Boston", "Providence");
        assertEquals(path.toString(), fastestPath.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                fastestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                fastestRoute), DELTA);

        /*
          Boston to Durham: Should be a single edge: (train, $3, 1 min)
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Durham"), Transport::getPrice);
        cheapestPath = "[Boston -> Durham, Type: train, Cost: $3.0, " +
                "Duration: 1.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 3.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);

       /*
          Boston to Durham: Should be a single edge: (train, $3, 1 min)
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Durham"), Transport::getMinutes);
        fastestPath = "[Boston -> Durham, Type: train, Cost: $3.0, " +
                "Duration: 1.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 3.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Boston to Chicago: The cheapest path is train via Durham.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Chicago"), Transport::getPrice);
        cheapestPath = "[Boston -> Durham, Type: train, Cost: $3.0, " +
                "Duration: 1.0 minutes, Durham -> Chicago, Type: train, " +
                "Cost: $2.0, Duration: 1.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 5.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 2.0, DELTA);
        assertEquals(path.size(), 2);

        /*
          Boston to Chicago: Train via Durham and via Washington both cost
          2min, but since Durham is evaluated first, it takes the path via
          Durham.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Boston"),
                this.graph2.getCity("Chicago"), Transport::getMinutes);
        fastestPath = "[Boston -> Durham, Type: train, Cost: $3.0," +
                " Duration: 1.0 minutes, Durham -> Chicago, Type: train, " +
                "Cost: $2.0, Duration: 1.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 5.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 2.0, DELTA);
        assertEquals(path.size(), 2);

       /*
          Providence to Boston: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Providence"),
                this.graph2.getCity("Boston"), Transport::getPrice);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);

        /*
          Providence to Boston: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Providence"),
                this.graph2.getCity("Boston"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);

        /*
          Durham to Boston: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Durham"),
                this.graph2.getCity("Boston"), Transport::getPrice);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);

        /*
          Durham to Boston: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Durham"),
                this.graph2.getCity("Boston"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);

        /*
          Durham to Washington: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Durham"),
                this.graph2.getCity("Washington"), Transport::getPrice);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);

        /*
          Durham to Washington: No cheapest or fastest path, should return
          empty list.
         */
        path = dij.getShortestPath(this.graph2,
                this.graph2.getCity("Durham"),
                this.graph2.getCity("Washington"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
    }

    /**
     * Test BFS using data from test-graph-cities-1.csv and
     * test-graph-transport-1.csv
     */
    @Test
    public void testGraph3() {
        this.createGraph3();
        Dijkstra<City, Transport> dij = new Dijkstra<>();

        /*
          Boston to Boston: reflexivity; empty list
         */
        List<Transport> path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Boston"),
                this.graph3.getCity("Boston"), Transport::getPrice);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> cheapestRoute = this.localController3.cheapestRoute(
                "Boston", "Boston");
        assertEquals(path.toString(), cheapestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                cheapestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                cheapestRoute), DELTA);

        /*
          Boston to Boston: reflexivity; empty list
         */
        path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Boston"),
                this.graph3.getCity("Boston"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> fastestRoute = this.localController3.fastestRoute(
                "Boston", "Boston");
        assertEquals(path.toString(), fastestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                fastestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                fastestRoute), DELTA);

        /*
           Seattle to Miami and Miami to Seattle: Fastest route is direct bus
           (12min), while the cheapest route is via Houston. Symmetrical paths.
         */
        path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Miami"),
                this.graph3.getCity("Seattle"), Transport::getPrice);
        String cheapestPath = "[Miami -> Houston, Type: bus, Cost: $10.0," +
                " Duration: 3.0 minutes, Houston -> Seattle, Type: train," +
                " Cost: $5.0, Duration: 10.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 15.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 13.0, DELTA);
        assertEquals(path.size(), 2);
        path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Miami"),
                this.graph3.getCity("Seattle"), Transport::getMinutes);
        String fastestPath = "[Miami -> Seattle, Type: bus, Cost: $20.0," +
                " Duration: 12.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 20.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 12.0, DELTA);
        assertEquals(path.size(), 1);
        path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Seattle"),
                this.graph3.getCity("Miami"), Transport::getPrice);
        cheapestPath = "[Seattle -> Houston, Type: train, Cost: $5.0," +
                " Duration: 10.0 minutes, Houston -> Miami, Type: bus," +
                " Cost: $10.0, Duration: 3.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 15.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 13.0, DELTA);
        assertEquals(path.size(), 2);
        path = dij.getShortestPath(this.graph3,
                this.graph3.getCity("Seattle"),
                this.graph3.getCity("Miami"), Transport::getMinutes);
        fastestPath = "[Seattle -> Miami, Type: bus, Cost: $20.0, " +
                "Duration: 12.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 20.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 12.0, DELTA);
        assertEquals(path.size(), 1);
    }

    /**
     * Test BFS using data from test-graph-cities-2.csv and
     * test-graph-transport-2.csv
     */
    @Test
    public void testGraph4() {
        this.createGraph4();
        Dijkstra<City, Transport> dij = new Dijkstra<>();
        /*
          Ithaca to Albany: two identical modes of transportation; either is
          correct
         */
        List<Transport> path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Ithaca"),
                this.graph4.getCity("Albany"), Transport::getPrice);
        String cheapestPath1 = "[Ithaca -> Albany, Type: train, Cost: $1.0," +
                " Duration: 1.0 minutes]";
        String cheapestPath2 = "[Ithaca -> Albany, Type: bus, Cost: $1.0, " +
                "Duration: 1.0 minutes]";
        assertTrue(cheapestPath1.equals(path.toString()) ||
                cheapestPath2.equals(path.toString()));
        assertEquals(TravelGraph.getTotalCost(path), 1.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Ithaca"),
                this.graph4.getCity("Albany"), Transport::getMinutes);
        String fastestPath1 = "[Ithaca -> Albany, Type: train, Cost: $1.0," +
                " Duration: 1.0 minutes]";
        String fastestPath2 = "[Ithaca -> Albany, Type: bus, Cost: $1.0," +
                " Duration: 1.0 minutes]";
        assertTrue(fastestPath1.equals(path.toString()) ||
                fastestPath2.equals(path.toString()));
        assertEquals(TravelGraph.getTotalCost(path), 1.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 1.0, DELTA);
        assertEquals(path.size(), 1);

        /*
          Albany to Rochester: cheapest and fastest by plane
         */
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Albany"),
                this.graph4.getCity("Rochester"), Transport::getPrice);
        String cheapestPath = "[Albany -> Rochester, Type: plane, Cost: $2.0," +
                " Duration: 3.0 minutes]";
        assertEquals(cheapestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 2.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 3.0, DELTA);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> cheapestRoute = this.localController4.cheapestRoute(
                "Albany", "Rochester");
        assertEquals(path.toString(), cheapestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                cheapestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                cheapestRoute), DELTA);

        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Albany"),
                this.graph4.getCity("Rochester"), Transport::getMinutes);
        String fastestPath = "[Albany -> Rochester, Type: plane, Cost: $2.0," +
                " Duration: 3.0 minutes]";
        assertEquals(fastestPath, path.toString());
        assertEquals(TravelGraph.getTotalCost(path), 2.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 3.0, DELTA);
        assertEquals(path.size(), 1);
        // Using TravelController methods:
        List<Transport> fastestRoute = this.localController4.fastestRoute(
                "Albany", "Rochester");
        assertEquals(path.toString(), fastestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                fastestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                fastestRoute), DELTA);

        /*
          Buffalo to Potsdam: long path (4 edges) with several potential detours;
          note that there are multiple correct paths.
         */
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Potsdam"), Transport::getPrice);
        cheapestPath1 = "[Buffalo -> Ithaca, Type: bus, Cost: $1.0," +
                " Duration: 1.0 minutes, Ithaca -> Albany, Type: bus, " +
                "Cost: $1.0, Duration: 1.0 minutes, Albany -> Rochester, " +
                "Type: plane, Cost: $2.0, Duration: 3.0 minutes, " +
                "Rochester -> Potsdam, Type: train, Cost: $48.0, " +
                "Duration: 23.0 minutes]";
        cheapestPath2 = "[Buffalo -> Ithaca, Type: bus, Cost: $1.0," +
                " Duration: 1.0 minutes, Ithaca -> Albany, Type: train, " +
                "Cost: $1.0, Duration: 1.0 minutes, Albany -> Rochester, " +
                "Type: plane, Cost: $2.0, Duration: 3.0 minutes, " +
                "Rochester -> Potsdam, Type: train, Cost: $48.0, " +
                "Duration: 23.0 minutes]";
        assertTrue(cheapestPath1.equals(path.toString()) ||
                cheapestPath2.equals(path.toString()));
        assertEquals(TravelGraph.getTotalCost(path), 52.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 28.0, DELTA);
        assertEquals(path.size(), 4);
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Potsdam"), Transport::getMinutes);
        fastestPath1 = "[Buffalo -> Ithaca, Type: bus, Cost: $1.0," +
                " Duration: 1.0 minutes, Ithaca -> Albany, Type: bus," +
                " Cost: $1.0, Duration: 1.0 minutes, Albany -> Rochester," +
                " Type: plane, Cost: $2.0, Duration: 3.0 minutes," +
                " Rochester -> Potsdam, Type: train, Cost: $48.0," +
                " Duration: 23.0 minutes]";
        fastestPath2 = "[Buffalo -> Ithaca, Type: bus, Cost: $1.0," +
                " Duration: 1.0 minutes, Ithaca -> Albany, Type: train," +
                " Cost: $1.0, Duration: 1.0 minutes, Albany -> Rochester," +
                " Type: plane, Cost: $2.0, Duration: 3.0 minutes," +
                " Rochester -> Potsdam, Type: train, Cost: $48.0," +
                " Duration: 23.0 minutes]";
        assertTrue(fastestPath1.equals(path.toString()) || fastestPath2.equals(
                path.toString()));
        assertEquals(TravelGraph.getTotalCost(path), 52.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 28.0, DELTA);
        assertEquals(path.size(), 4);

        /*
            Buffalo to Buffalo: reflexivity again; this time there is a path to
            Buffalo that is through the other vertices
         */
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Buffalo"), Transport::getPrice);
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(path.toString(), "[]");
        assertEquals(path.size(), 0);
        path = dij.getShortestPath(this.graph4,
                this.graph4.getCity("Buffalo"),
                this.graph4.getCity("Buffalo"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
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
        Dijkstra<City, Transport> dij = new Dijkstra<>();
        /*
          Rome to rome: single node with no edges should just return empty list
         */
        List<Transport> path = dij.getShortestPath(this.graph5,
                this.graph5.getCity("Rome"),
                this.graph5.getCity("Rome"), Transport::getMinutes);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> fastestRoute = this.localController5.fastestRoute(
                "Rome", "Rome");
        assertEquals(path.toString(), fastestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                fastestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                fastestRoute), DELTA);
        path = dij.getShortestPath(this.graph5,
                this.graph5.getCity("Rome"),
                this.graph5.getCity("Rome"), Transport::getPrice);
        assertEquals(path.toString(), "[]");
        assertEquals(TravelGraph.getTotalCost(path), 0.0, DELTA);
        assertEquals(TravelGraph.getTotalTime(path), 0.0, DELTA);
        assertEquals(path.size(), 0);
        // Using TravelController methods:
        List<Transport> cheapestRoute = this.localController5.cheapestRoute(
                "Rome", "Rome");
        assertEquals(path.toString(), cheapestRoute.toString());
        assertEquals(TravelGraph.getTotalCost(path), TravelGraph.getTotalCost(
                cheapestRoute), DELTA);
        assertEquals(TravelGraph.getTotalTime(path), TravelGraph.getTotalTime(
                cheapestRoute), DELTA);

    }

}
