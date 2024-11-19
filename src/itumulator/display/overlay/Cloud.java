package itumulator.display.overlay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import itumulator.display.utility.ImageResourceCache;
import itumulator.display.utility.ImageUtility;
import itumulator.display.utility.IsomorphicCoordinateFactory;

/**
 * A class to support the drawing of clouds
 */
public class Cloud {
    private final double IMAGE_HEIGHT_PERCENT = 0.12;
    private final double SPAWN_MIN_Y_PERCENT = 0;
    private final double SPAWN_MAX_Y_PERCENT = 0.4;
    private final double SPAWN_MIN_SPEED = 0.2; 
    private final double SPAWN_MAX_SPEED = 2.4; 
    private int pixelSize;
    private int currentY;
    private double currentSpeed;
    private double currentX;
    private BufferedImage img;
    private Random rnd;
    
    private String[] possibleClouds = new String[]{
        "cloud-1",
        "cloud-2",
        "cloud-3",
        "cloud-4",
        "cloud-5"
    };
    
    public Cloud(int pixelSize){
        this.pixelSize = pixelSize;
        rnd = new Random();
        reset();
        currentX = rnd.nextDouble() * pixelSize;
    }
    
    public void draw(Graphics g){
        if (currentX >= pixelSize){
            reset();
        }
        currentX += currentSpeed;
        g.drawImage(img, (int)currentX, currentY, null);
    }

    private void reset(){
        img = ImageResourceCache.Instance().getImage(
            possibleClouds[rnd.nextInt(possibleClouds.length)]);

        double ratio = (IsomorphicCoordinateFactory.Instance().getDisplaySize() * IMAGE_HEIGHT_PERCENT) / img.getHeight();
        img = ImageUtility.getScaledImage(img, (int)(ratio * img.getWidth()), 
        (int)(IsomorphicCoordinateFactory.Instance().getDisplaySize() * IMAGE_HEIGHT_PERCENT));

        double spawnYPercent = SPAWN_MIN_Y_PERCENT + (rnd.nextDouble()*SPAWN_MAX_Y_PERCENT);

        currentY = (int)(IsomorphicCoordinateFactory.Instance().getDisplaySize() * spawnYPercent);
        currentX = -img.getWidth();
        currentSpeed = SPAWN_MIN_SPEED + (rnd.nextDouble()*SPAWN_MAX_SPEED);
    }
}
