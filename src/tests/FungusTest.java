package tests;


import behaviours.Carcass;
import behaviours.Fungus;
import behaviours.plants.Grass;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class FungusTest {

    //fungus eats carcass
    @Test
    public void testFungusEatsCarcass() {
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0), true);

        carcass.act(world);
        assertEquals(2,carcass.getRemainingUses());
    }

    //fungus eats up
    @Test
    public void testFungusEatsAllOfCarcass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0), true);

        while (carcass.getRemainingUses() > 0){
            carcass.act(world);
        }

        assertEquals(1,world.getEntities().size());
    }

    //fungus spreads to other carcasses
    @Test
    public void testFungusSpreads(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0), true);

        carcass.act(world);
        carcass.act(world);
        carcass.act(world);

        Fungus fungus = carcass.getFungus();

        Carcass carcass2 = new Carcass(world, new Location(1, 1), false);

        for(int i=0;i<=10;i++){
            fungus.act(world);
        }

        assertTrue(carcass2.isInfested());
    }


    //Spawns grass when dead
    @Test
    public void testFungusSpawnsGrass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0), true);
        carcass.act(world);
        carcass.act(world);
        carcass.act(world);

        Fungus fungus = carcass.getFungus();
        for (int i=0;i<50;i++){
            fungus.act(world);
        }
        assertInstanceOf(Grass.class, world.getTile(new Location(0, 0)));
    }
}