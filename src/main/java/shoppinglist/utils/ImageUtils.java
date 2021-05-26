/**
 * 
 */
package shoppinglist.utils;

import javax.swing.ImageIcon;

import shoppinglist.ShoppingListApp;

/**
 * @author weis_
 *
 */
public class ImageUtils {
	
	private static String ICONPATH = "../icons/";
	private static ImageUtils IMAGEUTILS = new ImageUtils();
	
	protected ImageUtils() { }
	
	public static ImageUtils getInstance() {
		return IMAGEUTILS;
	}
	
	public ImageIcon getIcon(String iconName) {
		ImageIcon icon = null;
		String path = ICONPATH + iconName;
        java.net.URL imgURL = ShoppingListApp.class.getResource(path);
        if (imgURL != null) {
            icon = new ImageIcon(imgURL);
        }
        else {
        	System.err.println("Couldn't retrieve image for " + path);
        }
        return icon;
    }

}
