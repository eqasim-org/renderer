package ch.ethzm.matsim.renderer.main;

import java.awt.Rectangle;

import org.ejml.simple.SimpleMatrix;
import org.matsim.api.core.v01.Coord;

public class Transform2 {
	final private Rectangle windowBounds;
	final private Rectangle scenarioBounds;

	public Transform2(Rectangle windowBounds, Rectangle scenarioBounds) {
		this.windowBounds = windowBounds;
		this.scenarioBounds = scenarioBounds;
	}

	private SimpleMatrix cross3(SimpleMatrix a, SimpleMatrix b) {
		return new SimpleMatrix(new double[][] { //
				{ a.get(1) * b.get(2) - a.get(2) * b.get(1) }, //
				{ a.get(2) * b.get(0) - a.get(0) * b.get(2) }, //
				{ a.get(0) * b.get(1) - a.get(1) * b.get(0) }, //
		});
	}

	/**
	 * Creates a transformation that makes the "camera" look at a specific traget
	 * from a specific location and orientation.
	 */
	private SimpleMatrix createLookAtMatrix(SimpleMatrix eye, SimpleMatrix center, SimpleMatrix up) {
		SimpleMatrix forward = center.minus(eye);

		forward = forward.divide(forward.normF());
		up = up.divide(up.normF());

		SimpleMatrix side = cross3(forward, up);
		side = side.divide(side.normF());

		up = cross3(side, forward);

		SimpleMatrix orientationMatrix = new SimpleMatrix(new double[][] { //
				{ side.get(0), side.get(1), side.get(2), 0.0 }, //
				{ up.get(0), up.get(1), up.get(2), 0.0 }, //
				{ -forward.get(0), -forward.get(1), -forward.get(2), 0.0 }, //
				{ 0.0, 0.0, 0.0, 1.0 } //
		});

		SimpleMatrix translationMatrix = new SimpleMatrix(new double[][] { //
				{ 1.0, 0.0, 0.0, -eye.get(0) }, //
				{ 0.0, 1.0, 0.0, -eye.get(1) }, //
				{ 0.0, 0.0, 1.0, -eye.get(2) }, //
				{ 0.0, 0.0, 0.0, 1.0 } //
		});

		return orientationMatrix.mult(translationMatrix);
	}

	/**
	 * This matrix assumes that display information is contained in y = (-1 ... 1)
	 * and x = (-1 ... 1). It transforms the coordinates to window coordinates by
	 * setting (-1,-1) as the lower left corner and (1, 1) as the upper right
	 * corner.
	 */
	private SimpleMatrix createViewportMatrix() {
		return new SimpleMatrix(new double[][] { //
				{ 0.5 * windowBounds.getWidth(), 0.0, 0.0, windowBounds.getWidth() * 0.5 }, //
				{ 0.0, -0.5 * windowBounds.getHeight(), 0.0, windowBounds.getHeight() * 0.5 }, //
				{ 0.0, 0.0, 1.0, 0.0 }, //
				{ 0.0, 0.0, 0.0, 1.0 }//
		});
	}

	/**
	 * This creates a projection matrix that assumes that everything interesting is
	 * located in y = (-1 ... 1) and adapts x coordinates such that the aspect ratio
	 * of the window is kept.
	 * 
	 * TODO: We do not handle depth properly for the clipping space right now. We
	 * assume that 1.0 is "near" and 1000.0 is "far". However, we often draw things
	 * that are further away. So if we actually want to use clipping at some point
	 * we should make this configurable.
	 */
	private SimpleMatrix createOrthogonalProjectionMatrix(double height) {
		double ratio = windowBounds.getHeight() / windowBounds.getWidth();

		double zNear = 1.0;
		double zFar = 1000.0;

		return new SimpleMatrix(new double[][] { //
				{ ratio / height, 0.0, 0.0, 0.0 }, //
				{ 0.0, 1.0 / height, 0.0, 0.0 }, //
				{ 0.0, 0.0, -2.0 / (zFar - zNear), 1 + zFar / (zFar - zNear) }, //
				{ 0.0, 0.0, 0.0, 1.0 }//
		});
	}

	public Coord scenarioToWindow(Coord scenarioCoord) {

		SimpleMatrix eyeMatrix = new SimpleMatrix(new double[][] { //
				{ 1.0, 0.0, 0.0, -scenarioBounds.getCenterX() }, //
				{ 0.0, 1.0, 0.0, -scenarioBounds.getCenterY() }, //
				{ 0.0, 0.0, 1.0, 0.0 }, //
				{ 0.0, 0.0, 0.0, 1.0 }, //
		});

		/**
		 * double radius = 20000.0;
		 * 
		 * SimpleMatrix scaleMatrix = new SimpleMatrix(new double[][] { // { 1.0 /
		 * radius, 0.0, 0.0, 0.0 }, // { 0.0, 1.0 / radius, 0.0, 0.0 }, // { 0.0, 0.0,
		 * 1.0, 0.0 }, // { 0.0, 0.0, 0.0, 1.0 }, // });
		 */

		SimpleMatrix up = new SimpleMatrix(new double[][] { //
				{ 0.0 }, { 1.0 }, { 0.0 } //
		});

		SimpleMatrix center = new SimpleMatrix(new double[][] { //
				{ scenarioBounds.getCenterX() }, { scenarioBounds.getCenterY() }, { 0.0 } //
		});

		SimpleMatrix eye = new SimpleMatrix(new double[][] { //
				{ scenarioBounds.getCenterX() }, { scenarioBounds.getCenterY() }, { 20000.0 } //
		});

		SimpleMatrix cameraMatrix = createLookAtMatrix(eye, center, up);
		System.out.println(cameraMatrix);

		// -------------------

		// Create coordinate representation
		SimpleMatrix coordinate = new SimpleMatrix(
				new double[][] { { scenarioCoord.getX() }, { scenarioCoord.getY() }, { 0.0 }, { 1.0 } });

		// coordinate = eyeMatrix.mult(coordinate);
		// coordinate = scaleMatrix.mult(coordinate);
		coordinate = cameraMatrix.mult(coordinate);

		// Project coordinates to clipping space (-1.0 ... 1.0)
		SimpleMatrix projectionMatrix = createOrthogonalProjectionMatrix(20000.0);
		coordinate = projectionMatrix.mult(coordinate);

		// Consider w dimension
		coordinate = coordinate.divide(coordinate.get(3));

		// Transform (-1, 1) to window coordinates
		SimpleMatrix viewportMatrix = createViewportMatrix();
		coordinate = viewportMatrix.mult(coordinate);

		return new Coord(coordinate.get(0), coordinate.get(1));
	}
}
