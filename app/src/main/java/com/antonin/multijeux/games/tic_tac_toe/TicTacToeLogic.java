package com.antonin.multijeux.games.tic_tac_toe;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.antonin.multijeux.R;

public class TicTacToeLogic
{
    private char[][] map;
    private int tour;
    private boolean estGagner;
    private boolean gameBlocked;
    private Context ctx;

    private TicTacToeGame gameCtrl;
    private int level;

    public TicTacToeLogic(int size, Context ctx, TicTacToeGame gameCtrl)
    {
        this.map = new char[size][size];
        this.tour = 0;
        this.estGagner = false;
        this.ctx = ctx;
        this.gameCtrl = gameCtrl;
    }

    public boolean setMap(int x, int y)
    {
        if(this.estGagner || this.gameBlocked)
        {
            return false;
        }


        char tourJoueur = 'A';

        if(tour % 2 == 0)
        {
            tourJoueur = 'O';
        }else{
            tourJoueur = 'X';
        }

        if(this.map[x][y] == '\u0000') //Si vide
        {
            this.map[x][y] = tourJoueur;

            char gagnant = aGagner();

            if(gagnant != '\u0000')
            {
                this.estGagner = true;
            }else{
                this.newTurn();
            }
            return true;
        }

        return false;
    }

    public char aGagner()
    {
        int nbCasesVerif = this.map.length;

        int nbCasesPleines = 0;

        int nbXHorizontale = 0;
        int nbOHorizontale = 0;

        int nbXVerticale = 0;
        int nbOVerticale = 0;

        int nbXDiagonale = 0;
        int nbODiagonale = 0;


        if(nbCasesVerif > 4)
        {
            nbCasesVerif = 4;
        }

        //Verif horizontale
        for(int i = 0 ; i < this.map.length ; i++)
        {
            for(int j = 0 ; j < this.map[i].length ; j++)
            {
                if(this.map[i][j] == 'X')
                {
                    nbXHorizontale++;
                }else if(this.map[i][j] == 'O')
                {
                    nbOHorizontale++;
                }

                if(this.map[j][i] == 'X')
                {
                    nbXVerticale++;
                }else if(this.map[j][i] == 'O')
                {
                    nbOVerticale++;
                }

                if(this.map[j][j] == 'X')
                {
                    nbXDiagonale++;
                }else if(this.map[j][j] == 'O')
                {
                    nbODiagonale++;
                }

                if(nbOHorizontale >= nbCasesVerif || nbODiagonale >= nbCasesVerif || nbOVerticale >= nbCasesVerif)
                {
                    return 'O';
                }

                if(nbXHorizontale >= nbCasesVerif || nbXDiagonale >= nbCasesVerif || nbXVerticale >= nbCasesVerif)
                {
                    return 'X';
                }
            }

            nbCasesPleines += nbXHorizontale + nbOHorizontale;

            nbXHorizontale = 0;
            nbOHorizontale = 0;

            nbXVerticale = 0;
            nbOVerticale = 0;

            nbXDiagonale = 0;
            nbODiagonale = 0;
        }

        if(nbCasesPleines >= this.map.length * this.map.length)
        {
            this.gameBlocked = true;
        }

        return '\u0000';
    }

    private void newTurn()
    {
        this.tour++;

        if(this.level == 0)
        {
            return;
        }

        if(this.level == 1 && this.tour%2 == 1)
        {
            while(!this.setMap((int) (Math.random() * (this.map.length-1)), (int) (Math.round(Math.random() * (this.map.length-1))))){} //Choix aléatoire du bot si facile
            this.gameCtrl.updateFrame();
        }

        if(this.level == 2)
        {

        }
    }


    public char[][] getMap()
    {
        return this.map;
    }

    public boolean isWin()
    {
        return this.estGagner;
    }

    public boolean isGameBlocked()
    {
        return this.gameBlocked;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public char getJoueur()
    {
        char tourJoueur = 'A';

        if(tour % 2 == 0)
        {
            tourJoueur = 'O';
        }else{
            tourJoueur = 'X';
        }

        return tourJoueur;
    }
}
