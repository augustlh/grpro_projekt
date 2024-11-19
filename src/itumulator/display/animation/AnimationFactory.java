package itumulator.display.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import itumulator.display.animation.components.Animation;
import itumulator.display.animation.components.AnimationSet;
import itumulator.display.animation.components.AppearAnimation;
import itumulator.display.animation.components.DayNightAnimation;
import itumulator.display.animation.components.HideAnimation;
import itumulator.display.animation.components.MoveAnimation;
import itumulator.display.animation.components.StillAnimation;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * The AnimationFactory is used to determine which animations should be used for individual objects. It does so by comparing e (previous entities and their location in {@link World}) to e' (latest set of entitites and their location in {@link World}).
 * By comparing the individual objects changes in location (and whether they still exist / are in the world) allow us to determine which animations to use.
 */
public class AnimationFactory {
    private World world;
    private boolean isDay;
    private boolean isDayPrime;
    private Map<Object, Location> e;
    private Map<Object, Location> ePrime;
    private Map<Object, ObjectInformation> objectMap;
    private Map<Class, DisplayInformation> displayMap;

    public AnimationFactory(World world){
        this.world = world;
        objectMap = new HashMap<>();
        displayMap = new HashMap<>();
        e = world.getEntities();
        isDay = world.isDay();
    }

    public void setDisplayInformation(Class cl, DisplayInformation di){
        displayMap.put(cl, di);
    }

    public void requestUpdate(){
        e = world.getEntities();
        isDay = world.isDay();
    }

    /**
     * Generates renders of animation sets for the next simulation step
     * @param length is the amount of images to generate
     * @return a list of callable bufferedimages, generatable
     */
    public List<Callable<BufferedImage>> getImages(int length){
        ePrime = world.getEntities();
        isDayPrime = world.isDay();
        
        if(e == null) return new ArrayList<>();
        

        List<Animation> animations = new ArrayList<>();

        for (Entry<Object, Location> kvp : e.entrySet()) {
            Object k = kvp.getKey();
            Location l = kvp.getValue();
            ObjectInformation oi = getObjectInformation(k);
            
            // if the object has been deleted, do hide animation
            if (!ePrime.containsKey(k)){
                // and that it hasn't already been hidden:
                if(e.get(k) != null) animations.add(new HideAnimation(oi, l, length));
                continue;
            } 

            Location lPrime = ePrime.get(k);
            
            // if the object has been put on the map do an appear animation.
            if (l == null && lPrime != null){
                animations.add(new AppearAnimation(oi, lPrime, length));
                continue;
            }
            if (lPrime == null){
                // if it remains hidden, don't do anything
                if (l == null){
                    continue;
                }
                
                //otherwise it must have been removed, therefore hide animation
                animations.add(new HideAnimation(oi, l, length));
                continue;
            }
            // if it stands still, use still animation
            if (l.equals(lPrime)){
                animations.add(new StillAnimation(oi, l, length));
                continue;
            }

            // otherwise it must have moved
            animations.add(new MoveAnimation(oi, l, length, lPrime));
        }

        // if the element is completely new, also do an appear animation (if it isn't hidden of course)
        for (Entry<Object, Location> kvp : ePrime.entrySet()){
            Object k = kvp.getKey();
            Location l = kvp.getValue();
            ObjectInformation oi = getObjectInformation(k);
            if (!e.containsKey(k)){
                if(l != null) animations.add(new AppearAnimation(oi, l, length));
            }
        }

        // sort the animations so that the elements in the front are rendered first
        animations.sort((Animation a, Animation b) -> {
            int ay = a.getLocation().getY();
            int by = b.getLocation().getY();
            if (ay == by)
                return 0;
            return ay < by ? -1 : 1;
        });

        // add the day/night cycle animation
        animations.add(new DayNightAnimation(length, isDay, isDayPrime, world.getCurrentTime()));

        // sort so that nonBlocking appear lowest (using built in comparator of animations)
        Collections.sort(animations);

        // zip the individual animationFrames of each animation to make everything move at once
        List<Callable<BufferedImage>> sets = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            AnimationSet set = new AnimationSet();
            for (Animation animation : animations) {
                set.add(animation.next());
            }
            sets.add(set);
        }

        // update e to e'
        if(ePrime != null) e = ePrime;
        isDay = isDayPrime;
        ePrime = null;
        return sets;
    }

    //  Helper method for determining object information
    private ObjectInformation getObjectInformation(Object obj){
        ObjectInformation oi;
        if (objectMap.containsKey(obj)){
            oi = objectMap.get(obj);
        } else {
            if (obj instanceof DynamicDisplayInformationProvider){
                oi = new ObjectInformation(obj);
            } else {
                if (!displayMap.containsKey(obj.getClass())){
                    throw new MissingResourceException("Missing DisplayInformation for " + obj.getClass(), obj.getClass().getName(), obj.getClass().getName());
                }
                oi = new ObjectInformation(obj, displayMap.get(obj.getClass()));
            }
            objectMap.put(obj, oi);
        }
        return oi;
    }
}
