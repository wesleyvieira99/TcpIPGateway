package availablePorts;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GetAvailablePorts {
	
	public static List<Integer> getAvailablePorts(int portRangeStart, int portRangeEnd) {
		List<Integer> availablePorts = new ArrayList<>();

		for (int port = portRangeStart; port <= portRangeEnd; port++) {
			try {
				new ServerSocket(port).close();
				availablePorts.add(port);
			} catch (IOException e) {
				// Port is in use.
			}
		}

		return availablePorts;
	}
}
