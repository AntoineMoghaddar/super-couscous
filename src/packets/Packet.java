package packets;

public class Packet {

    private int[] pckt = new int[32];
    private int source_address, dest_address;

    public Packet(int sou_address, int dest_address) {
        this.source_address = sou_address;
        this.dest_address = dest_address;
    }

    public void setupPacket() {
        assert pckt != null;

        pckt[1] = source_address;
        pckt[2] = dest_address;
        pckt[3] = 0; // sequence number
        pckt[4] = 0; // acknowledgement number
        pckt[5] = 0; // checksum
        //pckt[6] = data; data is stored from 6th byte onwards

    }

    public int getSource_address() {
        return source_address;
    }

    public int getDest_address() {
        return dest_address;
    }


}
