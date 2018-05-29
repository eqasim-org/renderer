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
import ch.ethzm.matsim.renderer.network.LinkDatabase;
import ch.ethzm.matsim.renderer.traversal.TraversalDatabase;
import ch.ethzm.matsim.renderer.traversal.TraversalListener;
import ch.ethzm.matsim.renderer.traversal.VehicleDatabase;

public class Main {
	final static int windowWidth = 1280;
	final static int windowHeight = 720;

	static public void main(String[] args) {
		double startTime = 0.0 * 3600.0;
		double endTime = 24.0 * 3600.0;
		double binSize = 300.0;

		TraversalDatabase traversalDatabase = new TraversalDatabase(startTime, endTime, binSize);
		ActivityDatabase activityDatabase = new ActivityDatabase(startTime, endTime, binSize);
		LinkDatabase linkDatabase = new LinkDatabase();
		ActivityTypeMapper activityTypeMapper = new ActivityTypeMapper();
		VehicleDatabase vehicleDatabase = new VehicleDatabase();

		//String networkFile = "/home/sebastian/vidsmall/astra_network.xml.gz";
		//String eventsFile = "/home/sebastian/vidsmall/0.events.xml.gz";

		String networkFile = "/home/sebastian/vid/astra_network.xml.gz";
		String eventsFile = "/home/sebastian/vid/160.events.xml.gz";

		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(networkFile);

		for (Link link : network.getLinks().values()) {
			linkDatabase.addLink(link);
		}

		EventsManager eventsManager = new EventsManagerImpl();
		TraversalListener traversalListener = new TraversalListener(linkDatabase, traversalDatabase, network,
				vehicleDatabase);
		ActivitySliceListener activityListener = new ActivitySliceListener(linkDatabase, activityDatabase,
				activityTypeMapper, network);
		eventsManager.addHandler(traversalListener);
		eventsManager.addHandler(activityListener);
		new MatsimEventsReader(eventsManager).readFile(eventsFile);

		JFrame mainFrame = new JFrame("Renderer");
		RenderFrame renderFrame = new RenderFrame(traversalDatabase, linkDatabase, activityDatabase, activityTypeMapper,
				vehicleDatabase);

		mainFrame.add(renderFrame);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(windowWidth, windowHeight);
		mainFrame.setLayout(null);
		mainFrame.setVisible(true);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			renderFrame.repaint();
		}, 0, 10, TimeUnit.MILLISECONDS);
	}
}
