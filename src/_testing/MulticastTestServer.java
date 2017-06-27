/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author christiancolbach
 */
public class MulticastTestServer {

    protected static DatagramSocket socket;
    protected static String multicastGroupId = "224.0.0.1";
    protected static long delay = 1000l;
    protected static int port = 1244;

    public static void main(String[] args) throws IOException {
        socket = new DatagramSocket();

        InetAddress group = InetAddress.getByName(multicastGroupId);
        while (true) {
            try {
                byte[] buf = new byte[256];
                buf = "Hallo Potato".getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
                socket.send(packet);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            } catch (IOException e) {
                e.printStackTrace();
                socket.close();
                break;
            }
        }

    }

}
