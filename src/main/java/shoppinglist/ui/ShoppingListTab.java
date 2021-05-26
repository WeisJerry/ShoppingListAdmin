/**
 * 
 */
package shoppinglist.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import shoppinglist.messaging.MessageRouter;
import shoppinglist.ui.manager.ManagerPanel;
import shoppinglist.ui.template.TemplateEditorPanel;
import shoppinglist.ui.template.TemplatePanel;

/**
 * Manages the shoppinglist application tabs.
 * 
 * @author weis_
 *
 */
public class ShoppingListTab extends JPanel {
	

	private static final long serialVersionUID = 4139998869137527404L;
	
	JTabbedPane tabbedPane = new JTabbedPane();
	ManagerPanel managerPanel = null;
	TemplatePanel templatePanel = null;
	

	public ShoppingListTab()
	{
		super(new GridLayout(1, 1));
     
        managerPanel = new ManagerPanel();
        managerPanel.addToPane(tabbedPane);
        templatePanel = new TemplatePanel();
        templatePanel.addToPane(tabbedPane);
        
        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();
				if (index == 0) {
					MessageRouter.getInstance().notifySimpleListeners(MessageRouter.MNU_DISABLE_TEMPLATE);
				}
				else {
					MessageRouter.getInstance().notifySimpleListeners(MessageRouter.MNU_ENABLE_TEMPLATE);
				}
				
			}});
        
	}
	
	public void storeConfigurations() {
		templatePanel.storeConfigurations();
	}
	
	public void restoreConfigurations() {
		templatePanel.restoreConfigurations();
	}

	public TemplateEditorPanel getTemplateEditor() {
		return templatePanel.getTemplateEditor();
		
	}

}
