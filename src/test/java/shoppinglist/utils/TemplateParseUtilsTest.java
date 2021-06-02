package shoppinglist.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import shoppinglist.testutils.FileUtils;

class TemplateParseUtilsTest {
	
	private static String createdFile = "src/test/resources/created.tpl";
	private static String expectedFile = "src/test/resources/expected.tpl";
	private static int childCount = 4;
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		File file = new File(createdFile);
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	void testSaveModel() {
		//Set up the tree model to use for testing
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		for (int index=0; index<childCount; index++) {
			String childName = "node" + String.valueOf(index);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(childName);
			root.add(node);
			for (int innerIndex=0; innerIndex<childCount; innerIndex++) {
				String grandChildName = "subnode" + String.valueOf(innerIndex);
				DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(grandChildName);
				node.add(subnode);
			}
		}
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		
		//save the model
		TemplateParseUtils util = TemplateParseUtils.getInstance();
		assertTrue(util.saveModel(model, createdFile));
		
		//retrieve the saved model and compare it to the expected file format.
		String createdContent = FileUtils.getFile(createdFile, false);
		String expectedContent = FileUtils.getFile(expectedFile, false);
		assertEquals(expectedContent, createdContent);
	}

	@Test
	void testOpenModel() {
		TemplateParseUtils utils = TemplateParseUtils.getInstance();
		DefaultMutableTreeNode root = utils.openModel(expectedFile);
		assertNotNull(root);
		
		//check contents of tree
		assertEquals(childCount,root.getChildCount());
		
		for (int index=0; index<childCount; index++) {
			String nodeName = "node" + String.valueOf(index);
			TreeNode node = root.getChildAt(index);
			assertEquals(nodeName, node.toString());
			for (int innerIndex=0; innerIndex<childCount; innerIndex++) {
				String subNodeName = "subnode" + String.valueOf(innerIndex);
				TreeNode subNode = node.getChildAt(innerIndex);
				assertEquals(subNodeName, subNode.toString());
			}
		}
		
		
		
	}

}
