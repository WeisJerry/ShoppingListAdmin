package shoppinglist.dataobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GroceryTest {

	@Test
	void testGetToolTip() {
		String user = "user";
		int grocId = 1234;
		int catId = 2345;
		String groceryName = "groceryName"; 
		int qty = 12;
		
		Grocery grocery = new Grocery(user,grocId,catId,groceryName,qty);
		String result = grocery.getToolTip();
		assertEquals(result, groceryName);
	}

	@Test
	void testToString() {
		String user = "user";
		int grocId = 1234;
		int catId = 2345;
		String groceryName = "groceryName"; 
		int qty = 12;
		
		//test with qty
		String expected = groceryName + " - " + qty;
		Grocery grocery = new Grocery(user,grocId,catId,groceryName,qty);
		String result = grocery.toString();
		assertEquals(result, expected);
		
		//test without qty
		expected = groceryName;
		grocery = new Grocery(user,grocId,catId,groceryName,0);
		result = grocery.toString();
		assertEquals(result, expected);	
	}

	@Test
	void testEqualsObject() {
		String user1 = "user1";
		String user2 = "user2";
		int grocId1 = 1234;
		int grocId2 = 5678;
		int catId1 = 2345;
		int catId2 = 6789;
		String groceryName1 = "groceryName1";
		String groceryName2 = "groceryName2";
		int qty1 = 1;
		int qty2 = 2;

		Grocery grocery1 = new Grocery(user1,grocId1,catId1,groceryName1,qty1);
		Grocery grocery2 = new Grocery(user2,grocId2,catId2,groceryName2,qty2);
		assertFalse(grocery1.equals(grocery2));
		
		grocery2 = new Grocery(user2,grocId2,catId2,groceryName1,qty2);
		assertTrue(grocery1.equals(grocery2));
		
		grocery2 = new Grocery(user1,grocId1,catId1,groceryName2,qty1);
		assertFalse(grocery1.equals(grocery2));
	}

}
