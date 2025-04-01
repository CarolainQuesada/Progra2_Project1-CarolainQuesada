/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.mycompany.project_1.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Primary1Controller implements Initializable {


    @FXML
    private Button btnStartGamen;
    @FXML
    private Button btnInfo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickChangeToLobby(ActionEvent event) throws IOException {
        App.setRoot("lobby");
    }

    @FXML
    private void switchToInfo(ActionEvent event) throws IOException {
        App.setRoot("info");
    }

}
