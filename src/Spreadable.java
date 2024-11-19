import itumulator.simulator.Actor;
import itumulator.world.World;

/**
 * By implementing the {@link Spreadable} interface and adding an instance of such a class to a {@link World} will do so the simulation call the
 * {@link spread(World world) spread} by using {@link Actor} {@link act(World world) act} method
  */
public interface Spreadable extends Actor {

    /**
     * Defines how a {@link Spreadable} should spread during simulation
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    void spread(World world);
}
