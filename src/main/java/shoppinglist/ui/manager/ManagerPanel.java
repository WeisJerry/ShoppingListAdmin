package shoppinglist.ui.manager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import shoppinglist.ui.ButtonList;
import shoppinglist.ui.ShoppingListPanel;
import shoppinglist.utils.ImageUtils;

public class ManagerPanel extends ShoppingListPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8794388768393387495L;
	private ButtonList userList = null;
	private GroceryTree treeView = null;
	protected boolean connected = false;
	
	public boolean isConnected()
	{
		return connected;
	}

	@Override
	protected String getTitle() {
		return "Account Manager";
	}
	
	protected ImageIcon getIcon() {
		String ico = "db.png";
		return ImageUtils.getInstance().getIcon(ico);   
	}

	@Override
	protected void initializeUI() {
		GridBagLayout layout = new GridBagLayout(); 
		setLayout(layout);
		
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		GridBagConstraints constraints = new GridBagConstraints();
    
        
        //shopping list label
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0,0,10,0);
        add(new JLabel("Shopping List for Selected Account"), constraints);
		
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight=2;
        constraints.weightx=.35;
        constraints.weighty= 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0,0,0,20);
        constraints.fill = GridBagConstraints.BOTH;
		userList = new AccountsList();
		this.add(userList, constraints);
		
		constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight=1;
        constraints.weightx=.65;
        constraints.weighty=.95;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0,0,0,0);
   
		treeView = new GroceryTree();
		this.add(treeView, constraints);
		
	}
}
