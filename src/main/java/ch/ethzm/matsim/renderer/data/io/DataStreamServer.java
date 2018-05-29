package ch.ethzm.matsim.renderer.data.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.ethzm.matsim.renderer.data.DataStreamType;
import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;
import ch.ethzm.matsim.renderer.data.stream.DataStreamProvider;

public class DataStreamServer implements Runnable {
	private final Logger logger = Logger.getLogger(DataStreamServer.class);

	private final Map<String, DataStreamType> types = new HashMap<>();
	private final Map<String, DataStreamProvider> providers = new HashMap<>();

	private final Socket socket;

	public DataStreamServer(Socket socket) {
		this.socket = socket;
	}

	public void addProvider(DataStreamType type, DataStreamProvider provider) {
		types.put(type.getName(), type);
		providers.put(type.getName(), provider);
	}

	@Override
	public void run() {
		try {
			InputStream rawInputStream = socket.getInputStream();
			OutputStream rawOutputStream = socket.getOutputStream();

			DataInputStream dataInputStream = new DataInputStream(rawInputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(rawOutputStream);

			while (true) {
				String typeName = dataInputStream.readUTF();
				DataStreamType type = types.get(typeName);

				if (type != null) {
					DataStreamRequest request = type.getRequestDeserializer().deserialize(dataInputStream);
					DataStreamProvider provider = providers.get(typeName);

					provider.provide(request).forEachOrdered(data -> {
						try {
							dataOutputStream.writeBoolean(true);
							type.getDataSerializer().serialize(data, dataOutputStream);
						} catch (IOException e) {
							logger.error(e.getMessage());
							throw new RuntimeException(e);
						}
					});

					dataOutputStream.writeBoolean(false);
				} else {
					logger.warn("Requested unknown data provider: " + typeName);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
