/**
 * 
 */
package shoppinglist.ui.template;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.ui.ButtonList;

/**
 * @author weis_
 *
 */
public class CategoriesList extends ButtonList {

	private static final long serialVersionUID = 6852718318178242524L;

	public CategoriesList() {
		super(ButtonList.Buttons.ADDREMOVE, "Categories");
		setDragEnabled(true);
	}

	@Override
	public void onAdd() {
		String message = "Enter the new category name.";
		String newCategory = JOptionPane.showInputDialog(message);
		if (newCategory != null) {
			newCategory = newCategory.trim();
			if (!newCategory.isEmpty()) {
				Category category = new Category(newCategory);
				this.addToList(category);
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
