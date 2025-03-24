package com.antonin.multijeux.games.game_of_life.model;

public class Grid {
    private int rows;
    private int cols;
    private Cell[][] cells;

    // Constructeur
    public Grid(int rows, int cols, double density) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        // Initialisation des cellules
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j, Math.random() < density);
            }
        }

        // Ajout des voisins avec bords toriques
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di != 0 || dj != 0) { // Ne pas s'ajouter soi-même
                            int ni = (i + di + rows) % rows; // Gestion torique
                            int nj = (j + dj + cols) % cols;
                            cells[i][j].addNeighbour(cells[ni][nj]);
                        }
                    }
                }
            }
        }
    }

    // Récupérer une cellule à des coordonnées précises
    public Cell getCell(int i, int j) {
        if (i >= 0 && i < rows && j >= 0 && j < cols) {
            return cells[i][j];
        }
        return null;
    }

    // Mettre à jour l'état d'une cellule
    public void setCellState(int i, int j, boolean isAlive) {
        if (getCell(i, j) != null) {
            cells[i][j].setAlive(isAlive);
        }
    }

    // Retourner la grille complète
    public Cell[][] getCells() {
        return cells;
    }
}
