import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;

import java.awt.Color;

public class Rabbit extends Herbivore {
    RabbitHole rabbitHole;

    public Rabbit() {
        //randomize disse parametre
        super(0.8, 1.3);
    }

    @Override
    public void act(World world) {

    }

    @Override
    void eat(Eatable other) {

    }

    @Override
    public double onEaten() {
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
