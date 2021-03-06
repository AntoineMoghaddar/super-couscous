package prots.macprotocol;

/*
 *                                                          *
 **                 DO NOT EDIT THIS FILE                  **
 ***         YOU DO NOT HAVE TO LOOK IN THIS FILE         ***
 **** IF YOU HAVE QUESTIONS PLEASE DO ASK A TA FOR HELP  ****
 *****                                                  *****
 ************************************************************
 */

/**
 * Describes the interface used for MAC protocols
 *
 * @author Jaco ter Braak, University of Twente
 * @version 05-12-2013
 */
public interface IMACProtocol {
    /**
     * The (emulated) physical layer will announce a new timeslot to the protocol by calling this method.
     *
     * @param previousMediumState The state of the medium in the latest timeslot
     * @param controlInformation  Control information, if available (when previousMediumState == MediumState.Success). Otherwise undefined.
     * @param localQueueLength    The length of the local packet queue.
     * @return TransmissionInfo for the physical layer.
     */
    TransmissionInfo TimeslotAvailable(MediumState previousMediumState, int controlInformation, int localQueueLength);
}
