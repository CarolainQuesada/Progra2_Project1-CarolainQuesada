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
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

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
    @FXML
    private Label lblPlayer1;
    @FXML
    private Label lblPlayer2;
    @FXML
    private Label lblIndication;
    @FXML
    private Label lblTurnTimer;
    @FXML
    private Label lblIndication2;

    //variables
    private Timeline timeline;
    private int timeRemaining;
    private static GameDifficulty difficulty;
    private Timeline turnTimer;
    private int turnTimeRemaining;
    private boolean[][] occupiedCells = new boolean[10][10]; 
    

    @Override
public void initialize(URL url, ResourceBundle rb) {
    lblPlayer1.setText(LobbyController.playerName); 

    if (difficulty == GameDifficulty.EASY) {
        lblTimer.setText("00");
        lblIndication.setText("Partida sin tiempo");
    } else {
        lblIndication.setText("¡Rápido, el tiempo corre!"); 
        startTimer();
    }

    setupDragAndDrop();
    startTurnTimer();
}

    public static void setDifficulty(GameDifficulty selectedDifficulty) {
        difficulty = selectedDifficulty;
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
                lblTimer.setText("¡Se acabó el tiempo!");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
private void startTurnTimer() {
    turnTimeRemaining = 15;
    lblIndication2.setText("Tiempo por tiro");
    lblTurnTimer.setText(turnTimeRemaining + "s");

    if (turnTimer != null) {
        turnTimer.stop();
    }

    turnTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        turnTimeRemaining--;
        lblTurnTimer.setText(turnTimeRemaining + "s");

        if (turnTimeRemaining <= 0) {
            turnTimeRemaining = 15; 
            lblTurnTimer.setText(turnTimeRemaining + "s");
        }
    }));
    turnTimer.setCycleCount(Timeline.INDEFINITE);
    turnTimer.play();
}
 private void setupDragAndDrop() {
    setupShipDragEvents(acorazado, 4);
    setupShipDragEvents(crucero1, 3);
    setupShipDragEvents(crucero2, 3);
    setupShipDragEvents(destructor1, 2);
    setupShipDragEvents(destructor2, 2);
    setupShipDragEvents(destructor3, 2);
    setupShipDragEvents(submarino1, 1);
    setupShipDragEvents(submarino2, 1);
    setupShipDragEvents(submarino3, 1);
    setupShipDragEvents(submarino4, 1);

    // Configura el evento de (arrastrar sobre) el tablero
    gridPanePlayer.setOnDragOver(event -> handleDragOver(event));

    // Configura el evento de (soltar) sobre el tablero
    gridPanePlayer.setOnDragDropped(event -> handleDragDropped(event));
}

// Configura los eventos de arrastre para un barco específico
private void setupShipDragEvents(ImageView ship, int size) {
    ship.setOnDragDetected(event -> startDrag(event, ship));
    ship.setOnDragDone(event -> endDrag(event, ship));
}

// Maneja el evento de (arrastrar sobre) el tablero
private void handleDragOver(DragEvent event) {
    if (event.getGestureSource() instanceof ImageView && event.getDragboard().hasImage()) {
        event.acceptTransferModes(TransferMode.MOVE);
    }
    event.consume();
}

private void handleDragDropped(DragEvent event) {
    if (!(event.getGestureSource() instanceof ImageView)) return;

    Dragboard db = event.getDragboard();
    if (db.hasImage()) {
        ImageView draggedShip = (ImageView) event.getGestureSource();

        int col = calculateColumn(event);
        int row = calculateRow(event);

        int shipSize = getShipSize(draggedShip);

        if (isValidDropPosition(row, col, shipSize)) {
            placeShip(draggedShip, col, row, shipSize);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
    } else {
        event.setDropCompleted(false);
    }
    event.consume();
}

// Inicia el arrastre de un barco
private void startDrag(MouseEvent event, ImageView ship) {
    Dragboard db = ship.startDragAndDrop(TransferMode.MOVE);
    ClipboardContent content = new ClipboardContent();
    content.putImage(ship.getImage());
    db.setContent(content);
    ship.setVisible(false); 
    event.consume();
}

// Finaliza el arrastre del barco
private void endDrag(DragEvent event, ImageView ship) {
    ship.setVisible(true);
    event.consume();
}

// Calcula la columna donde se soltó el barco
private int calculateColumn(DragEvent event) {
    double cellWidth = gridPanePlayer.getWidth() / 10;
    return (int) (event.getX() / cellWidth);
}

// Calcula la fila donde se soltó el barco
private int calculateRow(DragEvent event) {
    double cellHeight = gridPanePlayer.getHeight() / 10;
    return (int) (event.getY() / cellHeight);
}

// Verifica si la posición es válida
private boolean isValidDropPosition(int row, int col, int shipSize) {
    if (col + shipSize > 10 || isOccupied(row, col, shipSize)) {
        return false;
    }
    return true;
}

// Coloca el barco en el tablero
private void placeShip(ImageView draggedShip, int col, int row, int shipSize) {
    markOccupied(row, col, shipSize, true);

    GridPane.setColumnIndex(draggedShip, col);
    GridPane.setRowIndex(draggedShip, row);
    GridPane.setColumnSpan(draggedShip, shipSize);
    GridPane.setRowSpan(draggedShip, 1);
    draggedShip.setRotate(0);

    if (!gridPanePlayer.getChildren().contains(draggedShip)) {
        gridPanePlayer.getChildren().add(draggedShip);
    }
}

// Verifica si hay colisiones
private boolean isOccupied(int row, int col, int size) {
    for (int i = 0; i < size; i++) {
        int c = col + i;
        if (c >= 10 || occupiedCells[row][c]) return true;
    }
    return false;
}

// Marca las celdas como ocupadas o libres
private void markOccupied(int row, int col, int size, boolean status) {
    for (int i = 0; i < size; i++) {
        int c = col + i;
        if (c < 10) occupiedCells[row][c] = status;
    }
}

// Devuelve el tamaño según el barco
private int getShipSize(ImageView ship) {
    if (ship == acorazado) return 4;
    if (ship == crucero1 || ship == crucero2) return 3;
    if (ship == destructor1 || ship == destructor2 || ship == destructor3) return 2;
    if (ship == submarino1 || ship == submarino2 || ship == submarino3 || ship == submarino4) return 1;
    return 1;
}

   
}