package br.com.gatewaytcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import availablePorts.GetAvailablePorts;



public class TcpGatewayExampleApplication {

	 private static final int PORT_RANGE_START = 9020;
	 private static final int PORT_RANGE_END = 9025;
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
	                 try {
	                    
	                //*****IN TCP/IP
	                   InputStream inputStream = clientSocket.getInputStream();
	                    
	                   byte[] data = new byte[1024];
	                   int bytesRead = inputStream.read(data);

	                   String payload = new String(data, 0, bytesRead);
	                   
	                   System.out.println(payload);

	                   //*****REQUEST HTTP
	                  URL url = new URL("http://localhost/test-gateway");
	                  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	                  connection.setRequestMethod("POST");
	                  connection.setRequestProperty("Content-Type", "application/json");
	                  connection.setRequestProperty("Content-Length", String.valueOf(payload.length()));
	                  connection.setDoOutput(true);
	                  connection.getOutputStream().write(payload.getBytes());

	                 //*****RESPONSE HTTP
	                  int responseCode = connection.getResponseCode();
	                  String responseMessage = connection.getResponseMessage();
	                  byte[] responseData = new byte[1024];
	                  int responseBytesRead = connection.getInputStream().read(responseData);
	                  String response = new String(responseData, 0, responseBytesRead);

	                 //*****OUT TCP/IP
	                   OutputStream outputStream = clientSocket.getOutputStream();
	                   outputStream.write(payload.getBytes());
	                    
	                 } catch (IOException e) {
	                   e.printStackTrace();
	                 }
	           }
	         } catch (IOException e) {
	           e.printStackTrace();
	         }
	       }
	     });
	   }
	 }
}
