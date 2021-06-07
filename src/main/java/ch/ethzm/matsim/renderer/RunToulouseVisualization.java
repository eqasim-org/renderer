package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunToulouseVisualization {
	static public void main(String[] args) throws ConfigurationException {
		/*-
		 * This script writes a series of png files to an output directory,
		 * visualizing a MATSim simulation. As input it needs a network file and an
		 * events file from the MATSim output. Afterwards, using FFMPEG the png files
		 * can be combined to a video with a command like this:
		 * 
		 * ffmpeg -framerate 25 -i video_%d.png -c:v libx264 -profile:v high -crf 20 -pix_fmt yuv420p output.mp4
		 */

		CommandLine cmd = new CommandLine.Builder(args) //
				.requireOptions("network-path", "events-path", "output-path") //
				.build();

		// START CONFIGURATION

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 1280;
		renderConfig.height = 720;

		renderConfig.networkPath = cmd.getOptionStrict("network-path");
		renderConfig.eventsPath = cmd.getOptionStrict("events-path");
		renderConfig.outputPath = cmd.getOptionStrict("output-path");

		renderConfig.startTime = 8.0 * 3600.0;
		renderConfig.endTime = 9.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(573361.0, 6278698.0);
		renderConfig.zoom = 40000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(200, 200, 200);

		VehicleConfig generalVehicle = new VehicleConfig();
		renderConfig.vehicles.add(generalVehicle);
		generalVehicle.color = Arrays.asList(241, 87, 38);

		ActivityConfig workActivity = new ActivityConfig();
		renderConfig.activities.add(workActivity);
		workActivity.types = Arrays.asList("work");
		workActivity.color = Arrays.asList(86, 196, 165);

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
