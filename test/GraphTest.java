package test;

import src.Transport;
import org.junit.Assert;
import org.junit.Test;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more
 * tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you
 * will not be graded on those.
 *
 */
public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private SimpleEdge edgeAB;
    private SimpleEdge edgeBC;
    private SimpleEdge edgeCA;
    private SimpleEdge edgeAC;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require
     * a different setup,
     * we manually call the setup method at the top of the test.
     *
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    /**
     * Test the getVertices method
     */
    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        // test getVertices to check this method AND insertVertex
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    /**
     * Create a set of Strings representing each City from the input
     *
     * @param set the Set of Cities to be converted to Strings
     * @return a Set of Strings representing each City from the input
     */
    public HashSet<String> setToString(Set<City> set) {
        HashSet<String> newSet = new HashSet<>();
        for (City item : set) {
            newSet.add(item.toString());
        }
        return newSet;
    }

    private TravelGraph testGraph1;

    private City Boston;
    private City Providence;
    private City NYC;
    private City Washington;
    private City Chicago;
    private City Miami;

    private Transport BtoPBus;
    private Transport PtoBBus;
    private Transport BtoPTrain;
    private Transport PtoBTrain;
    private Transport BtoNPlane;
    private Transport NtoBPlane;
    private Transport NtoPBus;
    private Transport NtoPTrain;
    private Transport WtoBPlane;
    private Transport BtoWPlane;
    private Transport CtoNBus;

    private HashSet<String> citiesInGraph1;
    private HashSet<String> newCitiesInGraph1;
    private HashSet<Transport> transportFromNYC;
    private HashSet<Transport> transportFromBoston;
    private HashSet<Transport> transportFromProvidence;
    private HashSet<Transport> newTransportFromBoston;
    private HashSet<Transport> transportFromChicago;
    private HashSet<Transport> transportFromWashington;

    /**
     * Method to setup test data
     */
    public void testSetup() {
        TravelController travelController1 = new TravelController();
        travelController1.load("data/cities1.csv", "data/transport1.csv");
        this.testGraph1 = travelController1.getGraph();

        this.NYC = new City("New York City");
        this.Boston = new City("Boston");
        this.Providence = new City("Providence");

        this.BtoPTrain = new Transport(this.Boston, this.Providence,
                TransportType.TRAIN, 13, 80);
        this.BtoPBus = new Transport(this.Boston, this.Providence,
                TransportType.BUS, 7, 150);
        this.PtoBTrain = new Transport(this.Providence, this.Boston,
                TransportType.TRAIN, 13, 80);
        this.PtoBBus = new Transport(this.Providence, this.Boston,
                TransportType.BUS, 7, 150);
        this.BtoNPlane = new Transport(this.Boston, this.NYC,
                TransportType.PLANE, 267, 50);
        this.NtoBPlane = new Transport(this.NYC, this.Boston,
                TransportType.PLANE, 267, 50);
        this.NtoPBus = new Transport(this.NYC, this.Providence,
                TransportType.BUS, 40, 225);

        this.Providence.addOut(this.PtoBTrain);
        this.Providence.addOut(this.PtoBBus);
        this.NYC.addOut(this.NtoBPlane);
        this.NYC.addOut(this.NtoPBus);
        this.NYC.addOut(this.NtoPTrain);
        this.Boston.addOut(this.BtoPTrain);
        this.Boston.addOut(this.BtoPBus);
        this.Boston.addOut(this.BtoNPlane);
        
        this.citiesInGraph1 = new HashSet<>();
        this.citiesInGraph1.add(this.Providence.toString());
        this.citiesInGraph1.add(this.Boston.toString());
        this.citiesInGraph1.add(this.NYC.toString());

        this.transportFromNYC = new HashSet<>();
        this.transportFromNYC.add(this.NtoBPlane);
        this.transportFromNYC.add(this.NtoPBus);

        this.transportFromBoston = new HashSet<>();
        this.transportFromBoston.add(this.BtoNPlane);
        this.transportFromBoston.add(this.BtoPTrain);
        this.transportFromBoston.add(this.BtoPBus);

        this.transportFromProvidence = new HashSet<>();
        this.transportFromProvidence.add(this.PtoBTrain);
        this.transportFromProvidence.add(this.PtoBBus);
    }

    /**
     * Testing the methods on a graph created from a csv file
     */
    @Test
    public void test1() {
        this.testSetup();

        //test getVertices
        Assert.assertEquals(this.setToString(this.testGraph1.getVertices()),
                this.citiesInGraph1);

        //test getEdgeSource
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.BtoPTrain),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.BtoPBus),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.PtoBTrain),
                this.Providence);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.PtoBBus),
                this.Providence);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.BtoNPlane),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.NtoBPlane),
                this.NYC);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.NtoPBus),
                this.NYC);

        //test getEdgeTarget
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.BtoPTrain),
                this.Providence);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.BtoPBus),
                this.Providence);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.PtoBTrain),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.PtoBBus),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.BtoNPlane),
                this.NYC);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.NtoBPlane),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.NtoPBus),
                this.Providence);

        //test getOutgoingEdges
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Boston),
                this.transportFromBoston);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Providence),
                this.transportFromProvidence);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.NYC),
                this.transportFromNYC);

        //test getCity
        Assert.assertEquals(this.testGraph1.getCity("New York City").toString(),
                "New York City");
        Assert.assertEquals(this.testGraph1.getCity("Providence").toString(),
                "Providence");
        Assert.assertEquals(this.testGraph1.getCity("Boston").toString(),
                "Boston");
    }

    /**
     * Method to add data for testing
     */
    public void addStuff() {
        this.Washington = new City("Washington");
        this.Chicago = new City("Chicago");
        this.Miami = new City("Miami");

        this.WtoBPlane = new Transport(this.Washington, this.Boston,
                TransportType.PLANE, 90, 120);
        this.BtoWPlane = new Transport(this.Boston, this.Washington,
                TransportType.PLANE, 90, 120);
        this.CtoNBus = new Transport(this.Chicago, this.NYC,
                TransportType.BUS, 20, 600);

        this.testGraph1.addVertex(this.Washington);
        this.testGraph1.addVertex(this.Chicago);
        this.testGraph1.addVertex(this.Miami);

        this.testGraph1.addEdge(this.Washington, this.WtoBPlane);
        this.testGraph1.addEdge(this.Boston, this.BtoWPlane);
        this.testGraph1.addEdge(this.Chicago, this.CtoNBus);

        this.Boston.addOut(this.BtoWPlane);
        this.Washington.addOut(this.WtoBPlane);
        this.Chicago.addOut(this.CtoNBus);

        this.newCitiesInGraph1 = new HashSet<>();
        this.newCitiesInGraph1.add(this.Providence.toString());
        this.newCitiesInGraph1.add(this.Boston.toString());
        this.newCitiesInGraph1.add(this.NYC.toString());
        this.newCitiesInGraph1.add(this.Washington.toString());
        this.newCitiesInGraph1.add(this.Chicago.toString());
        this.newCitiesInGraph1.add(this.Miami.toString());

        this.newTransportFromBoston = new HashSet<>();
        this.newTransportFromBoston.add(this.BtoNPlane);
        this.newTransportFromBoston.add(this.BtoPTrain);
        this.newTransportFromBoston.add(this.BtoPBus);
        this.newTransportFromBoston.add(this.BtoWPlane);

        this.transportFromChicago = new HashSet<>();
        this.transportFromChicago.add(this.CtoNBus);

        this.transportFromWashington = new HashSet<>();
        this.transportFromWashington.add(this.WtoBPlane);
    }

    /**
     * Testing the methods with manually added cities and transports with
     * addVertex and addEdge
     */
    @Test
    public void test2() {
        this.testSetup();
        this.addStuff();

        //test getVertices
        Assert.assertEquals(this.setToString(this.testGraph1.getVertices()),
                this.newCitiesInGraph1);

        //test getEdgeSource
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.WtoBPlane),
                this.Washington);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.BtoWPlane),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeSource(this.CtoNBus),
                this.Chicago);

        //test getEdgeTarget
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.WtoBPlane),
                this.Boston);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.BtoWPlane),
                this.Washington);
        Assert.assertEquals(this.testGraph1.getEdgeTarget(this.CtoNBus),
                this.NYC);

        //test getOutgoingEdges
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Boston),
                this.newTransportFromBoston);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Providence),
                this.transportFromProvidence);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.NYC),
                this.transportFromNYC);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Washington),
                this.transportFromWashington);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Chicago),
                this.transportFromChicago);
        Assert.assertEquals(this.testGraph1.getOutgoingEdges(this.Miami),
                new HashSet<>());

        //test getCity
        Assert.assertEquals(this.testGraph1.getCity("New York City").toString(),
                "New York City");
        Assert.assertEquals(this.testGraph1.getCity("Providence").toString(),
                "Providence");
        Assert.assertEquals(this.testGraph1.getCity("Boston").toString(),
                "Boston");
        Assert.assertEquals(this.testGraph1.getCity("Washington").toString(),
                "Washington");
        Assert.assertEquals(this.testGraph1.getCity("Chicago").toString(),
                "Chicago");
        Assert.assertEquals(this.testGraph1.getCity("Miami").toString(),
                "Miami");
    }
}
