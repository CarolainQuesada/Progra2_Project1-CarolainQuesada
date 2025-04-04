/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Usuario
 */
public class Board {
    private boolean[][] shipGrid = new boolean[10][10];

    public boolean hasShip(int row, int col) {
        return shipGrid[row][col];
    }

    public void placeShip(int row, int col, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            if (horizontal) {
                shipGrid[row][col + i] = true;
            } else {
                shipGrid[row + i][col] = true;
            }
        }
    }
}
