package shoppinglist.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.junit.jupiter.api.Test;

class MessageRouterTest {
	
	

	@Test
	void testNotifySimpleListeners() {
		//test simple notifications, and adding change listeners
		MessageRouter instance = MessageRouter.getInstance();
		MockPropertyChangeListener[] listeners = new MockPropertyChangeListener[3];
		for (int ctr=0; ctr<3; ctr++) {
			listeners[ctr] = new MockPropertyChangeListener();
			instance.addChangeListener(listeners[ctr]);
		}

		instance.notifySimpleListeners("message_id");

		for (int ctr=0; ctr<3; ctr++) {
			List<PropertyChangeEvent> events = listeners[ctr].getEvents();
			assertEquals(events.size(),1);
			PropertyChangeEvent evt = events.get(0);
			assertEquals(evt.getPropertyName(),"message_id");
			Object o = evt.getOldValue();
			assertNull(o);
			o = evt.getNewValue();
			assertNull(o);
		}
	}

	@Test
	void testNotifyListeners() {
		
		//test notifications, and adding change listeners
		MessageRouter instance = MessageRouter.getInstance();
		MockPropertyChangeListener[] listeners = new MockPropertyChangeListener[3];
		for (int ctr=0; ctr<3; ctr++) {
			listeners[ctr] = new MockPropertyChangeListener();
			instance.addChangeListener(listeners[ctr]);
		}
		
		Object oldObj = new String("old");
		Object newObj = new String("new");
		instance.notifyListeners("message_id", oldObj, newObj);
		
		for (int ctr=0; ctr<3; ctr++) {
			List<PropertyChangeEvent> events = listeners[ctr].getEvents();
			assertEquals(events.size(),1);
			PropertyChangeEvent evt = events.get(0);
			assertEquals(evt.getPropertyName(),"message_id");
			Object o = evt.getOldValue();
			assertNotNull(o);
			assertEquals("old",o.toString());
			o = evt.getNewValue();
			assertNotNull(o);
			assertEquals("new",o.toString());
		}	
	}
}

