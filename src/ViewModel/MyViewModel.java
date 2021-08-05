package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MyMazeDisplayer;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    MyModel model;
    private int characterPositionRowIndex;
    private int characterPositionColIndex;
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionCol = new SimpleStringProperty();
    private int goombaPositionRowIndex;
    private int goombaPositionColIndex;
    private int tortugaPositionRowIndex;
    private int tortugaPositionColIndex;
    private int mushroomPositionRowIndex;
    private int mushroomPositionColIndex;
    private ArrayList<AState> solutionPath;


    public MyViewModel(MyModel myModel) {
        this.model = myModel;
    }

    public static IMazeGenerator getConfigurationGeneratingAlgorithm() {
        return IModel.getConfiguartionGeneratingAlgorithm();
    }

    public static int getConfigurationNumberOfThreads() {
        return 1;
    }

    public void setConfig(int numOfThreads, String selectedGeneratingAlgorithm, String selectedSolvingAlgorithm) {
        model.setConfig(numOfThreads,selectedGeneratingAlgorithm,selectedSolvingAlgorithm);
    }


    public int[][] generateMaze(int width, int height) { return model.generateMaze(width,height); }


    public void load(File file) {
        model.load(file);
    }

    public void getSolution(MyViewModel viewModel, int characterPositionRow, int characterPositionCol, String s){ model.generateSolution(viewModel,characterPositionRow,characterPositionCol,s);}

    public Maze getMaze() { return model.getMaze(); }

    public void moveCharacter(KeyCode movement){ model.moveCharacter(movement); }

    public void moveCharacter(MouseEvent movement, double startX, double startY){ model.moveCharacter(movement,startX,startY); }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionCol() {
        return characterPositionColIndex;
    }

    public int getGoombaPositionRowIndex() {
        return goombaPositionRowIndex;
    }

    public int getGoombaPositionColIndex() {
        return goombaPositionColIndex;
    }

    public int getTortugaPositionRowIndex() {
        return tortugaPositionRowIndex;
    }

    public int getTortugaPositionColIndex() {
        return tortugaPositionColIndex;
    }

    public int getMushroomPositionRowIndex() {
        return mushroomPositionRowIndex;
    }

    public int getMushroomPositionColIndex() {
        return mushroomPositionColIndex;
    }

    public void stopServers(){ model.stopServers(); }

    @Override
    public void update(Observable o, Object arg) {
        if (model == o) {
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionColIndex = model.getCharacterPositionCol();
            characterPositionRow.set((characterPositionColIndex+""));
            characterPositionCol.set(characterPositionColIndex+"");
            setChanged();
            notifyObservers(arg);
        }
    }

    public Position getGoalPosition() {
        return model.getGoalPosition();
    }

    public boolean isSolved() {
        return model.isSolved();
    }

    public boolean gameFinished() {
        return model.gameFinished();
    }

    public int[][] getSolutionArr() {
        return model.getMazeSolutionArr();
    }

    public int[][] getMazeInArr() {
        return model.getMazeAsArr();
    }

    public void save(File file) { model.save(file); }
}
