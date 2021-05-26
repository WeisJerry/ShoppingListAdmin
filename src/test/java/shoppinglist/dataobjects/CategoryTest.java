package shoppinglist.dataobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryTest {

	protected String user1 = "user1";
	protected String user2 = "user2";
	protected String name1 = "name1";
	protected String name2 = "name2";
	protected int id1 = 12345;
	protected int id2 = 23456;
	protected Category category = new Category(user1, name1, id1);
	protected Category categorySame = new Category(user2, name1, id2);
	protected Category categoryDifferent = new Category(user2, name2, id2);

	@Test
	void testGetToolTip() {
		String tip = category.getToolTip();
		assertEquals(tip, name1);
		tip = categoryDifferent.getToolTip();
		assertEquals(tip, name2);
	}

	@Test
	void testToString() {
		String string = category.toString();
		assertEquals(string,name1);
		string = categoryDifferent.toString();
		assertEquals(string,name2);
	}

	@Test
	void testEqualsObject() {
		assertTrue(category.equals(categorySame));
		assertFalse(category.equals(categoryDifferent));
	}


}
