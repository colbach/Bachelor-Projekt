package utils.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class BroadcastListenerThread extends Thread {

    private final String multicastGroup;
    private final Integer multicastPort;
    private final BroadcastListenerThreadCallback broadcastListenerThreadCallback;

    private final byte[] buffer = new byte[65536];
    private final MulticastSocket socket;
    private volatile boolean running;

    public BroadcastListenerThread(String multicastGroup, Integer multicastPort, BroadcastListenerThreadCallback broadcastListenerThreadCallback) throws IOException {

        if (multicastGroup == null) {
            this.multicastGroup = Broadcast.DEFAULT_MULTI_CAST_SOCKET_HOST;
        } else {
            this.multicastGroup = multicastGroup;
        }
        if (multicastPort == null) {
            this.multicastPort = Broadcast.DEFAULT_MULTI_CAST_SOCKET_PORT;
        } else {
            this.multicastPort = multicastPort;
        }
        if (broadcastListenerThreadCallback == null) {
            throw new IllegalArgumentException("broadcastListenerThreadCallback darf nicht null sein.");
        } else {
            this.broadcastListenerThreadCallback = broadcastListenerThreadCallback;
        }

        InetAddress group = InetAddress.getByName(multicastGroup);
        socket = new MulticastSocket(multicastPort);
        socket.joinGroup(group);
        running = true;
    }

    @Override
    public void run() {

        while (running) {

            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] received = new byte[packet.getLength()];
                System.arraycopy(buffer, 0, received, 0, received.length);

                if (running) {
                    broadcastListenerThreadCallback.broadcastReceived(received);
                }

            } catch (IOException iOException) {
            }
            
        }
    }

    public void stopListening() {
        running = false;
        socket.close();
    }

//    public static void main(String[] args) throws IOException {
//        new BroadcastListenerThread(null, null, new BroadcastListenerThreadCallback() {
//            @Override
//            public void broadcastReceived(byte[] bytes) {
//                System.out.println("received:" + new String(bytes));
//            }
//        }).start();
//    }

}
