package itumulator.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import itumulator.display.overlay.OverlayCanvas;
import itumulator.display.utility.DayNightHelper;
import itumulator.display.utility.ImageResourceCache;
import itumulator.display.utility.ImageUtility;
import itumulator.simulator.Simulator;

/**
 * Provides a frame for the {@link Canvas} and controls for the
 * {@link Simulator}. This is not relevant to continue the project.
 */
public class Frame extends JFrame {
    private final int UI_HEIGHT = 25;
    private JTextField textField;
    private JLayeredPane layeredPane;
    private JPanel uiPanel;
    private DayNightHelper dayNightHelper;
    private OverlayCanvas overlayCanvas;


    public Frame(Canvas canvas, Simulator simulator, int pixel_size, boolean startIso) {
        dayNightHelper = new DayNightHelper();
        overlayCanvas = new OverlayCanvas(pixel_size, startIso);
        

        // Setup Frame
        setResizable(false);
        setTitle("Itumulator");
        setSize(pixel_size+16, pixel_size+36);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ToolTipManager.sharedInstance().setInitialDelay(1);
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        // Set layered pane
        layeredPane = new JLayeredPane();
        add(layeredPane);

        setLayout(new GridLayout());

        // Setup Canvas/Renderer
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);
        canvas.setSize(pixel_size, pixel_size);

        // Setup Overlay
        layeredPane.add(overlayCanvas, JLayeredPane.PALETTE_LAYER);

        // Setup UI
        uiPanel = new JPanel();
        FlowLayout uiLayout = new FlowLayout(FlowLayout.RIGHT, 10, 5);
        uiPanel.setLayout(uiLayout);
        layeredPane.add(uiPanel, JLayeredPane.POPUP_LAYER);

        textField = createTooltipTextField("How many iterations the simulation has run");
        textField.setBackground(null);
        textField.setBorder(null);
        textField.setFont(new Font("CourierNew", Font.PLAIN, 16));
        textField.setText("Steps " + simulator.getSteps());
        
        textField.setEditable(false);
        setTextFieldWidth(textField);

        // Initialize play/pause button
        JButton runButton = createTooltipButton("Play/Pause", "play",  "Start / Stop");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!simulator.isRunning()) {
                    simulator.run();
                } else {
                    simulator.stop();
                }
            }
        });
        
        // Initialize Step button
        JButton stepButton = createTooltipButton("step", "step", "Step");
        stepButton.addActionListener((e) -> {
            if (!simulator.isRunning())
                simulator.simulate();
        });

        // Initialize DayNightLabel
        JLabel dayNightLabel = dayNightHelper.initialize(UI_HEIGHT);
        dayNightLabel.setVisible(!startIso);

        // Initialize Swap Render Button
        JButton swapButton = createTooltipButton("Swap", "basic-display", "Change view");
        swapButton.addActionListener((e) -> {
            canvas.setIsomorphic(!canvas.isIsomorphic());
            if (canvas.isIsomorphic()){
                overlayCanvas.startRender();
                dayNightLabel.setVisible(false);
            } else {
                overlayCanvas.stopRender();
                dayNightLabel.setVisible(true);
            }
            canvas.paintImage();
        });

        uiPanel.setBounds(0, 0, pixel_size, UI_HEIGHT+20);
        uiPanel.add(textField);
        uiPanel.add(dayNightLabel);
        uiPanel.add(runButton);
        uiPanel.add(stepButton);
        uiPanel.add(swapButton);
        uiPanel.setOpaque(false);
    }

    
    public void updateDayNightLabel(boolean isDaytime) {
        dayNightHelper.update(isDaytime);
    }

    private void setTextFieldWidth(JTextField field){
        int preferredWidth = textField.getFontMetrics(textField.getFont()).stringWidth(textField.getText()) + 5; // Add padding
        field.setSize(new Dimension(preferredWidth, textField.getPreferredSize().height));
    }

    public void updateStepLabel(int steps) {
        textField.setText("Steps " + steps);

        setTextFieldWidth(textField);
        textField.setSize(textField.getPreferredSize());
        textField.revalidate();
        textField.repaint();
        uiPanel.revalidate();
        uiPanel.repaint();
    }

    private JButton createTooltipButton(String name, String imageKey, String tooltip){
        JButton b = new ReactingImageButton(name, imageKey);
        b.setToolTipText(tooltip);
        return b;
    }

    private JTextField createTooltipTextField(String tooltip){
        JTextField t = new JTextField(){

            public JToolTip createToolTip(){
                JToolTip tip = super.createToolTip();
                tip.setBackground(Color.WHITE);
                return tip;
            }
        };
        t.setToolTipText(tooltip);
        return t;
    }

    private class ReactingImageButton extends JButton{
        float alpha = 1f;

        public ReactingImageButton(String name, String imageKey){
            super(name);
            BufferedImage img = ImageResourceCache.Instance().getImage(imageKey);
            double ratio = (UI_HEIGHT * 1.0) / img.getHeight();
            BufferedImage scaledImg = ImageUtility.getScaledImage(img, (int)(ratio * img.getWidth()), UI_HEIGHT);
            ImageIcon imgIcon = new ImageIcon(scaledImg);
            setIcon(imgIcon);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(null);
            setText("");

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    alpha = 0.5f;
                    repaint();
                }
                
                public void mouseExited(MouseEvent e){
                    alpha = 1f;
                    repaint();
                }
            });
        }

        public JToolTip createToolTip(){
                JToolTip tip = super.createToolTip();
                tip.setBackground(Color.WHITE);
                return tip;
        }

        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintComponent(g);
        }
    }
}
