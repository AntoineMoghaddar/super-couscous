package packets;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address {

    private final int ip_id;
    private String ip_address;

    public Address(String getLocalhost, int id) {
        this.ip_address = getLocalhost;
        ip_id = id;
    }

    public Address(int ip_id) {
        try {
            this.ip_address = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip_address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.ip_id = ip_id;
    }

    public int getIp_id() {
        return ip_id;
    }

    public String getIp_address() {
        return ip_address;
    }
}
