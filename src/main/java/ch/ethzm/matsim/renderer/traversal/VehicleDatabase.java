package ch.ethzm.matsim.renderer.traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.vehicles.Vehicle;

public class VehicleDatabase {
	final private Map<String, Integer> map = new HashMap<>();
	final private List<String> ids = new ArrayList<>();

	public int addVehicle(Id<Vehicle> vehicleId) {
		return addVehicle(vehicleId.toString());
	}

	public int addVehicle(String vehicleId) {
		if (map.containsKey(vehicleId)) {
			return map.get(vehicleId);
		} else {
			int index = map.size();
			map.put(vehicleId, index);
			return index;
		}
	}

	public Map<String, Integer> getVehicleIndices() {
		return map;
	}

	public List<String> getData() {
		return ids;
	}

}
