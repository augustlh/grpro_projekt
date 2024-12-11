package tests;

import behaviours.Cordyceps;
import behaviours.rabbit.Rabbit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;


public class CordycepsTest {

    //Takes control of movement
    @Test
    public void testMoveTowardsOtherAnimals() {
        World world = new World(5);
        Rabbit rabbit1 = new Rabbit(world,new Location(0,0));
        Rabbit rabbit2 = new Rabbit(world,new Location(0,2));
        Cordyceps cordyceps = new Cordyceps();
        rabbit2.infect(cordyceps);
        world.setCurrentLocation(new Location(0,0));

        rabbit2.act(world);


        for (Location loc : world.getSurroundingTiles(world.getLocation(rabbit2))){
            if (world.getTile(loc) instanceof Rabbit){
                assertTrue(true);
                return;
            }
        }
        fail();

    }
    //infects other animals
    @Test
    public void testSpreadsToOtherAnimals() {
        World world = new World(5);
        Rabbit rabbit1 = new Rabbit(world,new Location(0,0));
        Rabbit rabbit2 = new Rabbit(world,new Location(0,2));
        Cordyceps cordyceps = new Cordyceps();
        rabbit2.infect(cordyceps);
        world.setCurrentLocation(new Location(0,0));

        rabbit2.onConsume(world);

        assertTrue(rabbit1.isInfected());
    }

}
