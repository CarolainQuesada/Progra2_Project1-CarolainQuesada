/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.GameDifficulty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Board1Controller implements Initializable {

    @FXML 
    private Label lblTimer;
    @FXML 
    private GridPane gridPanePlayer;
    @FXML 
    private GridPane gridPaneEnemy;

    @FXML 
    private ImageView acorazado;
    @FXML 
    private ImageView crucero1, crucero2;
    @FXML 
    private ImageView destructor1, destructor2, destructor3;
    @FXML 
    private ImageView submarino1, submarino2, submarino3, submarino4;

    private Timeline timeline;
    private int timeRemaining;
    private static GameDifficulty difficulty;

    private boolean[][] occupiedCells = new boolean[10][10]; // Matriz de ocupación
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (difficulty != GameDifficulty.EASY) {
            startTimer();
        } else {
            lblTimer.setText("00");
        }

        setupDragAndDrop(); // Activar arrastrar y soltar
    }

    public void startTimer() {
        if (difficulty == GameDifficulty.MEDIUM) {
            timeRemaining = 120;
        } else if (difficulty == GameDifficulty.HARD) {
            timeRemaining = 60;
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            lblTimer.setText(timeRemaining + "s");
            if (timeRemaining <= 0) {
                timeline.stop();
                lblTimer.setText("Se acabó el tiempo!!!");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void setDifficulty(GameDifficulty selectedDifficulty) {
        difficulty = selectedDifficulty;
    }

    private boolean isOccupied(int row, int col, int size) {
        for (int i = 0; i < size; i++) {
            int c = col + i;
            if (c >= 10 || occupiedCells[row][c]) return true;
        }
        return false;
    }

    private void markOccupied(int row, int col, int size, boolean status) {
        for (int i = 0; i < size; i++) {
            int c = col + i;
            if (c < 10) occupiedCells[row][c] = status;
        }
    }

    private int getShipSize(ImageView ship) {
        if (ship == acorazado) return 4;
        if (ship == crucero1 || ship == crucero2) return 3;
        if (ship == destructor1 || ship == destructor2 || ship == destructor3) return 2;
        if (ship == submarino1 || ship == submarino2 || ship == submarino3 || ship == submarino4) return 1;
        return 1;
    }

}
