package ch.ethzm.matsim.renderer.data.request;

import java.io.DataInputStream;
import java.io.IOException;

public interface RequestDeserializer {
	DataStreamRequest deserialize(DataInputStream inputStream) throws IOException;
}
