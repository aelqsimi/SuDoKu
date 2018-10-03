package com.ximisoft.sudoku;

/**
 * Created by abdel on 12/11/17.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Grille extends View {

    private int screenWidth;
    private int screenHeight;
    private int n,diff = 0;
    boolean verify= false;

    private Paint paint1;   // Pour dessiner la grille (lignes noires)
    private Paint paint2;   // Pour le texte des cases fixes
    private Paint paint3;   // Pour dessiner les lignes rouges (grosse)
    private Paint paint4;   // Pour le texte noir des cases a modifier
    private Paint paint5;
    private Paint paint6;


    private int[][] matrix = new int[9][9];
    private boolean[][] fixIdx = new boolean[9][9];

    public Grille(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grille(Context context) {
        super(context);
        init();
    }

    private void init() {
        //Grille de depart
        set("000105000140000670080002400063070010900000003010090520007200080026000035000409000");

        // Grille Gagnante
        //set("672145398145983672389762451263574819958621743714398526597236184426817935831459267");

        // Grille Perdante
        //set("672145198145983672389762451263574819958621743714398526597236184426817935831459267");

        paint1 = new Paint();
        paint1.setAntiAlias(true);
        // Couleur noire
        paint1.setColor(Color.BLACK);
        paint1.setTextSize(60);
        paint1.setTextAlign(Paint.Align.CENTER);


        paint2 = new Paint();
        paint2.setAntiAlias(true);
        // Couleur rouge
        paint2.setColor(Color.RED);
        // Taille du texte
        paint2.setTextSize(60);
        // Centre le texte
        paint2.setTextAlign(Paint.Align.CENTER);

        paint3 = new Paint();
        paint3.setAntiAlias(true);
        // Couleur rouge et grosses lignes
        paint3.setColor(Color.RED);
        paint3.setStrokeWidth(7);

        // Paint 4 ?
        paint4 = new Paint();
        paint4.setAntiAlias(true);
        // Couleur noire
        paint4.setColor(Color.BLACK);
        paint4.setStrokeWidth(5);
        paint4.setStyle(Paint.Style.STROKE);

        paint4 = new Paint();
        paint4.setAntiAlias(true);
        // Couleur noire
        paint4.setColor(Color.BLACK);
        paint4.setStrokeWidth(5);
        paint4.setStyle(Paint.Style.STROKE);

        paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint5.setColor(Color.RED);
        paint5.setStrokeWidth(10);
        paint5.setTextSize(200);
        paint5.setStyle(Paint.Style.STROKE);

        paint6 = new Paint();
        paint6.setAntiAlias(true);
        paint6.setColor(Color.GREEN);
        paint6.setStrokeWidth(10);
        paint6.setTextSize(200);
        paint6.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        int w = Math.min(screenWidth, screenHeight);
        w = w - (w%9);
        n = (w / 9 );

        // Dessiner w lignes verticles et w lignes horizontales noires
        for(int i = 0; i < 9; i ++)
            for(int j = 0; j <9; j++)
                canvas.drawRect(n*j+(n*diff/100),n*i+(n*diff/100),n*(j+1)-(n*diff/100),n*(i+1)-(n*diff/100),paint4);

        // Dessiner 2 lignes rouges verticales et 2 lignes rouges horizontales

        canvas.drawLine(3*n,0,3*n,9*n,paint3);
        canvas.drawLine(6*n,0,6*n,9*n,paint3);
        canvas.drawLine(0,3*n,9*n,3*n,paint3);
        canvas.drawLine(0,6*n,9*n,6*n,paint3);

        if(verify) {
            if(gagne()) {
                canvas.drawRect(0, 0, w, w, paint6);
                canvas.drawText("Gagné", w/4, w/2, paint6);
            }
            else {
                canvas.drawRect(0, 0, w, w, paint5);
                canvas.drawText("Perdu", w/4, w/2, paint5);
            }
            verify=false;
        }

        // Les contenus des cases
        String s;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s = "" + (matrix[j][i] == 0 ? "" : matrix[j][i]);
                if (fixIdx[j][i])
                    canvas.drawText(s, i * n + (n / 2) - (n / 10)+(n*diff/100), j * n
                            + (n / 2) + (n / 10)+(n*diff/100), paint2);
                else
                    canvas.drawText(s, i * n + (n / 2) - (n / 10)+(n*diff/100), j * n
                            + (n / 2) + (n / 10)+(n*diff/100), paint1);
            }
        }
    }

    public int getXFromMatrix(int x) {
        // Renvoie l'indice d'une case a partir du pixel x de sa position h
        return (x / n);
    }

    public int getYFromMatrix(int y) {
        // Renvoie l'indice d'une case a partir du pixel y de sa position v
        return (y / n);
    }

    public void set(String s, int i) {
        // Remplir la ieme ligne de la matrice matrix avec un vecteur String s
        int v;
        for (int j = 0; j < 9; j++) {
            v = s.charAt(j) - '0';
            matrix[i][j] = v;
            if (v == 0)
                fixIdx[i][j] = false;
            else
                fixIdx[i][j] = true;
        }
    }

    public void set(String s) {
        // Remplir la matrice matrix a partir d'un vecteur String s
        for (int i = 0; i < 9; i++) {
            set(s.substring(i * 9, i * 9 + 9), i);
        }
    }

    public void set(int x, int y, int v) {
        // Affecter la valeur v a la case (y, x)
        // y : ligne
        // x : colonne
        matrix[y][x] = v;
    }

    public String get() {
        // Affecter la valeur v a la case (y, x)
        // y : ligne
        // x : colonne
        String grille="";
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                grille = grille+String.valueOf(matrix[j][i]);
            }
        }
        return grille;
    }

    public boolean isNotFix(int x, int y) {
        // Renvoie si la case (y, x) n'est pas fixe
        return !fixIdx[y][x];
    }

    public boolean gagne() {
        // Verifier si la case n'est pas vide ou bien s'il existe
        // un numero double dans chaque ligne ou chaque colonne de la grille
        for (int v = 1; v <= 9; v++) {
            for (int i = 0; i < 9; i++) {
                boolean bx = false;
                boolean by = false;
                for (int j = 0; j < 9; j++) {
                    if (matrix[i][j] == 0) return false;
                    if ((matrix[i][j] == v) && bx) return false;
                    if ((matrix[i][j] == v) && !bx) bx=true;
                    if ((matrix[j][i] == v) && by) return false;
                    if ((matrix[j][i] == v) && !by) by=true;
                }
            }
        }
        // ------
        // Gagne
        return true;
    }

    public void redraw(int diff){
        this.diff = diff;
        invalidate();
        requestLayout();
    }

    public boolean verify(){
        verify = true;
        invalidate();
        requestLayout();
        return gagne();
    }
}