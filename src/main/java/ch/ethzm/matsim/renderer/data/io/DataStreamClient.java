package ch.ethzm.matsim.renderer.data.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import ch.ethzm.matsim.renderer.data.DataStreamType;
import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;

public class DataStreamClient {
	private final Logger logger = Logger.getLogger(DataStreamClient.class);

	private final Socket socket;
	private final Map<String, DataStreamType> types = new HashMap<>();

	public DataStreamClient(Socket socket) {
		this.socket = socket;
	}

	public void get(String type, DataStreamRequest request, Consumer<Object> consumer) {
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

			DataStreamType typeInstance = types.get(type);

			if (typeInstance != null) {
				typeInstance.getRequestSerializer().serialize(request, dataOutputStream);

				while (dataInputStream.readBoolean()) {
					consumer.accept(typeInstance.getDataDeserializer().deserialize(dataInputStream));
				}
			} else {
				logger.error("Requested unknown data type: " + type);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
