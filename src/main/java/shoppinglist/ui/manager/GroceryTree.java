/**
 * 
 */
package shoppinglist.ui.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.UserInfo;
import shoppinglist.messaging.MessageRouter;
import shoppinglist.utils.DBConnectionUtils;

/**
 * @author weis_
 *
 */
public class GroceryTree extends JTree implements PropertyChangeListener {

	
	private static final long serialVersionUID = 8720105637744338490L;
	
	
	
	public GroceryTree() {
		super();
		clearTree();
		MessageRouter.getInstance().addChangeListener(this);
		setRootVisible(false);
		setShowsRootHandles(true);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MessageRouter.MSG_CLEAR_USER_GROCERIES)) {
			clearTree();
		}
		else if (evt.getPropertyName().equals(MessageRouter.MSG_SHOW_USER_GROCERIES)) {
			clearTree();
			Object obj = evt.getNewValue();
			if (obj != null && obj instanceof UserInfo) {
				UserInfo info = (UserInfo) obj;
				fillTree(info);
				
			}
		}
		
	}
	
	protected void clearTree() {
		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {
			DefaultTreeModel treeModel = (DefaultTreeModel) model;
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
			treeModel.setRoot(root);
			setModel(treeModel); 
		}
	}
	
	protected void fillTree(UserInfo info) {
		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {
			DefaultTreeModel treeModel = (DefaultTreeModel) model;
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(info.getName());
			populateCategories(root, info);
			treeModel.setRoot(root);
			setModel(treeModel);
			
			//expand all nodes
			for (int i = 0; i < getRowCount(); i++) {
				   expandRow(i);
				}
			
			
		}
		
	}
	
	protected void populateCategories(DefaultMutableTreeNode root, UserInfo info) {
		ResultSet results = DBConnectionUtils.getInstance().getUserCategories(info);
		if (results != null) {
			try {
				while (results.next()) {
					String userName = results.getString("username");
					String catName = results.getString("categoryname");
					int id = results.getInt("categoryid");
					Category category = new Category(userName, catName, id);
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
					populateGroceries(node,category);
					root.add(node);
				}
			} catch (SQLException e) {
				String errMsg = "An error occurred while retrieving categories from the database.";
				JOptionPane.showMessageDialog(null, errMsg, "Getting Categories from Database", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void populateGroceries(DefaultMutableTreeNode root, Category category) {
		ResultSet results = DBConnectionUtils.getInstance().getUserGroceries(category);
		if (results != null) {
			try {
				while (results.next()) {
					String userName = results.getString("username");
					int grocId = results.getInt("groceryid");
					int catId = results.getInt("categoryid");
					String groceryName = results.getString("groceryname");
					Grocery grocery = new Grocery(userName, grocId, catId, groceryName);
					grocery.setQuantity(DBConnectionUtils.getInstance().getUserGroceriesQuantity(grocery));
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(grocery);
					root.add(node);
				}
			} catch (SQLException e) {
				String errMsg = "An error occurred while retrieving groceries from the database.";
				JOptionPane.showMessageDialog(null, errMsg, "Getting Groceries from Database", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
