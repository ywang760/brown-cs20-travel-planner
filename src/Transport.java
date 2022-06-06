package src;

/**
 * Edge class (edges are called Transports)
 */
public class Transport {
    private City source;
    private City target;
    private TransportType type;
    private double minutes;
    private double price;

    /**
     * Constructor for the Transport class
     *
     * @param source      the City where the Transport originates
     * @param destination the City where the Transport arrives
     * @param type        the form of transportation
     * @param price       the cost from travel from source to destination using this
     *                    Transport
     * @param minutes     the amount of time it takes to travel from source to
     *                    destination using this Transport
     */
    public Transport(City source, City destination, TransportType type, double price,
                     double minutes) {
        this.source = source;
        this.target = destination;
        this.type = type;
        this.price = price;
        this.minutes = minutes;
    }

    /**
     * Provides where the Transport originates
     *
     * @return where the Transport originates
     */
    public City getSource() {
        return this.source;
    }

    /**
     * Provides where the Transport arrives
     *
     * @return where the Transport arrives
     */
    public City getTarget() {
        return this.target;
    }

    /**
     * Provides how much the transportation costs
     *
     * @return how much the transportation costs
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Provides how long the transportation takes to get from its source to
     * target
     *
     * @return how long the transportation takes to get from the source to
     * target
     */
    public double getMinutes() {
        return this.minutes;
    }

    /**
     * Provides a String representation of a Transport object
     *
     * @return a String representation of a Transport object
     */
    @Override
    public String toString() {
        return this.source.toString() + " -> " + this.target.toString() +
                ", Type: " + this.type.getLabel() +
                ", Cost: $" + this.price +
                ", Duration: " + this.minutes + " minutes";
    }
}
