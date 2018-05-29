package ch.ethzm.matsim.renderer.data.example;

import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;

public class ExampleDataStreamRequest implements DataStreamRequest {
	final public int fromNumber;
	final public int toNumber;

	public ExampleDataStreamRequest(int fromNumber, int toNumber) {
		this.fromNumber = fromNumber;
		this.toNumber = toNumber;
	}
}
