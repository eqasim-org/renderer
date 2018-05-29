package ch.ethzm.matsim.renderer.data.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import ch.ethzm.matsim.renderer.data.io.DataStreamClient;
import ch.ethzm.matsim.renderer.data.io.DataStreamServer;

public class RunExample {
	static public void main(String[] args) throws IOException {
		ExampleDataStreamType type = new ExampleDataStreamType();

		Socket serverSocket = new Socket();
		serverSocket.bind(new InetSocketAddress(7777));

		DataStreamServer server = new DataStreamServer(serverSocket);
		server.addProvider(type, new ExampleDataProvider());

		Thread serverThread = new Thread(server);
		serverThread.run();

		Socket clientSocket = new Socket();
		clientSocket.connect(new InetSocketAddress(7777));
		DataStreamClient client = new DataStreamClient(clientSocket);

		client.get("example", new ExampleDataStreamRequest(5, 12), o -> {
			ExampleData data = (ExampleData) o;
			System.out.println("OBJECT: " + data.number + " " + data.squared);
		});

		serverThread.interrupt();
	}
}
