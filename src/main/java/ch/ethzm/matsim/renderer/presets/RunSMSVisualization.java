package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig.OutputFormat;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunSMSVisualization {
	static public void main(String[] args) {
		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 1080;
		renderConfig.height = 720;
		
		//renderConfig.networkPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_network.xml.gz";
		//renderConfig.eventsPath = "/home/shoerl/sms1/analysis/data/am_veh20_safetyirtx/output_events.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/sms1/uge/matsin_output_am/ITERS/it.0/0.events.xml.gz";
		renderConfig.networkPath = "/home/shoerl/sms1/uge/data/drt_network.xml.gz";
		renderConfig.outputPath = "video.mp4";
		renderConfig.outputFormat = OutputFormat.Video;

		renderConfig.startTime = 8.5 * 3600.0;
		renderConfig.endTime = 9.5 * 3600.0;
		renderConfig.secondsPerFrame = 60.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(359292.0, 6693132.0 + 1000.0);
		renderConfig.zoom = 6000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(240, 240, 240);

		NetworkConfig subwayNetwork = new NetworkConfig();
		renderConfig.networks.add(subwayNetwork);
		subwayNetwork.modes = Arrays.asList("subway", "rail");
		subwayNetwork.color = Arrays.asList(200, 200, 200);

		NetworkConfig drtNetwork = new NetworkConfig();
		renderConfig.networks.add(drtNetwork);
		roadNetwork.modes = Arrays.asList("drt");
		roadNetwork.color = Arrays.asList(0, 0, 0);

		VehicleConfig otherVehicle = new VehicleConfig();
		renderConfig.vehicles.add(otherVehicle);
		otherVehicle.color = Arrays.asList(160, 160, 160);
		otherVehicle.size = 2;

		VehicleConfig ptVehicle = new VehicleConfig();
		renderConfig.vehicles.add(ptVehicle);
		ptVehicle.contains = Arrays.asList("tram", "bus");
		ptVehicle.color = Arrays.asList(100, 100, 100);
		ptVehicle.size = 2;

		VehicleConfig drtVehicle = new VehicleConfig();
		renderConfig.vehicles.add(drtVehicle);
		drtVehicle.contains = Arrays.asList("drt");
		drtVehicle.color = Arrays.asList(7, 145, 222); // .asList(7, 145, 222);
		drtVehicle.size = 8;
		drtVehicle.stayAfterExit = true;

		ActivityConfig stopActivity = new ActivityConfig();
		renderConfig.activities.add(stopActivity);
		stopActivity.types.add("DrtBusStop");
		stopActivity.maximumLifetime = 60.0;
		stopActivity.size = 20;
		stopActivity.color = Arrays.asList(7, 145, 222);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
