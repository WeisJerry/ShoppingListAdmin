package shoppinglist;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import shoppinglist.ui.ShoppingListMainFrame;

/**
 * Top-level app for ShoppingList
 * @author weis_
 *
 */
public class ShoppingListApp {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new ShoppingListMainFrame();
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
            }
        });

	}

}
