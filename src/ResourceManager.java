import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;


public class ResourceManager {
    private Program program;

    public ResourceManager(String filepath, int displaySize, int delay) {
        try {
            initializeProgram(filepath, displaySize, delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    private int handleQuantity(String quantity) {
        if(quantity.contains("-")) {
            String[] values = quantity.split("-");
            return new Random().nextInt(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        }
        return Integer.parseInt(quantity);
    }

    private void spawnEntities(String entity, int quantity) {
        World world = program.getWorld();

        for (int i = 0; i < quantity; i++) {
            Object e = createEntity(entity);
            Location location = getValidRandomLocation(e, world);
            world.setCurrentLocation(location);
            world.setTile(location, e);

        }
    }

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

    public Program getProgram() {
        return program;
    }
}
