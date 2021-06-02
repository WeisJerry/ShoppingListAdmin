/**
 * 
 */
package shoppinglist.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import shoppinglist.dataobjects.DBConnectionInfo;
import shoppinglist.messaging.MessageRouter;
import shoppinglist.ui.manager.ConnectDialog;
import shoppinglist.ui.template.TemplateEditorPanel;
import shoppinglist.utils.DBConnectionUtils;
import shoppinglist.utils.DialogUtils;
import shoppinglist.utils.ImageUtils;
import shoppinglist.utils.configurations.ComponentColor;
import shoppinglist.utils.configurations.ComponentSizing;

/**
 * Main frame for the Shopping List.
 * @author weis_
 *
 */
public class ShoppingListMainFrame extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private ShoppingListTab tabbedPanel;
	private JMenuItem disconnectMenuItem;
	private JMenuItem connectMenuItem;

	private JMenuItem openTemplateMenuItem;
	private JMenuItem newTemplateMenuItem;
	private JMenuItem saveTemplateMenuItem;
	private JMenuItem saveAsTemplateMenuItem;

	protected ShoppingListMainFrame mainframe;

	private static final String CONNECTED = "Shopping List Administration Utility - Connected";
	private static final String UNCONNECTED = "Shopping List Administration Utility - Not Connected";

	public ShoppingListMainFrame() {
		initializeUI();
		initializeHandlers();
		mainframe = this;
		MessageRouter.getInstance().addChangeListener(this);
	}

	/**
	 * Save configurations
	 */
	public void storeConfigurations() {
		tabbedPanel.storeConfigurations();
	}

	/**
	 * Restore the configurations
	 */
	public void restoreConfigurations() {
		tabbedPanel.restoreConfigurations();
	}
	
	/**
	 * Get the template editor
	 * @return
	 */
	public TemplateEditorPanel getTemplateEditor() {
		return tabbedPanel.getTemplateEditor();
	}

	void initializeUI() {
		setTitle(UNCONNECTED);
		getContentPane().setBackground(ComponentColor.BACKGROUND.getColor());
		getLayeredPane().setOpaque(true);
		setIconImage(getImage());
		DialogUtils.getInstance().sizeComponent(this, ComponentSizing.APPSIZE.getSizing());

		// add tabbed panel
		tabbedPanel = new ShoppingListTab();
		add(tabbedPanel, BorderLayout.CENTER);
		createMenus();

		// Display the window.
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	void initializeHandlers() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();

				int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit the application?",
						"Exit Application", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					storeConfigurations();
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Retrieve the image icon to use
	 * @return Image
	 */
	protected Image getImage() {
		Image image = null;
		String ico = "shoppinglist.png";
		ImageIcon icon = ImageUtils.getInstance().getIcon(ico);
		if (icon != null) {
			image = icon.getImage();
		}
		return image;
	}

	/**
	 * Create the menu items for the application
	 */
	protected void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		// Build the first menu.
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription("File");
		menuBar.add(fileMenu);

		// connect to database
		connectMenuItem = new JMenuItem("Connect...", KeyEvent.VK_C);
		connectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		connectMenuItem.getAccessibleContext().setAccessibleDescription("Connect to the database");
		fileMenu.add(connectMenuItem);

		connectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToDatabase();
			}
		});

		// disconnect from database
		disconnectMenuItem = new JMenuItem("Disconnect", KeyEvent.VK_C);
		disconnectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		disconnectMenuItem.getAccessibleContext().setAccessibleDescription("Disconnect from the database");
		disconnectMenuItem.setEnabled(false);
		fileMenu.add(disconnectMenuItem);

		disconnectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (disconnectFromDatabase()) {
					setTitle(UNCONNECTED);
					disconnectMenuItem.setEnabled(false);
					MessageRouter.getInstance().notifySimpleListeners(MessageRouter.DB_CLEAR_USERS);
					MessageRouter.getInstance().notifySimpleListeners(MessageRouter.MSG_CLEAR_USER_GROCERIES);
				}

			}
		});

		fileMenu.addSeparator();

		// template functions
		newTemplateMenuItem = new JMenuItem("New Template", KeyEvent.VK_C);
		newTemplateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
		newTemplateMenuItem.getAccessibleContext().setAccessibleDescription("New Template");
		newTemplateMenuItem.setEnabled(false);
		fileMenu.add(newTemplateMenuItem);

		newTemplateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TemplateEditorPanel editor = getTemplateEditor();
				editor.newTemplate();
			}
		});
		
		openTemplateMenuItem = new JMenuItem("Open Template...", KeyEvent.VK_C);
		openTemplateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
		openTemplateMenuItem.getAccessibleContext().setAccessibleDescription("Open Template");
		openTemplateMenuItem.setEnabled(false);
		fileMenu.add(openTemplateMenuItem);

		openTemplateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TemplateEditorPanel editor = getTemplateEditor();
				editor.openTemplate();
			}
		});
		
		saveTemplateMenuItem = new JMenuItem("Save", KeyEvent.VK_C);
		saveTemplateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
		saveTemplateMenuItem.getAccessibleContext().setAccessibleDescription("Save Template");
		saveTemplateMenuItem.setEnabled(false);
		fileMenu.add(saveTemplateMenuItem);

		saveTemplateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TemplateEditorPanel editor = getTemplateEditor();
				editor.saveTemplate();
			}
		});
		
		saveAsTemplateMenuItem = new JMenuItem("Save as...", KeyEvent.VK_C);
		saveAsTemplateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
		saveAsTemplateMenuItem.getAccessibleContext().setAccessibleDescription("Save Template as");
		saveAsTemplateMenuItem.setEnabled(false);
		fileMenu.add(saveAsTemplateMenuItem);

		saveAsTemplateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TemplateEditorPanel editor = getTemplateEditor();
				editor.saveAsTemplate();
			}
		});
		
		fileMenu.addSeparator();
		
		// exit menu option
		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
		exitMenuItem.getAccessibleContext().setAccessibleDescription("Exit");
		fileMenu.add(exitMenuItem);

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconnectFromDatabase();
				JFrame frame = (JFrame) mainframe;
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

		setJMenuBar(menuBar);
	}

	/**
	 * connect to the database
	 */
	protected void connectToDatabase() {
		ConnectDialog dialog = new ConnectDialog(this);
		DBConnectionInfo info = dialog.showDialog();
		if (info != null) {
			DBConnectionUtils.getInstance().setConnectionInfo(info);
			try {
				Connection connection = DBConnectionUtils.getInstance().getConnection();
				if (connection != null) {
					setTitle(CONNECTED);
					disconnectMenuItem.setEnabled(true);
					MessageRouter.getInstance().notifySimpleListeners(MessageRouter.DB_GET_USERS);
				}
			} catch (SQLException e) {
				// this should not happen, but...
				String errMsg = "An error occurred while connecting to the database.";
				JOptionPane.showMessageDialog(null, errMsg, "Connect to Database", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Disconnect from the database
	 * @return true if disconnected
	 */
	protected boolean disconnectFromDatabase() {
		boolean disconnected = false;
		try {
			DBConnectionUtils.getInstance().closeConnection();
			disconnected = true;

		} catch (SQLException e) {
			String errMsg = "An error occurred while disconnecting from the database.";
			JOptionPane.showMessageDialog(null, errMsg, "Disconnect from Database", JOptionPane.ERROR_MESSAGE);
		}
		return disconnected;
	}
	
	/**
	 * Set the enabled state for all the template menus
	 * 
	 * @param enable
	 */
	public void enableTemplateMenu(boolean enable) {
		saveTemplateMenuItem.setEnabled(enable);
		saveAsTemplateMenuItem.setEnabled(enable);
		newTemplateMenuItem.setEnabled(enable);
		openTemplateMenuItem.setEnabled(enable);
	}

	/**
	 * Respond to property change messages.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(MessageRouter.MNU_DISABLE_TEMPLATE)) {
			enableTemplateMenu(false);
		}
		else if (evt.getPropertyName().equals(MessageRouter.MNU_ENABLE_TEMPLATE)) {
			enableTemplateMenu(true);
		}
		
	}

}
