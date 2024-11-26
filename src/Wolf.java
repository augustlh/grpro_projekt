import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Wolf extends Animal {

    private boolean alpha;
    private List<Wolf> pack;

    public Wolf(World world, Location location) {
        super(Species.Wolf, new Random().nextDouble(), new Random().nextDouble(1, 2), 1);
        world.setTile(location, this);
        alpha = false;
        pack = null;
    }

    @Override
    public void act(World world) {
        // Stops act if dead
        age(world);
        if(isDead){
            return;
        }

        // Eats nearby food
        List<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());
        for (Location loc:neighbours){
           if (world.getTile(loc) instanceof Organism o){
                if(this.canEat(o)){
                    this.consume(o,world);
                    world.move(this,loc);
                    return;
                }
           }
        }
        
        // Alpha moves
        if(alpha) {
            this.wander(world);
        }

        // Follow alpha
        if(!alpha) {
            this.pursue(world, world.getLocation(pack.getFirst()));
        }
        
    }
    
    

    private void age(World world){
        this.age ++;
        this.energy -=energyDecay;
        if (energy <=0 || age >=100){
            die();
            world.delete(this);
        }
    }

    public void setThisAlpha(boolean b) {
        this.alpha = b;
    }

    public void setPack(List<Wolf> wolves) {
        this.pack=wolves;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "wolf");
    }



}
