package shoppinglist.dataobjects;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

public class PartialMockDBConnectionInfo extends DBConnectionInfo {
	
	private boolean isTransformerBad = false;
	private String testConfigFileName = "testconfigurations.xml";
	
	public void returnBadTransformer(boolean val) {
		isTransformerBad = val;
	}
	
	public void setConfigFileName(String name) {
		testConfigFileName = name;
	}
	
	public String getConfigFileName() {
		return testConfigFileName;
	}
	
	protected Transformer getTransformer() throws TransformerConfigurationException {
		if (isTransformerBad) {
			throw new TransformerConfigurationException();
		}
		return super.getTransformer();
	}

	public void killConfigFile() {
		String fileName = getConfigFileName();
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}
}
