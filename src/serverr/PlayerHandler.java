/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elari
 */
public class PlayerHandler extends Thread implements Serializable {

    public PlayerHandler copy = this;

    public ObjectInputStream display;
    public ObjectOutputStream sender;
    public Socket mySocket;

    public static Vector<PlayerHandler> ClientVector = new Vector<PlayerHandler>();
    public static Vector<Player> PlayersVector = new Vector<Player>();
    public static Vector<Player> Players = new Vector<Player>();

    private Player player;
//    private String id;
//    private String name = null;
//    private int points;
//    private boolean isOnline = false;
    HashMap<Long, PlayerHandler> online;
    public static List<HashMap<String, Object>> onlinePlayers;

    public PlayerHandler(Socket cs) {
        mySocket = cs;
        try {
            display = new ObjectInputStream(cs.getInputStream());
            sender = new ObjectOutputStream(cs.getOutputStream());
            ClientVector.add(this);
            online = new HashMap();
            online.put(this.getId(), this);
            System.out.println("hi client");
            start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                HashMap<String, Object> map = (HashMap<String, Object>) display.readObject();
                System.out.println(map.get("action"));
                switch ((String) map.get("action")) {
                    case "login":
                        login((String) map.get("userID"), (String) map.get("password"));
                        break;
                    case "getOnline":
                        sendOnlinePlayers();
                        //ioStreams.sender.writeObject(map);
                        break;
                    case "signup":
                        signup((String) map.get("userID"), (String) map.get("name"), (String) map.get("password"));
                        break;
                    case "invite":
                        invite((String) map.get("inviter"), (String) map.get("invited"));
                        break;
                    case "inviteRes":
                        inviteRes(map);
                        break;
                    case "offline":
                        System.out.println((String) map.get("userID"));
                        updateStatus((String) map.get("userID"));
                        break;
                    case "move":
                        move(map);
                        break;
                    case "close":
                        updateStatus((String) map.get("userID"));
                        break;
                }
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean isOnline(String id) {
        for (PlayerHandler p : ClientVector) {
            if (p.player.getUserID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void login(String username, String password) {
        try {
            //TODO connect to db
            HashMap<String, Object> _map = new HashMap<String, Object>();
            _map.put("action", "login");
            _map.put("userID", username);
            _map.put("password", password);
            ConnectDB login = new ConnectDB(_map);
            System.out.print(username + ": ");
            System.out.println(login.map.get("userID"));
            System.out.print(password + ": ");
            System.out.println(login.map.get("password"));
            if (username.equals(login.map.get("userID")) && password.equals(login.map.get("password"))) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map = login.map;
                try {
                    sender.writeObject(map);

                    int flag = 0;
                    for (Player p : PlayersVector) {
                        if (login.map.get("userID").equals(p.getUserID())) {
                            flag = 1;
                            p.setStatus(true);
                            p.identity = this;
                        }

                    }
               /*     if (flag == 0) {
                        player = new Player();
                        PlayersVector.add(player);
                        player.setIdentity(this);
                        player.setUserID((String) login.map.get("userID"));
                        player.setName((String) login.map.get("name"));
                        player.setPoints((int) login.map.get("points"));
                        player.setStatus(true);
                    }*/


                    /*
                    player.setIdentity(this);
                    player.setUserID((String) login.map.get("userID"));
                    player.setName((String) login.map.get("name"));
                    player.setPoints((int) login.map.get("points"));
                    player.setStatus((boolean)login.map.get("success"));
                     */
 /*
                    id = (String) login.map.get("userID");
                    name = (String) login.map.get("name");
                    points = (int) login.map.get("points");

                    isOnline = true;
                     */
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("action", "login");
                map.put("success", false);
                try {
                    sender.writeObject(map);
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    private void sendOnlinePlayers() {
        List<HashMap<String, Object>> onlinePlayers = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < ClientVector.size(); i++) {
            PlayerHandler p = ClientVector.get(i);
            if (p.player.getStatus()) {
                HashMap<String, Object> playerMap = new HashMap<String, Object>();
                playerMap.put("userID", p.player.getUserID());
                playerMap.put("name", p.player.getName());
                playerMap.put("points", p.player.getPoints());
                playerMap.put("status", "online");
                //playerMap.put("socketID", this.getId());
                //playerMap.put("object", copy);

                onlinePlayers.add(playerMap);
            }
        }
                    //System.out.println("test...");
                    //System.out.println(onlinePlayers);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("action", "getOnline");
        map.put("success", true);
        map.put("onlinePlayers", onlinePlayers);
        System.out.println(map);
        try {        
            sender.writeObject(map);
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
//    public void Signals() {
//        System.out.println("inside signals");
//        for (Player p : PlayersVector) {
//            if (p.getStatus()) {
//                p.setStatus(true);
//                System.out.println(p.getUserID() + " is online");
//            } else {
//                p.setStatus(false);
//                System.out.println(p.getUserID() + " is offline");
//            }
//        }
//        System.out.println("after for signals");
//
//    }
    private void updateStatus(String _userID) {
        for (Player p : PlayersVector) {
            if (p.getUserID().equals(_userID)) {
                p.setStatus(false);
                System.out.println("player " + p.getUserID() + " is disconnected");
            }
        }
    }

    private void sendOnlinePlayers() {
        /*
        System.out.println("before signals");

        Signals();

        System.out.println("after signals");
         */
        onlinePlayers = new ArrayList<HashMap<String, Object>>();
        for (Player p : PlayersVector) {

            HashMap<String, Object> playerMap = new HashMap<String, Object>();
            playerMap.put("userID", p.getUserID());
            playerMap.put("name", p.getName());
            playerMap.put("points", p.getPoints());

            if (p.getStatus()) {
                playerMap.put("status", "online");
            } else {
                playerMap.put("status", "offline");
               /* try {
                    p.identity.display.close();
                    p.identity.sender.close();
                    p.identity.mySocket.close();
                    ClientVector.remove(this);
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }*/

            }
            //playerMap.put("socketID", this.getId());
            //playerMap.put("object", copy);

            onlinePlayers.add(playerMap);
        }
        //System.out.println("test...");
        //System.out.println(onlinePlayers);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("action", "getOnline");
        map.put("success", true);
        map.put("onlinePlayers", onlinePlayers);
        System.out.println(map);
        
        try {
            sender.writeObject(map);
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void signup(String userID, String name, String password) {
        try {
            //TODO connect to db
            HashMap<String, Object> _map = new HashMap<String, Object>();
            _map.put("action", "signup");
            _map.put("userID", userID);
            _map.put("name", name);
            _map.put("password", password);
            ConnectDB signup = new ConnectDB(_map);

            if ((boolean) signup.map.get("success")) {
                player = new Player();
                PlayersVector.add(player);
                player.setIdentity(this);
                player.setUserID(userID);
                player.setName(name);
                player.setPoints(0);
                player.setStatus(true);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map = signup.map;
                try {
                    sender.writeObject(map);
                    /*
                    id = (String) signup.map.get("userID");
                    name = (String) signup.map.get("name");
                    points = (int) signup.map.get("points");
                     
                    isOnline = true;
                     */
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("action", "signup");
                map.put("success", false);
                try {
                    sender.writeObject(map);
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void invite(String inviter, String invited) {
        int flag = 0;
        String n = inviter + " invited you to play a game";
        for (Player p : PlayersVector) {
            if (p.getUserID().equals(invited)) {
                flag = 1;
                try {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("action", "invite");
                    map.put("success", true);
                    map.put("msg", n);
                    map.put("inviter", inviter);
                    map.put("invited", invited);
                    p.identity.sender.writeObject(map);
                    //try {
                    // must be objecccct
                    //PlayerHandler test = (PlayerHandler)onlinePlayers.get(i).get("object");
                    //test.sender.writeObject(map);
                    //} catch (IOException ex) {
                    //Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    //}
                } catch (IOException ex) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        if (flag == 0) {
            try {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String r = invited + " is now offline";
                map.put("action", "invite");
                map.put("success", false);
                map.put("msg", r);
                map.put("inviter", inviter);
                map.put("invited", invited);

                sender.writeObject(map);
            } catch (IOException ex) {
                Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void inviteRes(HashMap<String, Object> res) {
        int flag = 0;
        if ((boolean) res.get("success")) {
            String n = res.get("invited") + " has accepted your invitation !";
            for (Player p : PlayersVector) {
                if (p.getUserID().equals(res.get("inviter"))) {
                    flag = 1;
                    try {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("action", "inviteRes");
                        map.put("success", true);
                        map.put("msg", n);
                        map.put("inviter", res.get("inviter"));
                        map.put("invited", res.get("invited"));
                        p.identity.sender.writeObject(map);
                        //try {
                        // must be objecccct
                        //PlayerHandler test = (PlayerHandler)onlinePlayers.get(i).get("object");
                        //test.sender.writeObject(map);
                        //} catch (IOException ex) {
                        //Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                        //}
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            for (Player p : PlayersVector) {
                if (p.getUserID().equals(res.get("inviter"))) {
                    try {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("action", "inviteRes");
                        map.put("success", false);
                        map.put("inviter", res.get("inviter"));
                        map.put("invited", res.get("invited"));

                        p.identity.sender.writeObject(map);
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }
    private void move(HashMap<String, Object> res) {
        String userID = (String) res.get("userID");
        for (Player p : PlayersVector) {
                if (p.getUserID().equals(userID)) {
                    try {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("action", "moveRes");
                        map.put("userID", userID);
                        map.put("pos", res.get("pos"));
                        p.identity.sender.writeObject(map);
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        
    }
}
