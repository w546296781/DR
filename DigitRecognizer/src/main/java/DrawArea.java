

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Component for drawing !
 *
 * @author sylsau
 */
public class DrawArea extends JComponent {

    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
    // Image in which we're going to draw
    private Image image;
    // Graphics2D object ==> used to draw on
    private Graphics2D g2;
    // Mouse coordinates
    private int currentX, currentY, oldX, oldY;
    public DrawArea() {
        setDoubleBuffered(false);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Please draw a digit",
                TitledBorder.LEFT,
                TitledBorder.TOP, sansSerifBold, Color.BLUE));
                oldX = e.getX();
        addMouseListener(new DrawAreaObserver(this));
        addMouseMotionListener(new DrawAreaObserver(this));
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            // image to draw null ==> we create
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        g2.setPaint(Color.white);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
	public void drawImage(int x, int y) {
		currentX = x;
		currentY = y;
	    if (g2 != null) {
	       g2.setStroke(new BasicStroke(10));
	       // draw line if g2 context not null
	       g2.drawLine(oldX, oldY, currentX, currentY);
	       // refresh draw area to repaint
	       repaint();
	       // store current coords x,y as olds x,y
	       oldX = currentX;
	       oldY = currentY;
	   }
	}
	
	public void getNowCordinate(int x, int y) {
		oldX = x;
		oldY = y;
	}
}