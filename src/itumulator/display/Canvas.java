package itumulator.display;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import itumulator.display.animation.AnimationFactory;
import itumulator.display.utility.ImageResourceCache;
import itumulator.display.utility.ImageUtility;
import itumulator.display.utility.IsomorphicCoordinateFactory;
import itumulator.display.utility.IsomorphicUtility;
import itumulator.display.utility.Point2DInt;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Provides a canvas painting the various objects within our world. This is not relevant to continue the project.
 */
public class Canvas extends JPanel {
    private final static Color COLOR_EMPTY = Color.WHITE; // used as the color representing empty vlaues
    private final static Color COLOR_NON_PAINTABLE = Color.GRAY; // used as as the color for elements not associated with a color
    private final static int MS_PER_FRAME = 4; 
    private final static int SLOW_DOWN_FRAMES = 6; // used to reduce the queued amount of images (for render)
    private final static int IMAGE_CACHE_SIZE = 60; // to avoid memory overflow, correct this one
    private final static int RENDER_PERMITS = 2; // to control how long the simulator awaits next simulate step
    
    private World world; 
    private Graphics graphics;
    private int size;
    private boolean isomorphic;
    private BufferedImage isoBackgroundImage;
    private AnimationFactory af;
    private java.util.Map<Class, Color> colorMap;
    List<Image> queue; // queue of images to render
    private boolean lastView; // used to clear queue of images
    private ExecutorService executor;
    private Semaphore renderPermits;
    private int renderCount;
    private int lastDelay;
    
    public Canvas(World world, int size, boolean startIso) {
        super();

        // 1 main thread
        // 1 simulator thread
        // 1 java thread
        // 1 overlay rendering thread
        // 1 buffer for safety
        // rest for rendering
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(availableProcessors > 5 ? availableProcessors - 5 : 3);

        setLayout(new BorderLayout());
        Dimension d = new Dimension(size, size);
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        setSize(d);

        this.world = world;
        this.size = size;
        this.renderPermits = new Semaphore(RENDER_PERMITS);
        this.af = new AnimationFactory(world);
        new Random();
        colorMap = new java.util.HashMap<>();
        queue = new LinkedList<>();
        BufferedImage img = ImageResourceCache.Instance().getImage("base");
        isoBackgroundImage = ImageUtility.getScaledImage(img, IsomorphicCoordinateFactory.Instance().getDisplaySize(), IsomorphicCoordinateFactory.Instance().getDisplaySize());
        setIsomorphic(startIso);
    }

    /**
     * @return if using isomorphic renderer
     */
    public boolean isIsomorphic() {
        return isomorphic;
    }

    /**
     * Acquire a new render permit (total permits acccording to RENDER_PERMITS)
     */
    public void acquireRenderPermit(){
        try {
            if(isomorphic) renderPermits.acquire();
        } catch (Exception e){{
            // error might occur when pausing when trying to acquire permit.
        }}
        
    }

    /**
     * Boolean to control whether to render isomorphic or topdown
     * @param isomorphic sets the boolean
     */
    public void setIsomorphic(boolean isomorphic) {
        this.isomorphic = isomorphic;
        // in case we are swapping view we want to clear the queue of images.
        if(lastView != isomorphic) {
            lastView = isomorphic;
            queue.clear();
            int available = renderPermits.availablePermits();
            int missing = RENDER_PERMITS - available;
            renderPermits.release( missing > 0 ? missing : 0);
            paintImage();
        }
        // and request an update.
        if (isomorphic){
            af.requestUpdate();
        }
    }

    /**
     * Decide on the graphical representation of objects when shown within the GUI.
     * @param cl is the class which to associate a given display type with (can be accessed by writing [ClassName].class).
     * @param di the {@link DisplayInformation} to associate the type of object with.
     */
    public void setDisplayInformation(Class cl, DisplayInformation di){
        colorMap.put(cl, di.getColor());
        af.setDisplayInformation(cl, di);
    }

    /**
     * Paints the proper image depending on whether we are showing grid or isomorphic view (with a delay of 0)
     */
    public void paintImage(){
        this.paintImage(0);
    }

    /**
     * Paints the proper image depending on whether we are showing grid or isomorphic view.
     * @param delay
     */
    public void paintImage(int delay) {
        this.lastDelay = delay;
        if (isomorphic){
            try {
                // Queue all images for parallel execution
                List<Future<BufferedImage>> queue = executor.invokeAll(af.getImages(delay == 0 ? 1 : delay/MS_PER_FRAME));
                
                /** draw each of the images with the appropriate background using isomorphic helpers */
                for (Future<BufferedImage> future : queue) {
                    Image img = createImage(size, size);
                    graphics = img.getGraphics();
                    graphics.drawImage(isoBackgroundImage, 0,IsomorphicCoordinateFactory.Instance().getDisplaySize()/2, null);
                    graphics.setColor(new Color(150, 210, 131));
                    graphics.fillPolygon(IsomorphicUtility.getIsoPolygon((IsomorphicCoordinateFactory.Instance().getDisplaySize()/2), IsomorphicCoordinateFactory.Instance().getDisplaySize()/2, IsomorphicCoordinateFactory.Instance().getDisplaySize()/2, IsomorphicCoordinateFactory.Instance().getDisplaySize()/4));
                    graphics.drawImage(future.get(), 0, 0,null);
                    this.queue.add(img);
                    repaint(); // bad practice, but appears to help rendering time on windows machines
                }
                System.gc();
            } catch (Exception e) {
                // This can happen because we start tasks and
                // futures from within the simulator thread
                // and it can't stacktrace and locate
                // the exception error
                if (e.getMessage() != null){
                    if(e instanceof NullPointerException && e.getMessage().contains("img")){
                        // image not loaded yet...
                    } else {   
                        System.out.println("Canvas thread error: " + e.getMessage() );
                    }
                    
                }
            }
        } else {
            /** For the non isomorphic view we simply use the colors to paint each square */
            try { 
                Image img = createImage(size, size);
                this.queue.add(img);
                graphics = img.getGraphics();

                graphics.setColor(COLOR_EMPTY);
                graphics.fillRect(0, 0, size, size);
                int tiles = world.getSize();
                for (int y = tiles-1; y >= 0; y--) {
                    for (int x = 0; x < tiles; x++) {
                        Location l = new Location(x, y);
                        Object o = world.getTile(l);
                        drawGridElement(l, o);
                    }
                }
                repaint();
            } catch (Exception e){
                // error might occur due to too fast image switching
            }
        }
    }

    /**
     * Draws each element as a square
     * @param l location to write the element on
     * @param o the object to draw (used to determine color)
     */
    private void drawGridElement(Location l, Object o) {
        int tiles = world.getSize();
        int pixelSize = size / tiles;
        Point2DInt pixelPoint;
        pixelPoint = new Point2DInt(pixelSize * l.getX(), pixelSize * l.getY());

        graphics.setColor(new Color(250, 250, 250));
        graphics.fillRect(pixelPoint.getX(), pixelPoint.getY(), pixelSize, pixelSize);
        
        // if the element is nonBlocking, draw it as a flat square
        if (world.containsNonBlocking(l) && world.getNonBlocking(l) != o)
            drawGridElement(l, world.getNonBlocking(l));

        // if a dynamic display information provider is used, dynamically determine the color
        if (o instanceof DynamicDisplayInformationProvider){
                setDisplayInformation(o.getClass(), ((DynamicDisplayInformationProvider)o).getInformation());
        }

        // otherwise, we simply draw it (if it is given a color)
        if (o != null){
            if (colorMap.containsKey(o.getClass())) {
                graphics.setColor(colorMap.get(o.getClass()));
            } else {
                graphics.setColor(COLOR_NON_PAINTABLE);
            }
            int x = pixelPoint.getX();
            int y = pixelPoint.getY();
            int width = pixelSize;
            int height = pixelSize;
            graphics.fillRect(x, y, width, height);
        }
    }

    /**
     * Override of the paintComponent to deplete from the queue of images left to render.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if(!queue.isEmpty()){
            reduceImgQueue(IMAGE_CACHE_SIZE); // in case of issues, maintain reduction of image queue
            Image img = queue.get(0);
            if(queue.size() > 1) queue.remove(0);
            g.drawImage(img, (this.getWidth()/2)-(size/2), (this.getHeight()/2)-(size/2), null);
            
            if(isomorphic){
                renderCount++;
                if(renderCount >= (lastDelay == 0 ? 0 : lastDelay / MS_PER_FRAME)){
                    renderCount = 0;
                    if(renderPermits.availablePermits() < RENDER_PERMITS) renderPermits.release();
                }
            }
            
        }
            
    }

    /**
     * To speed up the process of pausing, you can reduce the amount of remaining frames (according to SLOW_DOWN_FRAMES).
     */
    public void reduceImgQueue(){
        reduceImgQueue(SLOW_DOWN_FRAMES);
    }

    private void reduceImgQueue(int amount){
        int size = queue.size();
        if( size > amount*2){
            List<Image> imgs = new LinkedList<>();
            int nr = (size / (amount));
            for(int i = 0; i < size; i += nr){
                imgs.add(queue.get(i));
            }
            queue = imgs;
        }
    }

}
