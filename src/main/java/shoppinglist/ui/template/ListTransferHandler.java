package shoppinglist.ui.template;

import java.awt.Component;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import shoppinglist.dataobjects.ShoppingListObject;

public class ListTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -1702025745159835140L;
	private static ListTransferHandler LISTTRANSFERHANDLER = new ListTransferHandler();

	protected ListTransferHandler() {
	}

	public static ListTransferHandler getInstance() {
		return LISTTRANSFERHANDLER;
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Transferable createTransferable(JComponent source) {
		Transferable t = null;
		if (source instanceof JList<?>) {
			JList<ShoppingListObject> sourceList = (JList<ShoppingListObject>) source;
			t = sourceList.getSelectedValue();
		}
		return t;
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		System.out.println("export done");
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDrop()) {
			return false;
		}

		if (!(support.getDropLocation() instanceof JTree.DropLocation)) {
			return false;
		}

		return support.isDataFlavorSupported(ShoppingListObject.FLAVOR);
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		Transferable t = support.getTransferable();
		ShoppingListObject shoppingListObject;

		try {
			Object obj = t.getTransferData(ShoppingListObject.FLAVOR);
			if (obj != null && obj instanceof ShoppingListObject) {
				shoppingListObject = (ShoppingListObject) obj;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
		JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
		Component component = support.getComponent();
		
		if (component instanceof JTree) {
			Object obj = component.getParent();
			if (obj instanceof TemplateEditorPanel) {
				TemplateEditorPanel editor = (TemplateEditorPanel) obj;
				return editor.addShoppingListObject(shoppingListObject,dropLocation);
			}
		}
		return false;
		
		
	}
}
