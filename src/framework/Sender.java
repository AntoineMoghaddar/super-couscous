package framework;

import framework.client.Message;
import framework.client.MessageType;
import model.CouscousModel;
import packets.Address;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;

public class Sender extends Thread {
    private BlockingQueue<Message> sendingQueue;
    private BlockingQueue<Message> finalQueue;
    private SocketChannel sock;

    public Sender(SocketChannel sock, BlockingQueue<Message> sendingQueue) {
        super();
        this.sendingQueue = sendingQueue;
        this.sock = sock;
    }
    //added by tessa

    CouscousModel model = CouscousModel.getInstance();
    ArrayList<Address> addressList = model.getAddresses();
    Address myIP = addressList.get(0);
    int myIP_Id = model.getAddresses().get(0).getIp_id();
    int myIPIndex = addressList.indexOf(myIP);
    int amountNodes = addressList.size();
    Message token;
    String ipNextNode;
    ArrayList<Integer> IP_idList = new ArrayList<>();

    public void turnToSend(ByteBuffer tokenMessage) throws InterruptedException {
        for (int i = 0; i < addressList.size(); i++) {
            if (!IP_idList.contains(model.getAddresses().get(i).getIp_id())) {
                IP_idList.add(model.getAddresses().get(i).getIp_id());
            }
            Collections.sort(IP_idList);
            int myIP_IdIndex = IP_idList.indexOf(myIP_Id);
            int previousIP = IP_idList.get(myIP_IdIndex - 1);
            int sourceID = tokenMessage.get(0);
            int destID = tokenMessage.get(1);
            if (sourceID == previousIP && destID == myIP_Id) {
                sendWithToken();
            }
        }
    }

    public void sendWithToken() throws InterruptedException {
        Message firstInQueue = sendingQueue.element();
        sendingQueue.take();
        finalQueue.add(firstInQueue);
        int myIP_IdIndex = IP_idList.indexOf(myIP_Id);
        //then we need to send the token, a DATA_SHORT message
        String destinationIP_id = "" + IP_idList.get((myIP_IdIndex + 1) % amountNodes) + "";
        byte[] inputDest = destinationIP_id.getBytes(); // get bytes from input
        String sourceIP_Id = "" + myIP_Id + "";
        byte[] inputSource = sourceIP_Id.getBytes(); // get bytes from input
        java.nio.ByteBuffer toSend = ByteBuffer.allocate(inputDest.length + inputSource.length); // make a new byte buffer with the length of the input string
        toSend.put(inputSource, 0, inputSource.length);
        toSend.put(inputDest, 1, inputDest.length);
        token = new Message(MessageType.DATA_SHORT, toSend);
        finalQueue.add(token);
    }

//    private void tookTooLong(){
//        while (sock.isConnected()) {
//            if (sendingQueue.size() > 5){

    //}}}


    private void senderLoop() {
        while (sock.isConnected()) {
            try {
                Message msg = sendingQueue.take();
                if (msg.getData() != null && (msg.getType() == MessageType.DATA || msg.getType() == MessageType.DATA_SHORT)) {
                    ByteBuffer data = msg.getData();
                    // System.out.println("This is the value of data: " + msg.getData());
                    //data.position(0); //reset position just to be sure
                    int length = data.capacity(); //assume capacity is also what we want to send here!
                    ByteBuffer toSend = ByteBuffer.allocate(length);
                  /*  if (msg.getType() == MessageType.DATA) {
                        toSend.put((byte) 3);   // why is this 3
                    } else { // must be DATA_SHORT due to check above
                        toSend.put((byte) 6);   // why is this 6
                    }
                    toSend.put((byte) length);*/
                    toSend.put(data);
                    toSend.position(0);
                    System.out.println("Sending " + length + " bytes!");
                    sock.write(toSend);
                }
            } catch (IOException e) {
                System.err.println("Everything is broken!");
            } catch (InterruptedException e) {
                System.err.println("Failed to take from sendingQueue: " + e);
            }
            // }
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
