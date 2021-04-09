package model;

import design.logging.Logger;
import design.transfer.ExportIP;
import design.transfer.ImportIP;
import org.apache.commons.codec.binary.Hex;
import packets.Address;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CouscousModel {

    private ArrayList<Address> addresses;
    private ImportIP in;
    private ExportIP out;

    private static CouscousModel instance = null;
    private static int ip_id = 0;
    private static String filename;

    private CouscousModel() {
        in = new ImportIP();
        out = new ExportIP();

        addresses = in.getAddresses();
        filename = "addressing.txt";

        out.createFile(filename);
        in.readFromFile(filename);
    }

    public static CouscousModel getInstance() {
        if (instance == null)
            instance = new CouscousModel();

        return instance;
    }

    public void addIP(String ipaddress) {
        boolean create = true;

        for (Address ip : addresses) {
            if (ip.getIp_address().equals(ipaddress))
                create = false;
        }

        if (create) {
            addresses.add(new Address(ipaddress, ip_id));
            printIPs();
            ip_id++;
        } else
            Logger.err("IP already exists.");
    }

    private void printIPs() {
        Logger.debug("The following IP's now exist: ");
        for (Address a :
                addresses) {
            Logger.confirm(a.getIp_address() + " with identifier: /" + a.getIp_id());
        }
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    /**
     * call before closing connection.
     */
    public void closeConnection() {
        out.writeToFile(filename, addresses);
    }

    /**
     * @param value;     password combination unXORed with the hascode (created by getPassword())
     * @param secretkey; the secret key provided by the client
     * @return returns a perfectly fine API hash which is ready to be send as a header request
     * @Definition The used encoder for creating the required hash by the CCVShop API
     * @description XORes and Hashes the full API password provided by another method with SHA512
     * @--> and the provided secret key by the client
     * @use XORes the provided password combination (created in getPassword()) with the secret key
     * @--> and hashes it all into one SHA512 encoded String
     */
    public String SHA512_encoder(String value, String secretkey) {
        try {
            // Get an hmac_sha1 secretkey from the raw secretkey bytes
            byte[] keyBytes = secretkey.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA512");

            // Get an hmac_sha512 Mac instance and initialize with the signing secretkey
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            Logger.debug("Hashed Password" + new String(hexBytes, StandardCharsets.UTF_8));
            return new String(hexBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
