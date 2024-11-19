package itumulator.display.animation.components;

import java.awt.image.BufferedImage;
import java.lang.Math;

import itumulator.display.utility.ImageResourceCache;
import itumulator.display.utility.ImageUtility;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.display.utility.Point2DInt;
import itumulator.world.World;

/**
 * A special case animation used to show the sun / moon cycle.
 */
public class DayNightAnimation extends Animation {
    private final double IMAGE_HEIGHT_PERCENT = 0.15;
    private final double Y_POS_PERCENT = 0.2;
    private BufferedImage img;
    private boolean isDay;
    private boolean isDayPrime;
    private int timePrime;

    public DayNightAnimation(int animationLength, boolean day, boolean dayPrime, int timePrime) {
        super(null, null, animationLength, true);
        this.isDay = day;
        this.isDayPrime = dayPrime;
        this.timePrime = timePrime;
        setNewImage(isDay);
    }

    @Override
    protected AnimationFrame getFrame(int index) {
        if (animationLength / 2 == index){
            
        }

        if (isDay != isDayPrime){
                setNewImage(isDayPrime);
            }
        
        int dayPixelWidth = (IsomorphicCoordinateFactory.Instance().getDisplaySize()-(img.getWidth()/2))/World.getDayDuration();

        int actualTime = timePrime % World.getDayDuration();
        int relativeYPos = (int)(IsomorphicCoordinateFactory.Instance().getDisplaySize() * Y_POS_PERCENT);

        Point2DInt startPixelPoint = new Point2DInt((dayPixelWidth * actualTime) - (int)(img.getWidth()/2), relativeYPos);

        Point2DInt endPixelPoint = startPixelPoint.interpolate(new Point2DInt((startPixelPoint.getX() + dayPixelWidth), relativeYPos), (1.00 * index) / animationLength);

        double y = -Math.sin((actualTime + ((1.00 * index) / animationLength))*(Math.PI/World.getDayDuration())) * relativeYPos * 1.0;

        endPixelPoint.setY((int)y + (relativeYPos));

        return new AnimationFrame(img, endPixelPoint);
    }

    private void setNewImage(boolean day){
        if (day){
            img = ImageResourceCache.Instance().getImage("sun");
        } else {
            img = ImageResourceCache.Instance().getImage("moon");
        }
        double ratio = (IsomorphicCoordinateFactory.Instance().getDisplaySize() * IMAGE_HEIGHT_PERCENT) / img.getHeight();
        img = ImageUtility.getScaledImage(img, (int)(ratio * img.getWidth()), (int)(IsomorphicCoordinateFactory.Instance().getDisplaySize() * IMAGE_HEIGHT_PERCENT));
    }
}
