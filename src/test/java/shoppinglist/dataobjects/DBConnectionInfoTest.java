package shoppinglist.dataobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import shoppinglist.testutils.FileUtils;

class DBConnectionInfoTest {
	
	protected String host = "host";
	protected String port = "port";
	protected String dbname = "dbname";
	protected String user = "user";
	protected String password = "password";
	
	protected DBConnectionInfo info = new DBConnectionInfo(host, port, dbname, user, password);
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		PartialMockDBConnectionInfo info = new PartialMockDBConnectionInfo();
		info.killConfigFile();
		
	}

	@Test
	void testGetConnectionString() {
		String expected = "jdbc:postgresql://host:port/dbname?user=user&password=password&sslmode=require";
		String result = info.getConnectionString();
		assertEquals(expected, result);
	}

	@Test
	void testSaveSettings() {
		
		PartialMockDBConnectionInfo info = new PartialMockDBConnectionInfo();
		info.updateConnection(host, port, dbname, user, password);
		
		boolean result = info.saveSettings();
		assertTrue(result);
		
		//compare output configfile with expected
		String output = FileUtils.getFile(info.getConfigFileName(), false);
		String userDirectory = System.getProperty("user.dir");
		String expectedFile = userDirectory + "/src/test/resources/expectedDBcfg.xml";
		String expected = FileUtils.getFile(expectedFile, false);
		assertEquals(output,expected);
		
		//force bad configfile.
		info.returnBadTransformer(true);
		result = info.saveSettings();
		assertFalse(result);
		
	}

	@Test
	void testRestoreOldSettings() {
		PartialMockDBConnectionInfo info = new PartialMockDBConnectionInfo();
		info.setConfigFileName("src/test/resources/restoredSettingsTest.xml");
		info.restoreOldSettings();
		
		String expected = "jdbc:postgresql://host:port/dbname?user=user&password=password&sslmode=require";
		String result = info.getConnectionString();
		assertEquals(expected, result);

			
	}

}
