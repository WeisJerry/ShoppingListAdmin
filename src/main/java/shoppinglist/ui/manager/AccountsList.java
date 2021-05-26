package shoppinglist.ui.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.dataobjects.UserInfo;
import shoppinglist.messaging.MessageRouter;
import shoppinglist.ui.ButtonList;
import shoppinglist.utils.DBConnectionUtils;
import shoppinglist.utils.EncryptionUtils;

/**
 * display the user accounts.
 * @author weis_
 *
 */
public class AccountsList extends ButtonList implements PropertyChangeListener {

	private static final long serialVersionUID = -5512272279821206499L;

	public AccountsList() {
		super(ButtonList.Buttons.ADDREMOVE, "User Accounts");
		MessageRouter.getInstance().addChangeListener(this);
	}

	/**
	 * Add button pressed: Add new user account
	 */
	@Override
	public void onAdd() {
		//show dialog to add a user account
		AddUserAccountDialog dialog = new AddUserAccountDialog(this);
		UserInfo user = dialog.showDialog();
		if (user != null) {
			if (!isInList(user))
			{
				int step = 0;
				//encrypt the password
				String pwd = user.getPassword();
				try {
					user.setPassword(EncryptionUtils.encrypt(pwd));
					step++;
					DBConnectionUtils.getInstance().addUser(user);
					step++;
					addToList(user);
				}
				catch (Exception e)
				{
					String errMsg = "An error occurred while encrypting the new password.";
					if (step == 1) {
						errMsg = "An error occurred while adding the new user to the database.";
					}
					else if (step > 1) {
						errMsg = "An unidentified error occurred.";
					}
					JOptionPane.showMessageDialog(null, errMsg, "Add User to Database", JOptionPane.ERROR_MESSAGE);
				}	
			}
		}
	}

	/**
	 * Remove button pressed: Remove user account
	 */
	@Override
	public void onRemove(List<ShoppingListObject> values) {
		for (ShoppingListObject value : values) {
			if (value instanceof UserInfo)
			{
				UserInfo info = (UserInfo) value;
				DBConnectionUtils.getInstance().removeUser(info);
				removeFromList(info);
				MessageRouter.getInstance().notifySimpleListeners(MessageRouter.MSG_CLEAR_USER_GROCERIES);
			}
		}
		
	}

	/**
	 * Selection was changed in the list
	 */
	@Override
	public void onSelectionChanged(ListSelectionEvent e, List<ShoppingListObject> values) {
		if (values != null && !values.isEmpty()) {
			ShoppingListObject value = values.get(0);
			if (value instanceof UserInfo) {
				UserInfo info = (UserInfo) value;
				MessageRouter.getInstance().notifyListeners(MessageRouter.MSG_SHOW_USER_GROCERIES, info, info);
			}
		}
		else {
			MessageRouter.getInstance().notifySimpleListeners(MessageRouter.MSG_CLEAR_USER_GROCERIES);
		}
		
	}

	/**
	 * Handle property changes
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		boolean active = false;
		if (evt.getPropertyName().equals(MessageRouter.DB_GET_USERS)) {
			populateUsers();
			active = true;
		}
		else if (evt.getPropertyName().equals(MessageRouter.DB_CLEAR_USERS)) {
			getListModel().clear();
		}
		
		updateButtons(active);
	}
	
	/**
	 * Populate the list with the users listed in the DB
	 */
	protected void populateUsers() {
		ResultSet results = DBConnectionUtils.getInstance().getUsers();
		if (results != null) {
			try {
				List<UserInfo> users = new ArrayList<UserInfo>();
				while (results.next()) {
					String userName = results.getString("username");
					String password = results.getString("password");
					String hint = results.getString("hint");
					UserInfo info = new UserInfo(userName, password, hint);
					users.add(info);
				}
				getListModel().addAll(users);
			} catch (SQLException e) {
				String errMsg = "An error occurred while retrieving users from the database.";
				JOptionPane.showMessageDialog(null, errMsg, "Getting Users from Database", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			getListModel().clear();
		}
	}

	@Override
	public void defaultUpdateButtons() {
		updateButtons(false);		
	}

}
