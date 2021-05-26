package shoppinglist.dataobjects;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Store information for a user.
 * 
 * @author weis_
 *
 */
public class UserInfo extends ShoppingListObject {
	
	private static final long serialVersionUID = -1903355524213408561L;
	private String name;
	private String password;
	private String hint;
	private String template = "";

	public UserInfo(String name, String password, String hint) {
		setName(name);
		setPassword(password);
		setHint(hint);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getHint() {
		return hint;
	}
	
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
	
	@Override
	public String getToolTip() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("Name: ");
		buffer.append(name);
		buffer.append("<br>");
		buffer.append("Password: ");
		buffer.append(password);
		buffer.append("<br>");
		buffer.append("Hint: ");
		buffer.append(hint);
		buffer.append("</html>");
		return buffer.toString();
	}
	
	/**
	 * Allow comparisons with other UserInfo objects.
	 * If names are same, then they are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean same = false;
		if (obj instanceof UserInfo) {
			UserInfo u = (UserInfo) obj;
			if (u.getName().equalsIgnoreCase(name)) {
				same = true;
			}
		}
		return same;
	}

	@Override
	public DataFlavor getDataFlavor() {
		return new DataFlavor(UserInfo.class,"UserInfo");
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		//userinfo is not transferrable.
		return null;
	}
}
