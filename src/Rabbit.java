import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Rabbit is a specific type of Herbivore that exhibits particular behaviors such as eating grass,
 * moving to a rabbit hole when time is about to end, reproducing under certain conditions, and dying
 * when it runs out of energy or exceeds a certain age.
 */
public class Rabbit extends Herbivore {
    RabbitHole rabbitHole;

    /**
     * Constructs a new instance of the Rabbit class. This sets the Rabbit's age to 0 and its energy to 20.
     * The metabolism and energy decay rates are initialized to 0.8 and 1.3 respectively by calling the
     * superclass constructor.
     */
    public Rabbit() {
        //randomize disse parametre
        super(0.8, 1.3);
        this.age = 0;
        this.energy = 20;
    }

    /**
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        if (world.isNight() && this.rabbitHole != null) {
            //sohuld seek rabbithole if it its not there
            pursue(world, rabbitHole.getClosestHole(world.getLocation(this)));
            return;
        } else if (world.isNight() && this.rabbitHole == null) {
            //dig a hole
            //and enter it
            //seeks random locatgion to test
            pursue(world, new Location(1,1));
            return;
        }


        if(energy <= 0 || age >= 100) {
            System.out.println("dead");
            world.delete(this);
            return;
        }

        wander(world);

        // If there's grass, eat it
        if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
            this.eat((Eatable) world.getNonBlocking(world.getLocation(this)),world);
        }

        //dunno how this works, det må i fikse :)
//        // Reproduce if meet another rabbit
//        neighbours = new ArrayList<>(world.getSurroundingTiles(location));
//        if (neighbours.isEmpty()) {
//            return;
//        }
//        for (Location l : neighbours) {
//            if (world.getTile(l) instanceof Rabbit && 0 == rand.nextInt(5) && age >=10) {
//                for (Location l2 : neighbours) {
//                    if (world.isTileEmpty(l2)) {
//                        world.setCurrentLocation(l2);
//                        world.setTile(l2, new Rabbit());
//                        energy=energy-4;
//                        return;
//                    }
//                }
//            }
//        }
    }
    
    /**
     * Allows the Rabbit to consume another Eatable object, increasing its energy and marking the other object as eaten.
     *
     * @param other the Eatable object that the Rabbit will consume.
     * @param world the context in which the eating action takes place, providing necessary details about the environment.
     */
    //@Override
    void eat(Eatable other, World world) {
        energy += metabolism * other.getNutritionalValue();
        other.onEaten(world);
    }

    /**
     * Retrieves the current energy level of the Rabbit.
     *
     * @return the current energy level of the Rabbit.
     */
    @Override
    public double getNutritionalValue() {
        return this.energy;
    }


    /**
     * Retrieves display information about the Rabbit, including its color and associated image.
     *
     * @return a DisplayInformation object containing the color and image key for the Rabbit.
     */
    @Override
    public DisplayInformation getInformation() {

        return new DisplayInformation(Color.red, "rabbit-large");
    }

    public RabbitHole getRabbitHole() {

        return this.rabbitHole;
    }




}
