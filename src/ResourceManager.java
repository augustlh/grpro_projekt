import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;


/**
 * Manages resources required for running a simulation program.
 */
public class ResourceManager {
    private Program program;

    /**
     * Constructs a ResourceManager object and initializes the program with the given parameters.
     *
     * @param filepath the path to the configuration file
     * @param displaySize the size of the display window
     * @param delay the delay between simulation steps
     */
    public ResourceManager(String filepath, int displaySize, int delay) {
        try {
            initializeProgram(filepath, displaySize, delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the program by reading the required configuration from a given file path, setting up the world, and spawning entities.
     *
     * @param filepath the path to the configuration file
     * @param displaySize the size of the display window
     * @param delay the delay between simulation steps
     * @throws Exception if an error occurs while reading the file or initializing the program
     */
    public void initializeProgram(String filepath, int displaySize, int delay) throws Exception {
        FileReader fileReader = new FileReader(filepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int worldSize = Integer.parseInt(bufferedReader.readLine());
        this.program = new Program(worldSize, displaySize, delay);

        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] contents = currentLine.split("\\s+");

            String entity = contents[0];
            int quantity = handleQuantity(contents[1]);
            spawnEntities(entity, quantity);
        }

        bufferedReader.close();
        fileReader.close();
    }

    /**
     * Handles the parsing and processing of a quantity, which can either be a single number
     * or a range in the format "min-max". If the quantity is a range, a random number within the range is generated.
     *
     * @param quantity the quantity to handle, either as a single number or a range "min-max"
     * @return the resulting quantity as an integer, either parsed directly or randomly selected within a range
     */
    private int handleQuantity(String quantity) {
        if(quantity.contains("-")) {
            String[] values = quantity.split("-");
            return new Random().nextInt(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        }
        return Integer.parseInt(quantity);
    }

    /**
     * Spawns the specified quantity of entities in the world, setting each entity
     * in a valid random location.
     *
     * @param entity  the type of entity to spawn
     * @param quantity the number of entities to spawn
     */
    private void spawnEntities(String entity, int quantity) {
        World world = program.getWorld();

        for (int i = 0; i < quantity; i++) {
            Object e = createEntity(entity);
            Location location = null;
            //check if entity is a rabbithole/burrow
            if (entity.equals("burrow")){
                //makes location for hole
                location = getValidRandomLocation(e, world);
                RabbitHole rabbitHole = (RabbitHole)e;
                //makes hole
                Hole hole = new Hole(location,rabbitHole);
                //adds hole to rabbithole
                rabbitHole.addHole(hole);
                world.setTile(location,hole);
            }else {
                location = getValidRandomLocation(e, world);
                world.setTile(location, e);
            }
            world.setCurrentLocation(location);
        }
    }


    /**
     * Creates and returns an instance of the specified entity type.
     *
     * @param entity the type of entity to create; valid values are "grass", "rabbit", and "burrow".
     * @return an instance of the specified entity type.
     * @throws IllegalArgumentException if the entity is null or an unknown type.
     */
    private Object createEntity(String entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return switch (entity) {
            case "grass" -> new Grass();
            case "rabbit" -> new Rabbit();
            case "burrow" -> new RabbitHole();
            default -> throw new IllegalArgumentException("Unknown entity: " + entity);
        };
    }

    /**
     * Generates a valid random location within the given world for the specified entity.
     * A valid location is one that is either empty for blocking entities or does not contain
     * any non-blocking entities for non-blocking entities.
     *
     * @param e the entity for which location needs to be generated; could be blocking or non-blocking
     * @param world the world in which the location is to be generated
     * @return a valid random location within the specified world
     * @throws IllegalArgumentException if the world is null
     */
    private Location getValidRandomLocation(Object e, World world) {
        if(world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        int size = world.getSize();
        Random rand = new Random();
        Location location = new Location(rand.nextInt(size), rand.nextInt(size));

        if(e instanceof NonBlocking) {
            while (world.containsNonBlocking(location)) {
                location = new Location(rand.nextInt(size), rand.nextInt(size));
            }
        } else {
            while (!world.isTileEmpty(location)) {
                location = new Location(rand.nextInt(size), rand.nextInt(size));
            }
        }

        return location;

    }

    /**
     * Retrieves the current instance of the program in the ResourceManager.
     * This program instance handles simulations including world setup,
     * canvas drawing, simulation step execution, and frame management.
     *
     * @return the current Program instance managed by the ResourceManager.
     */
    public Program getProgram() {
        return program;
    }
}
