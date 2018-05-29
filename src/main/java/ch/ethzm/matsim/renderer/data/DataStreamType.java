package ch.ethzm.matsim.renderer.data;

import ch.ethzm.matsim.renderer.data.request.RequestDeserializer;
import ch.ethzm.matsim.renderer.data.request.RequestSerializer;
import ch.ethzm.matsim.renderer.data.stream.DataStreamDeserializer;
import ch.ethzm.matsim.renderer.data.stream.DataStreamSerializer;

public interface DataStreamType {
	DataStreamSerializer getDataSerializer();

	DataStreamDeserializer getDataDeserializer();

	RequestSerializer getRequestSerializer();

	RequestDeserializer getRequestDeserializer();

	String getName();
}
