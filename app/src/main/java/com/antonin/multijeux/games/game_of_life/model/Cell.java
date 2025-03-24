package com.antonin.multijeux.games.game_of_life.model;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private boolean state;
    private int x;
    private int y;
    private Set<Cell> neighbours;

    // Constructeur avec coordonnées et état
    public Cell(int x, int y, boolean state) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.neighbours = new HashSet<>();
    }

    // Vérifier si la cellule est vivante
    public boolean isAlive() {
        return this.state;
    }

    // Changer l'état de la cellule
    public void setAlive(boolean state) {
        this.state = state;
    }

    // Ajouter un voisin
    public void addNeighbour(Cell cell) {
        this.neighbours.add(cell);
    }

    // Obtenir les voisins
    public Set<Cell> getNeighbours() {
        return neighbours;
    }

    // Obtenir les coordonnées
    public int getX() { return x; }
    public int getY() { return y; }
}
