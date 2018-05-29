package ch.ethzm.matsim.renderer.data.stream;

import java.io.DataInputStream;
import java.io.IOException;

public interface DataStreamDeserializer {
	Object deserialize(DataInputStream inputStream) throws IOException;
}
