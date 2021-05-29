package shoppinglist.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import shoppinglist.dataobjects.Grocery;
import shoppinglist.dataobjects.ShoppingListObject;
import shoppinglist.testutils.FileUtils;

class ConfigParseUtilsTest {
	
	private static final String TEST_CONFIG = "test_config.cfg";
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		File file = new File(TEST_CONFIG);
		if (file.exists()) {
			file.delete();
		}
	}
	
	@Test
	void testCreateXmlConfigFragment() {
		String expected = "<topLevel><Grocery>grocery0</Grocery><Grocery>grocery1</Grocery>"
				+"<Grocery>grocery2</Grocery><Grocery>grocery3</Grocery><Grocery>grocery4</Grocery>"
				+"<Grocery>grocery5</Grocery><Grocery>grocery6</Grocery><Grocery>grocery7</Grocery>"
				+"<Grocery>grocery8</Grocery><Grocery>grocery9</Grocery></topLevel>";

		List<ShoppingListObject> items = new ArrayList<ShoppingListObject>();
		for (int x=0; x<10; x++) {
			String val = String.valueOf(x);
			Grocery object = new Grocery("user"+val, x, x, "grocery"+val, x+1);
			items.add(object);
		}
		
		ConfigParseUtils utils = ConfigParseUtils.getInstance();
		String result = utils.createXmlConfigFragment(items, "topLevel");
		assertEquals(expected,result);
	}

	@Test
	void testCreateXmlConfigDoc() {
		List<String> fragments = new ArrayList<String>();
		for (int x=0; x<10; x++) {
			String frag = "<fragment>" + String.valueOf(x) + "</fragment>";
			fragments.add(frag);
		}
		
		ConfigParseUtils utils = ConfigParseUtils.getInstance();
		assertTrue(utils.createXmlConfigDoc(fragments, TEST_CONFIG));
		
		String result = FileUtils.getFile(TEST_CONFIG, false);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Configurations>"
		+"<fragment>0</fragment><fragment>1</fragment><fragment>2</fragment><fragment>3</fragment>"
				+"<fragment>4</fragment><fragment>5</fragment><fragment>6</fragment><fragment>7</fragment>"
				+"<fragment>8</fragment><fragment>9</fragment></Configurations>";
		assertEquals(expected,result);
		
		//test bad filename
		assertFalse(utils.createXmlConfigDoc(fragments, ""));
		
	}

	@Test
	void testGetClassName() {
		String expected = "ConfigParseUtilsTest";
		ConfigParseUtils utils = ConfigParseUtils.getInstance();
		String name = utils.getClassName(this);
		assertEquals(name,expected);
	}

	@Test
	void testRestoreXmlConfigSettings() {
		String userDirectory = System.getProperty("user.dir");
		String fileName = userDirectory + "/src/test/resources/templateconfigstest.xml";
		ConfigParseUtils utils = ConfigParseUtils.getInstance();
		
		//test bad class name: should return empty set
		List<String> results = utils.restoreXmlConfigSettings("badclassname", fileName);
		assertEquals(0, results.size());
		
		//test Dairy, check contents of list returned
		results = utils.restoreXmlConfigSettings("Categories", fileName);
		assertEquals(5, results.size());
		
		int index = 0;
		for (String result: results) {
			String expected = "Dairy" + String.valueOf(index++);
			assertEquals(expected, result);
		}
		
		
	}

}
