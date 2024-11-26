import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.List;


public class Rabbit extends Animal {
    private RabbitHole hole;
    private boolean insideHole;
    private boolean canBreed;


    public Rabbit(World world, Location location) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);
        this.canBreed = true;
        this.insideHole = false;
        this.hole = null;
    }

    public Rabbit(World world, RabbitHole hole) {
        super(Species.Rabbit, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        this.insideHole = true;
        this.hole = hole;
        this.canBreed = false;
        world.add(this);
        System.out.println("sex happened");
    }

    @Override
    public void act(World world) {
        if(isDead) {
            return;
        }

        if(world.isDay()) {
            dayTimeBehaviour(world);
        }

        if(world.isNight()) {
            nightTimeBehaviour(world);
        }
    }

    private void age(World world) {
        this.age++;
        //this.energy -= this.energyDecay;

        if(energy <= 0 || age >= 100) {
            die();
            world.delete(this);
        }
    }
    private void nightTimeBehaviour(World world) {
        // If in hole, reproduce
        if(insideHole) {
            return;
        }

        if(this.hole == null) {
            if(world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
                this.hole = e;
                return;
            }


            if(new Random().nextDouble() < 0.3) {
                digHole(world, world.getLocation(this));
            } else {
                Location nearestHole = Utils.getClosestRabbitHole(world, world.getLocation(this), this.searchRadius);

                if(nearestHole != null) {
                    pursue(world, nearestHole);
                    return;
                }
            }

            if(this.hole == null) {
                wander(world);
            }

            return;
        }

        pursue(world, this.hole.getLocation());
        enterHole(world, world.getCurrentLocation());
    }
    private void dayTimeBehaviour(World world) {
        // Increment age
        age(world);

        // Exit hole at dawn
        if(this.insideHole) {
            exitHole(world);
            return;
        }

        reproduce(world);

        this.canBreed = true;

        if(this.hole == null && world.getNonBlocking(world.getLocation(this)) instanceof RabbitHole e) {
            this.hole = e;
        }

        // If stands on eatable organism, eat it
        if (world.getNonBlocking(world.getLocation(this)) instanceof Organism o) {
            if(canEat(o)) {
                this.consume(o, world);
                return;
            }
        }

        // Seeks nearby food or wander randomly
        Location target = Utils.closestConsumableEntity(world,this, this.searchRadius);
        if(target == null) {
            target = Utils.getClosestRabbitHole(world, world.getLocation(this), this.searchRadius);
        }

        if (target == null) {
            wander(world);
        }

        else {
            pursue(world, target);
        }

    }

    private void reproduce(World world) {
        if(!this.wantsToBreed() || this.hole == null) {
            return;
        }

        Object[] neighbours = world.getSurroundingTiles(world.getLocation(this)).toArray();
        for(Object o : neighbours) {
            if(o instanceof Rabbit rabbit) {
                if(rabbit.wantsToBreed()) {
                    new Rabbit(world, this.hole);
                    rabbit.toggleBreedStatus();
                    toggleBreedStatus();
                    break;
                }
            }
        }




        /*
                // Reproduce if meet another rabbit
        // The chance of reproduction upon meeting: 1 / repChance
        Random rand = new Random();
        ArrayList<Location> neighbours = new ArrayList<>(world.getSurroundingTiles(world.getLocation(this)));
        if (!neighbours.isEmpty()) {
            for (Location l : neighbours) {
                if (world.getTile(l) instanceof Rabbit && 0 == rand.nextInt(repChance) && age >= 5) {
                    for (Location l2 : neighbours) {
                        if (world.isTileEmpty(l2)) {
                            world.setCurrentLocation(l2);
                            world.setTile(l2, new Rabbit());
                            energy = energy - 4;
                            return;
                        }
                    }
                }
            }
        }
         */

    }

    public boolean isInsideHole() {
        return this.insideHole;
    }

    public void toggleBreedStatus() {
        this.canBreed = !this.canBreed;
        System.out.println("breed status: " + this.canBreed);
    }

    private boolean wantsToBreed() {
        return this.canBreed;
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

        if(location == null) {
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

        if(holeLocation == null) {
            return;
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
