import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.Random;


public class Rabbit extends Animal {
    private RabbitHole hole;
    private boolean insideHole;


    public Rabbit(World world, Location location) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);

    }

    @Override
    public void act(World world) {
        if(world.isDay()) {
            dayTimeBehaviour(world);
        }

        if(world.isNight()) {
            nightTimeBehaviour(world);
        }
    }

    private void age(World world) {
        this.age++;
        this.energy -= this.energyDecay;

        if(energy <= 0 || age >= 100) {
            die();
            return;
        }
    }
    private void nightTimeBehaviour(World world) {
        if(insideHole) {
            return;
        }

        if(this.hole == null) {
            if(world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
                this.hole = e;
                return;
            }

            digHole(world, world.getLocation(this));

            if(this.hole == null) {
                wander(world);
            }

            return;
        }

        pursue(world, this.hole.getLocation());
        enterHole(world, world.getCurrentLocation());
    }
    private void dayTimeBehaviour(World world) {
        System.out.println(this.energy);
        age(world);

        if(this.insideHole) {
            exitHole(world);
            return;
        }

        if(this.hole == null && world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
            this.hole = e;
        }

        Location nearestFood = Utils.closestConsumableEntity(world,this, this.searchRadius);

        if(nearestFood == null) {
            wander(world);
        } else{
            pursue(world, nearestFood);
        }


        if (world.getNonBlocking(world.getLocation(this)) instanceof Organism o) {
            if(canEat(o)) {
                this.consume(o, world);
            }
        }
    }
    private void digHole(World world, Location location) {
        if(this.hole != null || world.containsNonBlocking(location)) {
            return;
        }

        this.hole = new RabbitHole(world, location);
    }
    private void digExit(World world, Location location) {
        if(world.containsNonBlocking(location)) {
            return;
        }

        if(this.hole != null) {
            this.hole = new RabbitHole(world, location, this.hole.getHoles());
            return;
        }

        this.hole = new RabbitHole(world, location);
    }
    private void enterHole(World world, Location currentLocation) {
        if(this.hole == null) {
            return;
        }

        Location holeLocation = this.hole.getLocation();
        if (currentLocation.getX() != holeLocation.getX() || currentLocation.getY() != holeLocation.getY()) {
            return;
        }

        world.remove(this);
        this.insideHole = true;
    }
    private void exitHole(World world) {
        Location holeLocation = this.hole.getRandomExit(world);

        if(!world.isTileEmpty(holeLocation)) {
            Location newLocation = Utils.getValidRandomLocation(world);
            digExit(world, newLocation);
            holeLocation = newLocation;
        }

        if(new Random().nextDouble() < 0.3) {
            Location newLocation = Utils.getValidRandomLocation(world);
            digExit(world, newLocation);
            holeLocation = newLocation;
        }

        world.setTile(holeLocation, this);
        insideHole = false;
    }

    @Override
    public DisplayInformation getInformation() {
        if(age > 6) {
            return new DisplayInformation(Color.WHITE, "rabbit-large");
        }
        return new DisplayInformation(Color.WHITE, "rabbit-small");
    }
}
