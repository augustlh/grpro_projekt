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

//Test for wolves

class WolfTest {

    @Test
    //See if they kill rabbit when they can
    public void testKillRabbit(){
        World world = new World(5);
        Location location = new Location(0, 0);
        Rabbit rabbit = new Rabbit(world, location);
        world.setCurrentLocation(location);

        Wolf wolf = new WolfPack(world,new Location(0,0),1).getPack().getFirst();
        world.setCurrentLocation(world.getLocation(wolf));
        while (world.contains(rabbit)){
            wolf.act(world);
        }

        assertFalse(world.contains(rabbit));
    }

    //Checks if they move after they alpha
    @Test
    public void testMoveAfterAlpha(){
        int count = 0;
        for (int i = 0; i < 100; i++) {
            World world = new World(5);
            WolfPack wolfPack = new WolfPack(world,new Location(2,2),2);
            List<Wolf> wolfs = new ArrayList<>(wolfPack.getPack());
            world.setCurrentLocation(world.getLocation(wolfPack.getAlphaWolf()));
            for (Wolf wolf : wolfs) {
                wolf.act(world);
            } //checks the surrounding tiles of alpha to see if the wolf still is near by
            for (Location loc: world.getSurroundingTiles(world.getLocation(wolfs.getFirst()))){
                if (world.getTile(loc) instanceof Wolf){
                    count++;
                }
            }
        }
        double probability = (double)count/100;
        assertTrue(probability>= (0.75-0.05) && probability<= (0.75+0.05));
    }
    //See if they hunt rabbits
    @Test
    public void testHuntRabbit(){
        World world = new World(5);
        Rabbit rabbit = new Rabbit(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        Location location = new Location(1, 0);
        Wolf wolf = new WolfPack(world,location,1).getPack().getFirst();
        world.setCurrentLocation(world.getLocation(wolf));
        wolf.act(world);
        //checks if wolf is close to rabbit
        for (Location loc : world.getSurroundingTiles(world.getLocation(rabbit))){
            if (world.getTile(loc) instanceof Wolf){
                assertTrue(true);
                return;
            }
        }
        fail();

    }

    //Test that they eat Carcass
    @Test
    public void testEatCarcass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0,0), false);

        Location location = new Location(0, 1);
        Wolf wolf = new WolfPack(world,new Location(0,1),1).getPack().getFirst();
        world.setCurrentLocation(location);
        world.setDay();
        while (carcass.getRemainingUses()==3){
            wolf.act(world);
        }

        assertEquals(2,carcass.getRemainingUses());
    }

    //Reproduce test for wolf
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
        //gets them to keep trying to breed until they have made a baby
        wolf1.setSexChance(1);
        wolf2.setSexChance(1);
        while (world.getEntities().size()<4 && world.contains(wolf1)){
            world.setNight();
            wolf1.act(world);
            wolf2.act(world);
        }
        assertEquals(4, world.getEntities().size());

    }

    //Test if they fight other wolves from other wolf packs
    @Test
    public void testWolfFight(){
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