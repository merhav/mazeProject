package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

//import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MyMazeDisplayer extends Canvas {
    public static boolean movingRight;
    public static boolean goombaMovingRight;
    public static boolean tortugaMovingRight;
    public static boolean shrink;
    private Maze maze;
    private int[][] mazeInArr;
    private int previousCharacterPositionRow;
    private int previousCharacterPositionCol;
    private int characterPositionRow;
    private int characterPositionCol;
    private Solution solution;
    private int[][] solutionPath;

    private double startX;
    private double startY;
    private double cellWidth;
    private double cellHeight;
    private boolean showSolution;
    private Image wall;
    private Image path;
    private Image flag;
    private Image coin;
    private Image character;
    private Image goomba;
    private Image tortuga;
    private Image mushroom;
    //private GraphicsContext graphicsContext;
    private double canvasHeight;
    private double canvasWidth;
    private Position goalPosition;
    private int mushroomPositionRow;
    private int mushroomPositionCol;
    private int tortugaPositionRow;
    private int tortugaPositionCol;
    private int goombaPositionRow;
    private int goombaPositionCol;

    private volatile Object lock;
    private volatile Object lock2;
    private volatile Object cellHeightAndWidthLock;
    private volatile Object goombaLock;
    private volatile Object tortugaLock;
    private boolean isSolved;



//    public MyMazeDisplayer(){
//        lock = new Object();
//        lock2 = new Object();
//        cellHeightAndWidthLock = new Object();
//        goombaLock = new Object();
//        tortugaLock = new Object();

//        startX = 0;
//        startY = 0;
//        previousCharacterPositionRow = 1;
//        previousCharacterPositionCol = 1;
        //characterPositionRow = 1;
        //characterPositionCol = 1;
//        showSolution = false;
//        movingRight = true;
//        goombaMovingRight = true;
//        tortugaMovingRight = true;
        //try{
          //  graphicsContext = getGraphicsContext2D();
        //} catch (Exception e){}


       // wall = new Image(this.getClass().getResourceAsStream("/Displayed On Maze/brick.png"));
       // path = new Image(this.getClass().getResourceAsStream("/Displayed On Maze/path.png"));
       // flag = new Image(this.getClass().getResourceAsStream("/Displayed On Maze/flag.png"));
        //character = new Image(this.getClass().getResourceAsStream("/Mario Characters/mario_big_right01.png"));
//        goomba = new Image((this.getClass().getResourceAsStream("/Enemy Characters/gomba_right_01.png")));
//        tortuga = new Image((this.getClass().getResourceAsStream("/Enemy Characters/tortuga_right_01.png")));
        //mushroom = new Image((this.getClass().getResourceAsStream("/Displayed On Maze/mushroom.png")));

        //ChangingCharacterImage();
        //ChangingGoombaImage();
        //ChangingTortugaImage();
    //}

//    private void ChangingTortugaImage() {
//        Thread thread = new Thread(()->{
//            boolean firstImage = true;
//            while (true){
//                try {
//                    if(movingRight){
//                        if (firstImage)
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/tortuga_right_01.png"));
//                        else
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/tortuga_right_02.png"));
//                    }
//                    else {
//                        if (firstImage)
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/tortuga_left_01.png"));
//                        else
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/tortuga_left_01.png"));
//                    }
//                    firstImage = !firstImage;
//                    synchronized (tortugaLock){
//                        if(!showSolution){
//                            drawTortuga();
//                        }
//                        else {
//                            drawSpot(tortugaPositionRow, tortugaPositionCol);
//                        }
//                    }
//
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        });
//        thread.start();
//    }
//
//    private void ChangingGoombaImage() {
//        Thread thread = new Thread(()->{
//            boolean firstImage = true;
//            while (true){
//                try {
//                    if(movingRight){
//                        if (firstImage)
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/gomba_right_01.png"));
//                        else
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/gomba_right_02.png"));
//                    }
//                    else {
//                        if (firstImage)
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/gomba_left_01.png"));
//                        else
//                            character = new Image(this.getClass().getResourceAsStream("/Enemy Characters/gomba_left_01.png"));
//                    }
//                    firstImage = !firstImage;
//                    synchronized (goombaLock){
//                        if(!showSolution)
//                            drawGoomba();
//                        else {
//                            drawSpot(goombaPositionRow,goombaPositionCol);
//                        }
//                    }
//
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        });
//        thread.start();
//    }

//    private void ChangingCharacterImage() {
//        Thread thread = new Thread(()->{
//            boolean firstImage = true;
//            while (true){
//                    try {
//                        if(movingRight){
//                            if (firstImage)
//                                character = new Image(this.getClass().getResourceAsStream("/Mario Characters/mario_big_right01.png"));
//                            else
//                                character = new Image(this.getClass().getResourceAsStream("/Mario Characters/mario_big_right02.png"));
//                        }
//                        else {
//                            if (firstImage)
//                                character = new Image(this.getClass().getResourceAsStream("/Mario Characters/mario_big_left01.png"));
//                            else
//                                character = new Image(this.getClass().getResourceAsStream("/Mario Characters/mario_big_left02.png"));
//                        }
//                        firstImage = !firstImage;
//                        synchronized (lock){
//                            drawCharacter();
//                        }
//
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//            }
//
//        });
//        thread.start();
//    }

//    public void updateCanvasProperties(){
//        synchronized (cellHeightAndWidthLock){
//            canvasHeight = getHeight();
//            canvasWidth = getWidth();
//        }
//        drawMaze();
//    }

//    public void restartMazeView(){
//        if(maze != null){
//
//            drawMaze();
//        }
//    }

    public void setMaze(Maze maze){

            if (canvasHeight == 0 && canvasWidth == 0) {
                canvasHeight = getHeight();
                canvasWidth = getWidth();
            }

        this.maze = maze;
//        movingRight = true;
//        goombaMovingRight = true;
//        tortugaMovingRight = true;
//        restartMazeView();

    }
    public void setMaze(int[][] maze){
        this.mazeInArr = maze;
        //redraw();
    }

    public void setCharacterPosition(int row, int col){

            characterPositionRow = row;
            characterPositionCol = col;

            //removePreviousCharacter();
            drawCharacter();

    }

//    private void removePreviousCharacter() {
//        drawSpot(previousCharacterPositionRow,previousCharacterPositionCol);
//    }

    public int getCharacterPositionRow(){
        return characterPositionRow;
    }
    public int getCharacterPositionCol(){
        return characterPositionCol;
    }

/*
    private void drawMaze() {
        setCellsHeightAndWidth();
        graphicsContext.clearRect(0,0,getWidth(),getHeight());
        for(int i= 0;i<mazeInArr.length;i++){
            for(int j= 0;j<mazeInArr[0].length;j++){
                drawSpot(i,j);
            }
        }
    }

    private void drawSpot(int row, int col) {
        try{

                if (mazeInArr[row][col] == 1) {
                    graphicsContext.drawImage(wall, startX + col * cellWidth, startY + row * cellHeight, cellWidth, cellHeight);
                } else if (row == goalPosition.getRowIndex() && col == goalPosition.getColumnIndex()) {
                    graphicsContext.drawImage(flag, startX + col * cellWidth, startY + row * cellHeight, cellWidth, cellHeight);
                } else {
                    graphicsContext.drawImage(path, startX + col * cellWidth, startY + row * cellHeight, cellWidth, cellHeight);
                }

        } catch (Exception e){}
    }
*/
    private void setCellsHeightAndWidth() {

            if (maze != null) {
                cellHeight = canvasHeight / maze.getNumOfRows();
                cellWidth = canvasWidth / maze.getNumOfCols();
            }

    }

    private void drawCharacter(){
        if(maze != null){
            try {
                synchronized (cellHeightAndWidthLock) {
                    GraphicsContext graphicsContext = getGraphicsContext2D();
                    graphicsContext.drawImage(character, startX + characterPositionCol * cellWidth, startY + characterPositionRow * cellHeight, cellWidth, cellHeight);
                }
            } catch (Exception e){}

        }
    }

//    public void changeSolutionPath(ArrayList<AState> arg) {
//        synchronized (lock2){
//            this.solutionPath = arg;
//            showSolution = true;
//        }
//    }

//    public void setGoombaPosition(int row, int col){
//        synchronized (goombaLock){
//        drawSpot(row,col);
//        this.goombaPositionRow = row;
//        this.goombaPositionCol = col;
//        if(!showSolution)
//            drawGoomba();
//        }
//    }
//
//    private void drawGoomba() {
//        if(maze != null){
//            try{
//                synchronized (cellHeightAndWidthLock){
//                    graphicsContext.drawImage(goomba,startX+goombaPositionCol*cellWidth,startY+goombaPositionRow*cellHeight,cellWidth,cellHeight);
//                }
//            }catch (Exception e){}
//
//        }
//    }
//
//    public  void setTortugaPosition(int row, int col){
//        synchronized (tortugaLock) {
//            drawSpot(row, col);
//            this.tortugaPositionRow = row;
//            this.tortugaPositionCol = col;
//            if (!showSolution)
//                drawTortuga();
//        }
//    }
//
//    private void drawTortuga() {
//        if(maze != null){
//            try{
//                synchronized (cellHeightAndWidthLock){
//                    graphicsContext.drawImage(tortuga,startX+tortugaPositionCol*cellWidth,startY+tortugaPositionRow*cellHeight,cellWidth,cellHeight);
//                }
//            } catch (Exception e){}
//        }
//    }

    public void setMushroomPosition(int row, int col){
        //drawSpot(row,col);
        this.mushroomPositionRow = row;
        this.mushroomPositionCol = col;
        drawMushroom();
    }

    private void drawMushroom() {
        if(maze != null){
            //graphicsContext.drawImage(mushroom,startX+mushroomPositionCol*cellWidth,startY+mushroomPositionRow*cellHeight,cellWidth,cellHeight);
        }
    }

    public void removeSolution() {
        this.solution = null;
        showSolution = false;
    }
    public double getCellHeight(){
        return canvasHeight/mazeInArr[0].length;
    }
    public double getCellWidth(){
        return canvasWidth/mazeInArr.length;
    }

    public void goalPosition(Position goal) {
        this.goalPosition = goal;
    }

    public void redraw() {
        if(mazeInArr != null){
            canvasHeight = getHeight();
            canvasWidth = getWidth();
            cellHeight = getCellHeight();
            cellWidth = getCellWidth();
            try{
                GraphicsContext graphicsContext = getGraphicsContext2D();
                graphicsContext.clearRect(0,0,canvasHeight,canvasWidth);
                wall = new Image(this.getClass().getResourceAsStream("/wall.jpg"));
                //path = new Image(this.getClass().getResourceAsStream("/Displayed On Maze/path.png"));
                for(int i = 0; i< mazeInArr.length; i++){
                    for (int j =0; j< mazeInArr.length; j++){
                        if(mazeInArr[i][j] == 1) {
                            graphicsContext.drawImage(wall, j * cellHeight, i * cellWidth, cellHeight, cellWidth);
                        }
//                        else {
//                            graphicsContext.drawImage(path, startX + j * cellHeight, startY + i * cellWidth, cellWidth, cellHeight);
//                        }
                    }

                }
                flag = new Image(this.getClass().getResourceAsStream("/end.jpg"));
                graphicsContext.drawImage(flag, goalPosition.getColumnIndex()*cellHeight, goalPosition.getRowIndex()*cellWidth,cellHeight,cellWidth);
                if(isSolved){
                    Image solution = new Image(this.getClass().getResourceAsStream("/Displayed On Maze/mushroom.png"));
                    for(int i=0; i< solutionPath[0].length-1; i++){
                        int x = solutionPath[0][i];
                        int y = solutionPath[1][i];
                        graphicsContext.drawImage(solution,y*cellHeight, x*cellWidth,cellHeight,cellWidth);
                    }
                }
                Image character = new Image(this.getClass().getResourceAsStream("/logo.png"));
                graphicsContext.drawImage(character,characterPositionCol*cellHeight,characterPositionRow*cellWidth, cellHeight,cellWidth);
            }catch (Exception e){
                System.out.println(e);
            }
        }

    }

    public void setGoalPosition(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

    public void solved(int[][] solution) {
        solutionPath = solution;
    }

    public void isSolved(boolean solved) {
        this.isSolved = solved;
    }

    public void changeCursersPlace(double xTo, double yTo) {
        if(mazeInArr != null){
            double extraMoveX = (cellWidth*maze.getNumOfCols())/canvasWidth;
            double extraMoveY = (cellHeight*maze.getNumOfRows())/canvasHeight;
            if(xTo < 0){
                extraMoveX = -extraMoveX;
            }
            if (yTo < 0){
                extraMoveY = - extraMoveY;
            }
            startX += xTo + extraMoveX;
            startY += yTo + extraMoveY;
            redraw();
        }
    }
}
