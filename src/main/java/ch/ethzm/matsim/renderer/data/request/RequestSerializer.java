package ch.ethzm.matsim.renderer.data.request;

import java.io.DataOutputStream;
import java.io.IOException;

public interface RequestSerializer {
	void serialize(DataStreamRequest request, DataOutputStream outputStream) throws IOException;
}
