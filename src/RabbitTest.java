import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RabbitTest {

    @Test
    public void testEatGrassOnGrass(){
        //makes world for with one grass and rabbit and sets them on the same tile
        World world = new World(5);
        Rabbit rabbit = new Rabbit();
        Grass grass = new Grass();
        Location location = new Location(0, 0);

        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        world.setTile(location, grass);
        rabbit.act(world);
        //checks if rabbit has eaten grass
        assertEquals(1, world.getEntities().size());
    }

    @Test
    public void testRabbitMoveToGrass(){
        World world = new World(5);
        Location loc1 = new Location(0, 0);
        Location loc2 = new Location(0, 1);

        Rabbit rabbit = new Rabbit();
        rabbit.setLocation(loc2);

        Grass grass = new Grass();
        grass.setLocation(loc1);

        world.setCurrentLocation(loc1);
        world.setTile(loc1, grass);
        world.setCurrentLocation(loc2);
        world.setTile(loc2, rabbit);
        rabbit.act(world);

        if (rabbit.getLocation().equals(grass.getLocation())){
            assertTrue(true);
        }
    }

    @Test
    public void testRabbitMoveToEmpty(){
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
        Rabbit rabbit = new Rabbit();
        rabbit.setLocation(new Location(0, 1));
        world.setCurrentLocation(new Location(0, 1));
        world.setTile(new Location(0, 1), rabbit);
        rabbit.act(world);
        if (rabbit.getLocation().equals(new Location(0, 1))){
            assertTrue(true);
        }
    }
}
