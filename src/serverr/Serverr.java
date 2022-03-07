/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author elari
 */
public class Serverr {

    ServerSocket myServerSocket;
    Thread th;
    public static boolean running = false;

    public Serverr() throws Exception {
        try {
            myServerSocket = new ServerSocket(5005);
            running = true;
            System.out.println("1");

            th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("2");

                        while (true) {
                            if (running) {
                                Socket s = myServerSocket.accept();
                                System.out.println("3");

                                new PlayerHandler(s);
                                System.out.println("4");

                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            th.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            if (Serverr.running == false) {
                new Serverr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
