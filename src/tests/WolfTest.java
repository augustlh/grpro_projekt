package tests;

import behaviours.rabbit.Rabbit;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

class WolfTest {

    @Test
    public void testEatRabbit(){
        World world = new World(5);
        Location location = new Location(0, 0);
        Rabbit rabbit = new Rabbit(world, location);
        world.setCurrentLocation(location);

        Wolf wolf = new WolfPack(world,new Location(0,1),1).getPack().getFirst();
        world.setCurrentLocation(new Location(0,1));
        wolf.act(world);

        assertEquals(1,world.getEntities().size());
    }

    //wolves don't move right so test is 50/50
    @Test
    public void testMoveAfterAlpha(){
        World world = new World(5);
        Wolf wolf = new WolfPack(world,new Location(1,1),2).getPack().getFirst();
        world.setCurrentLocation(new Location(1,1));
        wolf.act(world);
        for (Location loc: world.getSurroundingTiles(world.getLocation(wolf))){
            if (world.getTile(loc) instanceof Wolf){
                assertTrue(true);
                return;
            }
        }
        fail();
    }
    //wolves don't move right so test fail
    @Test
    public void testHuntRabbit(){
        World world = new World(5);
        Rabbit rabbit = new Rabbit(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));

        Location location = new Location(2, 0);
        WolfPack wolfPack = new WolfPack(world,location,0);
        Wolf wolf = new Wolf(world,location,wolfPack);
        world.setCurrentLocation(location);
        wolf.act(world);

        for (Location loc : world.getSurroundingTiles(world.getLocation(rabbit))){
            if (world.getTile(loc) instanceof Wolf){
                assertTrue(true);
                return;
            }
        }
        fail();

    }


}