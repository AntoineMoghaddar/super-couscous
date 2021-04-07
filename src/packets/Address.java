package packets;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address {

    private static int ip_id;
    private String local_host;

    public Address(String getLocalhost) {
        this.local_host = getLocalhost;
        ip_id++;
    }

    public Address(String getLocalhost, int id) {
        this.local_host = getLocalhost;
        ip_id = id;
    }

    public Address() {
        try {
            this.local_host = InetAddress.getLocalHost().getHostAddress();
            System.out.println(local_host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ip_id++;
    }

    public static int getIp_id() {
        return ip_id;
    }

    public String getLocal_host() {
        return local_host;
    }
}
