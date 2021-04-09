package design.transfer;

import packets.Address;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImportIP {

    private ArrayList<Address> addresses;

    public ImportIP() {
        addresses = new ArrayList<>();
    }

    public void readFromFile(String filename) {

        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (!(line.startsWith("#")) && !(line.isEmpty())) {
                    processLine(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
        String[] lineData = line.split(";");
        if (line.isEmpty()) {
            System.out.println("No data found.");
        }

        if (lineData.length >= 1) {
            Address ip = new Address(lineData[2], Integer.parseInt(lineData[1]));
            addresses.add(ip);
        }
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }
}
