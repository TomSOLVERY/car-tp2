package ricm.distsys.nio.babystep1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Writer {
	private enum State {
		WriteLength, WritePayload, IDLE
	};

	State state = State.IDLE;
	ByteBuffer ploadBuffer;
	ByteBuffer lenBuffer;
	SocketChannel sc;
	SelectionKey key;
	ArrayList<ByteBuffer> messages;

	public Writer() {
		messages = new ArrayList<ByteBuffer>();
		lenBuffer = ByteBuffer.allocate(4);
	}

	public void handleWrite(SocketChannel sc,SelectionKey key) throws IOException {
		switch (state) {
		case IDLE:
			if (!messages.isEmpty()) {// check if messages not empty
				lenBuffer.position(0);
				lenBuffer.putInt(messages.get(0).capacity());
				lenBuffer.position(0);
				ploadBuffer = messages.get(0);
				state = State.WriteLength;
				messages.remove(0);
			} else
				key.interestOps(SelectionKey.OP_READ);
		case WriteLength:
			sc.write(lenBuffer);
			if (!lenBuffer.hasRemaining()) {
				state = State.WritePayload;
			}

			break;
		case WritePayload:
			sc.write(ploadBuffer);
			if (!ploadBuffer.hasRemaining()) {
				state = State.IDLE;

			}
		}
	}

	public void sendMsg(byte[] msg,SelectionKey key) {
		messages.add(ByteBuffer.wrap(msg));
		key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

}
