package shoppinglist.dataobjects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Holds info for DB connection
 * @author weis_
 *
 */
public class DBConnectionInfo {
	
	private static final String CONFIG_FILE = "configurations.xml";

	private String host = "";
	private String port = "";
	private String dbname = "";
	private String user = "";
	private String password = "";

	public DBConnectionInfo(String host, String port, String dbname, String user, String password) {
		updateConnection(host, port, dbname, user, password);
	}
	public DBConnectionInfo() { }

	/**
	 * Update a connection object
	 * 
	 * @param host
	 * @param port
	 * @param dbname
	 * @param user
	 * @param password
	 */
	public void updateConnection(String host, String port, String dbname, String user, String password) {
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.user = user;
		this.password = password;
	}

	// Getters and setters
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setDBName(String dbname) {
		this.dbname = dbname;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getDBName() {
		return dbname;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getConfigFileName() {
		return CONFIG_FILE;
	}

	/**
	 * Retrieve the connection string to use, baed on these settings.
	 * 
	 * @return string of format
	 *         "jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>&sslmode=require"
	 */
	public String getConnectionString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("jdbc:postgresql://");
		buffer.append(host);
		buffer.append(":");
		buffer.append(port);
		buffer.append("/");
		buffer.append(dbname);
		buffer.append("?user=");
		buffer.append(user);
		buffer.append("&password=");
		buffer.append(password);
		buffer.append("&sslmode=require");

		return buffer.toString();
	}

	/**
	 * Save the settings in this object
	 * @return true if successful
	 */
	public boolean saveSettings() {
		boolean saved = false;
		try {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("configurations");
		doc.appendChild(rootElement);
		Element connection = doc.createElement("dbconnectionsettings");
		rootElement.appendChild(connection);

		Element hostElement = doc.createElement("host");
		hostElement.appendChild(doc.createTextNode(host));
		connection.appendChild(hostElement);

		Element dbnameElement = doc.createElement("dbname");
		dbnameElement.appendChild(doc.createTextNode(dbname));
		connection.appendChild(dbnameElement);

		Element pwdElement = doc.createElement("pwd");
		pwdElement.appendChild(doc.createTextNode(password));
		connection.appendChild(pwdElement);

		Element portElement = doc.createElement("port");
		portElement.appendChild(doc.createTextNode(port));
		connection.appendChild(portElement);

		Element userElement = doc.createElement("user");
		userElement.appendChild(doc.createTextNode(user));
		connection.appendChild(userElement);

		// write the content into xml file
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(getConfigFileName());

		getTransformer().transform(source, result);
		saved = true;
		
		} catch (Exception e) {
			//don't do anything; handled by return
		}
		return saved;
	}
	
	/**
	 * Helper method to retrieve transformer to use.
	 * 
	 * @return transformer
	 * @throws TransformerConfigurationException
	 */
	protected Transformer getTransformer() throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		return transformer;
	}
	
	/**
	 * Restore old DB settings, if available.
	 * @return true on success
	 */
	public boolean restoreOldSettings() {
		boolean written = false;
		
		try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(getConfigFileName(), new DefaultHandler() {
				
				boolean inPort = false;
				boolean inUser = false;
				boolean inPwd = false;
				boolean inHost = false;
				boolean inDBname = false;
				
				@Override
				public void startElement(String uri, String localName,String name,
		                Attributes attributes) throws SAXException {
					
					if (name.equalsIgnoreCase("port")) {
						inPort = true;
					}
					else if (name.equalsIgnoreCase("host")) {
						inHost = true;
					}
					else if (name.equalsIgnoreCase("user")) {
						inUser = true;
					}
					else if (name.equalsIgnoreCase("pwd")) {
						inPwd = true;
					}
					else if (name.equalsIgnoreCase("dbname")) {
						inDBname = true;
					}
				}
				
				public void characters(char ch[], int start, int length) throws SAXException {

					if (inPort) {
						setPort(new String(ch, start, length));
						inPort = false;
					}
					else if (inHost) {
						setHost(new String(ch, start, length));
						inHost = false;
					}
					else if (inUser) {
						setUser(new String(ch, start, length));
						inUser = false;
					}
					else if (inPwd) {
						setPassword(new String(ch, start, length));
						inPwd = false;
					}
					else if (inDBname) {
						setDBName(new String(ch, start, length));
						inDBname = false;
					}
				}
				
			});
		
			written = true;
		} catch (Exception e) {
			//do nothing
		}
		
		return written; 
	}
}
