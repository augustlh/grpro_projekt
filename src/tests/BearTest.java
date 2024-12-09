package tests;

import behaviours.Carcass;
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

    /*
    * Needs to rewrite hunt of differnt food and carcass
    * */

public class BearTest {

    @Test
    public void testBearEatBerry() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Bush bush = new Bush(world,new Location(0,1));
        bush.setSpreadProbability(1);
        bush.act(world);
        for (int i = 0; i<20;i++){
            world.setDay();
            bear.act(world);
            System.out.println(world.getEntities());
        }

        assertEquals(0,bush.getBerryCount());

    }
    @Test
    public void testBearKillRabbit() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Rabbit rabbit = new Rabbit(world,new Location(0,1));
        while (world.contains(rabbit)) {
            world.setDay();
            bear.act(world);
        }

        assertFalse(world.contains(rabbit));

    }
    @Test
    public void testBearEatWolf() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Wolf wolf = new Wolf(world,new Location(0,1),new WolfPack(world,new Location(0,1),0));
        while (world.contains(wolf)) {
            world.setDay();
            bear.act(world);
        }

        assertFalse(world.contains(wolf));


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

    @Test
    public void testBearEatCarcass(){
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Carcass carcass = new Carcass(world,new Location(0,1));
        while (world.contains(carcass)) {
            world.setDay();
            bear.act(world);
        }
        assertEquals(1,world.getEntities().size());
    }

}
