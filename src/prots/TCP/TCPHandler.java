package prots.TCP;


import framework.Receiver;

import java.net.DatagramPacket;
import java.util.*;
import java.net.InetAddress;


public class TCPHandler {


    private final int TIMEOUT = 1000; // ms waiting time for resending of packet

    private Receiver ownReceiver;

    // Ack we expect to receive
    private HashMap<InetAddress, ArrayList<Integer>> ackExpected = new HashMap<>();

    // Ack we received
    private HashMap<InetAddress, ArrayList<Integer>> ackReceived = new HashMap<>();

    // Obtained Seq's, ie to check if we get a duplicate packet
    private HashMap<InetAddress, ArrayList<Integer>> ObtainedSeq = new HashMap<>();

    // All Timeout timers for expected ACK's
    private ArrayList<TCPTimerTask> Timers = new ArrayList<>();

    public TCPHandler(Receiver owner) { ownReceiver = owner; }

    public void packetSent(int ackNum, DatagramPacket packet) {
        // Create Timer + Add ACK to expected Ack's
        for(List<Integer> list : ackExpected.values()) {
            if(!list.contains(ackNum)) {
                list.add(ackNum);
            }
        }

        addTimer(ackNum, packet);
    }


    public void addTimer(int ackNumber, DatagramPacket packet) {
        Timer timer = new Timer();
        TCPTimerTask Task = new TCPTimerTask(this,ackNumber,packet);
        timer.schedule(Task, TIMEOUT);
    }

    public void stopTimer(int ackNumber) {
        for(TCPTimerTask task : Timers) {
            if(task.getAckNumber()==ackNumber) {
                task.cancel();
            }
        }
    }

    public void ackReceived(int ackNumber, InetAddress source) {
        if(ackReceived.get(source).contains(ackNumber)) {
            // Ignore, duplicate ACK
        } else if(ackExpected.get(source).contains(ackNumber)){
            // if we expect this ack
            stopTimer(ackNumber);

            // Add ack to ackReceived and remove from ackExpected
            ackExpected.get(source).remove(Arrays.asList(ackNumber));

            ackReceived.get(source).add(ackNumber);
        }
    }

    public void resend(DatagramPacket pack) {
        ownReceiver.getOwnSender().sendPacket(pack);
    }

    // Returns true if duplicate
    public boolean packetReceived(int seqNumber, InetAddress source){
        return ObtainedSeq.containsKey(source) && ObtainedSeq.get(source).contains(seqNumber);
    }

    public void addClient(InetAddress sourceA, InetAddress sourceB){
        if(!ackReceived.containsKey(sourceA)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }
        if(!ackExpected.containsKey(sourceA)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }
        if(!ObtainedSeq.containsKey(sourceA)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }

        if(!ackReceived.containsKey(sourceB)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }
        if(!ackExpected.containsKey(sourceB)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }
        if(!ObtainedSeq.containsKey(sourceB)) {
            ArrayList<Integer> arr = new ArrayList<>();
            ackReceived.put(sourceA, arr);
        }

    }
}