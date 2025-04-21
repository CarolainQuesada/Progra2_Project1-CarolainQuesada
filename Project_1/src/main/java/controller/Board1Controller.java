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
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;

public class Board1Controller implements Initializable {

    @FXML private Label lblTimer;
    @FXML private GridPane gridPanePlayer;
    @FXML private GridPane gridPaneEnemy;

    @FXML private ImageView acorazado;
    @FXML private ImageView crucero1, crucero2;
    @FXML private ImageView destructor1, destructor2, destructor3;
    @FXML private ImageView submarino1, submarino2, submarino3, submarino4;

    @FXML private Label lblPlayer1;
    @FXML private Label lblPlayer2;
    @FXML private Label lblIndication;
    @FXML private Label lblTurnTimer;
    @FXML private Label lblIndication2;
    @FXML private Button btnToggleEnemyShips;
    @FXML private Button btnReady;

    @FXML private ImageView acorazado11;
    @FXML private ImageView crucero11, crucero22;
    @FXML private ImageView destructor11, destructor22, destructor33;
    @FXML private ImageView submarino11, submarino22, submarino33, submarino44;

    private Timeline timeline;
    private int timeRemaining;
    private static GameDifficulty difficulty;
    private Timeline turnTimer;
    private int turnTimeRemaining;
    //
    private boolean[][] occupiedCells = new boolean[10][10];
    private boolean[][] enemyOccupiedCells = new boolean[10][10];
    private boolean shipsPlaced = false;
    private boolean enemyShipsVisible = false;
    private ImageView[] enemyShips;
    //
    private boolean playerTurn = true;
    private boolean[][] playerOccupiedCells = new boolean[10][10];
    private Button[][] playerCells = new Button[10][10];
    private Button[][] enemyCells = new Button[10][10];


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblPlayer1.setText(LobbyController.playerName);
        lblTimer.setText("--");
        lblTurnTimer.setText("--");
        lblIndication.setText("Coloca los barcos y presiona Listo ");
        btnToggleEnemyShips.setText("Mostrar barcos");
        btnToggleEnemyShips.setDisable(true);

        setupDragAndDrop();
        placeEnemyShipsRandomly();
        initializeShootingLogic();
    }

  private void initializeShootingLogic() {
    for (int row = 0; row < 10; row++) {
        for (int col = 0; col < 10; col++) {
            Button cell = new Button();
            cell.setPrefSize(40, 40);

            int finalRow = row;
            int finalCol = col;

            cell.setOnAction(event -> {
                if (!playerTurn) return; 
                if (cell.getStyle().contains("-fx-background-color")) return;

                if (enemyOccupiedCells[finalRow][finalCol]) {
                    cell.setStyle("-fx-background-color: red");
                    lblIndication2.setText("¡Impacto! Dispara de nuevo.");
                } else {
                    cell.setStyle("-fx-background-color: blue");
                    lblIndication2.setText("¡Fallaste! Turno de la computadora.");
                    playerTurn = false;
                    startTurnTimer(); 
                    computerTurn(); 
                }
            });

            enemyCells[row][col] = cell;
            gridPaneEnemy.add(cell, col, row);
        }
    }

    for (int row = 0; row < 10; row++) {
        for (int col = 0; col < 10; col++) {
            Button cell = new Button();
            cell.setPrefSize(40, 40);
            playerCells[row][col] = cell;
            gridPanePlayer.add(cell, col, row);
        }
    }
}
      private void computerTurn() {
    Timeline computerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        if (playerTurn) return; // Si ya cambió el turno, cancela

        int row, col;
        do {
            row = (int)(Math.random() * 10);
            col = (int)(Math.random() * 10);
        } while (playerCells[row][col].getStyle().contains("-fx-background-color"));

        Button cell = playerCells[row][col];

        if (occupiedCells[row][col]) {
            cell.setStyle("-fx-background-color: red");
            lblIndication2.setText("¡La computadora acertó y sigue!");
            computerTurn(); // Sigue jugando si acierta
        } else {
            cell.setStyle("-fx-background-color: blue");
            lblIndication2.setText("¡La computadora falló! Tu turno.");
            playerTurn = true;
            startTurnTimer();
        }
    }));
    computerTimeline.setCycleCount(1);
    computerTimeline.play();
}

    public static void setDifficulty(GameDifficulty selectedDifficulty) {
        difficulty = selectedDifficulty;
    }

    @FXML
    private void handleReadyButton() {
        if (validateShipPlacement()) {
            shipsPlaced = true;
            btnReady.setDisable(true);
            btnToggleEnemyShips.setDisable(false);
            lblIndication.setText("Partida sin timepo límite");

            if (difficulty != GameDifficulty.EASY) {
                startTimer();
            }
            startTurnTimer();
        } else {
            lblIndication.setText("¡Debes colocar todos tus barcos primero!");
        }
    }

    private boolean validateShipPlacement() {
        int requiredCells = 4 + 3 + 3 + 2 + 2 + 2 + 1 + 1 + 1 + 1;
        int placedCells = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (occupiedCells[i][j]) {
                    placedCells++;
                }
            }
        }
        return placedCells == requiredCells;
    }

    private void startTimer() {
        if (difficulty == GameDifficulty.MEDIUM) {
            timeRemaining = 240;
        } else if (difficulty == GameDifficulty.HARD) {
            timeRemaining = 180;
        }

        lblTimer.setText(timeRemaining + "s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (shipsPlaced) {
                timeRemaining--;
                lblTimer.setText(timeRemaining + "s");
                if (timeRemaining <= 0) {
                    timeline.stop();
                    lblTimer.setText("¡Se acabó el tiempo!");
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void startTurnTimer() {
        if (!shipsPlaced) return;

        turnTimeRemaining = 20;
        lblIndication2.setText("Tiempo por tiro");
        lblTurnTimer.setText(turnTimeRemaining + "s");

        if (turnTimer != null) {
            turnTimer.stop();
        }

        turnTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (shipsPlaced) {
                turnTimeRemaining--;
                lblTurnTimer.setText(turnTimeRemaining + "s");

                if (turnTimeRemaining <= 0) {
                    turnTimeRemaining = 20;
                    lblTurnTimer.setText(turnTimeRemaining + "s");
                }
            }
        }));
        turnTimer.setCycleCount(Timeline.INDEFINITE);
        turnTimer.play();
    }

    private void placeEnemyShipsRandomly() {
        enemyShips = new ImageView[] {
            acorazado11, crucero11, crucero22,
            destructor11, destructor22, destructor33,
            submarino11, submarino22, submarino33, submarino44
        };

        int[] sizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (int i = 0; i < enemyShips.length; i++) {
            int size = sizes[i];
            ImageView ship = enemyShips[i];
            ship.setPreserveRatio(true);
            ship.setRotate(0);
            placeEnemyShip(ship, size, false);
        }

        updateEnemyShipVisibility();
    }

    private void placeEnemyShip(ImageView ship, int size, boolean vertical) {
        boolean placed = false;

        while (!placed) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * (11 - size));

            if (canPlaceEnemyShip(row, col, size, false)) {
                for (int i = 0; i < size; i++) {
                    enemyOccupiedCells[row][col + i] = true;
                }

                gridPaneEnemy.add(ship, col, row, size, 1);
                placed = true;
            }
        }
    }

    private boolean canPlaceEnemyShip(int row, int col, int size, boolean vertical) {
        for (int i = 0; i < size; i++) {
            int c = col + i;
            if (c >= 10 || enemyOccupiedCells[row][c]) return false;
        }
        return true;
    }

    @FXML
    private void toggleEnemyShipsVisibility() {
        enemyShipsVisible = !enemyShipsVisible;
        updateEnemyShipVisibility();
        btnToggleEnemyShips.setText(enemyShipsVisible ? "Ocultar barcos" : "Mostrar barcos");
    }

    private void updateEnemyShipVisibility() {
        if (enemyShips == null) return;
        for (ImageView ship : enemyShips) {
            ship.setVisible(enemyShipsVisible);
        }
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

        gridPanePlayer.setOnDragOver(event -> handleDragOver(event));
        gridPanePlayer.setOnDragDropped(event -> handleDragDropped(event));
    }

    private void setupShipDragEvents(ImageView ship, int size) {
        ship.setOnDragDetected(event -> startDrag(event, ship));
        ship.setOnDragDone(event -> endDrag(event, ship));
    }

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
                if (validateShipPlacement()) {
                    lblIndication.setText("Todos los barcos colocados. Presiona 'Listo'");
                }
            } else {
                event.setDropCompleted(false);
            }
        } else {
            event.setDropCompleted(false);
        }
        event.consume();
    }

    private void startDrag(MouseEvent event, ImageView ship) {
        Dragboard db = ship.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();

        WritableImage snapshot = new WritableImage(
            (int) ship.getBoundsInLocal().getWidth(),
            (int) ship.getBoundsInLocal().getHeight()
        );
        ship.snapshot(null, snapshot);

        content.putImage(snapshot);
        db.setContent(content);
        ship.setOpacity(0.5);
        event.consume();
    }

    private void endDrag(DragEvent event, ImageView ship) {
        ship.setOpacity(1.0);
        event.consume();
    }

    private int calculateColumn(DragEvent event) {
        double cellWidth = gridPanePlayer.getWidth() / 10;
        return (int) (event.getX() / cellWidth);
    }

    private int calculateRow(DragEvent event) {
        double cellHeight = gridPanePlayer.getHeight() / 10;
        return (int) (event.getY() / cellHeight);
    }

    private boolean isValidDropPosition(int row, int col, int shipSize) {
        return col + shipSize <= 10 && !isOccupied(row, col, shipSize);
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
            occupiedCells[row][col + i] = status;
        }
    }

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

    private int getShipSize(ImageView ship) {
        if (ship == acorazado || ship == acorazado11) return 4;
        if (ship == crucero1 || ship == crucero2 || ship == crucero11 || ship == crucero22) return 3;
        if (ship == destructor1 || ship == destructor2 || ship == destructor3 || 
            ship == destructor11 || ship == destructor22 || ship == destructor33) return 2;
        return 1;
    }
}
