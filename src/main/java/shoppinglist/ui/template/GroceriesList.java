/**
 * 
 */
package shoppinglist.ui.template;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.ui.ButtonList;

/**
 * @author weis_
 *
 */
public class GroceriesList extends ButtonList {


	private static final long serialVersionUID = 6617266389300288663L;

	public GroceriesList() {
		super(ButtonList.Buttons.ADDREMOVE, "Groceries");
		setDragEnabled(true);
	}

	@Override
	public void onAdd() {
		String message = "Enter the new grocery name.";
		String newGrocery = JOptionPane.showInputDialog(message);
		if (newGrocery != null) {
			newGrocery = newGrocery.trim();
			if (!newGrocery.isEmpty()) {
				Grocery grocery = new Grocery(newGrocery);
				this.addToList(grocery);
			}
		}

	}

	@Override
	public void onRemove(List<ShoppingListObject> values) {
		if (values != null && !values.isEmpty()) {
			for (ShoppingListObject item: values) {
				this.removeFromList(item);
			}
		}

	}

	@Override
	public void onSelectionChanged(ListSelectionEvent e, List<ShoppingListObject> values) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void defaultUpdateButtons() {
		updateButtons(true);		
	}

}
