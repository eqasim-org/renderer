package ch.ethzm.matsim.renderer.main;

import java.awt.Color;
import java.awt.Font;
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

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.utils.misc.Time;

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
	final private BitSet isPt;
	final private BitSet isFreight;

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
		this.isPt = new BitSet(vehicleDatabase.getVehicleIndices().size());
		this.isFreight = new BitSet(vehicleDatabase.getVehicleIndices().size());

		for (Map.Entry<String, Integer> entry : vehicleDatabase.getVehicleIndices().entrySet()) {
			if (entry.getKey().contains("av")) {
				isAv.set(entry.getValue());
			}

			if (entry.getKey().contains("subway")) {
				isPt.set(entry.getValue());
			}

			if (entry.getKey().contains("freight")) {
				isFreight.set(entry.getValue());
			}
		}
	}

	private double time = 9.0 * 3600.0;
	private long previousRenderTime = -1;
	private boolean makeVideo = true;

	double timeStepPerSecond = 120; // 600; // 120.0;
	double framesPerSecond = 25.0;

	private long frameIndex = 0;

	private final Color systemX = new Color(7, 145, 222);
	private final Color odysseeOrange = new Color(241, 87, 38);
	private final Color odysseeGreen = new Color(86, 196, 165);
	private final Color black = new Color(0, 0, 0);
	private final Color yellow = new Color(255, 255, 60);
	private final Color red = new Color(255, 0, 0);

	// private Color vehicleColor = odysseeOrange; // black;
	// private Color activityColor = odysseeGreen; // systemX;
	private Color vehicleColor = black;
	private Color activityColor = systemX;

	Object imageLock = new Object();

	@Override
	public void paintComponent(Graphics windowGraphics) {
		Rectangle windowBounds = getBounds();

		BufferedImage surface = new BufferedImage(Main.windowWidth, Main.windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) surface.getGraphics();

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

		if (time > 10.0 * 3600.0) {
			time = 10.0 * 3600.0;
			System.exit(1);
		}

		// Coord bellevue = new Coord(2683253.0, 1246745.0);
		// double zoom = 10000.0;

		// Norway
		// Coord bellevue = CoordUtils.plus(new Coord(-447597.0, 1254618.0), new
		// Coord(0.0, 0.0));
		// double zoom = 20000.0 + (time - 8.0 * 3600.0) / (2.0 * 3600.0) * 380000.0;

		// Toulouse
		// Coord bellevue = CoordUtils.plus(new Coord(574004.0 - 10000, 6279261.0), new
		// Coord(0.0, 0.0));
		// double zoom = 10000.0 + (time - 5.0 * 3600.0) / (20.0 * 3600.0) * 40000.0;
		// zoom = 20000.0;
		// Occitanie center 665705 6278555

		// Paris
		Coord bellevue = CoordUtils.plus(new Coord(651791.0 - 5000.0, 6862293.0), new
		Coord(0.0, 0.0));
		//Coord bellevue = CoordUtils.plus(new Coord(654011.0, 6860737.0), new Coord(0.0, 0.0));
		double zoom = 10000.0; // + (time - 10.0 * 3600.0) / (2.0 * 3600.0) * 380000.0;

		// Lyon
		/*
		 * Coord bellevue = CoordUtils.plus(new Coord(841423.0, 6517233.0), new
		 * Coord(0.0, 0.0));
		 */

		// Confluence
		// Coord bellevue = CoordUtils.plus(new Coord(841469.0, 6517253.0), new
		// Coord(0.0, 0.0));
		// double zoom = 8000.0 - 6000.0 * (time - 7.5 * 3600.0) / ((20.0 - 7.5) *
		// 3600);

		/*
		 * double zoom = 10000.0 + (time - 7.5 * 3600.0) / (2.0 * 3600.0) * 40000.0;
		 * zoom = 8000.0;
		 */

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
				.filter(l -> l.getAllowedModes().contains("car")) //
				.forEach(l -> {
					g2d.setColor(new Color(240, 240, 240));

					Coord fromCoord = transform.scenarioToWindow(l.getFromNode().getCoord());
					Coord toCoord = transform.scenarioToWindow(l.getToNode().getCoord());

					g2d.drawLine((int) fromCoord.getX(), (int) fromCoord.getY(), (int) toCoord.getX(),
							(int) toCoord.getY());
				});

		linkDatabase.getLinks().stream().filter(l -> scenarioBounds.contains(l.getCoord().getX(), l.getCoord().getY()))
				.filter(l -> l.getAllowedModes().contains("subway")) //
				.forEach(l -> {
					g2d.setColor(new Color(180, 180, 180));

					Coord fromCoord = transform.scenarioToWindow(l.getFromNode().getCoord());
					Coord toCoord = transform.scenarioToWindow(l.getToNode().getCoord());

					g2d.drawLine((int) fromCoord.getX(), (int) fromCoord.getY(), (int) toCoord.getX(),
							(int) toCoord.getY());
				});

		traversalDatabase.getTraversalsAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).getCoord().getY()))
				.sequential().forEach(t -> {
					boolean show = true;
					
					if (/* isAv */ isAv.get(t.vehicleIndex)) {
						g2d.setColor(Color.BLUE.darker());
					} else if (isPt.get(t.vehicleIndex)) {
						g2d.setColor(systemX);
					} else if (isFreight.get(t.vehicleIndex)) {
						g2d.setColor(Color.RED);
					} else {
						g2d.setColor(vehicleColor);
						show = false;
					}

					Link link = linkDatabase.getLink(t.linkIndex);

					double s = (time - t.startTime) / (t.endTime - t.startTime);

					Coord coord = CoordUtils.plus(link.getFromNode().getCoord(), CoordUtils.scalarMult(s,
							CoordUtils.minus(link.getToNode().getCoord(), link.getFromNode().getCoord())));

					coord = transform.scenarioToWindow(coord);

					if (show) {
						g2d.fillOval((int) coord.getX() - 2, (int) coord.getY() - 2, 4, 4);
					}
				});

		double activityMarkerLifetime = 700.0;
		double activityMarkerSize = 20.0;

		int pickupTypeIndex = activityTypeMapper.hasActivityType("AVPickup") ? activityTypeMapper.getIndex("AVPickup")
				: -1;
		int dropoffTypeIndex = activityTypeMapper.hasActivityType("AVDropoff")
				? activityTypeMapper.getIndex("AVDropoff")
				: -1;
		int workTypeIndex = activityTypeMapper.hasActivityType("work") ? activityTypeMapper.getIndex("work") : -1;
		int leisureTypeIndex = activityTypeMapper.hasActivityType("leisure") ? activityTypeMapper.getIndex("leisure")
				: -1;
		int serviceTypeIndex = activityTypeMapper.hasActivityType("service") ? activityTypeMapper.getIndex("service")
				: -1;
		int startTypeIndex = activityTypeMapper.hasActivityType("start") ? activityTypeMapper.getIndex("start") : -1;
		int endTypeIndex = activityTypeMapper.hasActivityType("end") ? activityTypeMapper.getIndex("end") : -1;
		int fPickupTypeIndex = activityTypeMapper.hasActivityType("pickup") ? activityTypeMapper.getIndex("pickup")
				: -1;
		int fDeliverTypeIndex = activityTypeMapper.hasActivityType("delivery") ? activityTypeMapper.getIndex("delivery")
				: -1;

		activityDatabase.getActivitiesAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).getCoord().getY()))
				// .filter(t -> t.typeIndex == pickupTypeIndex || t.typeIndex ==
				// dropoffTypeIndex).sequential()
				// .filter(t -> t.typeIndex == workTypeIndex || t.typeIndex == leisureTypeIndex)
				// //
				// .filter(t -> t.typeIndex == serviceTypeIndex || t.typeIndex == startTypeIndex
				// || t.typeIndex == endTypeIndex || t.typeIndex == fPickupTypeIndex ||
				// t.typeIndex == fDeliverTypeIndex) //
				.filter(t -> t.typeIndex == workTypeIndex) //
				.sequential().forEach(a -> {
					Link link = linkDatabase.getLink(a.linkIndex);
					Coord coord = transform.scenarioToWindow(link.getCoord());

					double lifetime = time - a.startTime;
					double maximumLifetime = Math.min(activityMarkerLifetime, a.endTime - a.startTime);

					if (lifetime <= activityMarkerLifetime) {
						double intensity = lifetime / maximumLifetime;

						double size = activityMarkerSize * intensity;
						double alpha = Math.floor(200.0 - 200.0 * intensity);

						if (a.typeIndex == leisureTypeIndex) {
							// g2d.setColor(new Color(yellow.getRed(), yellow.getGreen(), yellow.getBlue(),
							// (int) alpha));
						} else if (a.typeIndex == pickupTypeIndex) {
							g2d.setColor(Color.RED);
						} else {
							g2d.setColor(new Color(activityColor.getRed(), activityColor.getGreen(),
									activityColor.getBlue(), (int) alpha));
						}

						/*
						 * if (a.typeIndex == pickupTypeIndex) { g2d.setColor(new Color(0, 0, 255, (int)
						 * alpha)); } else { g2d.setColor(new Color(255, 0, 0, (int) alpha)); }
						 */

						g2d.fillOval((int) (coord.getX() - 0.5 * size), (int) (coord.getY() - 0.5 * size), (int) size,
								(int) size);
					}
				});

		graphics.setColor(vehicleColor);
		graphics.setFont(new Font(graphics.getFont().getName(), Font.BOLD, 20));
		// graphics.drawString(Time.writeTime(time), 40, 40);

		if (makeVideo) {
			try {
				synchronized (imageLock) {
					ImageIO.write(surface, "png",
							new File(String.format("/home/shoerl/video/video_%d.png", frameIndex)));

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
