import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.border.TitledBorder;
/**
 * @author sylsau
 * @modified ShihYu Chang
 */
public class DrawAreaController extends JComponent {
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
    private Image image;
    private Graphics2D g2;
    private int currentX, currentY, oldX, oldY;
    public DrawAreaController() {
        setDoubleBuffered(false);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Please draw a digit",
                TitledBorder.LEFT,
                TitledBorder.TOP, sansSerifBold, Color.BLUE));
        addMouseListener(new DrawAreaObserver(this));
        addMouseMotionListener(new DrawAreaObserver(this));
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        g2.setPaint(Color.white);
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
	       g2.drawLine(oldX, oldY, currentX, currentY);
	       repaint();
	       oldX = currentX;
	       oldY = currentY;
	   }
	}
	
	public void getNowCordinate(int x, int y) {
		oldX = x;
		oldY = y;
	}
}