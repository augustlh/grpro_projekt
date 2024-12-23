package tests;

import behaviours.Carcass;
import behaviours.bear.Bear;
import behaviours.nests.RabbitHole;
import behaviours.plants.Bush;
import behaviours.plants.Grass;
import behaviours.rabbit.Rabbit;
import behaviours.wolf.Wolf;
import help.ResourceManager;
import itumulator.world.Location;
import itumulator.world.World;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ResourceManagerTest {
    @FunctionalInterface
    public interface Condition<T> {
        boolean test(T entity);
    }

    @Test
    public void testInitializationOfWorldFromFile() {
        String path = "src/Data/testfile.txt";

        ResourceManager resourceManager = new ResourceManager("src/Data/testfile.txt", 1000, 500);
        assertEquals(12, resourceManager.getProgram().getWorld().getSize());
        validateSpawns(resourceManager.getProgram().getWorld(), Bear.class, new Location(8,4));
        validateSpawns(resourceManager.getProgram().getWorld(), Grass.class, 10);
        validateSpawns(resourceManager.getProgram().getWorld(), Rabbit.class, 4, 8);
        validateSpawns(resourceManager.getProgram().getWorld(), Wolf.class, 5);
        validateSpawns(resourceManager.getProgram().getWorld(), RabbitHole.class, 1);
        validateSpawns(resourceManager.getProgram().getWorld(), Bush.class, 3);


        //Cordyceps
        validateCondition(resourceManager.getProgram().getWorld(), Rabbit.class, Rabbit::isInfected, 1);
        validateCondition(resourceManager.getProgram().getWorld(), Wolf.class, Wolf::isInfected, 2);

        //Carcass checks
        int countNormalCarcass = 0;
        int countInfectedCarcass = 0;

        for(Object entity : resourceManager.getProgram().getWorld().getEntities().keySet()) {
            if(entity instanceof Carcass c) {
                if (c.isInfested()) {
                    countInfectedCarcass++;
                } else {
                    countNormalCarcass++;
                }
            }
        }

        assertEquals(countNormalCarcass, 1);
        assertTrue(countInfectedCarcass >= 5 && countInfectedCarcass <= 10);
    }

    private <T> void validateSpawns(World world, Class <T> type, int quantity) {
        Map<Object, Location> entities = world.getEntities();
        int count = 0;

        for(Object entity : entities.keySet()) {
            if(type.isInstance(entity)) {
                count++;
            }
        }

        assertEquals(count, quantity);
    }

    private <T> void validateSpawns(World world, Class <T> type, int min, int max) {
        Map<Object, Location> entities = world.getEntities();
        int count = 0;

        for(Object entity : entities.keySet()) {
            if(type.isInstance(entity)) {
                count++;
            }
        }
        assertTrue(count >= min && count <= max);
    }


    private <T> void validateSpawns(World world, Class<T> type, Location location) {
        validateSpawns(world, type, 1);

        Map<Object, Location> entities = world.getEntities();
        for(Object entity : entities.keySet()) {
            if(type.isInstance(entity)) {
                Location actualLocation = entities.get(entity);
                assertEquals(location.getX(), actualLocation.getX());
                assertEquals(location.getY(), actualLocation.getY());
                return;
            }
        }

        fail("Entity not in world, this should not happen!");

    }

    private <T> void validateCondition(World world, Class<T> type, Condition<T> condition, int expectedCount) {
        Map<Object, Location> entities = world.getEntities();
        int count = 0;

        for (Object entity : entities.keySet()) {
            if (type.isInstance(entity)) {
                if (condition.test(type.cast(entity))) {
                    count++;
                }
            }
        }

        assertEquals(expectedCount, count,
                "Expected " + expectedCount + " entities of type " + type.getSimpleName() + " satisfying the condition, but found " + count);
    }

}
