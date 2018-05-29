package ch.ethzm.matsim.renderer.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.utils.misc.Time;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import ch.ethzm.matsim.renderer.activity.ActivityDatabase;
import ch.ethzm.matsim.renderer.activity.ActivityTypeMapper;
import ch.ethzm.matsim.renderer.network.LinkDatabase;
import ch.ethzm.matsim.renderer.traversal.TraversalDatabase;
import ch.ethzm.matsim.renderer.traversal.VehicleDatabase;

public class RenderFrame extends JPanel {
	final private TraversalDatabase traversalDatabase;
	final private LinkDatabase linkDatabase;
	final private ActivityDatabase activityDatabase;
	final private ActivityTypeMapper activityTypeMapper;
	final private VehicleDatabase vehicleDatabase;

	final private BitSet isAv;

	public RenderFrame(TraversalDatabase traversalDatabase, LinkDatabase linkDatabase,
			ActivityDatabase activityDatabase, ActivityTypeMapper activityTypeMapper, VehicleDatabase vehicleDatabase) {
		this.traversalDatabase = traversalDatabase;
		this.linkDatabase = linkDatabase;
		this.activityDatabase = activityDatabase;
		this.activityTypeMapper = activityTypeMapper;
		this.vehicleDatabase = vehicleDatabase;

		setSize(Main.windowWidth, Main.windowHeight);
		setVisible(true);
		setBackground(Color.WHITE);

		this.isAv = new BitSet(vehicleDatabase.getVehicleIndices().size());

		for (Map.Entry<String, Integer> entry : vehicleDatabase.getVehicleIndices().entrySet()) {
			if (entry.getKey().contains("av")) {
				isAv.set(entry.getValue());
			}
		}
	}

	private double time = 13.0 * 3600.0;
	private long previousRenderTime = -1;
	private boolean makeVideo = true;

	double timeStepPerSecond = 120.0;
	double framesPerSecond = 25.0;

	private long frameIndex = 0;

	Object imageLock = new Object();

	boolean useSvg = true;

	@Override
	public void paintComponent(Graphics windowGraphics) {
		Rectangle windowBounds = getBounds();

		BufferedImage surface = new BufferedImage(Main.windowWidth, Main.windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) surface.getGraphics();

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

		if (useSvg) {
			graphics = new SVGGraphics2D(document);
		}

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, Main.windowWidth, Main.windowHeight);

		long currentRenderTime = System.nanoTime();

		if (previousRenderTime == -1) {
			previousRenderTime = currentRenderTime;
		}

		long delay = currentRenderTime - previousRenderTime;
		previousRenderTime = currentRenderTime;

		if (makeVideo) {
			time += timeStepPerSecond / framesPerSecond;
			frameIndex++;
		} else if (delay > 0) {
			time += timeStepPerSecond * delay * 1e-9;
		}

		if (time > 24.0 * 3600.0) {
			time = 0.0;
		}

		// Coord bellevue = new Coord(2683253.0, 1246745.0);
		// double zoom = 10000.0;

		Coord bellevue = CoordUtils.plus(new Coord(2683253.0, 1246745.0), new Coord(-10000.0, 0.0));
		double zoom = 15000.0;

		double f = (double) Main.windowHeight / (double) Main.windowWidth;

		double minx = bellevue.getX() - zoom;
		double maxx = bellevue.getX() + zoom;
		double miny = bellevue.getY() - zoom * f;
		double maxy = bellevue.getY() + zoom * f;

		Rectangle scenarioBounds = new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));

		Transform transform = new Transform(windowBounds, scenarioBounds);

		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		linkDatabase.getLinks().stream().filter(l -> scenarioBounds.contains(l.getCoord().getX(), l.getCoord().getY()))
				.filter(l -> l.getAllowedModes().contains("car")).forEach(l -> {
					g2d.setColor(new Color(200, 200, 200));

					Coord fromCoord = transform.scenarioToWindow(l.getFromNode().getCoord());
					Coord toCoord = transform.scenarioToWindow(l.getToNode().getCoord());

					g2d.drawLine((int) fromCoord.getX(), (int) fromCoord.getY(), (int) toCoord.getX(),
							(int) toCoord.getY());
				});

		traversalDatabase.getTraversalsAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).getCoord().getY()))
				.sequential().forEach(t -> {
					if (isAv.get(t.vehicleIndex)) {
						g2d.setColor(Color.BLUE.darker());
					} else {
						g2d.setColor(Color.GRAY);
					}

					Link link = linkDatabase.getLink(t.linkIndex);

					double s = (time - t.startTime) / (t.endTime - t.startTime);

					Coord coord = CoordUtils.plus(link.getFromNode().getCoord(), CoordUtils.scalarMult(s,
							CoordUtils.minus(link.getToNode().getCoord(), link.getFromNode().getCoord())));

					coord = transform.scenarioToWindow(coord);

					g2d.fillOval((int) coord.getX() - 3, (int) coord.getY() - 3, 6, 6);
				});

		double activityMarkerLifetime = 120.0;
		double activityMarkerSize = 30.0;

		int pickupTypeIndex = activityTypeMapper.getIndex("AVPickup");
		int dropoffTypeIndex = activityTypeMapper.getIndex("AVDropoff");

		activityDatabase.getActivitiesAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).getCoord().getY()))
				.filter(t -> t.typeIndex == pickupTypeIndex || t.typeIndex == dropoffTypeIndex).sequential()
				.forEach(a -> {
					Link link = linkDatabase.getLink(a.linkIndex);
					Coord coord = transform.scenarioToWindow(link.getCoord());

					double lifetime = time - a.startTime;
					double maximumLifetime = Math.min(activityMarkerLifetime, a.endTime - a.startTime);

					if (lifetime <= activityMarkerLifetime) {
						double intensity = lifetime / maximumLifetime;

						double size = activityMarkerSize * intensity;
						double alpha = Math.floor(200.0 - 200.0 * intensity);

						if (a.typeIndex == pickupTypeIndex) {
							g2d.setColor(new Color(0, 0, 255, (int) alpha));
						} else {
							g2d.setColor(new Color(255, 0, 0, (int) alpha));
						}

						g2d.fillOval((int) (coord.getX() - 0.5 * size), (int) (coord.getY() - 0.5 * size), (int) size,
								(int) size);
					}
				});

		// graphics.setColor(Color.BLACK);
		// graphics.setFont(new Font(graphics.getFont().getName(), Font.BOLD, 20));
		// graphics.drawString(Time.writeTime(time), 20, 20);

		if (makeVideo) {
			try {
				synchronized (imageLock) {
					if (!useSvg) {
						ImageIO.write(surface, "jpg",
								new File(String.format("/home/sebastian/video/video_%d.jpg", frameIndex)));
					} else {
						((SVGGraphics2D) graphics)
								.stream(String.format("/home/sebastian/video/video_%d.svg", frameIndex));
					}

					System.out.println(Time.writeTime(time));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.paintComponent(windowGraphics);
		windowGraphics.drawImage(surface, 0, 0, this);
	}
}
