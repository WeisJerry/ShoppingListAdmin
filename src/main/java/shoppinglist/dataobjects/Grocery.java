package shoppinglist.dataobjects;

/**
 * Grocery DB object
 * 
 * @author weis_
 *
 */
public class Grocery extends ShoppingListObject {
	
	private static final long serialVersionUID = 2119119633765068798L;
	public String userName;
    public int groceryId;
	public int categoryId;
    public String groceryName;
    public int quantity;
    
    
    public Grocery(String groceryName) {
    	this("",0,0,groceryName);
    }
    public Grocery(String user, int grocId, int catId, String grocery) {
    	this(user, grocId, catId, grocery, 0);
    }
    
    public Grocery(String user, int grocId, int catId, String grocery, int qty) {
    	userName = user;
    	groceryId = grocId;
    	categoryId = catId;
    	groceryName = grocery;
    	quantity = qty;
    }
    
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }
    
    public String toString() { 
    	String buffer = groceryName;
    	if (quantity > 0) {
    		buffer += " - ";
    		buffer += String.valueOf(quantity);
    	}
    	return buffer; 
    }
    

	@Override
	public String getToolTip() {
		return groceryName;
	}

	public boolean equals(Object obj) {
		boolean same = false;
		if (obj instanceof Grocery) {
			Grocery u = (Grocery) obj;
			if (u.groceryName.equalsIgnoreCase(groceryName)) {
				same = true;
			}
		}
		return same;
	}
	@Override
	public String getName() {
		return groceryName;
	}
	
	@Override
	public void setName(String name) {
		groceryName = name;		
	}
	
}
