package framework.client;

import java.nio.ByteBuffer;
//import protocol.packets.Packet;
public class Message {
    private MessageType type;
    private ByteBuffer data;
  //  private Packet pack;


    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, ByteBuffer data) {
        this.type = type;
       // ByteBuffer bb1 = ByteBuffer.allocate(32);
//        byte[] source_address = getIp_id.getBytes();
//        byte[] dest_address = getIp_id.getBytes();
//        bb1.put(source_address);
//        bb1.put(dest_address);
     //   bb1.put(data);
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public ByteBuffer getData() {
        return data;
    }
}