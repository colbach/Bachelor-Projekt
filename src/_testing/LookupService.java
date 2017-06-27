package _testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import utils.network.Broadcast;

public class LookupService {


    public static String receive(int timeout) {

        return "/";
    }

    public static void main(String[] args) {
        Broadcast.send("Hallo", null, null);
    }

}
