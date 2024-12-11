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

public class BearTest {
    //bear eat berry
    @Test
    public void testBearEatBerry() {
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Bush bush = new Bush(world,new Location(0,1));
        bush.setSpreadProbability(1);
        bush.act(world);
        for (int i = 0; i<50;i++){
            world.setDay();
            bear.act(world);
        }

        assertEquals(0,bush.getBerryCount());

    }
    //bear kills rabbit
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
    //bear kills wolf
    @Test
    public void testBearKillWolf() {
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

    //bear kills inside territory
    @Test
    public void testBearHuntInsideTerritory(){
        World world = new World(5);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(world.getLocation(bear));
        Rabbit rabbit = new Rabbit(world,new Location(2,2));
        while(world.contains(rabbit) && world.contains(bear)) {
            System.out.println(world.getEntities());
            world.setDay();
            bear.act(world);
        }
        assertFalse(world.contains(rabbit));
    }
    //only eats inside territory
    @Test
    public void testBearOnlyEatInsideTerritory(){
        World world = new World(10);
        Bear bear = new Bear(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        world.move(bear,new Location(8,8));
        Bush bush = new Bush(world,new Location(9,9));
        bush.setSpreadProbability(1);
        bush.act(world);
        while(world.contains(bear)) {
            world.setDay();
            bear.act(world);
        }

        assertEquals(1,bush.getBerryCount());
    }
    //eats carcass
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
