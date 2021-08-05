package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class MyModel extends Observable implements IModel {
    private Maze myMaze;
    private int[][] mazeAsArr;

    public void setCharacterPositionRow(int characterPositionRow) {
        this.characterPositionRow = characterPositionRow;
    }

    public void setCharacterPositionCol(int characterPositionCol) {
        this.characterPositionCol = characterPositionCol;
    }

    private Solution solution;
    private int[][] mazeSolutionArr;
    private int characterPositionRow;
    private int characterPositionCol;
    private Position goalPosition;
//    private int mushroomPositionRow;
//    private int mushroomPositionCol;
//    private int tortugaPositionRow;
//    private int tortugaPositionCol;
//    private int goombaPositionRow;
//    private int goombaPositionCol;
    private Server mazeGeneratorServer;
    private Server mazeSolverServer;
    private boolean showMushroom;
    private boolean showGoomba;
    private boolean showTortuga;
    private String goombaDirection;
    private String tortugaDirection;
    private boolean isSolved;
    private boolean gameFinished;
    private boolean solved;


    public MyModel() {

        myMaze = null;
        solution = null;
        int characterPositionRow=1;
        int characterPositionCol=1;
        int mushroomPositionRow=1;
        int mushroomPositionCol=1;
        int tortugaPositionRow=1;
        int tortugaPositionCol=1;
        int goombaPositionRow=1;
        int goombaPositionCol=1;
        mazeGeneratorServer = new Server(5400,1000,new ServerStrategyGenerateMaze());
        mazeSolverServer = new Server(5401,1000,new ServerStrategySolveSearchProblem());
    }

    public void startServers(){
        mazeGeneratorServer.start();
        mazeSolverServer.start();
    }
    public void stopServers(){
        mazeGeneratorServer.stop();
        mazeSolverServer.stop();
    }
    @Override
    public int[][] generateMaze(int width, int height) {
        try {Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        isSolved = false;
                        gameFinished = false;
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0]*mazeDimensions[1]+8];
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        Position updatePosition = new Position(1,1);
                        updatePosition = maze.getStartPosition();
                        //maze.print();
                        //setMaze(maze);
                        myMaze = maze;
                        setCharacterPosition(updatePosition);
                        setGoalPosition(maze.getGoalPosition());
                        MazeToArr(maze);
                        //setCharacterPosition(maze.getStartPosition());
                        //setGoombaPosition(randomPositionOnMazePath());
                        //setTortugaPosition(randomPositionOnMazePath());
                        //setMushroomPosition(randomPositionOnMazePath());
                        //moveGoomba();
                        //moveTortuga();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mazeGeneratorServer.stop();
        setChanged();
        notifyObservers();
        return mazeAsArr;
    }

    private void MazeToArr(Maze maze) {
        int rows = maze.getNumOfRows();
        int cols = maze.getNumOfCols();
        mazeAsArr = new int[rows][cols];
        for( int i =0; i< rows; i++){
            for (int j =0; j< cols; j++){
                mazeAsArr[i][j] = maze.getValue(i,j);
            }
        }
    }

    private void setGoalPosition(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

//    private void moveTortuga() {
//        showTortuga = true;
//        Thread moveTortuga = new Thread(()->{
//            while(showTortuga){
//                Position moveTo = getAdjacentTortugaPosition(tortugaPositionRow,tortugaPositionCol);
//                setTortugaPosition(moveTo);
//                try{
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            setTortugaPosition(new Position(-1,-1));
//        });
//        moveTortuga.start();
//
//    }

//    private Position getAdjacentTortugaPosition(int tortugaPositionRow, int tortugaPositionCol) {
//        int prevPositionRow;
//        int prevPositionCol;
//        prevPositionRow = tortugaPositionRow;
//        prevPositionCol = tortugaPositionCol;
//
//        Position position = new Position(prevPositionRow,prevPositionCol);
//        MazeState mazeState = new MazeState(position.getRowIndex(),position.getColumnIndex());
//        SearchableMaze searchableMaze = new SearchableMaze(maze);
//        List<AState> aStateList = searchableMaze.getAllPossibleStates(mazeState);
//        aStateList.remove(new MazeState(mushroomPositionRow,mushroomPositionCol));
//        aStateList.remove(new MazeState(maze.getGoalPosition().getRowIndex(),maze.getGoalPosition().getColumnIndex()));
//
//        Random r = new Random();
//        int low = 0;
//        int high = aStateList.size();
//        int res = r.nextInt((high-low)+low);
//        MazeState nextState = (MazeState) aStateList.get(res);
//        Position nextPosition = new Position(nextState.getCol(),nextState.getCol());
//        int tempRow = nextPosition.getRowIndex();
//        int tempCol = nextPosition.getColumnIndex();
//        if(tempCol < prevPositionCol){
//            tortugaDirection = "Left";
//        }
//        else if(tempCol > prevPositionCol){
//            tortugaDirection = "Right";
//        }
//        else{
//            tortugaDirection = "";
//        }
//        return new Position(tempRow,tempCol);
//    }
//
//    private void moveGoomba() {
//        showGoomba = true;
//        Thread moveGoomba = new Thread(()->{
//            while(showGoomba){
//                Position moveTo = getAdjacentGoombaPosition(goombaPositionRow,goombaPositionCol);
//                setGoombaPosition(moveTo);
//                try{
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            setGoombaPosition(new Position(-1,-1));
//        });
//        moveGoomba.start();
//
//    }
//
//    private Position getAdjacentGoombaPosition(int goombaPositionRow, int goombaPositionCol) {
//        int prevPositionRow;
//        int prevPositionCol;
//        prevPositionRow = goombaPositionRow;
//        prevPositionCol = goombaPositionCol;
//
//        Position position = new Position(prevPositionRow,prevPositionCol);
//        MazeState mazeState = new MazeState(position.getRowIndex(),position.getColumnIndex());
//        SearchableMaze searchableMaze = new SearchableMaze(maze);
//        List<AState> aStateList = searchableMaze.getAllPossibleStates(mazeState);
//        aStateList.remove(new MazeState(mushroomPositionRow,mushroomPositionCol));
//        aStateList.remove(new MazeState(maze.getGoalPosition().getRowIndex(),maze.getGoalPosition().getColumnIndex()));
//
//        Random r = new Random();
//        int low = 0;
//        int high = aStateList.size();
//        int res = r.nextInt((high-low)+low);
//        MazeState nextState = (MazeState) aStateList.get(res);
//        Position nextPosition = new Position(nextState.getCol(),nextState.getCol());
//        int tempRow = nextPosition.getRowIndex();
//        int tempCol = nextPosition.getColumnIndex();
//        if(tempCol < prevPositionCol){
//            goombaDirection = "Left";
//        }
//        else if(tempCol > prevPositionCol){
//            goombaDirection = "Right";
//        }
//        else{
//            goombaDirection = "";
//        }
//        return new Position(tempRow,tempCol);
//    }

    private Position randomPositionOnMazePath() {
        int row = -1;
        int col = -1;
        do {
            Random random = new Random();
            int lowRow = 0;
            int highRow = myMaze.getNumOfRows();
            row = random.nextInt(highRow-lowRow)+lowRow;
            int lowCol = 0;
            int highCol = myMaze.getNumOfCols();
            col = random.nextInt(highCol-lowCol)+lowCol;
        }while (myMaze.getValue(row,col)!=0 &&((row != myMaze.getStartPosition().getRowIndex()) || (col != myMaze.getStartPosition().getColumnIndex())));
        Position ans = new Position(row,col);
        return ans;
    }


//    @Override
//    public void solveMaze() {
//        try {
//            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
//                @Override
//                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
//                    try {
//                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
//                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
//                        toServer.flush();
//                        //MyMazeGenerator mg = new MyMazeGenerator();
//                        //Maze maze = mg.generate(10,10);
//                        //maze.print();
//                        toServer.writeObject(maze);
//                        toServer.flush();
//                        //setSolution((Solution)fromServer.readObject());
//                        Solution mazeSolution = (Solution)fromServer.readObject();
//                        //print Maze Solution retrieved from the server
//                        //System.out.println(String.format("Solution steps: %s", mazeSolution));
//                        ArrayList<AState> mazeSolutionsSteps = mazeSolution.getSolutionPath();
//                        //for(int i = 0;  i< mazeSolutionsSteps.size(); i++){
//                        //    System.out.println(String.format("%s.%s", i, mazeSolutionsSteps.get(i).toString()));
//                        //}
//                        int size = mazeSolutionsSteps.size();
//                        mazeSolutionArr = new int[2][size];
//                        if(s == "solve")
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//            client.communicateWithServer();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//    }

    private boolean notLeagal(int x, int y){
        if(x < 0 || y <0 || x> mazeAsArr.length-1 || y > mazeAsArr[0].length-1){
            return true;
        }
        return mazeAsArr[x][y] == 1;
    }
    @Override
    public void moveCharacter(KeyCode movement) {
        if(myMaze != null){
            int x = characterPositionRow;
            int y = characterPositionCol;
            switch (movement){
                case UP:
                    if(!notLeagal(x-1,y)){
                        characterPositionRow--;
                    }
                    break;
                case NUMPAD8:
                    if(!notLeagal(x-1,y)){
                        characterPositionRow--;
                    }
                    break;
                case DOWN:
                    if(!notLeagal(x+1,y)){
                        characterPositionRow++;
                    }
                    break;
                case NUMPAD2:
                    if(!notLeagal(x+1,y)){
                        characterPositionRow++;
                    }
                    break;
                case RIGHT:
                    if(!notLeagal(x,y+1)){
                        characterPositionCol++;
                    }
                    break;
                case NUMPAD6:
                    if(!notLeagal(x,y+1)){
                        characterPositionCol++;
                    }
                    break;
                case LEFT:
                    if(!notLeagal(x,y-1)){
                        characterPositionCol--;
                    }
                    break;
                case NUMPAD4:
                    if(!notLeagal(x,y-1)){
                        characterPositionCol--;
                    }
                    break;
                case NUMPAD7:
                    if(!notLeagal(x-1,y-1)){
                        characterPositionCol--;
                        characterPositionRow--;
                    }
                    break;
                case NUMPAD9:
                    if(!notLeagal(x-1,y+1)){
                        characterPositionCol++;
                        characterPositionRow--;
                    }
                    break;
                case NUMPAD3:
                    if(!notLeagal(x+1,y+1)){
                        characterPositionCol++;
                        characterPositionRow++;
                    }
                    break;
                case NUMPAD1:
                    if(!notLeagal(x+1,y-1)){
                        characterPositionCol--;
                        characterPositionRow++;
                    }
                    break;
            }
            if(characterPositionRow==myMaze.getGoalPosition().getRowIndex() && characterPositionCol==myMaze.getGoalPosition().getColumnIndex()){
                gameFinished = true;
            }
            setChanged();
            notifyObservers();
//            if((characterPositionRow == goombaPositionRow && characterPositionCol==goombaPositionCol) || (characterPositionRow==tortugaPositionRow && characterPositionCol==tortugaPositionCol)){
//                collusion("collide");
//            }
//            if(characterPositionRow==mushroomPositionRow && characterPositionCol==mushroomPositionCol){
//                coliideWithMushroom("collideWithMushroom");
//            }


        }

    }

    private void cannotMove(KeyCode movement) {
        setChanged();
        notifyObservers(movement);
    }

    private void solvedMaze(boolean b) {
        setChanged();;
        notifyObservers(b);
    }

    private void collusion(String collide) {
        setChanged();
        notifyObservers(collide);
    }

//    private void coliideWithMushroom(String collideWithMushroom) {
//        showMushroom = false;
//        setMushroomPosition(new Position(-1,-1));
//        setChanged();
//        notifyObservers(collideWithMushroom);
//    }

    private void updatePossibleMove(Position position) {
        MazeState mazeState = new MazeState(position.getRowIndex(),position.getColumnIndex());
        SearchableMaze searchableMaze = new SearchableMaze(myMaze);
        List<AState> stateList = searchableMaze.getAllPossibleStates(mazeState);
        Object[] states = ((ArrayList) stateList).toArray();
        setChanged();
        notifyObservers(states);
    }

    @Override
    public void moveCharacter(MouseEvent movement, double startX, double startY) {
        if(myMaze != null){
           if(!movement.isControlDown()){
               if(movement.getY()<startY && (Math.abs(movement.getY()-startY)>10)){/////move up
                   if(myMaze.getValue(characterPositionRow-1,characterPositionCol) != 0){
                       cannotMove(movement);
                   }
                   else
                       setCharacterPosition(new Position(characterPositionRow-1,characterPositionCol));
               }
               else if(movement.getY()>startY && (Math.abs(movement.getY()-startY)>10)){/////move down
                   if(myMaze.getValue(characterPositionRow+1,characterPositionCol) != 0){
                       cannotMove(movement);
                   }
                   else
                       setCharacterPosition(new Position(characterPositionRow+1,characterPositionCol));
               }
               else if(movement.getX()>startX){/////move right
                   if(myMaze.getValue(characterPositionRow,characterPositionCol+1) != 0){
                       cannotMove(movement);
                   }
                   else
                       setCharacterPosition(new Position(characterPositionRow,characterPositionCol+1));
               }
               else if(movement.getX()<startX){/////move left
                   if(myMaze.getValue(characterPositionRow,characterPositionCol-1) != 0){
                       cannotMove(movement);
                   }
                   else
                       setCharacterPosition(new Position(characterPositionRow,characterPositionCol-1));
               }
               if(characterPositionRow==myMaze.getGoalPosition().getRowIndex() && characterPositionCol==myMaze.getGoalPosition().getColumnIndex()){
                   solvedMaze(true);
               }
//               if((characterPositionRow == goombaPositionRow && characterPositionCol==goombaPositionCol) || (characterPositionRow==tortugaPositionRow && characterPositionCol==tortugaPositionCol)){
//                   collusion("collide");
//               }
//               if(characterPositionRow==mushroomPositionRow && characterPositionCol==mushroomPositionCol){
//                   coliideWithMushroom("collideWithMushroom");
//               }
           }
        }
    }

    @Override
    public void generateSolution(MyViewModel viewModel, int characterPositionRow, int characterPositionCol, String s) {
        mazeSolverServer = new Server(5402,1000,new ServerStrategySolveSearchProblem());
        mazeSolverServer.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        Maze maze = myMaze;
                        maze.setStartPosition(new Position(characterPositionRow,characterPositionCol));
                        //MyMazeGenerator mg = new MyMazeGenerator();
                        //Maze maze = mg.generate(10,10);
                        //maze.print();
                        toServer.writeObject(maze);
                        toServer.flush();
                        //setSolution((Solution)fromServer.readObject());
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        isSolved = true;
                        //print Maze Solution retrieved from the server
                        //System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionsSteps = mazeSolution.getSolutionPath();
                        //for(int i = 0;  i< mazeSolutionsSteps.size(); i++){
                        //    System.out.println(String.format("%s.%s", i, mazeSolutionsSteps.get(i).toString()));
                        //}
                        int size = mazeSolutionsSteps.size();
                        mazeSolutionArr = new int[2][size];
                        if(s == "solve"){
                            for(int i=0; i< mazeSolutionsSteps.size(); i++){
                                mazeSolutionArr[0][i] = ((MazeState)(mazeSolutionsSteps.get(i))).getRow();
                                mazeSolutionArr[1][i] = ((MazeState)(mazeSolutionsSteps.get(i))).getCol();
                            }
                        }
                        setChanged();
                        notifyObservers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }



    public void setConfig(int numOfThreads, String selectedGeneratingAlgorithm, String selectedSolvingAlgorithm) {


    }

    private void cannotMove(MouseEvent movement) {
        setChanged();
        notifyObservers(movement);
    }

    public void load(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Maze m = (Maze) objectInputStream.readObject();
            setMaze(m);
            setGoalPosition(m.getGoalPosition());
            setCharacterPositionRow(m.getStartPosition().getRowIndex());
            setCharacterPositionCol(m.getStartPosition().getColumnIndex());
            solved = false;
            objectInputStream.close();
            setChanged();
            notifyObservers();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        setSolution(null);
//        setMaze(loadedMaze);
//        setCharacterPosition(loadedPosition);
    }



    private void setCharacterPosition(Position loadedPosition) {
        characterPositionCol = loadedPosition.getColumnIndex();
        characterPositionRow = loadedPosition.getRowIndex();

    }

    private void setMaze(Maze loadedMaze) {
        this.myMaze = loadedMaze;
        MazeToArr(loadedMaze);
        //this.solution = null;
        //setChanged();
        //notifyObservers(this.maze);
    }

    private void setSolution(Solution solution) {
        this.solution = solution;
        setChanged();
        notifyObservers(this.solution);
    }

    public Maze getMaze() {
        return myMaze;
    }

    public int[][] getMazeSolutionArr() {
        return mazeSolutionArr;
    }

    public Solution getSolution() {
        return solution;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionCol() {
        return characterPositionCol;
    }

//    public int getMushroomPositionRow() {
//        return mushroomPositionRow;
//    }
//
//    public int getMushroomPositionCol() {
//        return mushroomPositionCol;
//    }
//
//    public int getTortugaPositionRow() {
//        return tortugaPositionRow;
//    }
//
//    public int getTortugaPositionCol() {
//        return tortugaPositionCol;
//    }
//
//    public int getGoombaPositionRow() {
//        return goombaPositionRow;
//    }
//
//    public int getGoombaPositionCol() {
//        return goombaPositionCol;
//    }

    public void setCharacterPositionRow(int characterPositionRow,int characterPositionCol) {
        this.characterPositionRow = characterPositionRow;
        this.characterPositionCol = characterPositionCol;
    }

//    public void setMushroomPosition(Position mushroomPosition) {
//        this.mushroomPositionRow = mushroomPosition.getRowIndex();
//        this.mushroomPositionCol = mushroomPosition.getColumnIndex();
//    }

//    public void setTortugaPosition(Position tortugaPosition) {
//        this.tortugaPositionRow = tortugaPosition.getRowIndex();
//        this.tortugaPositionCol = tortugaPosition.getColumnIndex();
//    }

//    public void setGoombaPosition(Position goombaPosition) {
//        this.goombaPositionRow = goombaPosition.getRowIndex();
//        this.goombaPositionCol = goombaPosition.getColumnIndex();
//    }


    public Position getGoalPosition() {
        return goalPosition;
    }

    public boolean isSolved() {
        return this.isSolved;
    }

    public boolean gameFinished() {
        return  gameFinished;
    }

    public int[][] getMazeAsArr() {
        return mazeAsArr;
    }

    public void save(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            solved = false;
            myMaze.setStartPosition(new Position(characterPositionRow,characterPositionCol));
            objectOutputStream.writeObject(myMaze);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
