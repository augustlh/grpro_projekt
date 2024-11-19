package itumulator.display.overlay;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

/**
 * A class to show the overlay (i.e., buttons, information)
 */
public class OverlayCanvas extends JPanel{
    private final int CLOUD_AMOUNT = 7;
    private final int MS_PER_FRAME = 10;

    private int pixelSize;
    private List<Cloud> clouds;
    private BufferedImage img;
    private Graphics graphics;
    
    private AtomicBoolean running;
    private ExecutorService executor;

    public OverlayCanvas(int pixelSize, boolean startIso) {
        super();
        this.pixelSize = pixelSize;
        Dimension d = new Dimension(pixelSize, pixelSize);
        setPreferredSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        setSize(d);
        setOpaque(false);
        setBackground(null);
        running = new AtomicBoolean(false);
        this.clouds = new ArrayList<>();
        for (int i = 0; i < CLOUD_AMOUNT; i++) {
            clouds.add(new Cloud(pixelSize));
        }
        if (startIso)
        {
            startRender();
        }
    }

    public void render(boolean seeThrough){
        img = new BufferedImage(pixelSize, pixelSize, BufferedImage.TYPE_INT_ARGB);
        graphics = img.getGraphics();
        ((Graphics2D)graphics).setComposite(AlphaComposite.Clear);
        graphics.fillRect(0,0,pixelSize,pixelSize);
        ((Graphics2D)graphics).setComposite(AlphaComposite.SrcOver);

        if (!seeThrough){
            for (Cloud cloud : clouds) {
                cloud.draw(graphics);
            }
        }
        repaint();
    }

    public synchronized void stopRender(){
        if(!running.get()) throw new IllegalStateException("No current execution to stop");
        executor.shutdownNow();
        running.set(false);
    }

    public synchronized void startRender(){
        if (running.get())
            return;
        running.set(true);
        executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable(){
            @Override
            public void run() {
                while(true){
                    if(Thread.interrupted()) 
                        return;
                    render(false);
                    try {
                        Thread.sleep(MS_PER_FRAME);
                    } catch (InterruptedException e) {
                        render(true);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        if (img != null){
            g.drawImage(img, (this.getWidth()/2)-(pixelSize/2), (this.getHeight()/2)-(pixelSize/2), null);
        }
    }
}
