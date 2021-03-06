import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
/**
 * @author ShihYu Chang
 * @created on 10/07/2020
 */
public class DrawAreaObserver implements MouseMotionListener, MouseListener{
	DrawAreaController drawArea;
	public DrawAreaObserver(DrawAreaController drawArea) {
		this.drawArea = drawArea;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
        this.drawArea.drawImage(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.drawArea.getNowCordinate(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
