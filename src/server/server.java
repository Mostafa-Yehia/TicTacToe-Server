/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import serverr.PlayerHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import serverr.Serverr;

/**
 *
 * @author elari
 */
public class server {

    static ServerSocket myServerSocket;
    static Thread th;
    public static boolean running = false;

    public static void startServer() {
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

    public static void stopServer() throws IOException {
        th.stop();
        for (PlayerHandler soc : PlayerHandler.ClientVector) {
            soc.display.close();
            soc.sender.close();
            soc.mySocket.close();
        }
        serverr.PlayerHandler.ClientVector.clear();
        try {
            myServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
