package shoppinglist.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.DBConnectionInfo;
import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.UserInfo;

/**
 * Utilities to get information from and update the DB.
 * @author weis_
 *
 */

public class DBConnectionUtils {

	private static final String USERS_QUERY = "SELECT * FROM USERS;";

	private static DBConnectionUtils DBCONNECTIONUTILS = new DBConnectionUtils();
	private Connection connection = null;
	private DBConnectionInfo connectionInfo = null;

	protected DBConnectionUtils() {
	}

	public static DBConnectionUtils getInstance() {
		return DBCONNECTIONUTILS;
	}

	public void setConnectionInfo(DBConnectionInfo info) {
		connectionInfo = info;
	}

	/**
	 * Get the connection. If connection is not created and connectioninfo is
	 * specified, then create it.
	 * 
	 * @return connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (connection == null && connectionInfo != null) {
			connection = createConnection(connectionInfo);
		}
		return connection;
	}

	/**
	 * Close the connection.
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * Create the DB connection, using the specified info.
	 * 
	 * @param info
	 * @return connection (null if unsuccessful)
	 * @throws SQLException
	 */
	public Connection createConnection(DBConnectionInfo info) throws SQLException {
		Connection connection = null;
		if (info != null) {
			try {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(info.getConnectionString());
			} catch (SQLException e) {
				System.err.println("SQL exception occurred in creating DB connection.");
				System.out.println("Connection: " + info.getConnectionString());
				throw e;
			} catch (ClassNotFoundException e) {
				// should not happen if environment properly built.
				System.err.println("Class not found exception in DBConnectionUtils:createConnection.");
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * Execute a query
	 * 
	 * @param query
	 * @return resultset
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}

	/**
	 * Execute an update
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public void executeUpdate(String query) throws SQLException {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	/**
	 * Show an error dealing with database
	 * 
	 * @param errMsg
	 */
	public void showDBError(String errMsg) {
		JOptionPane.showMessageDialog(null, errMsg, "Database Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Get the resultset of users.
	 * 
	 * @return resultset if successful, else null.
	 */
	public ResultSet getUsers() {

		ResultSet result = null;
		try {
			result = executeQuery(USERS_QUERY);
		} catch (SQLException e) {
			result = null;
			showDBError("An error occurred while trying to access Users.");
		}
		return result;
	}

	/**
	 * Add a new user to the DB
	 * @param info
	 * @return
	 */
	public boolean addUser(UserInfo info) {
		boolean success = false;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("INSERT INTO USERS (USERNAME,PASSWORD,HINT) ");
			buffer.append("VALUES ('");
			buffer.append(info.getName().toLowerCase());
			buffer.append("','");
			buffer.append(info.getPassword());
			buffer.append("','");
			buffer.append(info.getHint());
			buffer.append("');");
			executeUpdate(buffer.toString());
			
			String template = info.getTemplate();
			if (template != null && !template.trim().isEmpty()) {
				success = addUserTemplate(template,info.getName().toLowerCase());
			}
			else {
				success = true;
			}
			
		} catch (SQLException e) {
			showDBError("An error occurred while trying to add a user to the Database.");
		}
		return success;
	}
	
	/**
	 * Create a new grocery category for a user
	 * @param categoryName
	 * @param userName
	 * 
	 * @return the categoryID of the new category, or -1 if unsuccessful
	 */
	protected int createCategory(String categoryName, String userName) {
		int id = -1;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("INSERT INTO CATEGORIES (CATEGORYNAME,USERNAME) ");
			buffer.append("VALUES ('");
			buffer.append(categoryName);
			buffer.append("','");
			buffer.append(userName);
			buffer.append("');");
			executeUpdate(buffer.toString());
			
			buffer = new StringBuffer();
			buffer.append("SELECT CATEGORYID FROM CATEGORIES ");
			buffer.append("WHERE lower(CATEGORYNAME)='");
			buffer.append(categoryName.toLowerCase());
			buffer.append("' AND lower(USERNAME)='");
			buffer.append(userName.toLowerCase());
			buffer.append("';");
			ResultSet result = executeQuery(buffer.toString());
			while (result.next()) {
				id = result.getInt("CATEGORYID");
			}
			System.out.println("Category created for " + categoryName + ": " + id);
			
		} catch (SQLException e) {
			showDBError("An error occurred while trying to add a category to the Database.");
		}
		return id;
	}
	
	/**
	 * Create a new grocery for a user.
	 * 
	 * @param groceryName
	 * @param categoryId
	 * @param userName
	 * 
	 * @return new grocery ID, or -1 on failure.
	 */
	protected int createGrocery(String groceryName, int categoryId, String userName) {
		int id = -1;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("INSERT INTO GROCERIES (GROCERYNAME,CATEGORYID,USERNAME) ");
			buffer.append("VALUES ('");
			buffer.append(groceryName);
			buffer.append("',");
			buffer.append(categoryId);
			buffer.append(",'");
			buffer.append(userName);
			buffer.append("');");
			executeUpdate(buffer.toString());
			
			buffer = new StringBuffer();
			buffer.append("SELECT GROCERYID FROM GROCERIES ");
			buffer.append("WHERE lower(GROCERYNAME)='");
			buffer.append(groceryName.toLowerCase());
			buffer.append("' AND lower(USERNAME)='");
			buffer.append(userName.toLowerCase());
			buffer.append("' AND CATEGORYID=");
			buffer.append(categoryId);
			buffer.append(";");
			ResultSet result = executeQuery(buffer.toString());
			while (result.next()) {
				id = result.getInt("GROCERYID");
			}
			System.out.println("Grocery created for " + groceryName + ": " + id);
			
		} catch (SQLException e) {
			showDBError("An error occurred while trying to add a grocery to the Database.");
		}
		return id;
	}
	
	/**
	 * Create a new selection for a grocery (a selection is when a user
	 * includes a grocery in their list)
	 * 
	 * @param groceryId
	 * @param categoryId
	 * @param userName
	 * 
	 * @return true if successful
	 */
	protected boolean createSelection(int groceryId, int categoryId, String userName) {
		boolean success = false;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("INSERT INTO SELECTIONS (GROCERYID,CATEGORYID,USERNAME,QUANTITY) ");
			buffer.append("VALUES (");
			buffer.append(groceryId);
			buffer.append(",");
			buffer.append(categoryId);
			buffer.append(",'");
			buffer.append(userName);
			buffer.append("',0);");
			executeUpdate(buffer.toString());
			
			System.out.println("Selection created for grocery " + groceryId);
			success = true;
			
		} catch (SQLException e) {
			showDBError("An error occurred while trying to add a selection to the Database.");
		}
		return success;
	}
	
	/**
	 * Add a template for a grocery list for a user.
	 * @param template
	 * @param userName
	 * @return
	 */
	public boolean addUserTemplate(String template, String userName) {
		boolean ok = true;
		try {
			File xmlTemplate = new File(template);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlTemplate);
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("category");
			for (int counter = 0; counter < list.getLength() && ok == true; counter++) {
				Node node = list.item(counter);
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(TemplateParseUtils.CATEGORY)) {
					//add category
					Element element = (Element) node;
					String categoryName = element.getAttribute(TemplateParseUtils.NAME);
					int catId = createCategory(categoryName, userName);
					if (catId<0) {
						ok = false;
					}
					else {
					
					//add groceries for this category
						NodeList children = element.getChildNodes();
						for (int inner = 0; inner<children.getLength() && ok == true; inner++) {
							Node childNode = children.item(inner);
							if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(TemplateParseUtils.GROCERY)) {
								Element childElement = (Element) childNode;
								String groceryName = childElement.getAttribute(TemplateParseUtils.NAME);
								int grocId = createGrocery(groceryName, catId, userName);
								if (grocId < 0) {
									ok = false;
								}
								else {
									ok = createSelection(grocId, catId, userName);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			String errMsg = "An error occurred while updating the database for " + template;
			showDBError(errMsg);
		}
		return ok;
	}

	/**
	 * Remove a user from the DB.
	 * 
	 * @param info
	 * @return true on success
	 */
	public boolean removeUser(UserInfo info) {
		boolean success = false;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("DELETE FROM USERS WHERE lower(USERNAME)='");
			buffer.append(info.getName().toLowerCase());
			buffer.append("';");

			executeUpdate(buffer.toString());
			
			//clear out user's DB
			buffer = new StringBuffer();
			buffer.append("DELETE FROM SELECTIONS WHERE lower(USERNAME)='");
			buffer.append(info.getName().toLowerCase());
			buffer.append("';");
			executeUpdate(buffer.toString());
			
			buffer = new StringBuffer();
			buffer.append("DELETE FROM GROCERIES WHERE lower(USERNAME)='");
			buffer.append(info.getName().toLowerCase());
			buffer.append("';");
			executeUpdate(buffer.toString());
			
			buffer = new StringBuffer();
			buffer.append("DELETE FROM CATEGORIES WHERE lower(USERNAME)='");
			buffer.append(info.getName().toLowerCase());
			buffer.append("';");
			executeUpdate(buffer.toString());
			success = true;
		} catch (SQLException e) {
			showDBError("An error occurred while trying to remove a user from the Database.");
		}
		return success;
	}

	/**
	 * Get categories for a user
	 * @param info
	 * @return
	 */
	public ResultSet getUserCategories(UserInfo info) {
		ResultSet result = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT * FROM CATEGORIES WHERE lower(USERNAME) = '");
			buffer.append(info.getName().toLowerCase());
			buffer.append("';");
			result = executeQuery(buffer.toString());
		} catch (SQLException e) {
			result = null;
			showDBError("An error occurred while trying to retrieve categories for user " + info.getName());
		}
		return result;
	}
	
	/**
	 * Get groceries for a user
	 * @param category
	 * @return
	 */
	public ResultSet getUserGroceries(Category category) {
		ResultSet result = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT * FROM GROCERIES WHERE lower(USERNAME) = '");
			buffer.append(category.userName.toLowerCase());
			buffer.append("' AND CATEGORYID = ");
			buffer.append(category.categoryID);
			buffer.append(";");
			result = executeQuery(buffer.toString());
		} catch (SQLException e) {
			result = null;
			showDBError("An error occurred while trying to retrieve groceries for category " + category.categoryName);
		}
		return result;
	}
	
	/**
	 * Get the qty of groceries for a user.
	 * @param grocery
	 * @return
	 */
	public int getUserGroceriesQuantity(Grocery grocery) {
		int quantity = 0;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT * FROM SELECTIONS WHERE lower(USERNAME) = '");
			buffer.append(grocery.userName.toLowerCase());
			buffer.append("' AND CATEGORYID = ");
			buffer.append(grocery.categoryId);
			buffer.append(" AND GROCERYID = ");
			buffer.append(grocery.groceryId);
			buffer.append(";");
			ResultSet result = executeQuery(buffer.toString());
			while (result.next()) {
				quantity = result.getInt("quantity");
			}
		} catch (SQLException e) {
	
			showDBError("An error occurred while trying to retrieve grocery quantities.");
		}
		return quantity;
	}

}
