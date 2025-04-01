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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class LobbyController implements Initializable {

    @FXML
    private ToggleGroup dificultad;
    @FXML
    private Button btnPlay;
    @FXML
    private RadioButton rbHard;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbEasy;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void comeBackToPrimary(ActionEvent event) throws IOException {
        App.setRoot("primary1");
    }

    @FXML
    private void changeToBoard(ActionEvent event)throws IOException {
        App.setRoot("board1");
    }
}

   
