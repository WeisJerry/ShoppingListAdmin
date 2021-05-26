package shoppinglist.messaging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * One-and-only message router to
 * manage messaging between different components.
 * 
 * This design pattern allows loose-coupling between system components.
 * For a component to "listen" to the messagerouter, it should implement
 * the propertychange listener and register itself with this class.
 * 
 * @author weis_
 *
 */
public class MessageRouter {
	
	//messaging identifiers
	
	//db messages
	public static final String DB_ADD_USER = "db-add-user";
	public static final String DB_REMOVE_USER = "db-remove-user";
	public static final String DB_GET_USERS = "db-get-users";
	public static final String DB_CLEAR_USERS = "db-clear-users";
	
	public static final String MSG_CLEAR_USER_GROCERIES = "msg-clear-user-groceries";
	public static final String MSG_SHOW_USER_GROCERIES = "msg-show-user-groceries";
	
	//template messages
	
	public static final String MNU_ENABLE_TEMPLATE = "mnu-enable-template";
	public static final String MNU_DISABLE_TEMPLATE = "mnu-disable-template";
	
	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	private static MessageRouter MESSAGEROUTER = new MessageRouter();
	
	protected MessageRouter() { }
	
	/**
	 * Retrieve the one-and-only instance
	 * @return
	 */
	public static MessageRouter getInstance() {
		return MESSAGEROUTER;
	}
	
	/**
	 * Call this to register a propertychange listener
	 * @param newListener
	 */
	public void addChangeListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }
	
	/**
	 * Update listeners with simple event change indication.
	 * 
	 * @param id - message identifier
	 */
	public void notifySimpleListeners(String id) {
		notifyListeners(id, null, null);
	}
	
	/**
	 * Call this to update all relevant listeners
	 * 
	 * @param id - message identifier
	 * @param oldVal - original object
	 * @param newVal - new object
	 * @param object - event source
	 */
	public void notifyListeners(String id, Object oldObj, Object newObj) {
		
		PropertyChangeEvent event = new PropertyChangeEvent(this, id, oldObj, newObj);
		for(PropertyChangeListener listener : listeners) {
			listener.propertyChange(event);
		}
	}

}
