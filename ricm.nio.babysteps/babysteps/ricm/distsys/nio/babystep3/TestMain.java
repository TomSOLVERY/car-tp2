package ricm.distsys.nio.babystep3;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A class which launches 1 server an 2 clients
 * 
 * Arguments should be: --server [server args] --client1 [client1 args]
 * --client2 [client2 args] in this order
 *
 */
public class TestMain {

	public static void main(String args[]) throws IOException {
		
		// We need to pass the right arguments
		boolean started = false;

		int i = 0;
		for (; i < args.length; i++) {
			// Arguments for the server
			if (args[i].equals("--server")) {
				ArrayList<String> ServerArgs = new ArrayList<String>();
				for (; i < args.length; i++) {
					if (args[i].equals("--client1"))
						break;
					ServerArgs.add(args[i]);
				}
				started = true;
				NioServer.main(ServerArgs.toArray(new String[0]));
			}
		}
		// If there are no arguments for the server we start it with an empty array
		// The server needs to be the first to start
		if (!started)
			NioServer.main(new String[0]);

		started = false;
		for (; i < args.length; i++) {
			if (args[i].equals("--client1")) {
				ArrayList<String> client1Args = new ArrayList<String>();
				for (; i < args.length; i++) {
					if (args[i].equals("--client2"))
						break;
					client1Args.add(args[i]);
				}
				started = true;
				NioClient.main(client1Args.toArray(new String[0]));
			}
		}
		if (!started)
			NioClient.main(new String[0]);

		started = false;
		for (; i < args.length; i++) {
			if (args[i].equals("--client2")) {
				ArrayList<String> client2Args = new ArrayList<String>();
				for (; i < args.length; i++) {
					client2Args.add(args[i]);
				}
				started = true;
				NioClient.main(client2Args.toArray(new String[0]));
			}
		}
		if (!started)
			NioClient.main(new String[0]);
	}
}
