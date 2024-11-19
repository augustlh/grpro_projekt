package itumulator.executable;

import itumulator.display.Canvas;
import itumulator.display.Frame;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.simulator.Simulator;
import itumulator.world.World;

/**
 * Sets up the program to run simulations. The Program will handle creation of {@link World}, {@link Simulator}, {@link Canvas}, {@link Frame} and correctly
 * connect these. As such, one can instantiate a single {@link Program} and access relevant objects through this, as well as running the simulation.
 * The Program contains two views (changable by clicking the topic right button). It defaults to the isomorphic 2.5d perspective, unless the size of the map exceeds 20.
 */
public class Program {
    private final int MAX_2D_SIZE_DEFAULT = 20;
    private int size;
    private World w;
    private Canvas c;
    private Simulator s;
    private Frame f;

    /**
     * Produces a new program.
     * @param size the size of the world to render (will be square).
     * @param display_size of the graphical window.
     * @param delay inbetween simulation steps when running {@link run()}.
     */
    public Program(int size, int display_size, int delay){
        this.size = size;
        IsomorphicCoordinateFactory.setupFactory(display_size, size);
        w = new World(size);
        boolean startIso = size <= MAX_2D_SIZE_DEFAULT;

        c = new Canvas(w, display_size, startIso);
        s = new Simulator(w, c, delay);
        f = new Frame(c, s, display_size, startIso);
        s.setFrame(f);
    }
    
    /**
     * Provides the world which this program concerns itself with. Using this world object you can add new actors to the world.
     * @return {@link World} wherein one can add elements to simulate.
     */
    public World getWorld(){
        return w;
    }

    /**
     * Provides the simulator itself, responsible for execution of simulations (Not necessary to execute the simulation).
     * @return Simulator
     */
    public Simulator getSimulator(){
        return s;
    }

    /**
     * Provides the canvas itself, responsible for drawing contents (Not necessary to execute the simulation).
     * @return Canvas.
     */
    public Canvas getCanvas(){
        return c;
    }

    /**
     * Provides the frame itself, responsible for buttons (Not necessary to execute the simulation).
     * @return Frame.
     */
    public Frame getFrame(){
        return f;
    }

    /**
     * Provides the size of the world
     * @return int representing both x and y dimensions.
     */
    public int getSize(){
        return size;
    }

    /**
     * Used to initially show the graphical interface.
     */
    public void show(){
        f.setVisible(true);
        c.paintImage();
    }


    /**
     * Executes a single 'step' of simulation.
     */
    public void simulate(){
        s.simulate();
    }

    /**
     * Executes rounds of simulation using the delay (in ms) given.
     */
    public void run(){
        s.run();
    }

    /**
     * Adjust the delay (in ms) between {@link run() run} executions
     * @param delay in ms
     */
    public void setDelay(int delay){
        s.setDelay(delay);
    }

    /**
     * Get the current delay (in ms)
     * @return int.
     */
    public int getDelay(){
        return s.getDelay();
    }

    /**
     * Decide on the graphical representation of objects when shown within the GUI.
     * @param cl is the class which to associate a given display type with (can be accessed by writing [ClassName].class).
     * @param di the {@link DisplayInformation} to associate the type of object with.
     */
    public void setDisplayInformation(Class cl, DisplayInformation di){
        c.setDisplayInformation(cl, di);
    }

}
