package sol;

import src.IDijkstra;
import src.IGraph;
import src.Transport;

import java.rmi.UnexpectedException;
import java.util.*;
import java.util.function.Function;

/**
 * A class for using Dijkstra's algorithm on a graph
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
public class Dijkstra<V, E> implements IDijkstra<V, E> {

    /**
     * Provides a list of all the edges that connects source to target
     *
     * @param graph  a data structure consisting of vertices and edges
     * @param source a vertex where the edge originates
     * @param target a vertex that should be connected to the source
     * @return a list of all the edges that connects source and target
     */
    private List<E> getConnectingEdges(IGraph<V, E> graph, V source, V target) {
        Set<E> edges = graph.getOutgoingEdges(source);
        // list is used because get method is needed in getBestOption
        List<E> connectingEdges = new ArrayList<>();
        for (E edge : edges) {
            if (graph.getEdgeTarget(edge).equals(target)) {
                connectingEdges.add(edge);
            }
        }
        return connectingEdges;
    }

    /**
     * Provides the set of vertices that are adjacent to the source
     *
     * @param graph  a data structure consisting of vertices and edges
     * @param source the vertex to which each neighbor is connected to
     * @return the set of adjacent vertices; an empty set if no neighbors exist
     */
    private Set<V> getNeighbors(IGraph<V, E> graph, V source) {
        Set<E> edges = graph.getOutgoingEdges(source);
        HashSet<V> neighbors = new HashSet<>();
        for (E edge : edges) {
            neighbors.add(graph.getEdgeTarget(edge));
        }
        return neighbors;
    }

    /**
     * Provides the edge connecting checkingVertex to neighbor with the best
     * field specified by edgeWeight
     *
     * @param graph      a data structure consisting of vertices and edges
     * @param source     a vertex where the edge originates
     * @param target     a vertex that should be connected to the source
     * @param edgeWeight a Function that specifies the determining field of the
     *                   edge
     * @return the edge with best field
     */
    private E getBestEdge(IGraph<V, E> graph, V source, V target, Function<E,
            Double> edgeWeight) {
        List<E> connectingEdges = this.getConnectingEdges(graph, source, target);
        try {
            E min = connectingEdges.get(0);
            for (E edge : connectingEdges) {
                if (edgeWeight.apply(edge) < edgeWeight.apply(min)) {
                    min = edge;
                }
            }
            return min;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Provides a path of edges specifying the route from the source to the
     * destination according to the criterion of edgeWeight
     *
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight  a Function that specifies the determining field of the
     *                    edge
     * @return a path of edges from source to destination; empty if no such
     * path exists
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        // setup HashMap that enumerates the cost of each vertex from the source
        HashMap<V, Double> routeCosts = new HashMap<>();
        // initialize the cost of every vertex to infinity except for the
        // source, which is 0
        for (V vertex : graph.getVertices()) {
            if (vertex.equals(source)) {
                routeCosts.put(vertex, 0.0);
            } else {
                routeCosts.put(vertex, Double.MAX_VALUE);
            }
        }
        // comparator for sorting PriorityQueue in ascending order
        Comparator<V> cheaperVertex = (v1, v2) -> {
            return Double.compare(routeCosts.get(v1), routeCosts.get(v2));
        };
        PriorityQueue<V> toCheckQueue = new PriorityQueue<>(cheaperVertex);
        toCheckQueue.addAll(graph.getVertices());
        // tracks (vertex, edge that points to vertex) for recursive backtracking
        HashMap<V, E> cameFrom = new HashMap<>();
        // path of edges that will be returned
        LinkedList<E> path = new LinkedList<>();
        while (!toCheckQueue.isEmpty()) {
            V checkingVertex = toCheckQueue.remove();
            if (destination.equals(checkingVertex)) {
                // backtrack through cameFrom and return route
                V backCheck = destination;
                while (!backCheck.equals(source)) {
                    E transport = cameFrom.get(backCheck);
                    if (transport == null) {
                        return new ArrayList<>();
                    }
                    V sourceVertex = graph.getEdgeSource(transport);
                    path.addFirst(transport);
                    backCheck = sourceVertex;
                }
                return path;
            }
            for (V neighbor : this.getNeighbors(graph, checkingVertex)) {
                E bestEdge = this.getBestEdge(graph, checkingVertex, neighbor,
                        edgeWeight);
                // accounts for no edge out of checkingVertex to neighbor
                if (bestEdge == null) {
                    break;
                }
                double bestEdgeCost = edgeWeight.apply(bestEdge);
                double newCost = routeCosts.get(checkingVertex) + bestEdgeCost;
                if (newCost < routeCosts.get(neighbor)) {
                    // update cost of neighbor
                    routeCosts.put(neighbor, newCost);
                    // update the edge that leads to neighbor
                    cameFrom.put(neighbor, bestEdge);
                    // update location of neighbor in priority queue
                    toCheckQueue.remove(neighbor);
                    toCheckQueue.add(neighbor);
                }
            }
        }
        return path;
    }
}
