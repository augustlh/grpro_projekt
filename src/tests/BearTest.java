package tests;

import behaviours.bear.Bear;
import behaviours.plants.Bush;
import behaviours.rabbit.Rabbit;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

public class BearTest {

    @Test
    public void testBearEatBerry() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Bush bush = new Bush(world,new Location(0,1));
        bush.setSpreadProbability(1);
        bush.act(world);
        bear.act(world);

        assertEquals(0,bush.getBerryCount());

    }
    @Test
    public void testBearEatRabbit() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Rabbit rabbit = new Rabbit(world,new Location(0,1));
        bear.act(world);

        assertEquals(1,world.getEntities().size());

    }
    @Test
    public void testBearEatWolf() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Wolf wolf = new Wolf(world,new Location(0,1),new WolfPack(world,new Location(0,1),0));
        bear.act(world);

        assertEquals(1,world.getEntities().size());


    }


    @Test
    public void testBearHuntInsideTerritory(){
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Bush bush = new Bush(world,new Location(2,1));
        bush.setSpreadProbability(1);
        bush.act(world);
        System.out.println(world.getEntities());
        bear.act(world);
        System.out.println(world.getEntities());
        for (Location loc : world.getSurroundingTiles(world.getLocation(bush))){
            if (world.getTile(loc) instanceof Bear){
                assertTrue(true);
                return;
            }
        }
        fail();
    }
    @Test
    public void testBearOnlyEatInsideTerritory(){
        World world = new World(10);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        world.move(bear,new Location(8,8));
        Bush bush = new Bush(world,new Location(9,9));
        bush.setSpreadProbability(1);
        bush.act(world);

        bear.act(world);

        assertEquals(1,bush.getBerryCount());
    }

}
