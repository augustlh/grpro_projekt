package tests;


import behaviours.Carcass;
import behaviours.Fungus;
import behaviours.plants.Grass;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import help.Utils;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


    // Quick check with group/tema 3
public class FungusTest {

    @Test
    public void testFungusEatsCarcass() {
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0));
        Fungus fungus = new Fungus(world,carcass, Utils.random.nextDouble()*2);
        carcass.setFungus(fungus);

        fungus.act(world);
        assertEquals(2,carcass.getRemainingUses());
    }

    @Test
    public void testFungusEatsAllOfCarcass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0));
        Fungus fungus = new Fungus(world,carcass, Utils.random.nextDouble()*2);
        carcass.setFungus(fungus);


        while (carcass.getRemainingUses() > 0){
            fungus.act(world);
        }

        assertEquals(1,world.getEntities().size());
    }

    @Test
    public void testFungusSpreads(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0));
        Fungus fungus = new Fungus(world,carcass, Utils.random.nextDouble()*2);
        carcass.setFungus(fungus);

        Carcass carcass2 = new Carcass(world, new Location(1, 1));

        for(int i=0;i<=15;i++){
            fungus.act(world);
        }
        assertTrue(carcass2.isInfested());
    }

    @Test
    public void testFungusSpawnsGrass(){
        World world = new World(5);
        Carcass carcass = new Carcass(world, new Location(0, 0));
        Fungus fungus = new Fungus(world,carcass, Utils.random.nextDouble()*10);
        carcass.setFungus(fungus);

        for (int i=0;i<50;i++){
            fungus.act(world);
        }
        assertInstanceOf(Grass.class, world.getTile(new Location(0, 0)));
    }

}
