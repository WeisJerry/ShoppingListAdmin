package shoppinglist.dataobjects;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Base class for individual objects in shopping lists.
 * Allows for drag and drop as well as serialization.
 * 
 * @author weis_
 *
 */
 public abstract class ShoppingListObject implements Serializable, Transferable{

	private static final long serialVersionUID = -67419020979547240L;

	//override in child data object classes
	abstract public String getToolTip();
	abstract public String getName();
	abstract public void setName(String name);
	
	
	//for drag n drop purposes
	public static DataFlavor FLAVOR = new DataFlavor(ShoppingListObject.class,"ShoppingListObject");

	public DataFlavor getDataFlavor() {
		return FLAVOR;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {getDataFlavor()};
		return flavors;
		
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(getDataFlavor());
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		Object obj = null;
		if (isDataFlavorSupported(flavor)) {
			obj = this;
		}
		return obj;
	}

}
