package utils.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Broadcast {
    
    public static int DEFAULT_MULTI_CAST_SOCKET_PORT = 4446;
    public static String DEFAULT_MULTI_CAST_SOCKET_HOST = "230.0.0.1";

    public static void send(String message, String multicastGroup, Integer multicastPort) {
        if (multicastGroup == null) {
            multicastGroup = DEFAULT_MULTI_CAST_SOCKET_HOST;
        }
        if (multicastPort == null) {
            multicastPort = 4446;
        }
        try {
            MulticastSocket socket = new MulticastSocket();
            byte[] messageAsByteArray = message.getBytes("UTF-8");
            socket.send(new DatagramPacket(messageAsByteArray, messageAsByteArray.length, InetAddress.getByName(multicastGroup), DEFAULT_MULTI_CAST_SOCKET_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
