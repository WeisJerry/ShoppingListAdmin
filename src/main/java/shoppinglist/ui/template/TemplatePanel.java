/**
 * 
 */
package shoppinglist.ui.template;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.Grocery;
import shoppinglist.ui.ButtonList;
import shoppinglist.ui.ShoppingListPanel;
import shoppinglist.utils.ConfigParseUtils;
import shoppinglist.utils.ImageUtils;

/**
 * @author weis_
 *
 */
public class TemplatePanel extends ShoppingListPanel implements PropertyChangeListener {
	
	private static final long serialVersionUID = -2839522928299178509L;

	private static final String FILENAME = "templateconfigs.xml";
	
	private ButtonList categoryList;
	private ButtonList groceryItemList;
	private TemplateEditorPanel templateEditorPanel;

	@Override
	protected String getTitle() {
		return "Template Creator";
	}
	
	protected ImageIcon getIcon() {
		String ico = "template.png";
		return ImageUtils.getInstance().getIcon(ico);   
	}

	@Override
	protected void initializeUI() {
		
		GridLayout gridLayout = new GridLayout(1,2,20,0);
		setLayout(gridLayout);
		
		JPanel leftPanel = new JPanel();
		add(leftPanel);
		
		templateEditorPanel = new TemplateEditorPanel();
		add(templateEditorPanel);
		
		GridLayout layout = new GridLayout(2,1,0,10); 
		leftPanel.setLayout(layout);
		
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		categoryList = new CategoriesList();
		groceryItemList = new GroceriesList();
		leftPanel.add(categoryList);
		leftPanel.add(groceryItemList);
		
		restoreConfigurations();
	}

	public void storeConfigurations() {
		String catConfigs = categoryList.getListXML();
		String grocConfigs = groceryItemList.getListXML();
		List<String> fragments = new ArrayList<String>();
		fragments.add(catConfigs);
		fragments.add(grocConfigs);
		ConfigParseUtils.getInstance().createXmlConfigDoc(fragments, FILENAME);
	}
	
	public void restoreConfigurations() {
		ConfigParseUtils parser = ConfigParseUtils.getInstance();
		String className = Category.class.getSimpleName();
		List<String> categories = parser.restoreXmlConfigSettings(className, FILENAME);
		for (String categoryName: categories) {
			Category category = new Category(categoryName);
			categoryList.addToList(category);
		}
		className = Grocery.class.getSimpleName();
		List<String> groceries = parser.restoreXmlConfigSettings(className, FILENAME);
		for (String groceryName: groceries) {
			Grocery grocery = new Grocery(groceryName);
			groceryItemList.addToList(grocery);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	
		
	}

	public TemplateEditorPanel getTemplateEditor() {
		return templateEditorPanel;
	}
	


}
