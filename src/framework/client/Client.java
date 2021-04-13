package framework.client;

import framework.Listener;
import framework.Sender;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;


public class Client {

    private SocketChannel sock;

    private BlockingQueue<Message> receivedQueue;
    private BlockingQueue<Message> sendingQueue;

    public void printByteBuffer(ByteBuffer bytes, int bytesLength) {
        System.out.print("DATA: ");
        for (int i = 0; i < bytesLength; i++) {
            System.out.print(Byte.toString(bytes.get(i)) + " ");
        }
        System.out.println();
    }

    public Client(String server_ip, int server_port, int frequency, BlockingQueue<Message> receivedQueue, BlockingQueue<Message> sendingQueue) {
        this.receivedQueue = receivedQueue;
        this.sendingQueue = sendingQueue;
        SocketChannel sock;
        Sender sender;
        Listener listener;
        try {
            sock = SocketChannel.open();
            sock.connect(new InetSocketAddress(server_ip, server_port));
            sender = new Sender(sock, sendingQueue);
            listener = new Listener(sock, receivedQueue, sender);


            sender.sendConnect(frequency);

            listener.start();
            sender.start();
        } catch (IOException e) {
            System.err.println("Failed to connect: " + e);
            System.exit(1);
        }
    }


}