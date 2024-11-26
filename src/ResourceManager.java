import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;


public class ResourceManager {
    private Program program;
    private World world;

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
        this.world = this.program.getWorld();

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
            Location location = Utils.getValidRandomLocation(world);
            createEntity(entity, location);
        }
    }

    private void createEntity(String entity, Location location) {
        if(entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        switch (entity) {
            case "grass" -> new Grass(world, location);
            case "rabbit" -> new Rabbit(world, location);
            case "burrow" -> new RabbitHole(world, location);
            default -> throw new IllegalArgumentException("Unknown entity: " + entity);
        }
    }

    public Program getProgram() {
        return program;
    }
}
