package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunLyonVisualization {
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

		renderConfig.width = 720;
		renderConfig.height = 720;

		renderConfig.networkPath = "/home/shoerl/temp/lyon10pct/ile_de_france_network.xml.gz";
		// renderConfig.eventsPath = "/home/shoerl/temp/lyon10pct/output_events.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/temp/lyon10pct/simulation_with_pt/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/temp/lyon10pct/video_small";

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 10.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(841642.0, 6517753.0);
		renderConfig.zoom = 20000.0;

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
