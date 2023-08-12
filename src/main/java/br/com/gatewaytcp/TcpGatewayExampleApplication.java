package br.com.gatewaytcp;

import availablePorts.GetAvailablePorts;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TcpGatewayExampleApplication {

  private static final int PORT_RANGE_START = 8000;
  private static final int PORT_RANGE_END = 9000;
  private static final int MAX_POOL_SIZE = 10;

  public static void main(String[] args) throws IOException {
    ExecutorService executorService = Executors.newFixedThreadPool(MAX_POOL_SIZE);

    // Get a list of available ports.
    List<Integer> availablePorts = GetAvailablePorts.getAvailablePorts(PORT_RANGE_START, PORT_RANGE_END);

    // Create a server socket for each available port.
    for (int port : availablePorts) {
      ServerSocket serverSocket = new ServerSocket(port);

      executorService.execute(new Runnable() {
        @Override
        public void run() {
          try {
            while (true) {
              Socket clientSocket = serverSocket.accept();

              executorService.execute(new Runnable() {
                @Override
                public void run() {
                  try {
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // Read data from the client.
                    byte[] data = new byte[1024]; //buffer of bytes
                    int bytesRead = inputStream.read(data);

                    // Send data to SageMaker.
                    Socket sagemakerSocket = new Socket("sagemaker.us-east-1.amazonaws.com", 8080);
                    OutputStream sagemakerOutputStream = sagemakerSocket.getOutputStream();
                    sagemakerOutputStream.write(data);

                    // Read response from SageMaker.
                    InputStream sagemakerInputStream = sagemakerSocket.getInputStream();
                    byte[] sagemakerResponse = new byte[1024];
                    int sagemakerBytesRead = sagemakerInputStream.read(sagemakerResponse);

                    // Send response to the client.
                    outputStream.write(sagemakerResponse);
                  } catch (IOException e) {
                    e.printStackTrace();
                  } finally {
                    try {
                      clientSocket.close();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  }
                }
              });
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
    }
  }
}
