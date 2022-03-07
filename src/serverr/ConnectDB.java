/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;

import java.sql.*;
import java.util.HashMap;
import static serverr.PlayerHandler.PlayersVector;

/**
 *
 * @author Mostafa
 */
public class ConnectDB {

    String host = "jdbc:postgresql://ec2-52-214-125-106.eu-west-1.compute.amazonaws.com:5432/d5oblbt8qo54qb";
    String user = "qfsndldtcxcwfh";
    String pass = "990b6416509f1619e5c3e78ef09328c127874447d85a7f0525a4f0774f598aa6";

    public Connection con;
    public Statement stmt;
    public ResultSet rs;
    public Player player;
    public HashMap<String, Object> map = new HashMap<String, Object>();

    public ConnectDB(HashMap<String, Object> _map) throws SQLException {

        this.con = DriverManager.getConnection(host, user, pass);
        this.stmt = con.createStatement();

        if (_map.get("action") == "login") {
            //handle rs as login (select) statement
            rs = stmt.executeQuery("SELECT * FROM players WHERE userid = '" + _map.get("userID") + "';");

            if (rs.next() && rs.getString("pass").equals(_map.get("password"))) {
                map.put("action", "login");
                map.put("success", true);
                map.put("userID", rs.getString("userID"));
                map.put("password", rs.getString("pass"));
                map.put("name", rs.getString("name"));
                map.put("points", rs.getInt("points"));
            } else {
                map.put("success", false);
            }
            rs.close();
        }

        if (_map.get("action") == "signup") {
            //handle rs as register (insert) statement
            rs = stmt.executeQuery("SELECT * FROM players WHERE userid = '" + _map.get("userID") + "';");

            if (rs.next()) {
                map.put("action", "signup");
                map.put("success", false);
            } else {
                int result = stmt.executeUpdate("insert into players values ('" + _map.get("userID") + "','" + _map.get("name") + "','" + _map.get("password") + "',0);");
                if (result == 0) {
                    System.out.println("error");
                } else {
                    map.put("action", "signup");
                    map.put("success", true);
                }
            }
            rs.close();
        }
        if (_map.get("action") == "open") {
            //handle rs as register (insert) statement
            rs = stmt.executeQuery("SELECT * FROM players;");

            while (rs.next()) {
                player = new Player();
                PlayerHandler.PlayersVector.add(player);
                player.setUserID((String) rs.getString("userID"));
                player.setName((String) rs.getString("name"));
                player.setPoints(Integer.parseInt(rs.getString("points")));
                if (PlayerHandler.isOnline(player.getUserID())) {
                    player.setStatus(true);

                } else {
                    player.setStatus(false);
                }
            }
            rs.close();

        }

        /*
        if (_purpose == "edit") {
            //handle rs as edit (update) statement
            stmt.executeUpdate(_query);
        }
         */
        stmt.close();
        con.close();
    }

}


/*
public static void main(String args[]) {
        try {
            con = DriverManager.getConnection(host,user,pass);
            stmt = con.createStatement();
            //String sql = 
               /*"insert into players values ('sbahader','sherine','12345',5)";*/
 /*"CREATE TABLE players " +
               " (userID TEXT PRIMARY KEY, " +
               " NAME TEXT NOT NULL, " +
               " pass TEXT NOT NULL, " +
               " points INT NOT NULL) ";*//*
               
            rs = stmt.executeQuery( "SELECT * FROM players;" );
            while ( rs.next() ) {
               String id = rs.getString("userID");
               String name = rs.getString("name");
               String pass = rs.getString("pass");
               int points  = rs.getInt("points");
               System.out.println( "ID = " + id );
               System.out.println( "NAME = " + name );
               System.out.println( "PASS = " + pass );
               System.out.println( "POINTS = " + points );
            }
            rs.close();
               
               
            //stmt.executeUpdate(sql);
            stmt.close();
            con.close();
            System.out.println("Success");
        } catch (SQLException ex) {
            System.out.println("failed");
        }
}
}
 */
