package ch.ethzm.matsim.renderer.data.stream;

import java.util.stream.Stream;

import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;

public interface DataStreamProvider {
	Stream<Object> provide(DataStreamRequest request);
}
