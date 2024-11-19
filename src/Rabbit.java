import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.Color;

public class Rabbit extends Herbivore {
    private Hole hole;

    public Rabbit() {
        this.hole = null;
        this.state = State.ACTIVE;
    }

    @Override
    void eat(Eatable eatable) {

    }

    @Override
    void reproduce() {

    }

    @Override
    public void act(World world) {

    }

    @Override
    public DisplayInformation getInformation() {
        if(this.state == State.ACTIVE) {
            return new DisplayInformation(Color.white, "rabbit-small");
        } else {
            return new DisplayInformation(Color.white, "rabbit-small-sleeping");
        }
    }

    private void digHole() {

    }
}
