package _testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastTestClient {

    final static String MULTICAST_GROUP = "224.0.0.1";
    final static int PORT = 1243;

    public static String getMultiCast() throws IOException {
        MulticastSocket socket = new MulticastSocket(PORT);
        InetAddress address = InetAddress.getByName(MULTICAST_GROUP);
        socket.joinGroup(address);
        DatagramPacket packet;
        byte[] buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String rtn = new String(packet.getData());
        socket.leaveGroup(address);
        socket.close();
        System.out.println("return");
        return rtn;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(getMultiCast());
    }

}
