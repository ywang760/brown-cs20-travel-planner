package sol;

import src.IBFS;
import src.IGraph;
import src.Transport;

import java.util.*;

/**
 * A class for using Breath First Search on a graph
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
public class BFS<V, E> implements IBFS<V, E> {

    /**
     * Provides the first edge that connects source to target
     *
     * @param graph  a data structure consisting of vertices and edges
     * @param source a vertex where the edge originates
     * @param target a vertex that should be connected to the source
     * @return an edge that connects source and target
     */
    public E getConnectingEdge(IGraph<V, E> graph, V source, V target) {
        Set<E> edges = graph.getOutgoingEdges(source);
        for (E edge : edges) {
            if (graph.getEdgeTarget(edge).equals(target)) {
                return edge;
            }
        }
        throw new IllegalArgumentException("no connecting edge");
    }

    /**
     * Provides the set of vertices that are adjacent to the source
     *
     * @param graph  a data structure consisting of vertices and edges
     * @param source the vertex to which each neighbor is connected to
     * @return the set of adjacent vertices; an empty set if no neighbors exist
     */
    public Set<V> getNeighbors(IGraph<V, E> graph, V source) {
        Set<E> edges = graph.getOutgoingEdges(source);
        HashSet<V> neighbors = new HashSet<>();
        for (E edge : edges) {
            neighbors.add(graph.getEdgeTarget(edge));
        }
        return neighbors;
    }

    /**
     * Determined the most direct path between the start and end vertices
     *
     * @param graph the graph including the vertices
     * @param start the start vertex
     * @param end   the end vertex
     * @return a list of edges representing the most direct path
     */
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        LinkedList<V> toCheck = new LinkedList<>();
        HashSet<V> visited = new HashSet<>();
        HashMap<V, V> cameFrom = new HashMap<>();
        LinkedList<E> path = new LinkedList<>();
        toCheck.addLast(start);
        while (!toCheck.isEmpty()) {
            V checkingVertex = toCheck.removeFirst();
            if (end.equals(checkingVertex)) {
                // backtrack through cameFrom and return route
                V backCheck = end;
                while (!backCheck.equals(start)) {
                    V currentCheck = backCheck;
                    backCheck = cameFrom.get(backCheck);
                    E transport = this.getConnectingEdge(graph, backCheck,
                            currentCheck);
                    path.addFirst(transport);
                }
                return path;
            }
            // checkingVertex != to end
            visited.add(checkingVertex);
            for (V neighbor : this.getNeighbors(graph, checkingVertex)) {
                if (!visited.contains(neighbor)) {
                    toCheck.addLast(neighbor);
                    // ensures that closest vertex to start is the place neighbor
                    // came from
                    if (!cameFrom.containsKey(neighbor)) {
                        cameFrom.put(neighbor, checkingVertex);
                    }
                }
            }
        }
        // return an empty List when there's no path, don't throw error
        return path;
    }

}
