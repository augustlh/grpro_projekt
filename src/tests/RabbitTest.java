package tests;

import behaviours.nests.RabbitHole;
import behaviours.plants.Grass;
import behaviours.rabbit.Rabbit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class RabbitTest {

    //Eats grass when on grass
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
    //Goes to grass
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


    //Test breeding for rabbit
    @Test
    public void testRabbitReproduce(){
        World world = new World(5);
        Rabbit rabbit = new Rabbit(world, new Location(1, 1));
        RabbitHole hole = new RabbitHole(world, new Location(1,1));
        world.setCurrentLocation(new Location(1, 1));
        world.setNight();
        rabbit.setHole(world);
        rabbit.act(world);
        Rabbit rabbit2 = new Rabbit(world, new Location(1, 1));
        rabbit2.setHole(world);
        rabbit2.act(world);
        while (world.contains(rabbit)&&world.contains(rabbit2)) {
            int count =0;
            world.setNight();
            rabbit.act(world);
            rabbit2.act(world);
            for(Object o : world.getEntities().keySet()){
                if (o instanceof Rabbit) {
                    count++;
                }
            }
            if(count >=3){
                assertTrue(true);
                return;
            }
        }
        System.out.println(world.getEntities());
        fail();

    }
}
