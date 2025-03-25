package com.antonin.multijeux.games.game_of_life.model;

import java.util.HashSet;
import java.util.Set;

public class Cell
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private boolean   state     ;
    private int       x         ;
    private int       y         ;
    private Set<Cell> neighbours;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public Cell(int x, int y, boolean state)
    {
        this.x          = x              ;
        this.y          = y              ;
        this.state      = state          ;
        this.neighbours = new HashSet<>();
    }




    // +---------+
    // | GETTERS |
    // +---------+

    public boolean isAlive() { return this.state; }

    public Set<Cell> getNeighbours() { return neighbours; }




    // +---------+
    // | SETTERS |
    // +---------+

    public void setAlive(boolean state) { this.state = state; }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    public void addNeighbour(Cell cell) { this.neighbours.add(cell); }



    public Cell clone()
    {
        Cell clonedCell = new Cell(this.x, this.y, this.state);
        return clonedCell;
    }
}
