package tests;

import behaviours.rabbit.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalPathTest {
    World world;
    int MAX_STEPS;
    int NUM_STEPS;
    @BeforeEach
    public void setUp() {
        this.world = new World(5);
        MAX_STEPS = world.getSize() * world.getSize();
        NUM_STEPS = 0;
    }

    @Test
    public void testPathing() {
        Rabbit rabbit = new Rabbit(this.world,  new Location(0,0));


        pathTestHelper(rabbit, new Location(4,4),  new Location(0,0));
        assertEquals( new Location(4,4), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");

        pathTestHelper(rabbit, new Location(0,4), new Location(0,0));
        assertEquals( new Location(0,4), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");

        pathTestHelper(rabbit, new Location(4,0), new Location(0,0));
        assertEquals( new Location(4,0), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");

        pathTestHelper(rabbit, new Location(0,0), new Location(4,4));
        assertEquals(new Location(0,0), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");

        pathTestHelper(rabbit, new Location(0,4), new Location(4,4));
        assertEquals(new Location(0,4), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");

        pathTestHelper(rabbit, new Location(4,0), new Location(4,4));
        assertEquals(new Location(4,0), world.getLocation(rabbit), "Animal pursue() did not correctly path to target within " + MAX_STEPS + " steps");
    }

    private void pathTestHelper(Rabbit rabbit, Location target, Location rabbitLocation) {
        world.move(rabbit, rabbitLocation);
        NUM_STEPS = 0;

        while (!world.getLocation(rabbit).equals(target)) {
            if(MAX_STEPS < NUM_STEPS) {
                break;
            }

            rabbit.pursue(this.world, target);
            NUM_STEPS++;
        }
    }

    @Test
    public void testRabbitPathingToNestDuringNight() {
        //for rabbit and wolf
        Rabbit rabbit = new Rabbit(world, new Location(0,0));
        //world.setTile(new Location(0,0);
        world.setNight();
        int previousDistance;
    }
}
