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
    private ArrayList<Byte> destination_nodes; // Activate after dry run
    private int source_id;
    private int header_length;
    //private int destination_id;
    public int get_source_add() {

        ArrayList<Address> all_nodes;
        all_nodes = CouscousModel.getInstance().getAddresses();
        destination_nodes = new ArrayList<>();

        for (Address node : all_nodes) {
            try {
                if (node.getIp_address().equals(InetAddress.getLocalHost().getHostAddress()))
                    source_id = node.getIp_id();
                else destination_nodes.add((byte)node.getIp_id());
            } catch (UnknownHostException e) {
                System.out.print("Host not found");
            }
        }
        return source_id;
    }

    // public int get_dest_add() { return destination_id;}
    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, ByteBuffer data) {
        this.type = type;
        try {
            byte[] gg = new byte[2];
            data.flip();
            data.get(gg, 0 , gg.length);
            data.clear();
            data.position(0);
            data.put((byte) get_source_add());
            data.put(destination_nodes.get(0));
           // data.put(gg);
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
            byte[] gg = new byte[32];
            data.flip();
            data.get(gg, 0 , data_length);
            data.clear();
            data.position(0); // just to be sure
            data.put((byte) get_source_add());
            //destination_nodes.toArray();
            data.put(destination_nodes.get(0)); // only uses the first destination node
            data.put((byte) data_length);
            data.put((byte) header_length);
            data.put(gg);
            this.data = data;
        } catch (BufferOverflowException e) {
            System.out.print("current position is higher than limit");
            System.out.print("Exception e");
        } catch (ReadOnlyBufferException e) {
            System.out.print("Exception again" + e);
        }
    }

  /*  public int get_header_length(){
        return header_length;
    }*/
    public MessageType getType() {
        return type;
    }

    public ByteBuffer getData() {
        return data;
    }
  /*  public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String give = "";
        give = sc.nextLine();
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.put(give.getBytes(), 0, give.length());
        Message mm = new Message(MessageType.DATA, bb, give.length());
        bb.flip();
        try{
        System.out.print("source_id" + ":"+ bb.get(0));
        System.out.print("\ndata_length:" + bb.get(1));
        System.out.print("\nheader_length:" + bb.get(2) + '\n');  // header_length = bb.get(2)
        for(int i = bb.get(2); i < (bb.get(2) + bb.get(1)); i++) {
            System.out.print((char) bb.get(i));
        }
        }catch(IndexOutOfBoundsException e){
            System.out.print("The exception fucked it over");
        }
    }*/
}