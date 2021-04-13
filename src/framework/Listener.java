package framework;

import framework.client.Message;
import framework.client.MessageType;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

public class Listener extends Thread {
    private BlockingQueue<Message> receivedQueue;
    private SocketChannel sock;
    private Sender senderA;

    public Listener(SocketChannel sock, BlockingQueue<Message> receivedQueue, Sender senderA) {
        super();
        this.receivedQueue = receivedQueue;
        this.sock = sock;
        this.senderA = senderA;
    }

    private ByteBuffer messageBuffer = ByteBuffer.allocate(1024);
    private int messageLength = -1;
    private boolean messageReceiving = false;
    private boolean shortData = false;

    private void parseMessage(ByteBuffer received, int bytesReceived) {
        // printByteBuffer(received, bytesReceived);

        try {
            for (int offset = 0; offset < bytesReceived; offset++) {
                byte d = received.get(offset);

                if (messageReceiving) {
                    if (messageLength == -1) {
                        messageLength = (int) d;
                        messageBuffer = ByteBuffer.allocate(messageLength);
                    } else {
                        messageBuffer.put(d);
                    }
                    if (messageBuffer.position() == messageLength) {
                        // Return DATA here
                        // printByteBuffer(messageBuffer, messageLength);
                        // System.out.println("pos: "+Integer.toString(messageBuffer.position()) );
                        messageBuffer.position(0);
                        ByteBuffer temp = ByteBuffer.allocate(messageLength);
                        temp.put(messageBuffer);
                        temp.rewind();
                        if (shortData) {
                            //receivedQueue.put(new Message(MessageType.DATA_SHORT, temp))
                            //calls turnToSend in Sender, we received a data short check there if it is our token
                            senderA.turnToSend(temp);

                        } else {
                            receivedQueue.put(new Message(MessageType.DATA, temp));
                        }
                        messageReceiving = false;
                    }
                } else {
                    if (d == 0x09) { // Connection successfull!
                        // System.out.println("CONNECTED");
                        receivedQueue.put(new Message(MessageType.HELLO));
                    } else if (d == 0x01) { // FREE
                        // System.out.println("FREE");
                        receivedQueue.put(new Message(MessageType.FREE));
                    } else if (d == 0x02) { // BUSY
                        // System.out.println("BUSY");
                        receivedQueue.put(new Message(MessageType.BUSY));
                    } else if (d == 0x03) { // DATA!
                        messageLength = -1;
                        messageReceiving = true;
                        shortData = false;
                    } else if (d == 0x04) { // SENDING
                        // System.out.println("SENDING");
                        receivedQueue.put(new Message(MessageType.SENDING));
                    } else if (d == 0x05) { // DONE_SENDING
                        // System.out.println("DONE_SENDING");
                        receivedQueue.put(new Message(MessageType.DONE_SENDING));
                    } else if (d == 0x06) { // DATA_SHORT
                        messageLength = -1;
                        messageReceiving = true;
                        shortData = true;
                    } else if (d == 0x08) { // END, connection closing
                        // System.out.println("END");
                        receivedQueue.put(new Message(MessageType.END));
                    }
                }
            }

        } catch (InterruptedException e) {
            System.err.println("Failed to put data in receivedQueue: " + e.toString());
        }
    }

    public void printByteBuffer(ByteBuffer bytes, int bytesLength) throws InterruptedException {
            System.out.print("DATA: ");
            for (int i = 0; i < bytesLength; i++) {
                System.out.print(Byte.toString(bytes.get(i)) + " ");
            }
            System.out.println();
    }

    public void receivingLoop() {
        int bytesRead = 0;
        ByteBuffer recv = ByteBuffer.allocate(1024);
        try {
            while (sock.isConnected()) {
                bytesRead = sock.read(recv);
                if (bytesRead > 0) {
                    // System.out.println("Received "+Integer.toString(bytesRead)+" bytes!");
                    parseMessage(recv, bytesRead);
                } else {
                    break;
                }
                recv.clear();
            }
        } catch (IOException e) {
            System.err.println("Error on socket: " + e);
        }

    }

    public void run() {
        receivingLoop();
    }

}
