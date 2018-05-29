package ch.ethzm.matsim.renderer.data.stream;

import java.io.DataOutputStream;
import java.io.IOException;

public interface DataStreamSerializer {
	void serialize(Object data, DataOutputStream outputStream) throws IOException;
}
