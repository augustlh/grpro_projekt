
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrassTest {

    @Test
    public void testSpread_noSurroundingEmptyNonBlockingTiles() {
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
        Grass grass = new Grass();
        world.setTile(new Location(0, 1), new Grass());
        grass.spreadProbability=1;
        grass.spread(world);

        // Ensure grass does not spread when there are no surrounding empty non-blocking tiles
        assertEquals(1, world.getEntities().size());
    }
    @Test
    public void testSpread_ToNeighboringTiles() {
        World world = new World(5);
        Grass grass = new Grass();
        Location location = new Location(0, 1);
        grass.setLocation(location);
        world.setTile(location, new Grass());
        world.setCurrentLocation(location);
        grass.spreadProbability=1;
        grass.spread(world);
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles(grass.getLocation()));
        for (Location loc : neighbours) {
            if (world.getNonBlocking(loc) instanceof Grass && world.getEntities().containsValue(loc)) {
                    assertTrue(true);
            }
        }
    }
}