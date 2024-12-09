package tests;

import behaviours.plants.Grass;
import behaviours.rabbit.Rabbit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.HashSet;
import java.util.Set;


    /*
    * For rabbit fix random movement and reproduce
    * Also needs test of pathfind to get to hole
    *
    * */
public class RabbitTest {

    @Test
    public void testEatGrassOnGrass(){
        //makes world for with one grass and rabbit and sets them on the same tile
        World world = new World(5);
        Location location = new Location(0, 0);
        Rabbit rabbit = new Rabbit(world, location);
        Grass grass = new Grass(world, location);

        world.setCurrentLocation(location);
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

        Rabbit rabbit = new Rabbit(world, loc2);
        Grass grass = new Grass(world, loc1);
        world.setCurrentLocation(loc2);
        rabbit.act(world);
        assertEquals(world.getLocation(rabbit), world.getLocation(grass));

    }


    //Not working
//    @Test
//    public void testRabbitMoveToEmpty(){
//        //Sets up world
//        World world = new World(5) {
//            @Override
//            //does so there are no tiles rabbit can move to
//            public Set<Location> getEmptySurroundingTiles() {
//                return new HashSet<>();
//            }
//        };
//        Rabbit rabbit = new Rabbit(world, new Location(0,1));
//        world.setCurrentLocation(new Location(0, 1));
//        rabbit.act(world);
//        //checks if rabbit has moved (it should not)
//        assertEquals(new Location(0, 1), world.getLocation(rabbit));
//    }

}
