package sol;

import src.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A class used to connect the view (user facing) and model (data organization).
 * Loads data into program and calls the path-finding algorithms
 */
public class TravelController implements ITravelController<City, Transport> {

    private TravelGraph graph;

    public TravelController() {
    }

    /**
     * @param citiesFile    the filename of the cities csv
     * @param transportFile the filename of the transportations csv
     * @return A success message when the two files are successfully loaded,
     * an error message with the corresponding file that failed to load.
     */
    @Override
    public String load(String citiesFile, String transportFile) {
        this.graph = new TravelGraph();
        TravelCSVParser parser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null; // need explicit return null to account for Void type
        };

        // Note: This function assumes that city names already exist in the graph
        Function<Map<String, String>, Void> addEdge = map -> {
            City source = this.graph.getCity(map.get("origin"));
            City destination = this.graph.getCity(map.get("destination"));
            Transport outgoing = new Transport(
                    source,
                    destination,
                    TransportType.fromString(map.get("type")),
                    Double.parseDouble(map.get("price")),
                    Double.parseDouble(map.get("duration"))
            );
            this.graph.addEdge(source, outgoing);
            return null;
        };

        try {
            // pass in string for CSV and function to create City (vertex)
            // using city name
            parser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }

        try {
            parser.parseTransportation(transportFile, addEdge);
        } catch (IOException e) {
            return "Error parsing file: " + transportFile;
        }

        return "Successfully loaded cities and transportation files.";
    }

    // Function object that selects price field from Transport object
    Function<Transport, Double> getPrice = Transport::getPrice;
    // Function object that selects minutes field from Transport object
    Function<Transport, Double> getTime = Transport::getMinutes;

    /**
     * Determine the fastest route the source vertex to the destination vertex
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list of Transport connecting source and destination if it
     * exists
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        City sourceCity = this.graph.getCity(source);
        City destinationCity = this.graph.getCity(destination);
        return new Dijkstra<City, Transport>().getShortestPath(this.graph,
                sourceCity, destinationCity, this.getTime);
    }

    /**
     * Determine the cheapest route the source vertex to the destination vertex
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list of Transport connecting source and destination if it
     * exists
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        City sourceCity = this.graph.getCity(source);
        City destinationCity = this.graph.getCity(destination);
        return new Dijkstra<City, Transport>().getShortestPath(this.graph,
                sourceCity, destinationCity, this.getPrice);
    }

    /**
     * Determine the most direct route the source vertex to the destination
     * vertex
     *
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return the list of Transport connecting source and destination if it
     * exists
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        City sourceCity = this.graph.getCity(source);
        City destinationCity = this.graph.getCity(destination);
        return new BFS<City, Transport>().getPath(this.graph, sourceCity,
                destinationCity);
    }

    /**
     * Getter method for the graph
     *
     * @return The travel graph
     */
    public TravelGraph getGraph() {
        return this.graph;
    }
}
