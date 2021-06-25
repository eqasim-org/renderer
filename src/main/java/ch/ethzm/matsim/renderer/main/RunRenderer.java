package ch.ethzm.matsim.renderer.main;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

import ch.ethzm.matsim.renderer.activity.ActivityDatabase;
import ch.ethzm.matsim.renderer.activity.ActivitySliceListener;
import ch.ethzm.matsim.renderer.activity.ActivityTypeMapper;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.network.LinkDatabase;
import ch.ethzm.matsim.renderer.traversal.TraversalDatabase;
import ch.ethzm.matsim.renderer.traversal.TraversalListener;
import ch.ethzm.matsim.renderer.traversal.VehicleDatabase;

public class RunRenderer {

	static public void run(RenderConfig renderConfig) {
		renderConfig.validate();

		// Prepare databases to read information

		double startTime = renderConfig.startTime;
		double endTime = renderConfig.endTime;
		double binSize = renderConfig.binSize;

		TraversalDatabase traversalDatabase = new TraversalDatabase(startTime, endTime, binSize);
		ActivityDatabase activityDatabase = new ActivityDatabase(startTime, endTime, binSize);

		LinkDatabase linkDatabase = new LinkDatabase(renderConfig);
		ActivityTypeMapper activityTypeMapper = new ActivityTypeMapper(renderConfig);

		VehicleDatabase vehicleDatabase = new VehicleDatabase(renderConfig);

		// Read input data

		String networkPath = renderConfig.networkPath;
		String eventsPath = renderConfig.eventsPath;

		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(networkPath);

		for (Link link : network.getLinks().values()) {
			linkDatabase.addLink(link);
		}

		EventsManager eventsManager = new EventsManagerImpl();
		TraversalListener traversalListener = new TraversalListener(linkDatabase, traversalDatabase, network,
				vehicleDatabase);
		ActivitySliceListener activityListener = new ActivitySliceListener(linkDatabase, activityDatabase,
				activityTypeMapper, network, renderConfig.minimumActivityDuration);
		eventsManager.addHandler(traversalListener);
		eventsManager.addHandler(activityListener);
		new MatsimEventsReader(eventsManager).readFile(eventsPath);

		// Prepare rendering

		JFrame mainFrame = new JFrame("Renderer");
		RenderFrame renderFrame = new RenderFrame(traversalDatabase, linkDatabase, activityDatabase, activityTypeMapper,
				vehicleDatabase, renderConfig);

		mainFrame.add(renderFrame);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(renderConfig.width, renderConfig.height);
		mainFrame.setLayout(null);
		mainFrame.setVisible(true);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			renderFrame.repaint();
		}, 0, 10, TimeUnit.MILLISECONDS);
	}
}
