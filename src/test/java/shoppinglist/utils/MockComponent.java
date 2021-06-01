package shoppinglist.utils;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

public class MockComponent extends Component {
	
	private Rectangle bounds;
	private Point location;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void setBounds(Rectangle rect) {
		bounds = new Rectangle(rect);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	@Override
	public void setLocation(int x, int y) {
        location = new Point(x,y);
    }
	
	@Override
	public Point getLocation() {
		return location;
	}
	

}
