package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunSmsVisualization {
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

		renderConfig.width = 1600; // 1280;
		renderConfig.height = 900; // 720;

		renderConfig.networkPath = "/home/shoerl/sms/scenario/sms_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/sms/scenario/simulation_with_pt/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/sms/video";

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 12.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0 * 10.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(682869.0 + 2000.0, 6848960.0 + 7000.0); // -5000.0
		renderConfig.zoom = 30000.0;

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
		ptVehicle.color = Arrays.asList(7, 145, 222);
		ptVehicle.size = 6;

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
