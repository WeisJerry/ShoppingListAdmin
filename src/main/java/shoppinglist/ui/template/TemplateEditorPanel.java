package shoppinglist.ui.template;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.JTree.DropLocation;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.utils.TemplateParseUtils;
import shoppinglist.utils.TemplateSelectionUtils;
import shoppinglist.utils.TemplateSelectionUtils.TemplateStatus;

/**
 * Template editor
 * 
 * @author weis_
 *
 */
public class TemplateEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTree tree;
	private JLabel templateNameLbl;
	private JPopupMenu popup;

	private boolean dirty = false;
	private String templateName;

	public TemplateEditorPanel() {
		this(true);
	}

	/**
	 * Constructor
	 * 
	 * @param editable: whether contents of tree can be removed or added to
	 */
	public TemplateEditorPanel(boolean editable) {
		initializeUI();
		clearTree();
		createPopupMenu();
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);

		if (editable) {
			// drag and drop
			tree.setDropMode(DropMode.ON_OR_INSERT);
			tree.setTransferHandler(ListTransferHandler.getInstance());

			// add listener for popup menu
			tree.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					showPopup(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					showPopup(e);
				}

				private void showPopup(MouseEvent e) {
					if (e.isPopupTrigger()) {
						if (!tree.isSelectionEmpty()) {
							popup.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
			});
		}
	}

	/**
	 * Construct the popup menu
	 */
	private void createPopupMenu() {
		popup = new JPopupMenu();

		JMenuItem menuItem = new JMenuItem("Remove");
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.getAccessibleContext().setAccessibleDescription("Remove");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					System.out.println(path.toString());
					removeFromTree(path);
				}
			}
		});
		popup.add(menuItem);
	}

	/**
	 * Remove a selection from the treemodel
	 * 
	 * @param path
	 */
	protected void removeFromTree(TreePath path) {
		Object obj = path.getLastPathComponent();
		TreeModel model = getModel();
		if (obj instanceof DefaultMutableTreeNode && model instanceof DefaultTreeModel) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
			DefaultTreeModel treeModel = (DefaultTreeModel) model;
			node.removeFromParent();

			setDirty(true);
			treeModel.reload();
			expandTree();
		}
	}

	/**
	 * Create the UI for the templateEditor
	 */
	private void initializeUI() {
		GridBagLayout gridbagLayout = new GridBagLayout();
		setLayout(gridbagLayout);

		GridBagConstraints constraints = new GridBagConstraints();

		// template list label
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 10, 0);
		templateNameLbl = new JLabel("Template Name: ");
		add(templateNameLbl, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = .65;
		constraints.weighty = .95;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);

		tree = new JTree();
		add(tree, constraints);

	}

	/**
	 * Check if dirty/changed
	 * 
	 * @return
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Set the dirty bit.
	 * 
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			setTemplateName(templateName);
		}
	}

	/**
	 * Retrieve the treemodel
	 * 
	 * @return the treemodel
	 */
	protected TreeModel getModel() {
		return tree.getModel();
	}

	/**
	 * Set the treemodel
	 * 
	 * @param model
	 */
	protected void setModel(TreeModel model) {
		tree.setModel(model);
	}

	/**
	 * Get the specified template name
	 * 
	 * @return string holding the template name
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Set the template name as well as the display
	 * 
	 * @param name
	 */
	public void setTemplateName(String name) {
		templateName = name;
		String displayName = "Template Name: ";
		if (!templateName.trim().isEmpty()) {
			displayName += templateName;
			if (isDirty()) {
				displayName += "*";
			}
		}
		templateNameLbl.setText(displayName);
	}

	/**
	 * Clear the template name
	 */
	public void clearTemplateName() {
		templateName = "";
		setTemplateName(templateName);
	}

	/**
	 * clear out the template editor tree
	 */
	protected void clearTree() {
		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {
			DefaultTreeModel treeModel = (DefaultTreeModel) model;
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
			treeModel.setRoot(root);
			setModel(treeModel);
			setDirty(false);
			clearTemplateName();
		}
	}

	/**
	 * Fill the template tree.
	 * 
	 * @param newRoot
	 */
	public void fillTree(String newTemplateName) {
		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {
			DefaultTreeModel treeModel = (DefaultTreeModel) model;

			DefaultMutableTreeNode root = TemplateParseUtils.getInstance().openModel(newTemplateName);
			if (root != null) {
				treeModel.setRoot(root);
				setModel(treeModel);
				setDirty(false);
				setTemplateName(newTemplateName);
			}
		}
	}

	/**
	 * New template
	 */
	public int newTemplate() {
		int selection = JOptionPane.YES_OPTION;
		if (dirty) {
			String errMsg = "Do you want to save the existing template?";
			selection = JOptionPane.showConfirmDialog(null, errMsg, "New Template", JOptionPane.YES_NO_CANCEL_OPTION);
			if (selection == JOptionPane.YES_OPTION) {
				selection = saveTemplate();
			}

			if (selection == JOptionPane.CANCEL_OPTION) {
				return selection;
			}
		}

		clearTree();
		return selection;
	}

	/**
	 * Open an existing template
	 */
	public int openTemplate() {
		int selection = newTemplate();
		if (selection != JOptionPane.CANCEL_OPTION) {
			String templateName = selectOpenTemplate();
			if (templateName.isEmpty()) {
				selection = JOptionPane.CANCEL_OPTION;
			} else {
				fillTree(templateName);
				expandTree();
			}
		}
		return selection;
	}

	/**
	 * Save the current template.
	 * 
	 * @return
	 */
	public int saveTemplate() {
		return saveTemplate(templateName);
	}

	/**
	 * Save a template, forcing a user prompt for template name.
	 * 
	 * @return
	 */
	public int saveAsTemplate() {
		return saveTemplate("");
	}

	/**
	 * Save a template file
	 * 
	 * @param template
	 * 
	 * @return int indicating save selection status (save, cancel)
	 */
	protected int saveTemplate(String template) {
		int retVal = JOptionPane.YES_OPTION;

		// no template name specified yet, so save as
		if (template == null || template.isEmpty()) {
			template = selectSaveTemplate();
			if (template.isEmpty()) {
				// user cancelled
				return JOptionPane.CANCEL_OPTION;
			}
		}

		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {
			if (!TemplateParseUtils.getInstance().saveModel((DefaultTreeModel) model, template)) {
				retVal = JOptionPane.CANCEL_OPTION;
			} else {
				templateName = template;
				setDirty(false);
			}
		}

		return retVal;
	}

	/**
	 * Get the user's selection for the name/location of the shopping list template.
	 * 
	 * @return template path and name
	 */
	public String selectSaveTemplate() {
		return TemplateSelectionUtils.getInstance().selectTemplate(TemplateStatus.SAVE, tree);
	}

	/**
	 * Get the user's selection for the shopping list template to open.
	 * 
	 * @return template path and name
	 */
	public String selectOpenTemplate() {
		return TemplateSelectionUtils.getInstance().selectTemplate(TemplateStatus.OPEN, tree);
	}

	/**
	 * Get user's selection for template, for either opening or saving.
	 * 
	 * @param mode (OPEN or SAVE)
	 * 
	 * @return full template pathname
	 */

	/**
	 * Does a treemodel or any of its children contain a specified
	 * shoppinglistobject?
	 * 
	 * @param treeModel
	 * @param shoppingListObject
	 * 
	 * @return true if the shoppinglistobject is somewhere in the tree.
	 */
	public boolean contains(DefaultTreeModel treeModel, ShoppingListObject shoppingListObject) {
		boolean contain = false;
		Object root = treeModel.getRoot();
		if (root instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root;
			contain = contains(node, shoppingListObject);
		}
		return contain;
	}

	/**
	 * Does a tree node or its children contain a specified shoppinglistobject
	 * 
	 * @param treeNode
	 * @param shoppingListObject
	 * 
	 * @return true if contained in tree node or one of its children.
	 */
	protected boolean contains(DefaultMutableTreeNode treeNode, ShoppingListObject shoppingListObject) {
		boolean contain = false;

		int children = treeNode.getChildCount();
		if (children > 0) {
			for (int i = 0; i < children && contain == false; i++) {
				TreeNode node = treeNode.getChildAt(i);
				if (node instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode treeSubNode = (DefaultMutableTreeNode) node;
					Object obj = treeSubNode.getUserObject();
					if (obj instanceof ShoppingListObject) {
						if (obj.equals(shoppingListObject)) {
							contain = true;
						} else { // recursively check children
							contain = contains(treeSubNode, shoppingListObject);
						}
					}
				}
			}
		}
		return contain;
	}

	/**
	 * Add a shoppinglistobject to the appropriate place in the tree, depending on
	 * whether it is a category or grocery item.
	 * 
	 * @param treeModel
	 * @param shoppingListObject
	 * @param dropLocation
	 * 
	 * @return true on success
	 */
	protected boolean addToTree(DefaultTreeModel treeModel, ShoppingListObject shoppingListObject,
			DropLocation dropLocation) {
		boolean retval = false;
		TreePath path = dropLocation.getPath();
		int index = dropLocation.getChildIndex();

		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(shoppingListObject);

		Object root = treeModel.getRoot();
		if (root instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) root;

			if (shoppingListObject instanceof Category) {
				if (index == -1) { // root
					treeRoot.add(newNode);
					retval = true;
				} else if (path != null && index > -1) { // put in the line
					treeRoot.insert(newNode, index);
					retval = true;
				}
			} else if (shoppingListObject instanceof Grocery && path != null) {
				if (path.getPathCount() == 2) {
					Object obj = path.getPathComponent(1);
					if (obj instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) obj;
						if (index == -1) {
							categoryNode.add(newNode);
						} else {
							categoryNode.insert(newNode, index);
						}
						setDirty(true);
						retval = true;
					}
				}
			}

			if (retval) {
				setDirty(true);
				treeModel.reload();
				expandTree();
			}
		}
		return retval;
	}

	/**
	 * Expand all branches of the tree.
	 */
	protected void expandTree() {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	/**
	 * Add a shoppinglistobject to the tree as a result of a drop
	 * 
	 * @param shoppingListObject
	 * @param dropLocation
	 * 
	 * @return true on success
	 */
	public boolean addShoppingListObject(ShoppingListObject shoppingListObject, DropLocation dropLocation) {

		boolean retval = false;
		TreeModel model = getModel();
		if (model instanceof DefaultTreeModel) {

			DefaultTreeModel treeModel = (DefaultTreeModel) model;
			if (!contains(treeModel, shoppingListObject)) {
				retval = addToTree(treeModel, shoppingListObject, dropLocation);
			}
		}
		return retval;
	}
}
