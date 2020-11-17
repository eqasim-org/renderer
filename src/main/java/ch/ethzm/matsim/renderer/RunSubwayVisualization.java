package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunSubwayVisualization {
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

		renderConfig.networkPath = "/home/shoerl/pt_sim/ile_de_france_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/pt_sim/0.events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/video";

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 9.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;
		
		renderConfig.center = Arrays.asList(a);
		renderConfig.zoom = 

		NetworkConfig roadNetwork = new NetworkConfig();
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(240, 240, 240);

		NetworkConfig subwayNetwork = new NetworkConfig();
		subwayNetwork.modes = Arrays.asList("subway");
		subwayNetwork.color = Arrays.asList(200, 200, 200);

		VehicleConfig ptVehicle = new VehicleConfig();
		ptVehicle.contains = Arrays.asList("subway");
		ptVehicle.color = Arrays.asList(7, 145, 222);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
