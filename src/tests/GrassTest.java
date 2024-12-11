package tests;

import behaviours.plants.Grass;
import behaviours.rabbit.Rabbit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GrassTest {
    //Cant spread test
    @Test
    public void testSpreadNoSurroundingEmptyNonBlockingTiles() {
        World world = new World(5) {
            @Override
            //does so there are no tiles grass can spread to
            public Set<Location> getSurroundingTiles() {
                return new HashSet<>();
            }

            @Override
            public boolean containsNonBlocking(Location location) {
                return true;
            }
        };
        Grass grass = new Grass(world, new Location(0, 1));
        grass.setSpreadProbability(1);
        grass.act(world);

        assertEquals(1, world.getEntities().size());
    }
    //Spreads to other tiles
    @Test
    public void testSpreadToNeighboringTiles() {
        int count = 0;
        int amount = 100000;
        for (int i = 0; i < amount; i++) {
            World world = new World(5);
            Location location = new Location(0, 1);
            Grass grass = new Grass(world,location);

            world.setCurrentLocation(location);
            grass.act(world);
            List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles(world.getLocation(grass)));
            for (Location loc : neighbours) {
                if (world.getNonBlocking(loc) instanceof Grass && world.getEntities().containsValue(loc)) {
                    count++;
                }
            }
        }
        double probability = (double)count/amount;
        assertTrue(probability>= (0.125-0.05) && probability<= (0.125+0.05));
    }

    //Blocking on nonBlocking test
    @Test
    public void testBlockingOnNonBlockingTiles() {
        World world = new World(5);
        Location location = new Location(0, 1);
        Grass grass = new Grass(world,location);
        world.setCurrentLocation(location);
        Rabbit rabbit = new Rabbit(world,location);

        assertEquals(world.getLocation(grass), world.getLocation(rabbit));
    }
}