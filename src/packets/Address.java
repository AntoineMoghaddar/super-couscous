package packets;

import  helperClasses.logging.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Address {

    private final int ip_id;
    private InetAddress ip_address;
    private HashMap<Address, Integer> cost_to_node;

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

    public void addCostOfNode(Address address, Integer RTT_time) {
        cost_to_node.put(address, RTT_time);
    }

    public HashMap<Address, Integer> getCost_to_node() {
        return cost_to_node;
    }

//    public Integer getCostSpecificNode(){
//        for (Address a :
//                cost_to_node) {
//        }
//    }
}
