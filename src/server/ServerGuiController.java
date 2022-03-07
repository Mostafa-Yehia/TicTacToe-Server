/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import serverr.*;

/**
 *
 * @author sara
 */
public class ServerGuiController implements Initializable {

    Thread th;
    ConnectDB open;
    @FXML
    private TableView playersListTable;

    @FXML
    private TableColumn userNameColumn;

    @FXML
    private TableColumn scoreColoumn;

    @FXML
    private TableColumn statusColoumn;

    @FXML
    private Label label;
    @FXML
    private Button turnONBtn;
    @FXML
    private Button turnOFFBtn;

    @FXML
    public void turnOFF(ActionEvent event) throws IOException {
        turnOFFBtn.setDisable(true);
        turnONBtn.setDisable(false);

        //ServerGui.running = false;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("action", "close");
        for (Player p : PlayerHandler.PlayersVector) {
             if(p.getStatus()){
            p.identity.sender.writeObject(map);
             }
        }
        server.stopServer();

    }

    @FXML
    public void turnON(ActionEvent event) throws IOException, SQLException {
// System.out.println(serverr.PlayerHandler.onlinePlayers.get(0).get("userID"));

        //ServerGui.running = true;
        server.startServer();
        turnONBtn.setDisable(true);
        turnOFFBtn.setDisable(false);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("action", "open");
        open = new ConnectDB(map);
        System.out.println(PlayerHandler.PlayersVector);


        refresh();
    }

    public void refresh() {

        th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    userNameColumn.setCellValueFactory(new PropertyValueFactory<ListPlayers, String>("userName"));
                    scoreColoumn.setCellValueFactory(new PropertyValueFactory<ListPlayers, String>("score"));
                    statusColoumn.setCellValueFactory(new PropertyValueFactory<ListPlayers, String>("status"));

                    try {
                        playersListTable.setItems(getPlayers());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerGuiController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        th.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerGuiController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });
        th.start();
    }

    public ObservableList<ListPlayers> getPlayers() throws InterruptedException {

        ObservableList<ListPlayers> playersList = FXCollections.observableArrayList();
        //List<HashMap<String, Object>> temp = ConnectDB.Players;
        System.out.println(PlayerHandler.PlayersVector);

        for (Player p: PlayerHandler.PlayersVector) {
            String arg1 = p.getUserID().toString();
            String arg2 = String.valueOf(p.getPoints());
            String arg3 =String.valueOf(p.getStatus()) ;
            ListPlayers tmp = new ListPlayers(arg1, arg2, arg3);
            playersList.add(tmp);
        }

        return playersList;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
