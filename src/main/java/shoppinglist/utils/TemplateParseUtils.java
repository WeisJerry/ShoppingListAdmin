package shoppinglist.utils;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shoppinglist.dataobjects.Category;
import shoppinglist.dataobjects.Grocery;

/**
 * Utilities to assist in parsing and saving templates.
 * (Templates are precreated shopping lists that can be applied to a user)
 * 
 * @author weis_
 *
 */
public class TemplateParseUtils {

	public static final String CATEGORY = "category";
	public static final String GROCERY = "grocery";
	public static final String NAME = "name"; 
	
	private static TemplateParseUtils TEMPLATEPARSEUTILS = new TemplateParseUtils();

	protected TemplateParseUtils() {
	}

	/**
	 * Get the one-and-only instance
	 * @return
	 */
	public static TemplateParseUtils getInstance() {
		return TEMPLATEPARSEUTILS;
	}

	/**
	 * Save the specified treemodel as a template.
	 * 
	 * @param model
	 * @param templateName filename for template
	 * @return true on success
	 */
	public boolean saveModel(DefaultTreeModel model, String templateName) {
		boolean success = false;
		if (!templateName.isEmpty()) {
			String tplName = templateName.toLowerCase().trim();

			if (tplName.lastIndexOf(TemplateSelectionUtils.DOT_EXTENSION) < 0) {
				tplName += TemplateSelectionUtils.DOT_EXTENSION;
			}

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("template");
				doc.appendChild(rootElement);

				Object root = model.getRoot();
				int count = model.getChildCount(root);

				for (int counter = 0; counter < count; counter++) {
					Object obj = model.getChild(root, counter);
					Element category = doc.createElement("category");
					category.setAttribute("name", obj.toString());
					rootElement.appendChild(category);

					int grocCount = model.getChildCount(obj);
					for (int grCounter = 0; grCounter < grocCount; grCounter++) {
						Object object = model.getChild(obj, grCounter);
						Element grocery = doc.createElement("grocery");
						grocery.setAttribute("name", object.toString());
						category.appendChild(grocery);
					}
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(tplName));
				transformer.transform(source, result);

				success = true;

			} catch (ParserConfigurationException e) {

			} catch (TransformerConfigurationException e) {

			} catch (TransformerException e) {

			}
		}

		return success;
	}

	/**
	 * Open the treemodel for the specified template file
	 * @param newTemplateName
	 * @return
	 */
	public DefaultMutableTreeNode openModel(String newTemplateName) {
		DefaultMutableTreeNode root = null;
		try {
			File xmlTemplate = new File(newTemplateName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlTemplate);
			doc.getDocumentElement().normalize();

			root = new DefaultMutableTreeNode("");

			NodeList list = doc.getElementsByTagName("category");
			for (int counter = 0; counter < list.getLength(); counter++) {
				Node node = list.item(counter);
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(CATEGORY)) {
					//add category
					Element element = (Element) node;
					Category cat = new Category(element.getAttribute(NAME));
					DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(cat);
					root.add(newTreeNode);
					//add groceries for this category
					NodeList children = element.getChildNodes();
					for (int inner = 0; inner<children.getLength(); inner++) {
						Node childNode = children.item(inner);
						if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(GROCERY)) {
							Element childElement = (Element) childNode;
							Grocery groc = new Grocery(childElement.getAttribute(NAME));
							DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(groc);
							newTreeNode.add(childTreeNode);
						}
					}
				}
			}

		} catch (Exception e) {
			String errMsg = "An error occurred while opening " + newTemplateName;
			JOptionPane.showMessageDialog(null, errMsg, "Open template file failure", JOptionPane.ERROR_MESSAGE);
		}

		return root;
	}

}
