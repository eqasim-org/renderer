package ch.ethzm.matsim.renderer.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VehicleConfig {
	public List<String> startsWith = new LinkedList<>();
	public List<String> endsWith = new LinkedList<>();
	public List<String> contains = new LinkedList<>();

	public List<Integer> color = Arrays.asList(200, 200, 200);

	public void validate() {
		if (color.size() != 3) {
			throw new IllegalStateException("Color must be (R,G,B)");
		}
	}

	public boolean isGeneric() {
		return startsWith.size() == 0 && endsWith.size() == 0 && contains.size() == 0;
	}
}
