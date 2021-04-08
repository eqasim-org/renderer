package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunLEADVisualization {
	static public void main(String[] args) {
		/*-
		 * This script writes a series of png files to an output directory,
		 * visualizing a MATSim simulation. As input it needs a network file and an
		 * events file from the MATSim output. Afterwards, using FFMPEG the png files
		 * can be combined to a video with a command like this:
		 * 
		 * ffmpeg -framerate 25 -i video_%d.png -c:v libx264 -profile:v high -crf 20 -pix_fmt yuv420p output.mp4
		 */

		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 1280;
		renderConfig.height = 720;

		renderConfig.networkPath = "/home/shoerl/lead/output/two_hubs/simulation_output/output_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/lead/output/two_hubs/simulation_output/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/video/lyon";

		renderConfig.startTime = 9.5 * 3600.0;
		renderConfig.endTime = 10.0 * 3600.0;
		renderConfig.secondsPerFrame = 300.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(841469.0 - 4000.0, 6517253.0);
		renderConfig.zoom = 8000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(220, 220, 220);

		/*
		 * VehicleConfig carVehicle = new VehicleConfig();
		 * renderConfig.vehicles.add(carVehicle); carVehicle.color = Arrays.asList(0, 0,
		 * 0);
		 */

		VehicleConfig freightVehicle = new VehicleConfig();
		renderConfig.vehicles.add(freightVehicle);
		freightVehicle.startsWith = Arrays.asList("freight_");
		freightVehicle.color = Arrays.asList(7, 145, 222);

		VehicleConfig hubVehicle = new VehicleConfig();
		renderConfig.vehicles.add(hubVehicle);
		hubVehicle.contains = Arrays.asList("hub");
		hubVehicle.color = Arrays.asList(200, 0, 0);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
