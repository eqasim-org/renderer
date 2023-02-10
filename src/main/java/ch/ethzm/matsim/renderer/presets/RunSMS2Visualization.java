package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig.OutputFormat;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunSMS2Visualization {
	static public void main(String[] args) {
		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 1080;
		renderConfig.height = 720;
		
		//renderConfig.networkPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_network.xml.gz";
		//renderConfig.eventsPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_events.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/sms2/video/output_events.xml.gz";
		renderConfig.networkPath = "/home/shoerl/sms2/video/output_network.xml.gz";
		renderConfig.outputPath = "/home/shoerl/sms2/video/video.mp4";
		renderConfig.outputFormat = OutputFormat.Video;

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 10.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = true;

		renderConfig.center = Arrays.asList(797014.0, 6644511.0);
		renderConfig.zoom = 16000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(240, 240, 240);

		NetworkConfig subwayNetwork = new NetworkConfig();
		renderConfig.networks.add(subwayNetwork);
		subwayNetwork.modes = Arrays.asList("subway", "rail", "bus", "pt");
		//subwayNetwork.color = Arrays.asList(200, 200, 200);
		subwayNetwork.color = Arrays.asList(240, 240, 240);

		VehicleConfig otherVehicle = new VehicleConfig();
		renderConfig.vehicles.add(otherVehicle);
		otherVehicle.color = Arrays.asList(160, 160, 160);
		otherVehicle.size = 4;

		VehicleConfig ptVehicle = new VehicleConfig();
		renderConfig.vehicles.add(ptVehicle);
		ptVehicle.contains = Arrays.asList("subway", "rail", "pt", "bus");
		//ptVehicle.color = Arrays.asList(7, 145, 222);
		ptVehicle.color = Arrays.asList(0, 0, 0);
		ptVehicle.size = 5;
		
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
