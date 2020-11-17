package ch.ethzm.matsim.renderer.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ActivityConfig {
	public List<String> types = new LinkedList<>();
	public List<Integer> color = Arrays.asList(200, 200, 200);

	public double maximumLifetime = 700.0;
	public double size = 20.0;

	public void validate() {
		if (color.size() != 3) {
			throw new IllegalStateException("Color must be (R,G,B)");
		}
	}

	public boolean isGeneric() {
		return types.size() == 0;
	}
}
