package shoppinglist.dataobjects;

/**
 * Category data object in DB.
 * @author weis_
 *
 */
public class Category extends ShoppingListObject {
	
	private static final long serialVersionUID = 9196106786373368794L;
	public String userName;		//user name
	public String categoryName; //category name
	public int categoryID;      //category ID
	
	public Category(String category) {
		this("", category, 0);
	}
	
	public Category(String user, String category, int id) {
		userName = user;
		categoryName = category;
		categoryID = id;
	}
	
	public String toString() { return categoryName; }

	@Override
	public String getToolTip() {
		return categoryName;
	}
	
	/**
	 * compare categories by categoryname. If 
	 * category names match, they are equal.
	 * param: obj to compare
	 * 
	 * return: true if match
	 */
	public boolean equals(Object obj) {
		boolean same = false;
		if (obj instanceof Category) {
			Category u = (Category) obj;
			if (u.categoryName.equalsIgnoreCase(categoryName)) {
				same = true;
			}
		}
		return same;
	}

	@Override
	public String getName() {
		return categoryName;
	}
	
	@Override
	public void setName(String name) {
		categoryName = name;		
	}


}
