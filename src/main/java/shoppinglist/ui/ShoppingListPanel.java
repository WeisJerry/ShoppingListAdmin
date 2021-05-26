/**
 * 
 */
package shoppinglist.ui;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Base class for tab panels
 * @author weis_
 *
 */
public abstract class ShoppingListPanel extends JPanel {
	
	private static final long serialVersionUID = 148628010672862792L;
	protected JTabbedPane parentPane = null;
	
	public ShoppingListPanel() {
		super(true);
		initializeUI();
	}
	
	public ShoppingListPanel addToPane(JTabbedPane parent) {
		parent.addTab(getTitle(), getIcon(), this, "Does nothing");
        parent.setMnemonicAt(0, KeyEvent.VK_1);
        return this;
	}
	
	/**
	 * Get the text to display in the tab.
	 * @return string
	 */
	abstract protected String getTitle();
	
	/**
	 * Initialize the UI for the tab component
	 */
	abstract protected void initializeUI();
	
	/**
	 * Get the icon to display for the tab
	 * @return
	 */
	abstract protected ImageIcon getIcon();
	

}
