package tests;

import behaviours.Carcass;
import behaviours.nests.WolfCave;
import behaviours.rabbit.Rabbit;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

    /*
    * Fix eating
    * make Reproduce and pathfinding test for hole/hunting
    *
    * */

class WolfTest {

    @Test
    public void testKillRabbit(){
        World world = new World(5);
        Location location = new Location(0, 0);
        Rabbit rabbit = new Rabbit(world, location);
        world.setCurrentLocation(location);

        Wolf wolf = new WolfPack(world,new Location(0,1),1).getPack().getFirst();
        world.setCurrentLocation(new Location(0,1));
        wolf.act(world);

        assertFalse(world.contains(rabbit));
    }

    //wolves don't move right so test is 50/50
    @Test
    public void testMoveAfterAlpha(){
        World world = new World(5);
        WolfPack wolfPack = new WolfPack(world,new Location(2,0),2);
        world.setCurrentLocation(new Location(2,2));
        List<Wolf> wolfs = new ArrayList<>(wolfPack.getPack());
        for (Wolf wolf : wolfs) {
           wolf.act(world);
        }
        for (Location loc: world.getSurroundingTiles(world.getLocation(wolfs.getFirst()))){
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

    @Test
    public void testEatCarcass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world,new Location(0,0));

        Location location = new Location(0, 1);
        Wolf wolf = new WolfPack(world,new Location(0,1),1).getPack().getFirst();
        world.setCurrentLocation(location);
        wolf.act(world);

        assertEquals(2,carcass.getRemainingUses());
    }

    @Test
    public void testWolfReproduce(){
        World world = new World(5);
        Location location = new Location(0, 0);
        WolfPack wolfPack = new WolfPack(world,location,2);
        WolfCave cave = new WolfCave(world,location);
        Wolf wolf1 = wolfPack.getPack().getFirst();
        Wolf wolf2 = wolfPack.getPack().getLast();
        wolfPack.setCave(cave);
        world.setCurrentLocation(location);
        while (world.getEntities().size()<4){
            world.setNight();
            wolf1.act(world);
            wolf2.act(world);
        }
        assertEquals(4, world.getEntities().size());

    }

    @Test
    public void testWolfReproduce2(){
        World world = new World(2);
        world.setDay();
        Location location = new Location(0, 0);
        Wolf wolf = new WolfPack(world,location,1).getPack().getFirst();
        world.setCurrentLocation(location);
        Wolf wolf2 = new WolfPack(world,new Location(0,0),1).getPack().getLast();

        wolf.act(world);
        assertTrue(!world.contains(wolf)||!world.contains(wolf2));
    }

}