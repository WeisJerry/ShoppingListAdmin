package shoppinglist.utils;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilities for handling encryption.
 * 
 * @author weis_
 *
 */
public class EncryptionUtils {

	private static final String ENCRYPTION_KEY = "aGVsbG8gd29ybGQ=Gvv4ql==2gceJer3";

	/**
	 * Encrypt a string
	 * 
	 * @param text
	 * @return encrypted text
	 * 
	 * @throws Exception
	 */
	public static String encrypt(String text) throws Exception {

		byte[] input = text.toString().getBytes("utf-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
		SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skc);

		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(cipherText);
	}
}