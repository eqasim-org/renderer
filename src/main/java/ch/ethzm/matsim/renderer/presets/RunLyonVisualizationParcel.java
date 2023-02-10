package ch.ethzm.matsim.renderer.presets;

import java.util.Arrays;

import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
import ch.ethzm.matsim.renderer.main.RunRenderer;

public class RunLyonVisualizationParcel {

	public static void main(String[] args) {

		RenderConfig renderConfig = new RenderConfig();

		renderConfig.width = 720;
		renderConfig.height = 720;

		renderConfig.networkPath = "/home/shoerl/lead/marc/output_network.xml.gz";
		renderConfig.eventsPath = "/home/shoerl/lead/marc/output_events.xml.gz";
		renderConfig.outputPath = "/home/shoerl/lead/marc/colors.mp4";

		renderConfig.outputFormat = RenderConfig.OutputFormat.Video;

		renderConfig.startTime = 8.0 * 3600.0 + 10.0 * 60.0;
		renderConfig.endTime = 9 * 3600.0;
		renderConfig.secondsPerFrame = 120.0;

		renderConfig.showTime = false;

		renderConfig.center = Arrays.asList(841642.0, 6517753.0);
//        renderConfig.center = Arrays.asList(845014.0, 6518624.0);
		renderConfig.zoom = 16000.0;

		NetworkConfig roadNetwork = new NetworkConfig();
		renderConfig.networks.add(roadNetwork);
		roadNetwork.modes = Arrays.asList("car");
		roadNetwork.color = Arrays.asList(220, 220, 220);

		int size = 6;

		{// laposte
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(128, 177, 211); // dark blue
			deliveryVehicle.startsWith = Arrays.asList("0_", "1_", "2_", "3_", "17_", "18_", "19_", "22_", "23_", "24_",
					"27_", "30_", "31_", "32_", "33_", "34_", "36_", "37_", "39_", "50_", "52_", "57_", "106_", "107_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}
		{// fedex
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(251, 128, 114); // purple
			deliveryVehicle.startsWith = Arrays.asList("21_", "35_", "53_", "64_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}
		{// ups
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(253, 180, 98); // brown
			deliveryVehicle.startsWith = Arrays.asList("62_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}
		{// dhl
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(255, 255, 179); // yellow
			deliveryVehicle.startsWith = Arrays.asList("20_", "26_", "63_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}
		{// dpd
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(190, 186, 218); // red
			deliveryVehicle.startsWith = Arrays.asList("25_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}
		{// chronopost
			VehicleConfig deliveryVehicle = new VehicleConfig();
			renderConfig.vehicles.add(deliveryVehicle);
			deliveryVehicle.color = Arrays.asList(141, 211, 199); // green
			deliveryVehicle.startsWith = Arrays.asList("28_", "29_", "38_", "103_");
			deliveryVehicle.size = size;
			deliveryVehicle.stayAfterExit = true;
		}

		RunRenderer.run(renderConfig);

	}
}