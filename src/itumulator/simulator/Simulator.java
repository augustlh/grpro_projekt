package itumulator.simulator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import itumulator.display.Canvas;
import itumulator.display.Frame;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Simulator handles the execution of actual simulations and keeps track of how many steps have been executed (Not required to execute and build simulations).
 */
public class Simulator {
    private World world;
    private Canvas canvas;
    private int steps;
    private AtomicBoolean running;
    private ExecutorService executor;
    private int delay;
    private Frame frame;

    /**
     * Initializes a new simulation based on an existing world, canvas, and initial delay.
     * @param world the world to simulate.
     * @param canvas the canvas to use for simulation.
     * @param delay between executing simulations in ms (when using {@link run() run}).
     */
    public Simulator(World world, Canvas canvas, int delay) {
        this.world = world;
        this.canvas = canvas;
        running = new AtomicBoolean(false);
        this.delay = delay;
    }

    /**
     * Sets the containing frame to allow updating of meta world information (day/night cycle).
     * @param frame to use.
     */
    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    /**
     * Provides the amount of iteration steps executed
     * @return steps executed.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Provides the current delay
     * @return the delay currently employed.
     */
    public int getDelay(){
        return delay;
    }

    /**
     * Simulate am iteration.
     */
    public void simulate() {
        // increment both internal tracking of steps as well as for the world.
        steps++; 
        world.step();
        // update frame settings regarding change of cycle
        frame.updateDayNightLabel(world.isDay());
        frame.updateStepLabel(steps);

        // iterate all actors of the world and execute their actions.
        Map<Object, Location> entities = world.getEntities();
        for(Object o : entities.keySet()){
            if(o instanceof Actor){
                if(!world.contains(o)) continue;
                Location l = null;
                if(world.isOnTile(o)) l = world.getLocation(o);
                world.setCurrentLocation(l);
                ((Actor)o).act(world);
            }
        }
        entities = null; // to avoid memory leak
        System.gc(); // force garbage collection to reduce leak
        // Here removed painting cycle (i.e., canvas.paintImage()) as I believe it was unecessary.
        canvas.paintImage(delay); //repaint according to updated simulation.
    }

    /**
     * Provides a status of whether {@link run() run} is currently being executed.
     * @return true if {@link run() run} is currently in progress.
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Updates the delay to be used.
     * @param delay to use in ms.
     * @throws IllegalArgumentException
     *          if delay is negative.
     * @throws IllegalStateException
     *          if modifying delay while running {@link run() run}.
     *      
     */
    public void setDelay(int delay){
        if(isRunning()) throw new IllegalStateException("Cannot modify delay while executing run");
        if(delay < 0) throw new IllegalArgumentException("Delay cannot be a negative number");
        this.delay = delay;
    }

    /**
     * Stops an execution of {@link run() run}.
     * @throws IllegalStateException
     *              if {@link run() run} is not currently executing.
     */
    public synchronized void stop(){
        if(!isRunning()) throw new IllegalStateException("No current execution to stop");
        executor.shutdownNow();
        canvas.reduceImgQueue();
        running.set(false);
    }

    /**
     * Executes simulation steps in a parallel process. Can be stopped using {@link stop() stop}.
     */
    public synchronized void run() {
        if (isRunning())
            return;
        running.set(true);
        // necessary to avoid blocking EDT / swing thread, we create a new interruptable executor service.
        
        // Optionally to reduce the memory of creating a new executor etc. every time, we should consider making
        // a pausable runnable, this is more in line with concurrency architecture, instead of Hard
        // interrupting a thread
        if(executor != null) executor.shutdownNow();
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(!isRunning()){
                            Thread.currentThread().interrupt();
                            return;
                        }
                        canvas.acquireRenderPermit(); // to not produce too many images
                        simulate();
                        if (delay == 0){
                            continue;
                        }
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

}
