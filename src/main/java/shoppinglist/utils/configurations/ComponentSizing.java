package shoppinglist.utils.configurations;

/**
 * Handle configuration for component sizing.
 * Sizing is decimal representing percent. 
 * (1.0 = %100)
 * 
 * @author weis_
 *
 */
public enum ComponentSizing {
	APPSIZE(0.85),
	MANAGERSIZE(.9),
	CREATORSIZE(.9),
	FRAMESIZE(0.75);
	
	private final double sizing;
	
	ComponentSizing(double size) {
		sizing=size;
	}
	
	/**
	 * Retrieve the specified sizing
	 * @return decimal representing sizing.
	 */
	public double getSizing() {
		return this.sizing;
	}

}
