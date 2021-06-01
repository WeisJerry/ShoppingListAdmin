package shoppinglist.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import org.junit.jupiter.api.Test;

class DialogUtilsTest {

	@Test
	void testSizeComponent() {
		int x = 800;
		int y = 600;
		PartialMockDialogUtils utils = new PartialMockDialogUtils(x, y);
		MockComponent component = new MockComponent();
		
		//check different ratios from 1.0 to 0.1
		double ratio = 1.0;
		
		while (ratio >= 0.1) {
		Component comp = utils.sizeComponent(component, ratio);
		assertSame(component,comp);
		Rectangle rect = comp.getBounds();
		assertEquals(y*ratio,rect.height);
		assertEquals(x*ratio,rect.width);
		assertEquals((int)(x/2-(x*ratio/2)),rect.x);
		assertEquals((int)(y/2-(y*ratio/2)),rect.y);
		ratio = ((int)(ratio*10) - 1)/10;
		}
		
		//test outside boundaries
		Component comp = utils.sizeComponent(component, 1.5);
		assertSame(component,comp);
		Rectangle rect = comp.getBounds();
		assertEquals(y,rect.height);
		assertEquals(x,rect.width);
		assertEquals(0,rect.x);
		assertEquals(0,rect.y);
		
		comp = utils.sizeComponent(component, 0.01);
		assertSame(component,comp);
		rect = comp.getBounds();
		assertEquals(y*0.1,rect.height);
		assertEquals(x*0.1,rect.width);
		assertEquals((int)(x/2-x*0.1/2),rect.x);
		assertEquals((int)(y/2-y*0.1/2),rect.y);
	}

	@Test
	void testPositionComponent() {
		int x = 800;
		int y = 600;
		PartialMockDialogUtils utils = new PartialMockDialogUtils(x,y);
		MockComponent component = new MockComponent();
		
		Component comp = utils.positionComponent(component);
		assertSame(component,comp);
		Point point = component.getLocation();
		assertEquals(x/2,point.x);
		assertEquals(y/2,point.y);
	}

	@Test
	void testCalculateBounds() {
		//this is tested through testSizeComponent()
	}

}
