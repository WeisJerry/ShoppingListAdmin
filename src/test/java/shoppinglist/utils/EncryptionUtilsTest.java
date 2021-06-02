package shoppinglist.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EncryptionUtilsTest {

	@Test
	void testEncrypt() throws Exception {
		
		String[] start = {
				"",
				"abcdEFG",
				"a1b2c3d4",
				"QWERT12345",
				"asdf asdf"
				
		};
		String[] expected = {
				"zp87ihrPQDossp0l2JWWYw==",
				"Bsyljb48F8PGDXxTdl2j/g==",
				"UNKUnJZYpeu1Gd6g3Qanqw==",
				"fEEF/+9u/gFHUbbAdYbOHQ==",
				"zPo/nVkA3vJqRGNvqythlA=="
		}; 
		
		for (int index=0; index<start.length; index++) {
			String end = EncryptionUtils.encrypt(start[index]);
			assertEquals(expected[index],end);
		}	
	}
}
