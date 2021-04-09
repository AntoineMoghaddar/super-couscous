package framework;

import framework.client.Message;
import framework.client.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

public class Sender extends Thread {
    private BlockingQueue<Message> sendingQueue;
    private SocketChannel sock;

    public Sender(SocketChannel sock, BlockingQueue<Message> sendingQueue) {
        super();
        this.sendingQueue = sendingQueue;
        this.sock = sock;
    }

    private void senderLoop() {
        while (sock.isConnected()) {
            try {
                Message msg = sendingQueue.take();
                if (msg.getType() == MessageType.DATA || msg.getType() == MessageType.DATA_SHORT) {
                    ByteBuffer data = msg.getData();
                    data.position(0); //reset position just to be sure
                    int length = data.capacity(); //assume capacity is also what we want to send here!
                    ByteBuffer toSend = ByteBuffer.allocate(length + 2);
                    if (msg.getType() == MessageType.DATA) {
                        toSend.put((byte) 3);
                    } else { // must be DATA_SHORT due to check above
                        toSend.put((byte) 6);
                    }
                    toSend.put((byte) length);
                    toSend.put(data);
                    toSend.position(0);
                    // System.out.println("Sending "+Integer.toString(length)+" bytes!");
                    sock.write(toSend);
                }
            } catch (IOException e) {
                System.err.println("Alles is stuk!");
            } catch (InterruptedException e) {
                System.err.println("Failed to take from sendingQueue: " + e);
            }
        }
    }

    public void sendConnect(int frequency) {
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.put((byte) 9);
        buff.put((byte) ((frequency >> 16) & 0xff));
        buff.put((byte) ((frequency >> 8) & 0xff));
        buff.put((byte) (frequency & 0xff));
        buff.position(0);
        try {
            sock.write(buff);
        } catch (IOException e) {
            System.err.println("Failed to send HELLO");
        }
    }

    public void run() {
        senderLoop();
    }

    public void sendPacket(DatagramPacket pack) {
    }
}
