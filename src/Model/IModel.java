package Model;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public interface IModel {
    static IMazeGenerator getConfiguartionGeneratingAlgorithm() {
        return new MyMazeGenerator();
    }

    int[][] generateMaze(int width, int height);
    //void solveMaze();
    void moveCharacter(KeyCode movement);
    void moveCharacter(MouseEvent movement, double startX, double startY);

    void generateSolution(MyViewModel viewModel, int characterPositionRow, int characterPositionCol, String s);


    //void  setConfig(int numOfThreads, String selectedGeneratingAlgorithm, String selectedSolvingAlgorithm);



}
