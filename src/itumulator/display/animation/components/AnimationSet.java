package itumulator.display.animation.components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import itumulator.display.utility.IsomorphicCoordinateFactory;

/**
 * AnimationSets are used to parallelise the drawing of individual frames (by aggregating different object drawings, i.e., {@link AnimationFrame})
 */
public class AnimationSet implements Callable<BufferedImage>{
    private List<AnimationFrame> frames;

    public AnimationSet(){
        frames = new ArrayList<>();
    }

    public void add(AnimationFrame frame){
        frames.add(frame);
    }

    @Override
    public BufferedImage call() {
        int isoSize = IsomorphicCoordinateFactory.Instance().getDisplaySize();
        BufferedImage image = new BufferedImage(isoSize, isoSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        for (AnimationFrame animationFrame : frames) {
            animationFrame.draw(graphics);
        }
        frames = null;
        return image;
    }
    
}
