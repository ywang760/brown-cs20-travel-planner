package src;

import java.util.HashSet;
import java.util.Set;

/**
 * vertex class (vertices are called Cities)
 */
public class City {
    private Set<Transport> outgoing;
    private String name;

    /**
     * Single argument constructor for the City class
     *
     * @param name the title of the City
     */
    public City(String name) {
        this.name = name;
        //stores all outgoing Transports (edges) for given City (vertex)
        this.outgoing = new HashSet<>();
    }

    /**
     * Provides the set of transports leaving this City
     *
     * @return a set of transports, the outgoing field
     */
    public Set<Transport> getOutgoing() {
        return this.outgoing;
    }

    /**
     * Adds outgoing edge (Transport) to vertex (City)
     *
     * @param transport the edge to be added to the City
     */
    public void addOut(Transport transport) {
        this.outgoing.add(transport);
    }

    /**
     * Provides a String representation of the City object
     *
     * @return a String representation of the City object
     */
    @Override
    public String toString() {
        return this.name;
    }
}
