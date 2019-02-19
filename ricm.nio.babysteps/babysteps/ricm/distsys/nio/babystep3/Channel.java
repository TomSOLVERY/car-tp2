package ricm.distsys.nio.babystep3;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Channel {
	Reader reader;
	Writer writer;
	
	public Channel() {
		writer = new Writer();
		reader = new Reader(writer);
	}
	
	public void sendMsg (byte[] first, SelectionKey key) {
		writer.sendMsg(first, key);
	}
	
	public void handleRead (SocketChannel sc, SelectionKey key) throws IOException {
		reader.handleRead(sc, key);
	}
	
	public void handleWrite (SocketChannel sc,SelectionKey key) throws IOException {
		writer.handleWrite(sc, key);
	}
}
