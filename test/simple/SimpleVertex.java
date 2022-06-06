package test.simple;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that models a simple vertex.
 */
public class SimpleVertex {
    public String id;
    public Set<SimpleEdge> outgoingEdges = new HashSet<>();

    /**
     * Single argument constructor for the SimpleVertex class
     *
     * @param id
     */
    public SimpleVertex(String id) {
        this.id = id;
    }

    /**
     * Adds an edge to the vertex
     *
     * @param edge the edge to be added
     */
    public void addEdge(SimpleEdge edge) {
        this.outgoingEdges.add(edge);
    }

    /**
     * Provides a String representation of the vertex
     *
     * @return
     */
    @Override
    public String toString() {
        return this.id;
    }
}
