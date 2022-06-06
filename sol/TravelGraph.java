package sol;

import src.City;
import src.IGraph;
import src.Transport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * A class used to organize City and Transport data into a graph of vertices
 * and edges
 */
public class TravelGraph implements IGraph<City, Transport> {
    private HashMap<String, City> graph;

    /**
     * Zero-argument constructor for the TravelGraph class
     */
    public TravelGraph() {
        this.graph = new HashMap<String, City>();
    }


    /**
     * Adds a City to the graph
     *
     * @param vertex the City
     */
    @Override
    public void addVertex(City vertex) {
        this.graph.put(vertex.toString(), vertex);
    }

    /**
     * Adds a form of transportation to the provided City
     *
     * @param origin the origin of the edge
     * @param edge   the form of transportation leaving the origin
     */
    public void addEdge(City origin, Transport edge) {
        origin.addOut(edge);
    }

    /**
     * Provides a set of the vertices of the graph
     *
     * @return a set containing every vertex
     */
    @Override
    public Set<City> getVertices() {
        return new HashSet<>(this.graph.values());
    }

    /**
     * Provides the City that an edge originates from to (the source field)
     *
     * @param edge the edge that leads to some City
     * @return the City that the edge comes from
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    /**
     * Provides the City that an edge leads to (the target field)
     *
     * @param edge the edge that leads to some City
     * @return the City that the edge leads to
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    /**
     * Provides the set of edges that leave the provides vertex
     *
     * @param fromVertex the vertex from which the edges leave
     * @return a set of the edges that leave the vertex
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    /**
     * Given a City name, the City object that corresponds to it
     *
     * @param name the title of the City
     * @return the City object corresponding to the City name
     */
    public City getCity(String name) {
        return this.graph.get(name);
    }

    /**
     * Provides the total cost of travelling from one vertex to another using
     * a list of Transport representing the path
     *
     * @param path the Transports that connect two vertices
     * @return the sum of the price field for each of the Transports
     */
    public static double getTotalCost(List<Transport> path) {
        double total = 0.0;
        for (Transport transport : path) {
            total += transport.getPrice();
        }
        return total;
    }

    /**
     * Provides the total time of travelling from one vertex to another using
     * a list of Transport representing the path
     *
     * @param path the Transports that connect two vertices
     * @return the sum of the minutes field for each of the Transports
     */
    public static double getTotalTime(List<Transport> path) {
        double total = 0.0;
        for (Transport transport : path) {
            total += transport.getMinutes();
        }
        return total;
    }
}