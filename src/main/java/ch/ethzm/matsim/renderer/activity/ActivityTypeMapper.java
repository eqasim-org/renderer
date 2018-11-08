package ch.ethzm.matsim.renderer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityTypeMapper {
	final private Map<String, Integer> map = new HashMap<>();
	final private List<String> types = new ArrayList<>();

	public int addActivityType(String activityType) {
		if (map.containsKey(activityType)) {
			return map.get(activityType);
		}

		int index = map.size();
		map.put(activityType, index);
		types.add(activityType);

		return index;
	}

	public List<String> getData() {
		return types;
	}

	public String getActivityType(int index) {
		return types.get(index);
	}
	
	public boolean hasActivityType(String activityType) {
		return map.containsKey(activityType);
	}

	public int getIndex(String activityType) {
		Integer result = map.get(activityType);

		if (result == null) {
			throw new IllegalStateException(activityType);
		}

		return result;
	}
}
