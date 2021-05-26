package shoppinglist.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.ui.template.ListTransferHandler;
import shoppinglist.utils.ConfigParseUtils;
import shoppinglist.utils.ImageUtils;
import shoppinglist.utils.configurations.ComponentColor;

/**
 * Visual component that provides a list with some small optional control
 * buttons near the top. (Add, Remove)
 * 
 * @author weis_
 *
 */
abstract public class ButtonList extends JPanel {

	private static final long serialVersionUID = -2552832798199502464L;

	public enum Buttons {
		ADD, REMOVE, ADDREMOVE
	};

	private Buttons buttonsToDisplay;
	private JList<ShoppingListObject> list;
	private DefaultListModel<ShoppingListObject> listModel = new DefaultListModel<ShoppingListObject>();
	private JButton addButton;
	private JButton removeButton;
	private String title;

	public ButtonList(Buttons buttons, String title) {
		buttonsToDisplay = buttons;
		this.title = title;
		initializeUI();
		addListeners();
		
	}
	
	// Handler methods to be instantiated by child classes
	
	public abstract void onAdd();

	public abstract void onRemove(List<ShoppingListObject> values);

	public abstract void onSelectionChanged(ListSelectionEvent e, List<ShoppingListObject> values);

	public abstract void defaultUpdateButtons();
	
	public void setDragEnabled(boolean enable) {
		list.setDragEnabled(enable);
		list.setTransferHandler(ListTransferHandler.getInstance());
	}
	
	/**
	 * Initialize the UI
	 */
	private void initializeUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		// add beginning space (optionally a label later on)
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 6;
		constraints.weightx = .9;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 10, 0);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel(title), constraints);

		constraints.gridx = 6;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weightx = .1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel();
		int cols = 1;
		if (buttonsToDisplay == Buttons.ADDREMOVE) {
			cols = 2;
		}
		buttonPanel.setLayout(new GridLayout(1, cols));
		add(buttonPanel, constraints);

		if (buttonsToDisplay == Buttons.ADDREMOVE || buttonsToDisplay == Buttons.ADD) {
			addButton = new JButton(ImageUtils.getInstance().getIcon("plus.png"));
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onAdd();
					updateButtons(true);
				}
			});
			buttonPanel.add(addButton);
		}
		if (buttonsToDisplay == Buttons.ADDREMOVE || buttonsToDisplay == Buttons.REMOVE) {
			removeButton = new JButton(ImageUtils.getInstance().getIcon("minus.png"));
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<ShoppingListObject> values = list.getSelectedValuesList();
					onRemove(values);
					for (ShoppingListObject value : values) {
						removeFromList(value);
					}
					updateButtons(true);
				}
			});
			buttonPanel.add(removeButton);
		}
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 8;
		constraints.weightx = 1.0;
		constraints.weighty = .99;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.fill = GridBagConstraints.BOTH;
		list = new JList<ShoppingListObject>();
		list.setBackground(ComponentColor.LIGHT.getColor());
		list.setModel(listModel);
		add(list, constraints);

		defaultUpdateButtons();
	}

	private void addListeners() {
		// list
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (list.getSelectedIndex() >= 0) {
					List<ShoppingListObject> values = list.getSelectedValuesList();
					onSelectionChanged(e, values);
					updateButtons(true);
				}
			}
		});
		list.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateToolTip(e);
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				updateToolTip(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				list.setToolTipText(null);
			}
		});
	}

	private void updateToolTip(MouseEvent e) {
		int size = listModel.getSize();
		if (size > 0) {
			Rectangle rect = list.getCellBounds(0, size - 1);
			Point point = e.getPoint();
			if (point.y >= rect.y && point.y <= rect.y + rect.height) {
				int index = list.locationToIndex(point);
				ShoppingListObject object = listModel.getElementAt(index);
				list.setToolTipText(object.getToolTip());
			} else {
				list.setToolTipText(null);
			}
		} else {
			list.setToolTipText(null);
		}

	}

	/**
	 * Retrieve the underlying list model
	 * 
	 * @return DefaultListModel
	 */
	public DefaultListModel<ShoppingListObject> getListModel() {
		return listModel;
	}

	/**
	 * Is specified item in the list.
	 * 
	 * @param item
	 * @return true if in list
	 */
	public boolean isInList(ShoppingListObject item) {
		boolean found = false;

		for (int counter = 0; counter < getListModel().getSize(); counter++) {
			Object element = getListModel().getElementAt(counter);
			if (element.equals(item)) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Add specified item to the list, if not already present.
	 * 
	 * @param item to add
	 * @return true if added
	 */
	public boolean addToList(ShoppingListObject item) {
		boolean found = isInList(item);
		if (found == false) {
			getListModel().addElement(item);
			updateButtons(true);
		}
		return (!found);
	}

	/**
	 * Removes an item from the list.
	 * 
	 * @param item to remove
	 */
	public void removeFromList(ShoppingListObject item) {
		getListModel().removeElement(item);
		updateButtons(true);
	}

	/**
	 * Update the list all-at-once
	 * 
	 * @param model to use for list
	 */
	public void updateList(DefaultListModel<ShoppingListObject> model) {
		listModel = model;
		updateButtons(true);
	}

	/**
	 * Determine whether buttons should be enabled
	 * 
	 * @param active indicates whether the list is active
	 */
	protected void updateButtons(boolean active) {
		boolean plusEnabled = false;
		boolean minusEnabled = false;

		if (active) {
			plusEnabled = true;
			minusEnabled = true;

			if (listModel.getSize() == 0 || list.getSelectedIndex() < 0) {
				minusEnabled = false;
			}

		}
		addButton.setEnabled(plusEnabled);
		removeButton.setEnabled(minusEnabled);
	}
	
	/**
	 * Retrieve the XML fragment holding the config information for this component.
	 * @return
	 */
	public String getListXML() {
		List<ShoppingListObject> list = new ArrayList<ShoppingListObject>();
		for (int x=0; x<listModel.getSize(); x++) {
			list.add(listModel.get(x));
		}
		
		//remove spaces in title
		String root = title;
		root = root.replace(' ', '_');
		
		return ConfigParseUtils.getInstance().createXmlConfigFragment(list, root);
	}

}
