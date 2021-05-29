/**
 * 
 */
package shoppinglist.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import shoppinglist.dataobjects.ShoppingListObject;

/**
 * @author weis_
 *
 */
public class ConfigParseUtils {

	private static ConfigParseUtils CONFIGPARSEUTILS = new ConfigParseUtils();

	protected ConfigParseUtils() {
	}

	public static ConfigParseUtils getInstance() {
		return CONFIGPARSEUTILS;
	}

	/**
	 * Create a configuration fragment from a shopping list item.
	 * 
	 * @param items
	 * @param topLevel
	 * @return
	 */
	public String createXmlConfigFragment(List<ShoppingListObject> items, String topLevel) {
		String domString = "";
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(topLevel);
			doc.appendChild(rootElement);

			for (ShoppingListObject item : items) {
				String name = getClassName(item);
				Element element = doc.createElement(name);
				element.appendChild(doc.createTextNode(item.getName()));
				rootElement.appendChild(element);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			domString = writer.getBuffer().toString();

		} catch (Exception e) {
			System.out.println("There was a problem creating the XML fragment configuration.");
		}
		return domString;
	}

	/**
	 * Create a config XML doc from a bunch of fragments.
	 * 
	 * @param fragments
	 * @param fileName
	 * @return true if successfully created and saved
	 */
	public boolean createXmlConfigDoc(List<String> fragments, String fileName) {
		boolean saved = false;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		buffer.append("<Configurations>");
		for (String fragment : fragments) {
			buffer.append(fragment);
		}
		buffer.append("</Configurations>");

		try (PrintWriter out = new PrintWriter(fileName)) {
			out.println(buffer.toString());
			out.close();
			saved = true;
		} catch (FileNotFoundException e) {
			// do nothing, handled in return
		}
		return saved;
	}

	/**
	 * Get the shortened class name for an object.
	 * 
	 * @param item
	 * @return string holding shortened class name
	 */
	public String getClassName(Object item) {
		String name = item.getClass().toString();
		name = name.substring(name.lastIndexOf('.') + 1);
		return name;
	}

	public List<String> restoreXmlConfigSettings(String className, String fileName) {
		final String classNameString = className;
		final List<String> configs = new ArrayList<String>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(fileName, new DefaultHandler() {
				boolean inclass = false;
				
				@Override
				public void startElement(String uri, String localName, String name, Attributes attributes)
						throws SAXException {

					if (name.equalsIgnoreCase(classNameString)) {
						inclass = true;
					}
				}

				@Override
				public void endElement(String uri, String localName, String name) throws SAXException {

					if (name.equalsIgnoreCase(classNameString)) {
						inclass = false;
					}
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (inclass) {
						String buffer = new String(ch, start, length);
						if (!buffer.equals("\n")) {
							configs.add(buffer);
						}
					}

				}

			});

		} catch (Exception e) {
			//do nothing
		}

		return configs;
	}
}
