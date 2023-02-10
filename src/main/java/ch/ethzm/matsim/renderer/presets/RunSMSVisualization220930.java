package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig.OutputFormat;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunSMSVisualization220930 {
	static public void main(String[] args) {
		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 720;
		renderConfig.height = 720;
		
		//renderConfig.networkPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_network.xml.gz";
		//renderConfig.eventsPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_events.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/sms1/temp/output_events.xml.gz";
		renderConfig.networkPath = "/home/shoerl/sms1/temp/output_network.xml.gz";
		renderConfig.outputPath = "/home/shoerl/sms1/temp/sms_scenario.mp4";
		renderConfig.outputFormat = OutputFormat.Video;

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 10.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = true;

		renderConfig.center = Arrays.asList(359632.0, 6693928.0);
		renderConfig.zoom = 6000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(240, 240, 240);

		VehicleConfig otherVehicle = new VehicleConfig();
		renderConfig.vehicles.add(otherVehicle);
		otherVehicle.color = Arrays.asList(160, 160, 160);
		otherVehicle.size = 4;

		ActivityConfig workActivity = new ActivityConfig();
		renderConfig.activities.add(workActivity);
		workActivity.types.add("work");
		workActivity.maximumLifetime = 300.0;
		workActivity.size = 14;
		workActivity.color = Arrays.asList(7, 145, 222);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
