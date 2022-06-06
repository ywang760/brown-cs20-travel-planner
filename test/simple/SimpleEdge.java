package test.simple;

/**
 * A class that models a simple edge between two simple vertices.
 */
public class SimpleEdge {
    public double weight;
    public SimpleVertex source;
    public SimpleVertex target;

    /**
     * Three-argument constructor for the SimpleEdge class
     *
     * @param weight a quantity representing the weight of the edge
     * @param source where the edge originates
     * @param target where the edge arrives
     */
    public SimpleEdge(double weight, SimpleVertex source, SimpleVertex target) {
        this.weight = weight;
        this.source = source;
        this.target = target;
    }

    /**
     * Provides the weight of the edge
     *
     * @return the weight of the edge
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Provides a String representation of the SimpleEdge
     *
     * @return a String representation of the SimpleEdge
     */
    @Override
    public String toString() {
        return "SimpleEdge{" +
                "weight=" + this.weight +
                ", source=" + this.source +
                ", target=" + this.target +
                '}';
    }
}
