package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunTaxiVisualization {
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

		renderConfig.networkPath = "/home/shoerl/Downloads/taxi/output_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/Downloads/taxi/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/video";

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 9.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(2683253.0, 1246745.0);
		renderConfig.zoom = 10000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(200, 200, 200);

		VehicleConfig generalVehicle = new VehicleConfig();
		renderConfig.vehicles.add(generalVehicle);
		generalVehicle.color = Arrays.asList(200, 200, 200);

		VehicleConfig taxiVehicle = new VehicleConfig();
		renderConfig.vehicles.add(taxiVehicle);
		taxiVehicle.contains = Arrays.asList("amodeus");
		taxiVehicle.color = Arrays.asList(0, 0, 0);

		ActivityConfig pudoActivity = new ActivityConfig();
		renderConfig.activities.add(pudoActivity);
		pudoActivity.types = Arrays.asList("pickup", "dropoff");
		pudoActivity.color = Arrays.asList(7, 145, 222);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
