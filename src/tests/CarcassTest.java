package tests;

import behaviours.Carcass;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CarcassTest {
        World world;
        Carcass carcass;
        Wolf wolf;
    @BeforeEach
    void setUp() {
        world = new World(5);
        wolf = new WolfPack(world,new Location(0,0),1).getPack().getFirst();
        world.setCurrentLocation(new Location(0,0));
        carcass = new Carcass(world,new Location(1,1));
    }

    //can be eaten
    @Test
    public void testGetsEatenByCarnivore() {
        wolf.act(world);

        assertEquals(2,carcass.getRemainingUses());
    }
    //deleted when done
    @Test
    public void testGetsDeletedWhenNoMoreUses() {
        while (carcass.getRemainingUses() > 0) {
            wolf.act(world);
        }

        assertFalse(world.contains(carcass));
    }


}
