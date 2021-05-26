/**
 * 
 */
package shoppinglist.utils.configurations;

import java.awt.Color;

/**
 * @author weis_
 * 
 * Manage the UI color scheme
 *
 */
public enum ComponentColor {
	BACKGROUND {
		@Override
		public Color getColor() {
			return new Color(125, 125, 125);
		}
	},
	FOREGROUND {
		@Override
		public Color getColor() {
			return new Color(200,200,200);
		}
	},
	LIGHT {
		@Override
		public Color getColor() {
			return new Color(255,255,255);
		}
		
	};
	
	public abstract Color getColor();
}
