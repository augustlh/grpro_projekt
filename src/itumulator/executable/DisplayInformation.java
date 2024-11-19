package itumulator.executable;

import java.awt.Color;

/**
 * An abstraction used to determine the visual appearance of objects within the simulation.
 * Images have precedence over colors, however, if a class implements {@link DynamicDisplayInformationProvider}, this preceeds images.
 */
public class DisplayInformation {
    private Color color;
    private String imageKey;
    private boolean random_direction;

    /**
     * Only display using a color.
     * @param color to display as.
     */
    public DisplayInformation(Color color){
        if(color == null) throw new IllegalArgumentException("Color must be provided");
        this.color = color;
    }

    /**
     * Display using an image (and color when image is not applicable), allowing you to choose whether an icon can face a random direction.
     * @param color to fall back upon.
     * @param imageKey to the image
     * @param random_direction Whether or not the image can be placed in a random direction.
     */
    public DisplayInformation(Color color, String imageKey, boolean random_direction){
        if(color == null || imageKey == null) throw new IllegalArgumentException("Both parameters must be provided");
        this.color = color;
        this.imageKey = imageKey;
        this.random_direction = random_direction;
    }

    /**
     * Display using an image (and color when image is not applicable). Will always use the same image orientation.
     * @param color to fall back upon.
     * @param imageKey to the image
     */
    public DisplayInformation(Color color, String imageKey){
        this(color, imageKey, false);
    }

    /**
     * Get the color.
     * @return color.
     */
    public Color getColor(){
        return color;
    }

    /**
     * Get the imageKey of the image.
     * @return imageKey (string).
     */
    public String getImageKey(){
        return imageKey;
    }

    /**
     * Get whether or not the orientation of the image can be randomized.
     * @return true if allowed to randomize orientation.
     */
    public boolean getRandomDirection(){
        return random_direction;
    }
}
