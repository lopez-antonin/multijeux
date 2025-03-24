package com.antonin.multijeux.games.curve_tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * CurveTrackingView est une SurfaceView personnalisée qui affiche un chemin circulaire
 * et suit les mouvements du joueur le long de celui-ci. La vue permet au joueur de naviguer
 * le long du chemin et de visualiser sa trajectoire, tout en le pénalisant s'il quitte
 * le chemin. Elle gère également la boucle de jeu, le dessin du chemin et des éléments du joueur,
 * et contrôle l'état du jeu.
 *
 * <p>
 * Cette classe implémente {@link SurfaceHolder.Callback} pour gérer la Surface sous-jacente et
 * {@link Runnable} pour exécuter la boucle de jeu sur un thread séparé.
 * </p>
 */
public class CurveTrackingView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private int           level                   ;

    private float         playerX                 ;
    private float         playerY                 ;
    private float         pathStrokeWidth         ;
    private float         playerStrokeWidth       ;
    private float         startPlayerX            ;
    private float         startPlayerY            ;
    private float         distanceTraveled        ;

    private boolean       canDraw           = true;
    private boolean       playerPathVisible = true;
    private boolean       isRunning               ;

    private Thread        gameThread              ;

    private SurfaceHolder holder                  ;

    private Paint         pathPaint               ;
    private Paint         playerPaint             ;

    private Path          path                    ;
    private Path          playerPath              ;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    /**
     * Constructeur pour la classe CurveTrackingView.
     * Initialise la vue et configure les composants de dessin.
     *
     * @param context Le contexte dans lequel la vue est exécutée.
     * @param level   Le niveau de difficulté du jeu. Cela affecte l'épaisseur des chemins.
     *                Plus le niveau est élevé, plus les chemins sont fins. L'épaisseur est calculée comme suit : 50 - niveau * 10.
     *                Par exemple, niveau 1 : 40, niveau 2 : 30, niveau 3 : 20, niveau 4 : 10, niveau 5 : 0.
     *                Le niveau doit être compris entre 0 et 5. Si le niveau est 0, l'épaisseur sera de 50. Si le niveau est supérieur à 5, le chemin aura une épaisseur égale à 0.
     */
    public CurveTrackingView(Context context, int level)
    {
        super(context);

        this.level = level;

        holder = getHolder();
        holder.addCallback(this);

        // Path
        this.pathStrokeWidth = 50 - this.level * 10;
        pathPaint = new Paint();
        path      = new Path() ;
        pathPaint.setColor      (0xFFA63A50)        ;
        pathPaint.setStrokeWidth(pathStrokeWidth)   ;
        pathPaint.setStyle      (Paint.Style.STROKE);

        // Player path
        this.playerStrokeWidth = 50 - this.level * 10;
        playerPaint = new Paint();
        playerPath  = new Path() ;
        playerPaint.setColor      (0xFFBA6E6E)        ;
        playerPaint.setStrokeWidth(playerStrokeWidth) ;
        playerPaint.setStyle      (Paint.Style.STROKE);
    }




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    /**
     * Cette méthode est appelée immédiatement après la première création de la surface.
     * Elle initialise l'état du jeu, y compris la configuration du chemin circulaire,
     * le positionnement du joueur et le démarrage du thread du jeu.
     *
     * @param holder Le SurfaceHolder dont la surface est en cours de création.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        int w       = getWidth ()       ;
        int h       = getHeight()       ;
        int centerX = w              / 2;
        int centerY = h              / 2;
        int radius  = Math.min(w, h) / 3;

        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CW);

        this.startPlayerX = centerX         ;
        this.startPlayerY = centerY - radius;
        playerX = startPlayerX;
        playerY = startPlayerY;

        playerPath.moveTo(playerX, playerY);

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        isRunning = false;

        try
        {
            if (gameThread != null)
            {
                gameThread.join();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void run()
    {
        while (isRunning)
        {
            if (!holder.getSurface().isValid())
            {
                continue;
            }

            Canvas canvas = holder.lockCanvas();
            if (canvas != null)
            {
                drawGame(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            if (getContext() instanceof CurveTrackingGame)
            {
                CurveTrackingGame gameActivity = (CurveTrackingGame) getContext();
                if (gameActivity.getGameManager().isGameCompleted(this))
                {
                    break;
                }
            }

            try
            {
                Thread.sleep(8); // 120 FPS
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }




    // +---------+
    // | GETTERS |
    // +---------+

    public float getPlayerX()
    {
        return this.playerX;
    }

    public float getPlayerY()
    {
        return this.playerY;
    }

    public float getStartPlayerX() {return this.startPlayerX;}

    public float getStartPlayerY() {return this.startPlayerY;}

    public double getDistanceTraveled() {return this.distanceTraveled;}

    public int getLevel() {return this.level;}




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    /**
     * Dessine les éléments du jeu sur le canevas fourni.
     *
     * Cette méthode est responsable du rendu du tracé statique du jeu, de la couleur de fond
     * et, en option, du tracé du joueur, s'il est configuré pour être visible.
     *
     * @param canvas L'objet Canvas sur lequel dessiner. Il est généralement fourni par la
     *               méthode onDraw() de la View.
     *
     * @throws NullPointerException Si le canevas (canvas) est nul.
     *
     * @see Canvas#drawColor(int)
     * @see Canvas#drawPath(Path, Paint)
     */
    private void drawGame(Canvas canvas)
    {
        canvas.drawColor(0xFFF0E7D8);
        canvas.drawPath(path, pathPaint);
        if (playerPathVisible)
        {
            canvas.drawPath(playerPath, playerPaint);
        }
    }



    /**
     * Vérifie si un point donné (x, y) se trouve à l'extérieur de la zone du chemin défini.
     * La zone du chemin est définie comme un anneau circulaire (couronne) centré au milieu de la vue.
     * Les rayons intérieur et extérieur de l'anneau sont déterminés par les dimensions de la vue, le `pathStrokeWidth` et le `playerStrokeWidth`.
     *
     * @param x La coordonnée x du point à vérifier.
     * @param y La coordonnée y du point à vérifier.
     * @return True si le point est à l'extérieur de la zone du chemin, false sinon.
     *         Un point est considéré comme étant à l'extérieur s'il est soit trop éloigné du centre (au-delà du rayon extérieur),
     *         soit trop proche du centre (à l'intérieur du rayon intérieur).
     */
    private boolean isOutsidePath(float x, float y)
    {
        int centerX = getWidth () / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        return distance > radius + this.pathStrokeWidth / 2 + this.playerStrokeWidth / 2 || distance < radius - this.pathStrokeWidth / 2 - this.playerStrokeWidth / 2;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    /**
     * Arrête le processus de dessin.
     *
     * Cette méthode met le flag interne `canDraw` à `false`,
     * empêchant effectivement toute opération de dessin supplémentaire d'être exécutée.
     * Toute tentative ultérieure de dessin sera ignorée jusqu'à ce que `canDraw` soit
     * remis à `true`.
     */
    public void stopDrawing()
    {
        canDraw = false;
    }



    /**
     * Inverse la visibilité du chemin du joueur.
     *
     * Cette méthode inverse l'état actuel de l'indicateur 'playerPathVisible'.
     * Si le chemin du joueur était visible, il deviendra caché, et inversement.
     * Ceci est généralement utilisé pour afficher ou masquer une représentation visuelle
     * de l'historique des mouvements du joueur ou de son itinéraire prévu dans un jeu
     * ou une simulation.
     */
    public void togglePlayerPathVisibility()
    {
        playerPathVisible = !playerPathVisible;

        Canvas canvas = holder.lockCanvas();
        if (canvas != null)
        {
            drawGame(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }



    /**
     * Met à jour la position et le tracé du joueur en fonction des déplacements donnés.
     *
     * Cette méthode déplace le joueur dans la zone de jeu et enregistre son tracé.
     * Elle vérifie également si le joueur sort du tracé défini et ajuste le score en conséquence.
     *
     * @param dx Variation de la coordonnée x (déplacement horizontal).
     * @param dy Variation de la coordonnée y (déplacement vertical).
     *
     * Comportement :
     * 1. **Vérification de dessin:** Si `canDraw` est faux, la méthode s'arrête.
     * 2. **Seuil de mouvement:** Si `dx` et `dy` sont inférieurs à 0.5 en valeur absolue, la méthode s'arrête (pour éviter les micro-mouvements).
     * 3. **Mise à jour de la position:** Les coordonnées `playerX` et `playerY` sont mises à jour.
     * 4. **Vérification de sortie de tracé:** `isOutsidePath` est appelée pour détecter une sortie de tracé. Si c'est le cas, `updateScore` du `GameManager` est appelée via l'activité `CurveTrackingGame` avec une pénalité de 0.1.
     * 5. **Mise à jour du tracé:** `playerPath` est mis à jour en ajoutant un segment de la position précédente à la nouvelle position (`playerX`, `playerY`).
     *
     * Remarque : `playerX`, `playerY`, `playerPath`, `isOutsidePath`, `getContext`, `CurveTrackingGame`, et `getGameManager` sont supposés être des membres de la classe.
     */
    public void updateMovement(float dx, float dy)
    {
        if (!canDraw)
        {
            return;
        }

        if (Math.abs(dx) < 0.5f && Math.abs(dy) < 0.5f)
        {
            return;
        }

        playerX += dx;
        playerY += dy;

        if (isOutsidePath(playerX, playerY))
        {
            CurveTrackingGame gameActivity = (CurveTrackingGame) getContext();
            gameActivity.getGameManager().updateScore(0.1);
        }

        playerPath.lineTo(playerX, playerY);

        distanceTraveled += Math.sqrt(dx * dx + dy * dy);
    }
}
