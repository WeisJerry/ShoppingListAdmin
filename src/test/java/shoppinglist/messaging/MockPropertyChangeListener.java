package shoppinglist.messaging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MockPropertyChangeListener implements PropertyChangeListener {
	
	private List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
	
	public List<PropertyChangeEvent> getEvents() {
		return events;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		events.add(evt);
	}

}
