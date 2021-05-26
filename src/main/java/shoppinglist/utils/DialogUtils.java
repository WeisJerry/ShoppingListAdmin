package shoppinglist.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * Contain Utilities for sizing visual components. (Singleton)
 * 
 * @author weis_
 *
 */
public class DialogUtils {

	private static DialogUtils DIALOGUTILS = new DialogUtils();

	/**
	 * Internal constructor for singleton DialogUtils object
	 * 
	 */
	protected DialogUtils() {
	}

	/**
	 * Get the one and only instance of DialogUtils.
	 * 
	 * @return DialogUtils
	 */
	public static DialogUtils getInstance() {
		return DIALOGUTILS;
	}

	/**
	 * sizes a component as a ratio of its parent component.
	 * 
	 * @param component to size
	 * @param ratio     (0.1 to 1.0)
	 * @return resized component
	 */
	public Component sizeComponent(Component component, double ratio) {
		if (component != null) {
			Rectangle rect = calculateBounds(component,ratio);
			component.setBounds(rect);
		}
		return component;
	}
	/**
	 * Position a component in relation to its parent, but don't resize.
	 * 
	 * @param component
	 * 
	 * @return positioned component
	 */
	public Component positionComponent(Component component) {
		if (component != null) {
			Dimension screen = getParentFrameSize(component);
			double width = screen.getWidth();
			double height = screen.getHeight();
			Dimension size = component.getSize();
			
			int x = (int)(width - size.width)/2;
			int y = (int)(height - size.height)/2;
			
			component.setLocation(x, y);
		}
		return component;
	}

	/**
	 * Calculate a component size and position in relation to its parent.
	 * 
	 * @param component
	 * @param ratio
	 * @return rectangle specifying size
	 */
	public Rectangle calculateBounds(Component component, double ratio) {
		Rectangle rectangle = null;
		if (component != null) {
			if (ratio > 1.0) {
				ratio = 1.0;
			} else if (ratio <= 0.1) {
				ratio = 0.1;
			}

			Dimension screen = getParentFrameSize(component);
			double width = screen.getWidth();
			double height = screen.getHeight();

			int rectWidth = (int) (width * ratio);
			int rectHeight = (int) (height * ratio);
			int left = (int) (width - rectWidth) / 2;
			int top = (int) (height - rectHeight) / 2;

			rectangle = new Rectangle(left, top, rectWidth, rectHeight);
		}
		return rectangle;
	}

	/**
	 * Get the parent container's size (or screensize if top-level component)
	 * 
	 * @return dimension holding the size
	 */
	public Dimension getParentFrameSize(Component component) {
		Frame parent = getParentFrame(component);
		Dimension dimension;
		if (parent == component) { // top-level
			dimension = getScreenDimension();
		} else {
			dimension = parent.getSize();
		}
		return dimension;
	}
	
	/**
	 * Retrieve the parent frame for this component.
	 * (recursive)
	 * 
	 * @param component
	 * @return frame
	 */
	public Frame getParentFrame(Component component) {
		Frame frameParent = null;
		Component parent = component.getParent();
		if (parent != null) {
			frameParent = getParentFrame(parent);
		}
		else if (component instanceof Frame) {
			frameParent = (Frame) component;
		}
		return frameParent;
    }
	

	/**
	 * Retrieve the screen dimensions.
	 * @return
	 */
	protected Dimension getScreenDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

}
