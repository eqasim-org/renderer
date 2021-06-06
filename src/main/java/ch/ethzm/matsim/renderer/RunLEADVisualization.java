package ch.ethzm.matsim.renderer;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.ActivityConfig;
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

		renderConfig.width = 720;
		renderConfig.height = 720;

		renderConfig.networkPath = "/home/shoerl/lead/movements_output/output_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/lead/movements_output/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/lead/movements_visualisation";

		renderConfig.startTime = 6.0 * 3600.0 + 10.0 * 60.0;
		renderConfig.endTime = 7.0 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(841642.0, 6517753.0 - 500.0);
		renderConfig.zoom = 1200.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(220, 220, 220);

		{
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(27,158,119);
			deliveryVehicle.contains = Arrays.asList("vehicle_1");
			deliveryVehicle.size = 8;
		}

		{
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(217,95,2);
			deliveryVehicle.contains = Arrays.asList("vehicle_2");
			deliveryVehicle.size = 8;
		}

		{
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(117,112,179);
			deliveryVehicle.contains = Arrays.asList("vehicle_3");
			deliveryVehicle.size = 8;
		}

		{
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(231, 138, 195);
			deliveryVehicle.contains = Arrays.asList("vehicle_4");
			deliveryVehicle.size = 8;
		}

		{
			ActivityConfig deliveryActivity = new ActivityConfig();
			renderConfig.activities.add(deliveryActivity);
			deliveryActivity.types.add("deliverShipment_vehicle_1");
			deliveryActivity.maximumLifetime = 600.0;
			deliveryActivity.size = 25;
			deliveryActivity.color = Arrays.asList(27,158,119);
		}

		{
			ActivityConfig deliveryActivity = new ActivityConfig();
			renderConfig.activities.add(deliveryActivity);
			deliveryActivity.types.add("deliverShipment_vehicle_2");
			deliveryActivity.maximumLifetime = 600.0;
			deliveryActivity.size = 25;
			deliveryActivity.color = Arrays.asList(217,95,2);
		}

		{
			ActivityConfig deliveryActivity = new ActivityConfig();
			renderConfig.activities.add(deliveryActivity);
			deliveryActivity.types.add("deliverShipment_vehicle_3");
			deliveryActivity.maximumLifetime = 600.0;
			deliveryActivity.size = 25;
			deliveryActivity.color = Arrays.asList(117,112,179);
		}

		{
			ActivityConfig deliveryActivity = new ActivityConfig();
			renderConfig.activities.add(deliveryActivity);
			deliveryActivity.types.add("deliverShipment_vehicle_4");
			deliveryActivity.maximumLifetime = 600.0;
			deliveryActivity.size = 25;
			deliveryActivity.color = Arrays.asList(231, 138, 195);
		}

		for (int i = 0; i < 4; i++) {
			ActivityConfig pickupActivity = new ActivityConfig();
			renderConfig.activities.add(pickupActivity);
			pickupActivity.types.add("pickupShipment_vehicle_" + i);
			pickupActivity.maximumLifetime = 600.0;
			pickupActivity.size = 25;
			pickupActivity.color = Arrays.asList(0, 0, 0);
		}

		// END CONFIGURATION

		RunRenderer.run(renderConfig);
	}
}
