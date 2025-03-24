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

    private TicTacToeAI ai;

    public TicTacToeLogic(int size, Context ctx, TicTacToeGame gameCtrl)
    {
        this.map = new char[size][size];
        this.tour = 0;
        this.estGagner = false;
        this.gameBlocked = false;
        this.ctx = ctx;
        this.gameCtrl = gameCtrl;
        this.ai = new TicTacToeAI(this);
    }

    public boolean setMap(int x, int y)
    {
        if(this.estGagner || this.gameBlocked)
        {
            return false;
        }

        char tourJoueur = (tour % 2 == 0) ? 'O' : 'X'; // Choix du joueur (O ou X)

        if(this.map[x][y] == '\u0000') // Si la case est vide
        {
            this.map[x][y] = tourJoueur; // Remplir la case avec le tour du joueur

            char gagnant = aGagner(); // Vérifier si quelqu'un a gagné

            if(gagnant != '\u0000')
            {
                this.estGagner = true; // Si un gagnant est trouvé
            }else{
                this.newTurn();
            }

            return true;
        }

        return false; // Si la case est déjà occupée, retourner faux
    }

    public char aGagner()
    {
        int nbCasesVerif = Math.min(4, this.map.length); // Toujours 4 max pour gagner

        for (int i = 0; i < this.map.length; i++)
        {
            int nbXHorizontale = 0, nbOHorizontale = 0;
            int nbXVerticale = 0, nbOVerticale = 0;

            for (int j = 0; j < this.map[i].length; j++)
            {

                if (this.map[i][j] == 'X')
                {
                    nbXHorizontale++;
                    nbOHorizontale = 0;
                } else if (this.map[i][j] == 'O')
                {
                    nbOHorizontale++;
                    nbXHorizontale = 0;
                } else {
                    nbXHorizontale = 0;
                    nbOHorizontale = 0;
                }

                // Vérification verticale
                if (this.map[j][i] == 'X')
                {
                    nbXVerticale++;
                    nbOVerticale = 0;
                } else if (this.map[j][i] == 'O')
                {
                    nbOVerticale++;
                    nbXVerticale = 0;
                } else {
                    nbXVerticale = 0;
                    nbOVerticale = 0;
                }

                // Vérification de victoire
                if (nbXHorizontale >= nbCasesVerif || nbXVerticale >= nbCasesVerif) return 'X';
                if (nbOHorizontale >= nbCasesVerif || nbOVerticale >= nbCasesVerif) return 'O';
            }
        }

        // Vérification diagonales
        for (int i = 0; i < this.map.length; i++)
        {
            for (int j = 0; j < this.map.length; j++)
            {
                if (checkDiagonale(i, j, nbCasesVerif, 'X')) return 'X';
                if (checkDiagonale(i, j, nbCasesVerif, 'O')) return 'O';
            }
        }

        return '\u0000'; // Aucun gagnant
    }


    private boolean checkDiagonale(int startX, int startY, int nbCasesVerif, char joueur)
    {
        int count1 = 0; // Diagonale principale (\)
        int count2 = 0; // Diagonale secondaire (/)

        for (int i = 0; i < nbCasesVerif; i++)
        {
            if (startX + i < this.map.length && startY + i < this.map.length && this.map[startX + i][startY + i] == joueur)
            {
                count1++;
            } else {
                count1 = 0;
            }

            if (startX + i < this.map.length && startY - i >= 0 && this.map[startX + i][startY - i] == joueur)
            {
                count2++;
            } else {
                count2 = 0;
            }

            if (count1 >= nbCasesVerif || count2 >= nbCasesVerif)
            {
                return true;
            }
        }
        return false;
    }


    public void newTurn()
    {
        int nbCasesPleines = 0;

        boolean isEmpty = true;

        for(int i = 0 ; i < this.map.length ; i++)
        {
            for(int j = 0 ; j < this.map.length ; j++)
            {
                // Comptage des cases pleines uniquement quand elles sont occupées
                if (this.map[i][j] == 'X' || this.map[i][j] == 'O')
                {
                    nbCasesPleines++;
                }
            }
        }

        // Vérification de l'égalité (match nul)
        if (nbCasesPleines == this.map.length * this.map.length)
        {
            this.gameBlocked = true; // Partie bloquée si la grille est pleine
        }

        this.gameCtrl.updateFrame();

        if(this.estGagner || this.gameBlocked)
        {
            return;
        }

        this.tour++;

        if(this.level == 0)
        {
            return;
        }

        if(this.level == 1 && this.tour%2 == 1)
        {
            while(isEmpty && !estGagner && !gameBlocked)//Choix aléatoire du bot si facile
            {
                int col = (int) Math.round(Math.random() * (this.map.length - 1));
                int lig = (int) Math.round(Math.random() * (this.map.length - 1));


                if(this.map[col][lig] == '\u0000')
                {
                    isEmpty = false;

                    this.setMap(col, lig);
                }
            }
            this.gameCtrl.updateFrame();
        }

        if(this.level == 2 && this.tour%2 == 1)
        {
            this.handleMedium();
            this.gameCtrl.updateFrame();
        }

        if(this.level == 3 && this.tour%2 == 1)
        {
            this.handleImpossible();
            this.gameCtrl.updateFrame();
        }
    }

    private void handleMedium()
    {
        boolean isEmpty = true;

        //50% de chance IA, 33% de chance random
        if(Math.round(Math.random() * 3) == 2)
        {
            while(isEmpty && !estGagner && !gameBlocked)//Choix aléatoire du bot si facile
            {
                int col = (int) Math.round(Math.random() * (this.map.length - 1));
                int lig = (int) Math.round(Math.random() * (this.map.length - 1));


                if(this.map[col][lig] == '\u0000')
                {
                    isEmpty = false;

                    this.setMap(col, lig);
                }
            }
        }else{
            this.ai.playBotMove();
        }
    }

    private void handleImpossible()
    {
        this.ai.playBotMove();
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

    public boolean isAIPlaying()
    {
        return this.ai.isPlaying();
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
