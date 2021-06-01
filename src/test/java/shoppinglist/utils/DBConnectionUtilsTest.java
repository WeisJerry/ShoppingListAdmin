package shoppinglist.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.UserInfo;

class DBConnectionUtilsTest {

	@Test
	void testGetUsers() throws SQLException {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();

		//validate query string to get users
		ResultSet set = utils.getUsers();
		String result = set.getNString(0);
		String expected = "SELECT * FROM USERS;";
		assertEquals(expected,result);

		//validate error message
		utils.forceException(true);
		set = utils.getUsers();
		assertNull(set);
		result = utils.getDbError();
		expected = "An error occurred while trying to access Users.";
		assertEquals(expected,result);

	}

	@Test
	void testAddUser() {

		//test adding user
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		UserInfo info = new UserInfo("name","password","hint");
		utils.addUser(info);
		assertEquals(1,utils.getQueryCount());
		String result = utils.getQueryString(0);
		String expected = "INSERT INTO USERS (USERNAME,PASSWORD,HINT) VALUES ('name','password','hint');";
		assertEquals(expected,result);

		//test mixed case name
		utils = new PartialMockDBConnectionUtils();
		info = new UserInfo("NAme","password","hint");
		utils.addUser(info);
		assertEquals(1,utils.getQueryCount());
		result = utils.getQueryString(0);
		expected = "INSERT INTO USERS (USERNAME,PASSWORD,HINT) VALUES ('name','password','hint');";
		assertEquals(expected,result);

		//testing adding user template is in testAddUserTemplate()

		//test display error
		utils.forceException(true);
		assertFalse(utils.addUser(info));
		result = utils.getDbError();
		expected = "An error occurred while trying to add a user to the Database.";
		assertEquals(expected,result);

		//testing adding user template is in testAddUserTemplate()

		//test display error
		utils.forceException(true);
		assertFalse(utils.addUser(info));
		result = utils.getDbError();
		expected = "An error occurred while trying to add a user to the Database.";
		assertEquals(expected,result);
	}

	@Test
	void testCreateCategory() {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		assertEquals(10,utils.createCategory("MyCategory", "user"));
		assertEquals(2,utils.getQueryCount());
		String q1 = utils.getQueryString(0);
		String q2 = utils.getQueryString(1);
		String expected1 = "INSERT INTO CATEGORIES (CATEGORYNAME,USERNAME) VALUES ('MyCategory','user');";
		String expected2 = "SELECT CATEGORYID FROM CATEGORIES WHERE lower(CATEGORYNAME)='mycategory' AND lower(USERNAME)='user';";
		assertEquals(expected1,q1);
		assertEquals(expected2,q2);

		utils.forceException(true);
		assertEquals(-1,utils.createCategory("MyCategory", "user"));

		String result = utils.getDbError();
		String expected = "An error occurred while trying to add a category to the Database.";
		assertEquals(expected,result);

	}

	@Test
	void testCreateGrocery() {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		assertEquals(9,utils.createGrocery("MyGrocery", 15, "user"));
		assertEquals(2,utils.getQueryCount());
		String q1 = utils.getQueryString(0);
		String q2 = utils.getQueryString(1);
		String expected1 = "INSERT INTO GROCERIES (GROCERYNAME,CATEGORYID,USERNAME) VALUES ('MyGrocery',15,'user');";
		String expected2 = "SELECT GROCERYID FROM GROCERIES WHERE lower(GROCERYNAME)='mygrocery' AND lower(USERNAME)='user' AND CATEGORYID=15;";
		assertEquals(expected1,q1);
		assertEquals(expected2,q2);

		utils.forceException(true);
		assertEquals(-1,utils.createGrocery("MyGrocery", 15, "user"));

		String result = utils.getDbError();
		String expected = "An error occurred while trying to add a grocery to the Database.";
		assertEquals(expected,result);
	}

	@Test
	void testCreateSelection() {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		assertTrue(utils.createSelection(14, 15, "user"));
		assertEquals(1,utils.getQueryCount());
		String q1 = utils.getQueryString(0);
		String expected = "INSERT INTO SELECTIONS (GROCERYID,CATEGORYID,USERNAME,QUANTITY) VALUES (14,15,'user',0);";
		assertEquals(expected,q1);

		utils.forceException(true);
		assertFalse(utils.createSelection(14, 15, "user"));

		String result = utils.getDbError();
		expected = "An error occurred while trying to add a selection to the Database.";
		assertEquals(expected,result);
	}

	@Test
	void testAddUserTemplate() {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		String userDirectory = System.getProperty("user.dir");
		String templateName = userDirectory + "/src/test/resources/test.tpl";
		boolean result = utils.addUserTemplate(templateName, "user");
		assertTrue(result);

		int count = utils.getQueryCount();
		assertEquals(13, count);

		List<String>queries = utils.getQueries();
		String[] expected = {
				"INSERT INTO CATEGORIES (CATEGORYNAME,USERNAME) VALUES ('Dairy','user');",
				"SELECT CATEGORYID FROM CATEGORIES WHERE lower(CATEGORYNAME)='dairy' AND lower(USERNAME)='user';",
				"INSERT INTO GROCERIES (GROCERYNAME,CATEGORYID,USERNAME) VALUES ('Milk',10,'user');",
				"SELECT GROCERYID FROM GROCERIES WHERE lower(GROCERYNAME)='milk' AND lower(USERNAME)='user' AND CATEGORYID=10;",
				"INSERT INTO SELECTIONS (GROCERYID,CATEGORYID,USERNAME,QUANTITY) VALUES (9,10,'user',0);",
				"INSERT INTO CATEGORIES (CATEGORYNAME,USERNAME) VALUES ('Hardware','user');",
				"SELECT CATEGORYID FROM CATEGORIES WHERE lower(CATEGORYNAME)='hardware' AND lower(USERNAME)='user';",
				"INSERT INTO GROCERIES (GROCERYNAME,CATEGORYID,USERNAME) VALUES ('Hammer',10,'user');",
				"SELECT GROCERYID FROM GROCERIES WHERE lower(GROCERYNAME)='hammer' AND lower(USERNAME)='user' AND CATEGORYID=10;",
				"INSERT INTO SELECTIONS (GROCERYID,CATEGORYID,USERNAME,QUANTITY) VALUES (9,10,'user',0);",
				"INSERT INTO GROCERIES (GROCERYNAME,CATEGORYID,USERNAME) VALUES ('Nails',10,'user');",
				"SELECT GROCERYID FROM GROCERIES WHERE lower(GROCERYNAME)='nails' AND lower(USERNAME)='user' AND CATEGORYID=10;",
				"INSERT INTO SELECTIONS (GROCERYID,CATEGORYID,USERNAME,QUANTITY) VALUES (9,10,'user',0);"	
		};

		int index = 0;
		for(String query: queries) {
			assertEquals(expected[index++],query);
		}

		//now test error
		utils.forceException(true);
		result = utils.addUserTemplate(templateName, "user");
		assertFalse(result);

		String expectedErr = "An error occurred while trying to add a category to the Database.";
		assertEquals(expectedErr,utils.getDbError());
	}

	@Test
	void testRemoveUser() {
		//test removing user
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		UserInfo info = new UserInfo("name","password","hint");
		utils.removeUser(info);
		assertEquals(4,utils.getQueryCount());
		String expected = "DELETE FROM USERS WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(0));
		expected = "DELETE FROM SELECTIONS WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(1));
		expected = "DELETE FROM GROCERIES WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(2));
		expected = "DELETE FROM CATEGORIES WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(3));

		//test upper case name
		utils = new PartialMockDBConnectionUtils();
		info = new UserInfo("NAME","password","hint");
		utils.removeUser(info);
		assertEquals(4,utils.getQueryCount());
		expected = "DELETE FROM USERS WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(0));
		expected = "DELETE FROM SELECTIONS WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(1));
		expected = "DELETE FROM GROCERIES WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(2));
		expected = "DELETE FROM CATEGORIES WHERE lower(USERNAME)='name';";
		assertEquals(expected,utils.getQueryString(3));

		//test display error
		utils = new PartialMockDBConnectionUtils();
		utils.forceException(true);
		assertFalse(utils.removeUser(info));
		String result = utils.getDbError();
		expected = "An error occurred while trying to remove a user from the Database.";
		assertEquals(expected,result);
	}

	@Test
	void testGetUserCategories() throws SQLException {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		UserInfo info = new UserInfo("name","password","hint");

		//validate query string to get users
		ResultSet set = utils.getUserCategories(info);
		String result = set.getNString(0);
		String expected = "SELECT * FROM CATEGORIES WHERE lower(USERNAME) = 'name';";
		assertEquals(expected,result);
		
		//validate error message
		utils.forceException(true);
		set = utils.getUserCategories(info);
		assertNull(set);
		result = utils.getDbError();
		expected = "An error occurred while trying to retrieve categories for user name";
		assertEquals(expected,result);
		
		//check uppercase
		utils = new PartialMockDBConnectionUtils();
		info = new UserInfo("NAME","password","hint");

		//validate query string to get users
		set = utils.getUserCategories(info);
		result = set.getNString(0);
		expected = "SELECT * FROM CATEGORIES WHERE lower(USERNAME) = 'name';";
		assertEquals(expected,result);

		//validate error message
		utils.forceException(true);
		set = utils.getUserCategories(info);
		assertNull(set);
		result = utils.getDbError();
		expected = "An error occurred while trying to retrieve categories for user NAME";
		assertEquals(expected,result);
	}

	@Test
	void testGetUserGroceries() throws SQLException {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		Category category = new Category("name", "cat", 5);

		//validate query string to get users
		ResultSet set = utils.getUserGroceries(category);
		String result = set.getNString(0);
		String expected = "SELECT * FROM GROCERIES WHERE lower(USERNAME) = 'name' AND CATEGORYID = 5;";
		assertEquals(expected,result);
		
		//validate error message
		utils.forceException(true);
		set = utils.getUserGroceries(category);
		assertNull(set);
		result = utils.getDbError();
		expected = "An error occurred while trying to retrieve groceries for category cat";
		assertEquals(expected,result);
		
		//check uppercase
		utils = new PartialMockDBConnectionUtils();
		category = new Category("NAME", "cat", 5);

		//validate query string to get users
		set = utils.getUserGroceries(category);
		result = set.getNString(0);
		expected = "SELECT * FROM GROCERIES WHERE lower(USERNAME) = 'name' AND CATEGORYID = 5;";
		assertEquals(expected,result);

		//validate error message
		utils.forceException(true);
		set = utils.getUserGroceries(category);
		assertNull(set);
		result = utils.getDbError();
		expected = "An error occurred while trying to retrieve groceries for category cat";
		assertEquals(expected,result);
	}

	@Test
	void testGetUserGroceriesQuantity() {
		PartialMockDBConnectionUtils utils = new PartialMockDBConnectionUtils();
		Grocery grocery = new Grocery("user", 10, 11, "grocery", 6);
		int qty = utils.getUserGroceriesQuantity(grocery);
		assertEquals(8,qty);
		assertEquals(1,utils.getQueryCount());
		String result = utils.getQueryString(0);
		String expected = "SELECT * FROM SELECTIONS WHERE lower(USERNAME) = 'user' AND CATEGORYID = 11 AND GROCERYID = 10;";
		assertEquals(expected, result);
		
		//validate error message
		utils.forceException(true);
		qty = utils.getUserGroceriesQuantity(grocery);
		assertEquals(0,qty);
		result = utils.getDbError();
		expected = "An error occurred while trying to retrieve grocery quantities.";
		assertEquals(expected,result);
	}

}
