package ch.ethzm.matsim.renderer.data.example;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;
import ch.ethzm.matsim.renderer.data.stream.DataStreamProvider;

public class ExampleDataProvider implements DataStreamProvider {
	@Override
	public Stream<Object> provide(DataStreamRequest _request) {
		ExampleDataStreamRequest request = (ExampleDataStreamRequest) _request;
		List<ExampleData> data = new LinkedList<>();

		for (int i = request.fromNumber; i < request.toNumber; i++) {
			data.add(new ExampleData(i, i * i));
		}

		return data.stream().map(d -> (Object) d);
	}
}
