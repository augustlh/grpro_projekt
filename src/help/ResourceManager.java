package help;

import behaviours.Carcass;
import behaviours.Cordyceps;
import behaviours.bear.Bear;
import behaviours.plants.Bush;
import behaviours.plants.Grass;
import behaviours.rabbit.Rabbit;
import behaviours.nests.RabbitHole;
import behaviours.wolf.Wolf;
import behaviours.wolf.WolfPack;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

/**
 * The ResourceManager class is responsible for initializing a Program instance
 * and populating its World with various entities based on the contents of a specified file.
 */
public class ResourceManager {
    private Program program;
    private World world;

    /**
     * Constructs a ResourceManager instance and initializes the program with the given parameters.
     *
     * @param filepath the path to the file containing the configuration data for the program.
     * @param displaySize the size of the graphical display window.
     * @param delay the delay between simulation steps in milliseconds.
     */
    public ResourceManager(String filepath, int displaySize, int delay) {
        try {
            initializeProgram(filepath, displaySize, delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the program by reading configuration data from the specified file,
     * setting up the program with the provided display size and delay, and spawning entities
     * described in the file.
     *
     * @param filepath the path to the file containing the configuration data for the program
     * @param displaySize the size of the graphical display window
     * @param delay the delay between simulation steps in milliseconds
     * @throws Exception if an error occurs while reading the file or initializing the program
     */
    public void initializeProgram(String filepath, int displaySize, int delay) throws Exception {
        FileReader fileReader = new FileReader(filepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int worldSize = Integer.parseInt(bufferedReader.readLine());
        this.program = new Program(worldSize, displaySize, delay);
        this.world = this.program.getWorld();

        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            String[] contents = currentLine.split("\\s+");

            if(contents[0].equals("cordyceps")) {
                handleCordyceps(contents);
            } else if(contents[1].equals("fungi")) {
                handleCarcasser(contents);
            } else {
                String entity = contents[0];
                int quantity = handleQuantity(contents[1]);
                Location location = getLocation(contents, 2);
                spawnEntities(entity, quantity, location);
            }
        }

        bufferedReader.close();
        fileReader.close();
    }

    /**
     * Handles the processing of "cordyceps" entities by extracting relevant data from the contents array.
     * The method interprets the fungi type, the associated entity, the quantity of entities, and their location.
     * Subsequently, it triggers the spawning of these entities in the program's world.
     *
     * @param contents an array of strings where:
     *                 contents[0] is expected to be the fungi type,
     *                 contents[1] is the entity type to be spawned,
     *                 contents[2] is the quantity or range of quantities for the entities,
     */
    public void handleCordyceps(String[] contents) {
        String fungi = contents[0];
        String entity = contents[1];
        int quantity = handleQuantity(contents[2]);


        if (entity.equals("wolf")) {
            Location location = Utils.getValidRandomLocation(world);
            WolfPack wolfPack = new WolfPack(world,location,quantity);
            for (Wolf wolf : wolfPack.getPack()) {
                Cordyceps cordyceps = new Cordyceps();
                wolf.infect(cordyceps);
            }
        } else if (entity.equals("rabbit")) {
            for (int i =0; i<quantity; i++) {
                Location location = Utils.getValidRandomLocation(world);
                Cordyceps cordyceps = new Cordyceps();
                Rabbit rabbit = new Rabbit(world,location);
                rabbit.infect(cordyceps);
            }

        }


    }

    /**
     * Handles the spawning of carcasses and associated fungi in specified quantities.
     * For each quantity, a carcass is created at a random valid location in the world,
     * and a fungus is associated with each carcass.
     *
     * @param contents an array of strings where:
     *        contents[0] is expected to be the entity type,
     *        contents[1] is the fungi type,
     *        contents[2] is the quantity or range of quantities for the carcasses.
     */
    public void handleCarcasser(String[] contents) {
        String entity = contents[0];
        String fungi = contents[1];
        int quantity = handleQuantity(contents[2]);


        for (int i = 0; i < quantity; i++) {
            Location location = Utils.getValidRandomLocation(world);
            new Carcass(world, location, true);
        }

    }

    /**
     * Extracts a location from the given array of strings based on the specified position.
     * If the array contains more elements than the specified position, it parses the element
     * at the given position as coordinates and returns a Location object.
     *
     * @param contents an array of strings where some elements might contain coordinates in the form "(x,y)"
     * @param numArgs the index in the array where the coordinates might be located
     * @return a Location object parsed from the coordinates at the specified index if present,
     *         otherwise null if the index is out of bounds or the format is invalid
     */
    public Location getLocation(String[] contents, int numArgs) {
        if(contents.length > numArgs) {
            String location = contents[numArgs];
            String[] coords = location.replaceAll("[()]", "").split(",");
            return new Location(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        }
        return null;
    }

    /**
     * Handles the given quantity string and returns an integer value.
     * If the string contains a dash ("-"), it is treated as a range, and
     * a random value within that range is returned.
     * Otherwise, the string is parsed as a single integer.
     *
     * @param quantity the quantity string, either a single integer or a range in the form "min-max"
     * @return the parsed or randomly generated integer quantity
     */
    private int handleQuantity(String quantity) {
        if(quantity.contains("-")) {
            String[] values = quantity.split("-");
            return new Random().nextInt(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        }
        return Integer.parseInt(quantity);
    }

    /**
     * Spawns the specified quantity of entities within the world.
     * If the entity is "wolf", it creates a pack at a random valid location.
     * Otherwise, it creates each entity at a different random valid location.
     *
     * @param entity The type of the entity to spawn.
     * @param quantity The number of entities to spawn.
     */
    private void spawnEntities(String entity, int quantity) {
        if (entity.equals("wolf")){
            Location location = Utils.getValidRandomLocation(world);

            if(location == null) {
                throw new RuntimeException("Failed to spawn entity. Could not find valid spawn location in allowed amount of tries.");
            }

            createEntity(entity, location, quantity);
            return;
        }

        for (int i = 0; i < quantity; i++) {
            Location location = Utils.getValidRandomLocation(world);

            if(location == null) {
                throw new RuntimeException("Failed to spawn entity. Could not find valid spawn location in allowed amount of tries.");
            }

            createEntity(entity, location);
        }
    }

    /**
     * Spawns the specified quantity of entities within the world on a given location
     *
     * @param entity The type of the entity to spawn.
     * @param quantity The number of entities to spawn.
     * @param location The location to spawn the entity
     */
    private void spawnEntities(String entity, int quantity, Location location) {
        if(location == null) {
            spawnEntities(entity, quantity);
            return;
        }

        if (entity.equals("wolf")) return;
        for (int i = 0; i < quantity; i++) {
            createEntity(entity, location);
        }
    }

    /**
     * Creates a new entity at the specified location with the given quantity.
     *
     * @param entity The type of the entity to create.
     * @param location The location where the entity should be created.
     * @param quantity The number of entities to create.
     * @throws IllegalArgumentException If the entity type is null or unknown.
     */
    private void createEntity(String entity, Location location, int quantity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        if (entity.equals("wolf")) {
            new WolfPack(world, location, quantity);
        } else {
            throw new IllegalArgumentException("Unknown entity: " + entity);
        }
    }

    /**
     * Creates a new entity at the specified location.
     * The entity types supported include "grass", "rabbit", "burrow", "bear", and "berry".
     * Throws an IllegalArgumentException if the entity type is null or unknown.
     *
     * @param entity  The type of the entity to create.
     * @param location  The location where the entity should be created.
     * @throws IllegalArgumentException If the entity type is null or unknown.
     */
    private void createEntity(String entity, Location location) {
        if(entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        switch (entity.toLowerCase()) {
            case "grass" -> new Grass(world, location);
            case "rabbit" -> new Rabbit(world, location);
            case "burrow" -> new RabbitHole(world, location);
            case "bear" -> new Bear(world, location);
            case "berry" -> new Bush(world, location);
            case "carcass" -> new Carcass(world, location, false);
            default -> throw new IllegalArgumentException("Unknown entity: " + entity);
        }
    }

    /**
     * Returns the current instance of the Program.
     *
     * @return the Program instance associated with this help.ResourceManager
     */
    public Program getProgram() {
        return program;
    }

}
