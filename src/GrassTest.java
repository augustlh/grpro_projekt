
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.HashSet;
import java.util.Set;

public class GrassTest {

    @Test
    public void testSpread_noSurroundingEmptyNonBlockingTiles() {
        World world = new World(5) {
            @Override
            public Set<Location> getSurroundingTiles() {
                return new HashSet<>();
            }

            @Override
            public boolean containsNonBlocking(Location location) {
                return true;
            }
        };
        Grass grass = new Grass();
        grass.spreadProbability=1;
        grass.spread(world);

        // Ensure grass does not spread when there are no surrounding empty non-blocking tiles
        assertEquals(0, world.getEntities().size());
    }

    @Test
    public void testSpread_withSurroundingEmptyNonBlockingTiles() {
        World world = new World(5) {
            @Override
            public Set<Location> getSurroundingTiles() {
                Set<Location> locations = new HashSet<>();
                locations.add(new Location(0, 1));
                return locations;
            }

            @Override
            public boolean containsNonBlocking(Location location) {
                return false;
            }
        };

        Grass grass = new Grass();
        grass.spreadProbability=1;
        grass.spread(world);

        // Ensure a new grass spreads to an empty non-blocking tile
        assertEquals(1, world.getEntities().size());
        assertTrue(world.getEntities().values().contains(new Location(0, 1)));
    }

    @Test
    public void testSpread_randomProbabilityBelowSpreadThreshold() {
        World world = new World(5) {
            @Override
            public Set<Location> getSurroundingTiles() {
                Set<Location> locations = new HashSet<>();
                locations.add(new Location(0, 1));
                return locations;
            }

            @Override
            public boolean containsNonBlocking(Location location) {
                return false;
            }
        };

        Grass grass = new Grass() {
            @Override
            public void spread(World world) {
                this.spreadProbability = 1.0;  // Force probability to ensure spreading occurs
                super.spread(world);
            }
        };
        grass.spread(world);

        // Ensure a new grass spreads to an empty non-blocking tile with forced threshold
        assertEquals(1, world.getEntities().size());
        assertTrue(world.getEntities().values().contains(new Location(0, 1)));
    }

    @Test
    public void testSpread_randomProbabilityAboveSpreadThreshold() {
        World world = new World(5) {
            @Override
            public Set<Location> getSurroundingTiles() {
                Set<Location> locations = new HashSet<>();
                locations.add(new Location(0, 1));
                return locations;
            }

            @Override
            public boolean containsNonBlocking(Location location) {
                return false;
            }
        };

        Grass grass = new Grass() {
            @Override
            public void spread(World world) {
                this.spreadProbability = 0.0;  // Force probability to ensure spreading does not occur
                super.spread(world);
            }
        };
        grass.spread(world);

        // Ensure grass does not spread when the random probability is above the spread threshold
        assertEquals(0, world.getEntities().size());
    }
}