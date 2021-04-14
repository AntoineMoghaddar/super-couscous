package packets;

public class Packet {
    private static final int SIZE = 32;

    private int s_address, d_addres, seq, offset;
    private boolean followup;

    private String data;


    public Packet(int s_address, int d_addres, int seq, int offset, boolean followup, String data) {
        this.s_address = s_address;
        this.d_addres = d_addres;
        this.seq = seq;
        this.offset = offset;
        this.followup = followup;
        this.data = data;
    }

    public Packet(int[] packet){
        this.s_address = packet[0];

    }

}
