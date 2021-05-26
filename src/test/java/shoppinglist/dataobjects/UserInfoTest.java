package shoppinglist.dataobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserInfoTest {


	@Test
	void testGetToolTip() {
		String name = "name"; 
		String password = "password";
		String hint = "hint";
		String expected = "<html>Name: name<br>Password: password<br>Hint: hint</html>";
		
		UserInfo info1 = new UserInfo(name, password, hint);
		String tip = info1.getToolTip();
		assertEquals(expected, tip);
	}

	@Test
	void testEqualsObject() {
		String name = "name"; 
		String password = "password";
		String hint = "hint";
		String name2 = "name2"; 
		String password2 = "password2";
		String hint2 = "hint2";
		
		UserInfo info1 = new UserInfo(name, password, hint);
		UserInfo info2 = new UserInfo(name2, password2, hint2);
		assertFalse(info1.equals(info2));
		
		info2 = new UserInfo(name2, password, hint);
		assertFalse(info1.equals(info2));
		
		info2 = new UserInfo(name, password2, hint2);
		assertTrue(info1.equals(info2));
	}

}
