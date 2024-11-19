import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;


public abstract class Animal implements Actor, DynamicDisplayInformationProvider {
    enum State {
        ACTIVE,
        SLEEPING,
    }

    protected int age;
    protected double energy;
    protected State state;

    abstract void eat(Eatable eatable);
    abstract void reproduce();
    //method to path to some field/felt.

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
