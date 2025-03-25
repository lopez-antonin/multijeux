package com.antonin.multijeux.games.game_of_life.model;

public class Grid
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private int      rows ;
    private int      cols ;

    private Cell[][] cells;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public Grid(int rows, int cols, double density)
    {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        // Cellules
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                cells[i][j] = new Cell(i, j, Math.random() < density);
            }
        }

        // Voisines
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                for (int di = -1; di <= 1; di++)
                {
                    for (int dj = -1; dj <= 1; dj++)
                    {
                        if (di != 0 || dj != 0)
                        {
                            int ni = (i + di + rows) % rows;
                            int nj = (j + dj + cols) % cols;
                            cells[i][j].addNeighbour(cells[ni][nj]);
                        }
                    }
                }
            }
        }
    }




    // +-----------+
    // | OVERRIDES |
    // +-----------+

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grid grid = (Grid) obj;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.cells[i][j].isAlive() != grid.cells[i][j].isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }




    // +---------+
    // | GETTERS |
    // +---------+

    public Cell[][] getCells() { return cells; }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    private int countLivingNeighbors(Cell cell)
    {
        int count = 0;
        for (Cell neighbor : cell.getNeighbours())
        {
            if (neighbor.isAlive())
            {
                count++;
            }
        }
        return count;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    public Grid clone()
    {
        Grid clonedGrid = new Grid(this.rows, this.cols, 0);

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                clonedGrid.cells[i][j] = this.cells[i][j].clone();
            }
        }

        return clonedGrid;
    }



    public void updateGrid()
    {
        boolean[][] newStates = new boolean[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                int livingNeighbors = countLivingNeighbors(cells[i][j]);
                boolean isAlive = cells[i][j].isAlive();

                if (isAlive && (livingNeighbors < 2 || livingNeighbors > 3))
                {
                    newStates[i][j] = false; // Meurt
                }
                else if (!isAlive && livingNeighbors == 3)
                {
                    newStates[i][j] = true; // Vie
                }
                else
                {
                    newStates[i][j] = isAlive; // Ne change pas
                }
            }
        }

        // Appliquer les nouveaux états
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                cells[i][j].setAlive(newStates[i][j]);
            }
        }
    }
}

