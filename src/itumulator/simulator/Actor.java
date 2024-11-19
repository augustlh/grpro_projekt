package itumulator.simulator;

import itumulator.world.World;

/**
 * By implementing the {@link Actor} interface and adding an instance of such a class to a {@link World} will make the simulation call the
 * {@link act(World world) act} method during each step of the simulation. 
 */
public interface Actor {
    
    /**
     * The method called whenever the actor needs to simulate actions. The worlds 'current location' is only set at the time when the method is called.
     * Thus, if you use setTile, remove, or delete, the current position does not automatically update. This has multiple implications, e.g.,  if you want to use {@link World#getSurroundingTiles}  after
     * using {@link World#setTile} within the act method, the program will throw an exception. This can be circumvented by updating the current location ({@link World#setCurrentLocation}) or 
     * exclusively using the World methods  which accepts a location parameter (by the same name).
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    public void act(World world);
    
}
