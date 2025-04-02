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

public class Board1Controller implements Initializable {

    @FXML
    private Label lblTimer;

    private Timeline timeline;
    private int timeRemaining; // Tiempo en segundos

    private static GameDifficulty difficulty; // Se establece desde LobbyController

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (difficulty != GameDifficulty.EASY) { // Solo iniciar el temporizador si no es fácil
            startTimer();
        } else {
            lblTimer.setText("∞"); // Mostrar infinito en fácil
        }
    }

    public void startTimer() {
        if (difficulty == GameDifficulty.MEDIUM) {
            timeRemaining = 120; // 2 minutos
        } else if (difficulty == GameDifficulty.HARD) {
            timeRemaining = 60; // 1 minuto
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            lblTimer.setText(timeRemaining + "s");

            if (timeRemaining <= 0) {
                timeline.stop();
                lblTimer.setText("Time's up!");
                // Aquí puedes añadir lógica para finalizar el juego
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Método para recibir la dificultad desde LobbyController
    public static void setDifficulty(GameDifficulty selectedDifficulty) {
        difficulty = selectedDifficulty;
    }
}
