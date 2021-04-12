package framework;

import framework.client.Client;
import framework.client.Message;
import framework.client.MessageType;
import model.CouscousModel;
import packets.Address;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is just some example code to show you how to interact
 * with the server using the provided 'Client' class and two queues.
 * Feel free to modify this code in any way you like!
 */

public class MyProtocol {

    // The host to connect to. Set this to localhost when using the audio interface tool.
    private static String SERVER_IP = "netsys.ewi.utwente.nl"; //"127.0.0.1";
    // The port to connect to. 8954 for the simulation server.
    private static int SERVER_PORT = 8954;
    // The frequency to use.
    private static int frequency = 3800;//TODO: Set this to your group frequency!

    private BlockingQueue<Message> receivedQueue;
    private BlockingQueue<Message> sendingQueue;

    public MyProtocol(String server_ip, int server_port, int frequency) {
        receivedQueue = new LinkedBlockingQueue<Message>();
        sendingQueue = new LinkedBlockingQueue<Message>();

        new Client(SERVER_IP, SERVER_PORT, frequency, receivedQueue, sendingQueue); // Give the client the Queues to use
        broadcastIP();

        new ReceiveThread(receivedQueue).start(); // Start thread to handle received messages!

        // handle sending from stdin from this thread.
        try {
            Scanner console = new Scanner(System.in);
            String input = "";
            while (true) {
                input = console.nextLine(); // read input
                byte[] inputBytes = input.getBytes(); // get bytes from input
                ByteBuffer toSend = ByteBuffer.allocate(inputBytes.length); // make a new byte buffer with the length of the input string
                toSend.put(inputBytes, 0, inputBytes.length); // copy the input string into the byte buffer.
                Message msg;
                if(input.length() > 26){  // data = 26 bytes and packet_header = 6 bytes
                    ByteBuffer toSend1 = ByteBuffer.allocate(26);
                    for(int i = 0; i < 27; i++){
                        toSend1.put(inputBytes[i]);
                    }
                    ByteBuffer toSend2 = ByteBuffer.allocate(26);
                    for(int j = 27; j < input.length(); j++){
                        toSend2.put(inputBytes[j]);
                    }
                    Message msg2;
                    msg = new Message(MessageType.DATA, toSend1, 26);                 // fragments of data
                    msg2 = new Message(MessageType.DATA, toSend2, input.length()-26); // 2nd fragment of data
                    sendingQueue.put(msg);
                    sendingQueue.put(msg2);
                } else if (input.length() > 2 && input.length() < 27) {
                    msg = new Message(MessageType.DATA, toSend, inputBytes.length);
                   // msg = new Message(MessageType.DATA, toSend);
                    sendingQueue.put(msg);
                } else {
                    msg = new Message(MessageType.DATA_SHORT, toSend);
                    sendingQueue.put(msg);
                }
                //sendingQueue.put(msg);
            }
        } catch (InterruptedException e) {
            System.exit(2);
        }
    }

    private void broadcastIP() {
        String input = null;
        try {
            try {
                input = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            if (input != null) {
                input = "IP" + input + "/";

                byte[] inputBytes = input.getBytes(); // get bytes from input
                ByteBuffer toSend = ByteBuffer.allocate(inputBytes.length); // make a new byte buffer with the length of the input string
                toSend.put(inputBytes, 0, inputBytes.length); // copy the input string into the byte buffer.

                Message bc = new Message(MessageType.DATA, toSend);

                sendingQueue.put(bc);
            }
        } catch (InterruptedException e) {
            System.exit(2);
        }
    }

    public static void main(String args[]) {
        if (args.length > 0) {
            frequency = Integer.parseInt(args[0]);
        }
        new MyProtocol(SERVER_IP, SERVER_PORT, frequency);
    }


}

