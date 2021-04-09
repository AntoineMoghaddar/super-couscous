package prots.TCP;

import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rowin on 18-4-2017.
 */
public class TCPTimerTask extends TimerTask{

    private TCPHandler owner;

    private int ackNumber;

    private DatagramPacket packet;

    public TCPTimerTask(TCPHandler _owner, int _ackNumber, DatagramPacket _packet) {
        owner = _owner;
        ackNumber= _ackNumber;
        packet = _packet;
    }

    @Override
    public void run() {
        // Assume here that the ack has not been received

        owner.resend(packet);

        // Add another timer
        owner.addTimer(ackNumber, packet);
    }

    public int getAckNumber() {
        return ackNumber;
    }
}