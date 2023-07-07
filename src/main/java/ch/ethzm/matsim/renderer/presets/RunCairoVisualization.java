package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunCairoVisualization {
	static public void main(String[] args) {
		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 1280;
		renderConfig.height = 720;

		renderConfig.networkPath = "simulation_output/output_network.xml.gz";
		renderConfig.eventsPath = "simulation_output/output_events.xml.gz";
		renderConfig.outputPath = "video.mp4";

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 9.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(638372.0, 818741.0);
		renderConfig.zoom = 40000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(240, 240, 240);

		NetworkConfig subwayNetwork = new NetworkConfig();
		renderConfig.networks.add(subwayNetwork);
		subwayNetwork.modes = Arrays.asList("subway", "rail");
		subwayNetwork.color = Arrays.asList(200, 200, 200);

		VehicleConfig otherVehicle = new VehicleConfig();
		renderConfig.vehicles.add(otherVehicle);
		otherVehicle.color = Arrays.asList(160, 160, 160);
		otherVehicle.size = 2;

		VehicleConfig ptVehicle = new VehicleConfig();
		renderConfig.vehicles.add(ptVehicle);
		ptVehicle.contains = Arrays.asList("subway", "rail");
		ptVehicle.color = Arrays.asList(0, 0, 0); // .asList(7, 145, 222);
		ptVehicle.size = 4;

		ActivityConfig workActivity = new ActivityConfig();
		renderConfig.activities.add(workActivity);
		workActivity.types.add("work");
		workActivity.maximumLifetime = 300.0;
		workActivity.size = 12;
		workActivity.color = Arrays.asList(7, 145, 222);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
