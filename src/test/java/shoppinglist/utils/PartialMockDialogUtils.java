package shoppinglist.utils;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Partially mock dialogutils for purpose of unit testing.
 * @author weis_
 *
 */
public class PartialMockDialogUtils extends DialogUtils {
	private int width = 0;
	private int height = 0;
	
	public PartialMockDialogUtils(int x, int y) {
		width = x;
		height = y;
	}
	
	@Override
	public Dimension getParentFrameSize(Component component) {
		return new Dimension(width,height);
	}
	
	@Override
	protected Dimension getScreenDimension() {
		return new Dimension((int)(width*1.5),(int)(height*1.5));
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
