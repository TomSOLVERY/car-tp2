package ricm.distsys.nio.babystep1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Reader {
	private enum State {
		ReadLength, ReadPayload
	};

	State state = State.ReadLength;
	ByteBuffer ploadBuffer;
	ByteBuffer lenBuffer;
	SocketChannel sc;
	Writer writer;

	public Reader(Writer writer) {
		this.writer = writer;
	}

	public void handleRead(SocketChannel sc, SelectionKey key) throws IOException {
		switch (state) {
		case ReadLength:
			lenBuffer = ByteBuffer.allocate(4);
			sc.read(lenBuffer);
			if (lenBuffer.remaining() == 0) {
				state = State.ReadPayload;
				lenBuffer.rewind();
				int len = lenBuffer.getInt();
				ploadBuffer = ByteBuffer.allocate(len);
			}
			break;
		case ReadPayload:
			sc.read(ploadBuffer);
			if (ploadBuffer.remaining() == 0) {
				state = State.ReadLength;
				byte[] dst = new byte[ploadBuffer.capacity()];
				ploadBuffer.rewind();
				ploadBuffer.get(dst, 0, ploadBuffer.capacity());
				processMsg(dst,key);
			}
			break;
		default : 
			System.out.println("Erreur Etat inconnu dans le Reader");
			break;
		}
	}
	
	public void processMsg(byte[] msg,SelectionKey key) {
		writer.sendMsg(msg,key);
		System.out.println("Message envoyé : " + new String(msg));
	}
}