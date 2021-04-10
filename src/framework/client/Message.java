package framework.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.Scanner;
import packets.Address;
import model.CouscousModel;

    // DON'T MAKE ANY CHANGES TO ANYTHING--- Ujjwal
public class Message {
    private MessageType type;
    private ByteBuffer data;
   // private ArrayList<Integer> destination_nodes; // Activate after dry run
   // private int source_id;                        // Activate after dry run
    //private int destination_id;
   /* public int get_source_add() {

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
    }*/ // Activate after dry run

    // public int get_dest_add() { return destination_id;}
    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, ByteBuffer data) {
        this.type = type;
        try {
            byte[] gg = new byte[data.position()];
            data.flip();
            data.get(gg, 0 , gg.length);
            data.clear();
            data.put((byte) 68);
//          bb1.put((byte)destination);
            data.put(gg);
            this.data = data;
        }catch(BufferOverflowException e) {
            System.out.print("current position is higher than limit");
            System.out.print("Exception e");
        }catch(ReadOnlyBufferException e) {
            System.out.print("Exception again" + e);
        }
    }

    public Message(MessageType type, ByteBuffer data, int data_length) {
        this.type = type;
        try {
            byte[] gg = new byte[data.position()];
            data.flip();
            data.get(gg, 0 , gg.length);
            data.clear();
            data.put((byte) 68);
//          bb1.put((byte)destination);
            data.put((byte) data_length);
//// int header_length = source.size() + dest.size() + data_length.size();
            //bb1.put((byte) header_length);
            data.put(gg);
            this.data = data;
        } catch (BufferOverflowException e) {
            System.out.print("current position is higher than limit");
            System.out.print("Exception e");
        } catch (ReadOnlyBufferException e) {
            System.out.print("Exception again" + e);
        }
    }

    public MessageType getType() {
        return type;
    }

    public ByteBuffer getData() {
        return data;
    }
   /* public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String give = "";
        give = sc.nextLine();
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.put(give.getBytes(), 0, give.length());
        Message mm = new Message(MessageType.DATA, bb, give.length());
        bb.flip();
        try{
        System.out.print("fuck" + " "+ (char) bb.get(21));
        }catch(IndexOutOfBoundsException e){
            System.out.print("The exception fucked it over");
        }
    } */
}