package test.simple;

import src.IGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that models a simple graph with simple edges and simple vertices.
 * You can use these Simple classes to test your BFS and Dijkstra's, but you will not be graded on
 * any tests you write using these classes.
 */
public class SimpleGraph implements IGraph<SimpleVertex, SimpleEdge> {
    public Set<SimpleVertex> vertices = new HashSet<>();

    /**
     * Adds a vertex to the graph
     *
     * @param vertex the vertex
     */
    @Override
    public void addVertex(SimpleVertex vertex) {
        this.vertices.add(vertex);
    }

    /**
     * Adds an edge to the graph
     *
     * @param origin the origin of the edge.
     * @param edge   the edge to be added
     */
    @Override
    public void addEdge(SimpleVertex origin, SimpleEdge edge) {
        origin.addEdge(edge);
    }

    /**
     * Provides the vertices of the graph
     *
     * @return the vertices of the graph
     */
    @Override
    public Set<SimpleVertex> getVertices() {
        return this.vertices;
    }

    /**
     * Provides the origin of the edge
     *
     * @param edge the edge
     * @return the origin of the edge
     */
    @Override
    public SimpleVertex getEdgeSource(SimpleEdge edge) {
        return edge.source;
    }

    /**
     * Provides the place where the edge arrives
     *
     * @param edge the edge
     * @return the place where the edge arrives
     */
    @Override
    public SimpleVertex getEdgeTarget(SimpleEdge edge) {
        return edge.target;
    }

    /**
     * Provides the edges that leave from the provided vertex
     *
     * @param fromVertex the vertex
     * @return the edges that leave from the provided vertex
     */
    @Override
    public Set<SimpleEdge> getOutgoingEdges(SimpleVertex fromVertex) {
        return fromVertex.outgoingEdges;
    }

    /**
     * Provides the sum of the edge weights for every edge in the provided path
     *
     * @param path a sequence of edges connecting one or more cities
     * @return the sum of the edge weights
     */
    public static double getTotalEdgeWeight(List<SimpleEdge> path) {
        double total = 0;
        for (SimpleEdge segment : path) {
            total += segment.getWeight();
        }
        return total;
    }
}
