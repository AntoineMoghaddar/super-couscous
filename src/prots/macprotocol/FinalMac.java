package prots.macprotocol;

import framework.MyProtocol;
import framework.client.Message;
import framework.client.MessageType;
import model.CouscousModel;
import packets.Address;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

//package prots.macprotocol;
//
//import framework.client.Message;
//import framework.client.MessageType;
//import model.CouscousModel;
//import packets.Address;
//
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class FinalMac {
////mac protocol
//    public static class MACProtocol {
//        private CouscousModel model = CouscousModel.getInstance();
//        private packets.Address idNextNode;
//        ArrayList<Address> addressList = model.getAddresses();
//        private Address myIP = addressList.get(0);
//        private int myIP_Id = model.getAddresses().get(0).getIp_id();
//        private int myIPIndex = addressList.indexOf(myIP);
//        private int amountNodes = 4;
//        Message infomsg;
//        String ipNextNode;
//        ArrayList<Integer> IP_idList;
//
//        public void makeAndSortIP_IDList() {
//            for (int i = 0; i < addressList.size(); i++) {
//                IP_idList.add(model.getAddresses().get(i).getIp_id());
//            }
//            Collections.sort(IP_idList);
//        }
//        public boolean myTurn(ByteBuffer bytes, int bytesLength) throws InterruptedException {
//                if(bytes.get(0) == myIP_Id){
//                    return true;}
//                else{return false;}
//            }
//        public void turnToSend() throws InterruptedException {
            //first send own packet, if exist.

//            //then we need to send the token, a DATA_SHORT message
//            ipNextNode = "" + IP_idList.get((myIPIndex + 1 )% amountNodes) + "";
//            byte[] inputBytes = ipNextNode.getBytes(); // get bytes from input
//            java.nio.ByteBuffer toSend = ByteBuffer.allocate(inputBytes.length); // make a new byte buffer with the length of the input string
//            toSend.put(inputBytes, 0, inputBytes.length);
//            infomsg = new Message(MessageType.DATA_SHORT, toSend);
 //           sendingQueue.add(infomsg);

