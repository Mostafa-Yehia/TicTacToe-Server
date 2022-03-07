/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverr;


/**
 *
 * @author mostafa
 */
public class Player {
        
    private String userID;
    private String name;
    //private String pass;
    private int points;
    
    private String[] games;
    
    private boolean status;
    private boolean availability;
    
    public PlayerHandler identity;
        
    
    
    
//    public Player () {
//        points = 0;
//    }
    

    
    public void setIdentity (PlayerHandler _identity) {
        identity = _identity;
    }
    public PlayerHandler getIdentity () {
        return identity;
    }
    
    
    
    public void setUserID (String _userID) {
        userID = _userID;
    }
    public String getUserID () {
        return userID;
    }
    
    
    public void setName (String _name) {
        name = _name;
    }
    public String getName () {
        return name;
    }
    
    /*
    public void setPass (String _pass) {
        pass = _pass;
    }
    public String getPass () {
        return pass;
    }
    */
    
    public void setPoints (int _points) {
        points = _points;
    }
    public int getPoints () {
        return points;
    }
    
    
    
    
    public void addGame (String _gameID) {
        games[games.length+1] = _gameID;
    }
    public String[] getGames () {
        return games;
    }
    

    
    
    public void setStatus (boolean _status) {
        //set established socket client-server true (online) or false (offline);
        status = _status;
    }
    public boolean getStatus () {
        //return true (online) or false (offline);
        return status;
    }
    
    
    
    public void setAvailability (boolean _availability) {
        //set established socket client-server && !client-client true (invitable) or false (not invitable);
        availability = _availability;
    }
    public boolean getAvailability () {
        //return true (invitable) or false (not invitable);
        return availability;
    }
}


