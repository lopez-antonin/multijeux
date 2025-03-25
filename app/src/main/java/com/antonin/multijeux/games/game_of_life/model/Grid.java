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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grid grid = (Grid) obj;

        // Comparer les cellules de manière détaillée
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.cells[i][j].isAlive() != grid.cells[i][j].isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }


    // Nouvelle méthode pour cloner la grille
    public Grid clone() {
        Grid clonedGrid = new Grid(this.rows, this.cols, 0); // Crée une nouvelle grille

        // Clonage des cellules
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                clonedGrid.cells[i][j] = this.cells[i][j].clone(); // On suppose que Cell a aussi une méthode clone()
            }
        }
        return clonedGrid;
    }

    public void updateGrid() {
        boolean[][] newStates = new boolean[rows][cols]; // Tableau des nouveaux états

        // Calcul des nouveaux états sans modifier immédiatement les cellules
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int livingNeighbors = countLivingNeighbors(cells[i][j]);
                boolean isAlive = cells[i][j].isAlive();

                if (isAlive && (livingNeighbors < 2 || livingNeighbors > 3)) {
                    newStates[i][j] = false; // Meurt
                } else if (!isAlive && livingNeighbors == 3) {
                    newStates[i][j] = true; // Devient vivante
                } else {
                    newStates[i][j] = isAlive; // Ne change pas
                }
            }
        }

        // Appliquer les nouveaux états aux cellules existantes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].setAlive(newStates[i][j]);
            }
        }
    }

    // Compte les cellules vivantes autour d'une cellule donnée
    private int countLivingNeighbors(Cell cell) {
        int count = 0;
        for (Cell neighbor : cell.getNeighbours()) {
            if (neighbor.isAlive()) {
                count++;
            }
        }
        return count;
    }

    // Retourne la grille complète
    public Cell[][] getCells() {
        return cells;
    }
}

