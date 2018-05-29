package ch.ethzm.matsim.renderer.activity;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;

import ch.ethzm.matsim.renderer.network.LinkDatabase;

public class ActivitySliceListener implements ActivityStartEventHandler, ActivityEndEventHandler {
	final private LinkDatabase linkDatabase;
	final private ActivityDatabase activityDatabase;
	final private Network network;
	final private ActivityTypeMapper typeMapper;

	final private Map<Id<Person>, ActivityStartEvent> startEvents = new HashMap<>();

	public ActivitySliceListener(LinkDatabase linkDatabase, ActivityDatabase activityDatabase,
			ActivityTypeMapper typeMapper, Network network) {
		this.linkDatabase = linkDatabase;
		this.activityDatabase = activityDatabase;
		this.network = network;
		this.typeMapper = typeMapper;
	}

	@Override
	public void handleEvent(ActivityEndEvent endEvent) {
		ActivityStartEvent startEvent = startEvents.get(endEvent.getPersonId());

		if (startEvent != null) {
			int linkIndex = linkDatabase.getIndex(network.getLinks().get(startEvent.getLinkId()));
			ActivitySlice slice = new ActivitySlice(startEvent.getTime(), endEvent.getTime(),
					typeMapper.addActivityType(startEvent.getActType()), linkIndex);
			activityDatabase.addActivity(slice);
		}
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {
		startEvents.put(event.getPersonId(), event);
	}
}
