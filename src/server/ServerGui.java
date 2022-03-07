/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import serverr.PlayerHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import serverr.Serverr;

/**
 *
 * @author sara
 */
public class ServerGui extends Application {

    ServerSocket myServerSocket;
    Thread th;
    public static boolean running = false;

    public ServerGui() throws Exception {
       /* try {
            myServerSocket = new ServerSocket(5005);
            //running = true;
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
        }*/
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        launch(args);
        try {
            if (ServerGui.running == false) {
                new ServerGui();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}