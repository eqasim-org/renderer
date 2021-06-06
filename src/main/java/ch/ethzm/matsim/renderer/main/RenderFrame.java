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
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.utils.misc.Time;

import ch.ethzm.matsim.renderer.activity.ActivityDatabase;
import ch.ethzm.matsim.renderer.activity.ActivityTypeMapper;
import ch.ethzm.matsim.renderer.config.ActivityConfig;
import ch.ethzm.matsim.renderer.config.NetworkConfig;
import ch.ethzm.matsim.renderer.config.RenderConfig;
import ch.ethzm.matsim.renderer.config.VehicleConfig;
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

	private final int windowWidth;
	private final int windowHeight;
	private final RenderConfig renderConfig;

	private final List<Color> linkColors;
	private final List<Color> vehicleColors;
	private final List<Integer> vehicleSizes;
	private final List<Color> activityColors;
	private final List<Double> activityMaximumLifetimes;
	private final List<Double> activitySizes;

	public RenderFrame(TraversalDatabase traversalDatabase, LinkDatabase linkDatabase,
			ActivityDatabase activityDatabase, ActivityTypeMapper activityTypeMapper, VehicleDatabase vehicleDatabase,
			RenderConfig renderConfig) {
		this.traversalDatabase = traversalDatabase;
		this.linkDatabase = linkDatabase;
		this.activityDatabase = activityDatabase;
		this.activityTypeMapper = activityTypeMapper;
		this.vehicleDatabase = vehicleDatabase;

		this.renderConfig = renderConfig;
		this.time = renderConfig.startTime;
		windowWidth = renderConfig.width;
		windowHeight = renderConfig.height;

		this.timeStepPerSecond = renderConfig.secondsPerFrame;

		setSize(renderConfig.width, renderConfig.height);
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

		this.linkColors = new ArrayList<>(renderConfig.networks.size());

		for (NetworkConfig networkConfig : renderConfig.networks) {
			linkColors
					.add(new Color(networkConfig.color.get(0), networkConfig.color.get(1), networkConfig.color.get(2)));
		}

		this.vehicleColors = new ArrayList<>(renderConfig.vehicles.size());

		for (VehicleConfig vehicleConfig : renderConfig.vehicles) {
			vehicleColors
					.add(new Color(vehicleConfig.color.get(0), vehicleConfig.color.get(1), vehicleConfig.color.get(2)));
		}

		this.vehicleSizes = new ArrayList<>(renderConfig.vehicles.size());

		for (VehicleConfig vehicleConfig : renderConfig.vehicles) {
			vehicleSizes.add(vehicleConfig.size);
		}

		this.activityColors = new ArrayList<>(renderConfig.activities.size());
		this.activityMaximumLifetimes = new ArrayList<>(renderConfig.activities.size());
		this.activitySizes = new ArrayList<>(renderConfig.activities.size());

		for (ActivityConfig activtiyConfig : renderConfig.activities) {
			activityColors.add(
					new Color(activtiyConfig.color.get(0), activtiyConfig.color.get(1), activtiyConfig.color.get(2)));
			activityMaximumLifetimes.add(activtiyConfig.maximumLifetime);
			activitySizes.add(activtiyConfig.size);
		}
	}

	private double time;
	private long previousRenderTime = -1;
	private boolean makeVideo = true;

	private double timeStepPerSecond; // 600; // 120.0;
	double framesPerSecond = 25.0;

	private long frameIndex = 0;

	Object imageLock = new Object();

	@Override
	public void paintComponent(Graphics windowGraphics) {
		Rectangle windowBounds = getBounds();

		BufferedImage surface = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) surface.getGraphics();

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, windowWidth, windowHeight);

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

		if (time > renderConfig.endTime) {
			if (makeVideo) {
				System.exit(1);
			} else {
				time = renderConfig.startTime;
			}
		}

		Coord center = new Coord(renderConfig.center.get(0), renderConfig.center.get(1));
		double zoom = renderConfig.zoom;

		double f = (double) windowHeight / (double) windowWidth;

		double minx = center.getX() - zoom;
		double maxx = center.getX() + zoom;
		double miny = center.getY() - zoom * f;
		double maxy = center.getY() + zoom * f;

		Rectangle scenarioBounds = new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));

		Transform transform = new Transform(windowBounds, scenarioBounds);

		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		linkDatabase.getLinks().stream()
				.filter(l -> scenarioBounds.contains(l.link.getCoord().getX(), l.link.getCoord().getY()))
				.filter(l -> l.typeIndex >= 0).forEach(l -> {
					g2d.setColor(linkColors.get(l.typeIndex));

					Coord fromCoord = transform.scenarioToWindow(l.link.getFromNode().getCoord());
					Coord toCoord = transform.scenarioToWindow(l.link.getToNode().getCoord());

					g2d.drawLine((int) fromCoord.getX(), (int) fromCoord.getY(), (int) toCoord.getX(),
							(int) toCoord.getY());
				});

		traversalDatabase.getTraversalsAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).link.getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).link.getCoord().getY()))
				.filter(t -> t.vehicleType >= 0).sequential().forEach(t -> {
					g2d.setColor(vehicleColors.get(t.vehicleType));

					Link link = linkDatabase.getLink(t.linkIndex).link;

					double s = (time - t.startTime) / (t.endTime - t.startTime);

					Coord coord = CoordUtils.plus(link.getFromNode().getCoord(), CoordUtils.scalarMult(s,
							CoordUtils.minus(link.getToNode().getCoord(), link.getFromNode().getCoord())));

					coord = transform.scenarioToWindow(coord);

					int size = vehicleSizes.get(t.vehicleType);
					g2d.fillOval((int) coord.getX() - size / 2, (int) coord.getY() - size / 2, size, size);
				});

		activityDatabase.getActivitiesAtTime(time)
				.filter(t -> scenarioBounds.contains(linkDatabase.getLink(t.linkIndex).link.getCoord().getX(),
						linkDatabase.getLink(t.linkIndex).link.getCoord().getY()))
				.filter(t -> t.typeIndex >= 0) //
				.sequential().forEach(a -> {
					Link link = linkDatabase.getLink(a.linkIndex).link;
					Coord coord = transform.scenarioToWindow(link.getCoord());

					double lifetime = time - a.startTime;

					double activityLifetime = activityMaximumLifetimes.get(a.typeIndex);
					double maximumLifetime = Math.min(activityLifetime, a.endTime - a.startTime);

					if (lifetime <= activityLifetime) {
						double intensity = lifetime / maximumLifetime;

						double size = activitySizes.get(a.typeIndex) * intensity;
						double alpha = Math.floor(200.0 - 200.0 * intensity);
						Color color = activityColors.get(a.typeIndex);

						g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha));

						g2d.fillOval((int) (coord.getX() - 0.5 * size), (int) (coord.getY() - 0.5 * size), (int) size,
								(int) size);
					}
				});

		if (renderConfig.showTime) {
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font(graphics.getFont().getName(), Font.BOLD, 20));
			graphics.drawString(Time.writeTime(time), 40, 40);
		}

		if (makeVideo) {
			try {
				synchronized (imageLock) {
					ImageIO.write(surface, "png",
							new File(String.format(renderConfig.outputPath + "/video_%d.png", frameIndex)));

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
