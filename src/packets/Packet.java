package packets;

import framework.client.Message;

import java.nio.ByteBuffer;

public class Packet {
    private static final int SIZE = 32;

    private int s_address, d_address, seq, offset;
    private boolean followup;

    private String data;


    public Packet(int s_address, int d_address, int seq, int offset, boolean followup, String data) {
        this.s_address = s_address;
        this.d_address = d_address;
        this.seq = seq;
        this.offset = offset;
        this.followup = followup;
        this.data = data;
    }

    public Packet(int[] packet) {
        this.s_address = packet[0];
        this.d_address = packet[1];
        this.seq = packet[2];
        this.offset = packet[3];
        this.followup = (packet[4] == 0);
        //string to bytes
//        byte[] inputBytes = data.getBytes(); // get bytes from input
//        ByteBuffer toSend = ByteBuffer.allocate(inputBytes.length); // make a new byte buffer with the length of the input string
//        toSend.put(inputBytes, 0, inputBytes.length); // copy the input string into the byte buffer.
//        Message msg;
//        this.data = packet[5];

    }

    public static int getSIZE() {
        return SIZE;
    }

    public int getS_address() {
        return s_address;
    }

    public void setS_address(int s_address) {
        this.s_address = s_address;
    }

    public int getD_address() {
        return d_address;
    }

    public void setD_address(int d_address) {
        this.d_address = d_address;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean hasFollowUp() {
        return followup;
    }

    public void setFollowup(boolean followup) {
        this.followup = followup;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
