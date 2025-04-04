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

    private boolean[][] occupiedCells = new boolean[10][10]; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (difficulty != GameDifficulty.EASY) {
            startTimer();
        } else {
            lblTimer.setText("00");
        }

        setupDragAndDrop(); 
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

    private void setupDragAndDrop() {
        
        setupDragEvents(acorazado, 4);
        setupDragEvents(crucero1, 3);
        setupDragEvents(crucero2, 3);
        setupDragEvents(destructor1, 2);
        setupDragEvents(destructor2, 2);
        setupDragEvents(destructor3, 2);
        setupDragEvents(submarino1, 1);
        setupDragEvents(submarino2, 1);
        setupDragEvents(submarino3, 1);
        setupDragEvents(submarino4, 1);

        gridPanePlayer.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof ImageView && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        gridPanePlayer.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage()) {
                ImageView draggedShip = (ImageView) event.getGestureSource();
                int col = (int) (event.getX() / (gridPanePlayer.getWidth() / 10));
                int row = (int) (event.getY() / (gridPanePlayer.getHeight() / 10));
                int shipSize = getShipSize(draggedShip);

                if (col + shipSize > 10 || isOccupied(row, col, shipSize)) {
                    event.setDropCompleted(false);
                    return;
                }
                markOccupied(row, col, shipSize, true);
                
                GridPane.setColumnIndex(draggedShip, col);
                GridPane.setRowIndex(draggedShip, row);
                GridPane.setColumnSpan(draggedShip, shipSize);
                GridPane.setRowSpan(draggedShip, 1);
                draggedShip.setRotate(0); 
                if (!gridPanePlayer.getChildren().contains(draggedShip)) {
                    gridPanePlayer.getChildren().add(draggedShip);
                }
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private void setupDragEvents(ImageView ship, int size) {
        ship.setOnDragDetected(event -> {
            Dragboard db = ship.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(ship.getImage());
            db.setContent(content);
            ship.setVisible(false);
            event.consume();
        });

        ship.setOnDragDone(event -> {
            ship.setVisible(true);
            event.consume();
        });
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
