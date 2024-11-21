import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rabbit extends Herbivore {
    RabbitHole rabbitHole;
    Random rand = new Random();

    public Rabbit() {
        //randomize disse parametre
        super(0.8, 1.3);
        this.age = 0;
        this.energy = 10;
    }

    @Override
    public void act(World world) {
        location = world.getLocation(this);
        //energy--;
        age++;

        if(!world.contains(this)){
            world.setCurrentLocation(location);
            world.setTile(location,this);
        }

        if(energy <= 0 || age >= 100) {
            System.out.println("dead");
            world.delete(this);
        }

        // If there's grass, eat it
        if (world.getNonBlocking(location) instanceof Grass && world.isDay()) {
            System.out.println("hej");
            this.eat((Eatable) world.getNonBlocking(location),world);
            return;
        }

        // Get neighbouring tiles
        List<Location> neighbours = new ArrayList<Location>(world.getEmptySurroundingTiles());

        if(neighbours.isEmpty()) {
            return;
        }

        // Moves to a neighbouring tile with grass
        for (Location loc : neighbours) {
            if (world.getNonBlocking(loc) instanceof Grass) {
                location=loc;
                world.setCurrentLocation(loc);
                world.move(this,loc);
                return;
            }
        }

        // If no neighbouring grass, move randomly
        location = neighbours.get(rand.nextInt(neighbours.size()));
        world.setCurrentLocation(location);
        world.move(this, location);

        // Reproduce if meet another rabbit
        neighbours = new ArrayList<>(world.getSurroundingTiles(location));
        if (neighbours.isEmpty()) {
            return;
        }
        for (Location l : neighbours) {

            if (world.getTile(l) instanceof Rabbit && 0 == rand.nextInt(4)) {
                for (Location l2 : neighbours) {
                    if (world.isTileEmpty(l2)) {
                        world.setCurrentLocation(l2);
                        world.setTile(l2, new Rabbit());
                        return;
                    }
                }
            }
        }
    }

    //@Override
    void eat(Eatable other, World world) {
        energy += other.getEnergy();
        other.eaten(world);
    }

    @Override
    public double getEnergy() {
        return this.energy;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "rabbit-large");
    }

    public RabbitHole getRabbitHole() {
        return this.rabbitHole;
    }




}
