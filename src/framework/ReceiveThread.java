package framework;

import framework.client.Message;
import framework.client.MessageType;
import helperClasses.logging.Logger;
import model.CouscousModel;

import java.util.concurrent.BlockingQueue;

public class ReceiveThread extends Thread {

    private BlockingQueue<Message> receivedQueue;
    private CouscousModel model = CouscousModel.getInstance();

    public ReceiveThread(BlockingQueue<Message> receivedQueue) {
        super();
        this.receivedQueue = receivedQueue;
    }

    // Handle messages from the server / audio framework
    public void run() {
        while (true) {
            try {
                Logger.println(Logger.Type.CONSOLE, java.time.LocalTime.now().toString());

                Message m = receivedQueue.take();
                if (m.getType() == MessageType.BUSY) { // The channel is busy (A node is sending within our detection range)
                    System.out.println("BUSY");
                } else if (m.getType() == MessageType.FREE) { // The channel is no longer busy (no nodes are sending within our detection range)
                    System.out.println("FREE");
                } else if (m.getType() == MessageType.DATA) { // We received a data frame!
                    System.out.print("DATA: ");

                    String data = model.printByteBuffer(m.getData(), m.getData().capacity());

                    if (data.startsWith("IP")) {
                        Logger.debug("This is the filtered IP: " + model.filterIP(data));
                        model.addIP(model.filterIP(data));
                    } else {
                        Logger.confirm("this is not IP data.");
                        //TODO Process this data into readable data.
                        System.out.println(data);
                    }

                } else if (m.getType() == MessageType.DATA_SHORT) { // We received a short data frame!
                    System.out.print("DATA_SHORT: ");
                    model.printByteBuffer(m.getData(), m.getData().capacity()); //Just print the data
                } else if (m.getType() == MessageType.DONE_SENDING) { // This node is done sending
                    System.out.println("DONE_SENDING");
                } else if (m.getType() == MessageType.HELLO) { // Server / audio framework hello message. You don't have to handle this
                    System.out.println("HELLO");
                } else if (m.getType() == MessageType.SENDING) { // This node is sending
                    System.out.println("SENDING");
                } else if (m.getType() == MessageType.END) { // Server / audio framework disconnect message. You don't have to handle this
                    System.out.println("END");
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                System.err.println("Failed to take from queue: " + e);
            }
        }
    }
}