/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Usuario
 */
public class Ship {
    private int size;
    private boolean isSunk = false;

    public Ship(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void hit() {
        size--;
        if (size == 0) {
            isSunk = true;
        }
    }
}
