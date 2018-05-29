package ch.ethzm.matsim.renderer.data.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.ethzm.matsim.renderer.data.DataStreamType;
import ch.ethzm.matsim.renderer.data.request.DataStreamRequest;
import ch.ethzm.matsim.renderer.data.request.RequestDeserializer;
import ch.ethzm.matsim.renderer.data.request.RequestSerializer;
import ch.ethzm.matsim.renderer.data.stream.DataStreamDeserializer;
import ch.ethzm.matsim.renderer.data.stream.DataStreamSerializer;

public class ExampleDataStreamType implements DataStreamType {
	@Override
	public DataStreamSerializer getDataSerializer() {
		return new DataStreamSerializer() {
			@Override
			public void serialize(Object data, DataOutputStream outputStream) throws IOException {
				outputStream.writeInt(((ExampleData) data).number);
				outputStream.writeInt(((ExampleData) data).squared);
			}
		};
	}

	@Override
	public DataStreamDeserializer getDataDeserializer() {
		return new DataStreamDeserializer() {
			@Override
			public Object deserialize(DataInputStream inputStream) throws IOException {
				return new ExampleData(inputStream.readInt(), inputStream.readInt());
			}
		};
	}

	@Override
	public RequestSerializer getRequestSerializer() {
		return new RequestSerializer() {
			@Override
			public void serialize(DataStreamRequest request, DataOutputStream outputStream) throws IOException {
				outputStream.writeInt(((ExampleDataStreamRequest) request).fromNumber);
				outputStream.writeInt(((ExampleDataStreamRequest) request).toNumber);
			}
		};
	}

	@Override
	public RequestDeserializer getRequestDeserializer() {
		return new RequestDeserializer() {
			@Override
			public DataStreamRequest deserialize(DataInputStream inputStream) throws IOException {
				return new ExampleDataStreamRequest(inputStream.readInt(), inputStream.readInt());
			}
		};
	}

	@Override
	public String getName() {
		return "example";
	}
}
