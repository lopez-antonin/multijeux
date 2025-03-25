package com.antonin.multijeux.games.tic_tac_toe;

public class TicTacToeAI {
    private int size;
    private int alignToWin;
    private char bot = 'X';
    private char player = 'O';
    private TicTacToeLogic logic;

    private boolean playing;

    // Paramètre pour limiter la profondeur du minimax
    private static final int MAX_DEPTH = 4; // Limite de profondeur de recherche

    public TicTacToeAI(TicTacToeLogic logic) {
        this.logic = logic;
        this.size = logic.getMap().length;
        this.alignToWin = (size == 3) ? 3 : size;
    }

    private boolean hasWon(char joueur) {
        return logic.aGagner() == joueur;
    }

    private int evaluate() {
        if (hasWon(bot)) return 1000;
        if (hasWon(player)) return -1000;
        return 0;
    }

    private int[] findBlockingMove() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (logic.getMap()[i][j] == '\u0000') {
                    logic.getMap()[i][j] = player;
                    if (hasWon(player)) {
                        logic.getMap()[i][j] = '\u0000';
                        return new int[] {i, j};
                    }
                    logic.getMap()[i][j] = '\u0000';
                }
            }
        }
        return null;
    }

    private int[] findWinningMove() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (logic.getMap()[i][j] == '\u0000') {
                    logic.getMap()[i][j] = bot;
                    if (hasWon(bot)) {
                        logic.getMap()[i][j] = '\u0000';
                        return new int[] {i, j};
                    }
                    logic.getMap()[i][j] = '\u0000';
                }
            }
        }
        return null;
    }

    private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        int score = evaluate();
        if (score == 1000 || score == -1000 || depth >= MAX_DEPTH || logic.isGameBlocked()) {
            return score - depth;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (logic.getMap()[i][j] == '\u0000') {
                        logic.getMap()[i][j] = bot;
                        int currentScore = minimax(depth + 1, false, alpha, beta);
                        logic.getMap()[i][j] = '\u0000';
                        bestScore = Math.max(bestScore, currentScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (logic.getMap()[i][j] == '\u0000') {
                        logic.getMap()[i][j] = player;
                        int currentScore = minimax(depth + 1, true, alpha, beta);
                        logic.getMap()[i][j] = '\u0000';
                        bestScore = Math.min(bestScore, currentScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        }
    }

    public int[] findBestMove() {
        int[] blockMove = findBlockingMove();
        if (blockMove != null) return blockMove;

        int[] winMove = findWinningMove();
        if (winMove != null) return winMove;

        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (logic.getMap()[i][j] == '\u0000') {
                    logic.getMap()[i][j] = bot;
                    int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    logic.getMap()[i][j] = '\u0000';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    public void playBotMove() {
        if (logic.isWin() || logic.isGameBlocked()) return;

        this.playing = true;

        int[] bestMove = findBestMove();

        if (bestMove[0] != -1)
        {
            logic.setMap(bestMove[0], bestMove[1]);
        }else {
            for(int i = 0 ; i < this.logic.getMap().length ; i++)
            {
                for(int j = 0 ; j < this.logic.getMap().length ; j++)
                {
                    if(this.logic.getMap()[i][j] == '\u0000' && this.playing)
                    {
                        this.logic.setMap(i, j);
                        this.playing = false;
                        return;
                    }
                }
            }
        }

        this.playing = false;
    }

    public boolean isPlaying()
    {
        return this.playing;
    }
}
