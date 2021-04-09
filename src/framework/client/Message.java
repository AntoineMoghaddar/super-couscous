package framework.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import packets.Address;
import model.CouscousModel;

//import protocol.packets.Packet;
public class Message {
    private MessageType type;
    private ByteBuffer data;
    private ArrayList<Integer> destination_nodes;
    private int source_id;
    //  private Packet pack;

    public int get_source_add() {

        ArrayList<Address> all_nodes;
        all_nodes = CouscousModel.getInstance().getAddresses();
        destination_nodes = new ArrayList<>();

        for (Address node : all_nodes) {
            try {
                if (node.getIp_address().equals(InetAddress.getLocalHost().getHostAddress()))
                    source_id = node.getIp_id();
                else destination_nodes.add(node.getIp_id());
            } catch (UnknownHostException e) {
                System.out.print("Host not found");
            }
        }
        return source_id;
    }

    public Message(MessageType type) {
        this.type = type;
    }
    /*Notes for Antoine
     *  if you get done with the ID/IP
     */

    public Message(MessageType type, ByteBuffer data) {
        this.type = type;
        ByteBuffer bb1 = ByteBuffer.allocate(34);
        int source = get_source_add();
        // byte[] dest_address = getIp_id.getBytes();
        bb1.put((byte) source);
        //bb1.put(dest_address);
        bb1.put(data);
        this.data = bb1;
    }

    public Message(MessageType type, ByteBuffer data, int data_length) {
        this.type = type;

        ByteBuffer bb1 = ByteBuffer.allocate(34);
        int source = get_source_add();
        //byte[] dest_address = getIp_id.getBytes();
        bb1.put((byte) source);
//        //  bb1.put(dest_address);
        bb1.put((byte) data_length);
//// int header_length = source.size() + dest.size() + data_length.size();
        //bb1.put((byte) header_length);
        bb1.put(data);
        this.data = bb1;
    }

    public MessageType getType() {
        return type;
    }

    public ByteBuffer getData() {
        return data;
    }
}