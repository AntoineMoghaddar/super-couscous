package packets;

import helperClasses.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address {

    private final int ip_id;
    private InetAddress ip_address;

    public Address(String ipaddress, int id) {
        this.ip_address = converter(ipaddress);
        ip_id = id;
    }

    public Address(int ip_id) {
        try {
            this.ip_address = converter(InetAddress.getLocalHost().getHostAddress());
//            this.ip_address = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip_address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.ip_id = ip_id;
    }

    private InetAddress converter(String name) {
        try {
            return InetAddress.getByName(name);
        } catch (UnknownHostException e) {
            Logger.err("Not an IP address");
            System.exit(2);
        }
        return null;
    }

    public int getIp_id() {
        return ip_id;
    }

    public InetAddress getIp_address() {
        return ip_address;
    }
}
