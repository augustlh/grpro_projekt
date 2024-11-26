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
        //Sets up world with rabbit and grass
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
        //checks if rabbit is on grass
        assertEquals(rabbit.getLocation(), grass.getLocation());

    }

    @Test
    public void testRabbitMoveToEmpty(){
        //Sets up world
        World world = new World(5) {
            @Override
            //does so there are no tiles rabbit can move to
            public Set<Location> getEmptySurroundingTiles() {
                return new HashSet<>();
            }
        };
        Rabbit rabbit = new Rabbit();
        rabbit.setLocation(new Location(0, 1));
        world.setCurrentLocation(new Location(0, 1));
        world.setTile(new Location(0, 1), rabbit);
        rabbit.act(world);
        //checks if rabbit has moved (it should not)
        assertEquals(new Location(0, 1), rabbit.getLocation());;
    }
    @Test
    public void testRabbitReproduce() {
        //set up world with two rabbits and set chance for reproducing to 100% and make them old enough to reproduce
        World world = new World(5);
        Rabbit rabbit = new Rabbit();
        rabbit.repChance=1;
        rabbit.age=6;
        world.setCurrentLocation(new Location(0, 1));
        world.setTile(new Location(0, 1), rabbit);

        Rabbit rabbit2 = new Rabbit();
        rabbit2.age=6;
        world.setCurrentLocation(new Location(0, 2));
        world.setTile(new Location(0, 2), rabbit2);
        //reproduce
        rabbit.act(world);
        //checks if a new rabbit has spawned
        assertEquals(3, world.getEntities().size());
    }
}
