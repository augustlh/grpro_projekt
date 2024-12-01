package tests;

import behaviours.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;
public class BushTest {

    @Test
    public void bushMakesBerries(){
        World world = new World(5);
        Bush bush = new Bush(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));

        bush.setSpreadProbability(1);
        bush.spread(world);
        assertEquals(1,bush.getBerryCount());
    }

    @Test
    public void bushStopsMakingBerries(){
        World world = new World(5);
        Bush bush = new Bush(world,new Location(0,0));
        world.setCurrentLocation(new Location(0,0));
        bush.setSpreadProbability(1);
        for (int i = 0; i<10;i++){
            bush.spread(world);
        }
        assertEquals(4,bush.getBerryCount());
    }
}
