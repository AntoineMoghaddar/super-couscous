package prots.macprotocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A custom protocol built upon the roots of the Round Robin principle.
 * The protocol initiates a list of all possible nodes in a rundown and assigns states to the nodes.
 * <p>
 * Given the states, the protocol will decide to retransmit or to set as successful.
 *
 * @author Antoine Moghaddar, Simon van Eldik
 * @version v-3.0.1, University of Twente
 * @implNote we have removed all logging and output as this had a slight effect on the timing of the protocol.
 * For scores and execution details, please check the networkingchallenges.ewi.utwente.nl dashboard.
 * @since 17-02-2021
 */
public class MACProtocol implements IMACProtocol {

    /**
     * The constant NODES.
     * The maximum amount of nodes connected to this system.
     */
    public static final int NODES = 4;
    /**
     * The constant QUEUE_SIZE.
     * The optimal queue size for the best performance
     */
    public static final int QUEUE_SIZE = 6;

    //Integer variables necessary for setting the turns and indices of the counts.
    private int index, turn, sent, currentTurn;

    //Boolean to set a rundown to be finished or not.
    private boolean finished;

    /**
     * List of nodes with their corresponding return value.
     * The initial value of this list should be equal to MediumState.Collision.
     * This list can later be filled by replacing the collisions with Type Data.
     */
    private MediumState[] stats;

    /**
     * Instantiates a new protocol.
     */
    public MACProtocol() {
        index = NODES;
        finished = false;
        initStats();
        turn = new Random().nextInt(NODES);
    }

    /**
     * Instantiates a 'empty' list.
     * This list can later be filled by replacing the collisions with Type Data.
     */
    private void initStats() {
        this.stats = new MediumState[NODES];
        Arrays.fill(stats, MediumState.Collision);
    }

    /**
     * Processes the transmission timeslot.
     *
     * @param previousMediumState The state of the previous node within the rundown.
     * @param controlInformation  The information for controlling of the previous node.
     * @param localQueueLength    The length of the local queue.
     */
    @Override
    public TransmissionInfo TimeslotAvailable(MediumState previousMediumState, int controlInformation, int localQueueLength) {
        //if a single rundown has not finished yet, it will continue...to the next item in the array
        if (!finished) {

            //replace item in the list of all possible nodes in rundown, replace value of n-1 with state of previousMediumState
            stats[(index - 1) % NODES] = previousMediumState;
            if (index % NODES == 0) {

                //check if there are collisions in rundown
                switch (collisionCount(stats)) {
                    case 0:                    //if no collisions, rundown succesful
                        finished = true;
                        break;
                    case 1:                    //if one collision, get the index of the item that collided and retransmit.
                        if (stats[turn] == MediumState.Collision)
                            turn = randomTurn(getFreePositions(stats));
                        break;
                    case 2:                    //if more than 1 collisions, get random value to retransmit on random basis
                    default:
                        turn = new Random().nextInt(NODES);
                        break;
                }

                //reset the stats list for next rundown
                stats = new MediumState[NODES];
            }

            //transmit corresponding data/values
            if (index++ % NODES == turn)
                return (localQueueLength > 0) ?
                        new TransmissionInfo(TransmissionType.Data, 0) :
                        new TransmissionInfo(TransmissionType.NoData, 0);

            //reset if finished
        } else if (currentTurn != turn && previousMediumState == MediumState.Idle || controlInformation < 1) {
            sent = 0;
            currentTurn = (currentTurn + 1) % NODES;
        }

        //validate transmission
        if (currentTurn == turn) {
            if (localQueueLength > 0 && sent < QUEUE_SIZE) {
                sent++;
                return new TransmissionInfo(TransmissionType.Data, Math.min(localQueueLength - 1, QUEUE_SIZE - sent));
            }
        }
        return new TransmissionInfo(TransmissionType.Silent, 0);
    }

    /**
     * A counter method to keep track of the amount of collisions in a rundown.
     * With this we can trace back where the collisions happened, and if required, retransmit.
     *
     * @param states a list of all states within rundown.
     * @return a integer value of failed/collided transmissions in a single rundown.
     */
    private int collisionCount(MediumState[] states) {
        int result = 0;
        for (MediumState state : states) {
            if (state == MediumState.Collision) result++;
        }
        return result;
    }

    /**
     * A list containing all the possible positions in a single rundown to be filled.
     *
     * @param states a list of all states within rundown.
     * @return a list of all free positions, a.k.a all positions that are of type Collision or Idle.
     */
    private List<Integer> getFreePositions(MediumState[] states) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < states.length; i++) {
            if (states[i] == MediumState.Collision || states[i] == MediumState.Idle) result.add(i);
        }
        return result;
    }

    /**
     * Instantiates a random turn from a given list of nodes in a single rundown.
     *
     * @param turn a list of all possible turns.
     * @return a item from a list of nodes in the current rundown.
     */
    private int randomTurn(List<Integer> turn) {
        return turn.get(new Random().nextInt(turn.size()));
    }
}
