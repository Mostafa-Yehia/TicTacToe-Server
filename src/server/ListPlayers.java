/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author sara
 */
public class ListPlayers {
  public String userName,status; 
  
  public String score;

    public ListPlayers(String userName, String score, String status) {
        this.userName = userName;
        this.status = status;
        
        this.score = score;
    }



    public String getStatus() {
        return status;
    }

    

    public String getScore() {
        return score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setStatus(String status) {
        this.status = status;
    }

    

    public void setScore(String score) {
        this.score = score;
    }
    
}
