package View;


import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView/*, Initializable*/ {
    @FXML
    public MyViewModel viewModel = new MyViewModel(new MyModel());
    public javafx.scene.control.TextField textFiledRowsNum;
    public javafx.scene.control.TextField textFiledColsNum;
    public javafx.scene.control.Button buttonGenerateMaze;
    public javafx.scene.control.Button buttonSolveMaze;
    public javafx.scene.control.Button buttonSave;
    public Button buttonHelp;
    @FXML
    private javafx.scene.layout.BorderPane borderPane_view;
    @FXML
    private javafx.scene.control.Label labelNumOfRows;
    @FXML
    private javafx.scene.control.Label labelNumOfCols;

    public MyMazeDisplayer mazeDisplayer = new MyMazeDisplayer();
    private StringProperty characterPositionRow = new SimpleStringProperty();
    private StringProperty characterPositionCol = new SimpleStringProperty();
    //private volatile Object directionLock = new Object();
    private boolean stopThemeSong;
    private Thread ThreadOfMusic;
    private MediaPlayer themeMediaPlayer;
    private boolean startedDragging;
    private boolean startedDraggingWithCtrl;
    private double startX;
    private double startY;
    private double dragAndCtrlPreviousX;
    private double dragAndCtrlPreviousY;
    @FXML
    private javafx.scene.control.MenuItem menuItemSave;
    private boolean songonce = true;
    private boolean showOnce = false;
    private boolean startDragging;
    private int index = 0;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);

    }
    public  void bindProperties(MyViewModel viewModel){
        labelNumOfRows.textProperty().bind(viewModel.characterPositionRow);
        labelNumOfCols.textProperty().bind(viewModel.characterPositionCol);
    }
    public void KeyPressed(KeyEvent keyEvent){
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void mouseReleased(MouseEvent mouseEvent){
        DragDone(mouseEvent);
        mouseEvent.consume();
    }

    private void DragDone(MouseEvent mouseEvent) {
        startedDragging = false;
        startedDraggingWithCtrl = false;
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
        dragAndCtrlPreviousX = mouseEvent.getX();
        dragAndCtrlPreviousY = mouseEvent.getY();
    }
    public void mousePressed(MouseEvent mouseEvent){
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();
    }

    public void generateMaze(){
        int height = 0;
        int width = 0;
        if(songonce == true){
            Music(0);
        }
        buttonSave.setVisible(true);
        showOnce = false;
        try{
            height = Integer.valueOf(textFiledRowsNum.getText());
            width = Integer.valueOf(textFiledColsNum.getText());
        } catch (Exception e){
            showAlert("Values must be numeric");
            return;
        }
        if(height < 3|| width < 3){
            showAlert("Height and Width must be larger than 3");
            return;
        }

        buttonSolveMaze.setDisable(true);
        //mazeDisplayer.removeSolution();
        int[][] temp = viewModel.generateMaze(height,width);
        //mazeDisplayer.setMaze(temp);
        mazeDisplayer.goalPosition(viewModel.getGoalPosition());
        buttonSolveMaze.setDisable(false);
//        if(ThreadOfMusic != null && ThreadOfMusic.isAlive()){
//            themeMediaPlayer.stop();
//            stopThemeSong = false;
//        }
        //menuItemSave.setDisable(false);
        //displayMaze(viewModel.getMazeInArr());
        //playTheme();

    }

    private void Music(int i) {
        if(themeMediaPlayer != null){
            themeMediaPlayer.stop();
        }
        String path = "";
        if(i == 0){
            songonce = false;
            path = "/theme.mp3";
        }
        else {
            songonce = true;
            path = "/win sound.mp3"; // ending
        }
        Media media = new Media(this.getClass().getResource(path).toString());
        themeMediaPlayer = new MediaPlayer(media);
        themeMediaPlayer.play();
    }

    private void playTheme() {
        stopThemeSong = false;
        ThreadOfMusic = new Thread(()->{
                try {
                    while ((!stopThemeSong)) {
                        Media sound = new Media(this.getClass().getResource("/theme.mp3").toString());
                        themeMediaPlayer = new MediaPlayer(sound);
                        themeMediaPlayer.play();
                        Thread.sleep(220000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        ThreadOfMusic.start();
    }

    public void solveMaze(){
        showAlert("Solving");
        viewModel.getSolution(this.viewModel,this.viewModel.getCharacterPositionRow(),viewModel.getCharacterPositionCol(),"solve");
//        stopThemeSong = true;
//        themeMediaPlayer.stop();
//        buttonSolveMaze.setDisable(true);
//        viewModel.solveMaze();
    }

    public void load(ActionEvent actionEvent){
        //mazeDisplayer.removeSolution();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Loading maze");
        File loadFile = new File("./Mazes/");
        if(!loadFile.exists()){
            loadFile.mkdir();
        }
        fileChooser.setInitialDirectory(loadFile);
        File file = fileChooser.showOpenDialog(new PopupWindow() {
        });
        if(file != null && file.exists() && !file.isDirectory()){
            viewModel.load(file);
            if(songonce == true){
                Music(0);
            }
            mazeDisplayer.redraw();
        }
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze progress File","*mzprg"));
//        File file = fileChooser.showOpenDialog(null);
//        try {
//            if(file != null) {
//                File loadFile = new File(file.getPath());
//                FileInputStream fileInputStream = new FileInputStream(loadFile);
//                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                Object[] objects = (Object[]) objectInputStream.readObject();
//                Maze loadedMaze = (Maze) objects[0];
//                Position loadedPosition = (Position) objects[1];
//                viewModel.load(loadedMaze, loadedPosition);
//                objectInputStream.close();
//                objectInputStream.close();
//            }
//            else {
//                showAlert("Could not load file");
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }



    public void saveMaze(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File path = new File("./Mazes/");
        if(!path.exists()){
            path.mkdir();
        }
        fileChooser.setTitle("Saving");
        fileChooser.setInitialFileName("Maze number "+ index);
        index++;
        fileChooser.setInitialDirectory(path);
        File file = fileChooser.showSaveDialog(mazeDisplayer.getScene().getWindow());
        if(file != null){
            viewModel.save(file);
        }
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze progress File","*mzpf"));
//        File file = fileChooser.showOpenDialog(null);
//        try {
//            if(file != null) {
//                File saveFile = new File(file.getPath());
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
//                Object[] objects = new Object[2];
//                objects[0] = viewModel.getMaze();
//                objects[1] = new Position(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionCol());
//                objectOutputStream.writeObject(objects);
//                objectOutputStream.flush();
//                objectOutputStream.close();
//            }
//            else {
//                showAlert("An Error accored while saving");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(s);
        alert.show();
    }


    @Override
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionCol = viewModel.getCharacterPositionCol();
        mazeDisplayer.setCharacterPosition(characterPositionRow,characterPositionCol);
        mazeDisplayer.setGoalPosition(viewModel.getGoalPosition());
        mazeDisplayer.solved(viewModel.getSolutionArr());
        mazeDisplayer.isSolved(viewModel.isSolved());
        this.characterPositionRow.set(characterPositionRow+"");
        this.characterPositionCol.set(characterPositionCol+"");
        if(viewModel.isSolved()){
            mazeDisplayer.redraw();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == viewModel){
            mazeDisplayer.setMaze(viewModel.getMaze());
            mazeDisplayer.setCharacterPosition(viewModel.getCharacterPositionRow(),viewModel.getCharacterPositionCol());
            mazeDisplayer.setGoalPosition(viewModel.getGoalPosition());
            displayMaze(viewModel.getMazeInArr());
            buttonGenerateMaze.setDisable(false);
            buttonSave.setVisible(true);
            if(viewModel.gameFinished() && !showOnce){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("The Game is done!");
                Music(1);
                alert.show();
                showOnce = true;
            }
            mazeDisplayer.redraw();
//            if(arg instanceof  Maze){
//                updateMaze((Maze)arg);
//            }
//            else if(arg instanceof Position){
//                updatePosition((Position)arg);
//            }
//            else if(arg instanceof ArrayList){
//                updateSolutionPath((ArrayList<AState>)arg);
//            }
//            else if( arg instanceof Boolean){
//                mazeIsSolved();
//            }
//            else if(arg instanceof String){
//                if(((String)arg).equals("GombaMoved")){
//                    updateGoombaPosition();
//                }
//                else if(((String)arg).equals("TortugaMoved")){
//                    updateTortugaPosition();
//                }
//                else if(((String)arg).equals("MushroomMoved")){
//                    updateMushroomPosition();
//                }
//                else if(((String)arg).equals("Collision")){
//                    playCollisionSound();
//                }
//
//            }
//
       }
    }

    private void playCollisionSound() {
        Media sound = new Media(this.getClass().getResource("").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void updateMushroomPosition() {
        mazeDisplayer.setMushroomPosition(viewModel.getMushroomPositionRowIndex(),viewModel.getMushroomPositionColIndex());
    }

//    private void updateTortugaPosition() {
//        mazeDisplayer.setTortugaPosition(viewModel.getTortugaPositionRowIndex(),viewModel.getTortugaPositionColIndex());
//    }
//
//    private void updateGoombaPosition() {
//        mazeDisplayer.setGoombaPosition(viewModel.getGoombaPositionRowIndex(),viewModel.getGoombaPositionColIndex());
//
//    }

    private void mazeIsSolved() {
        playSolvedMusic();
        openSolvedScene();
    }

    private void openSolvedScene() {

        try {
            Stage stage = new Stage();
            stage.setTitle("You Solved The Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("solvedScene.fxml").openStream());
            Scene scene = new Scene(root,400,200);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSolvedMusic() {
        stopThemeSong = true;
        themeMediaPlayer.stop();
        Media sound = new Media(this.getClass().getResource("").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

//    private void updateSolutionPath(ArrayList<AState> arg) {
//        playCoinCollected();
//        mazeDisplayer.changeSolutionPath(arg);
//        buttonSolveMaze.setDisable(false);
//    }

    private void playCoinCollected() {
        Media sound = new Media((this.getClass().getResource("").toString()));
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void updatePosition(Position arg) {
        characterPositionCol.set(arg.getColumnIndex()+"");
        characterPositionRow.set(arg.getRowIndex()+"");
        mazeDisplayer.setCharacterPosition(arg.getRowIndex(),arg.getColumnIndex());
    }

//    private void updateMaze(Maze arg) {
//        displayMaze(arg);
//        buttonGenerateMaze.setDisable(false);
//        buttonSolveMaze.setDisable(false);
//    }
/*
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mazeDisplayer.heightProperty().bind(borderPane_view.heightProperty().divide(1.1));
        mazeDisplayer.widthProperty().bind(borderPane_view.widthProperty().divide(1.1).add(-100));
        borderPane_view.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()*((double)4/(double)5));
        borderPane_view.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()*((double)2/(double)3));
    }
*/
    public void openAboutScene(ActionEvent actionEvent){
        try {
            //viewModel.stopServers();
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root,614,400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            //Stage currentStage = (Stage) buttonSolveMaze.getScene().getWindow();
//            if(themeMediaPlayer != null){
//                themeMediaPlayer.stop();
//            }
//            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeScene(ActionEvent actionEvent){
        Stage currentStage = (Stage) buttonSolveMaze.getScene().getWindow();
        currentStage.close();
        viewModel.stopServers();
        System.exit(0);
    }

//    public void openStart(ActionEvent actionEvent){
//        Stage currentStage = (Stage) buttonSolveMaze.getScene().getWindow();
//        currentStage.close();
//        try {
//            Stage stage = new Stage();
//            stage.setTitle("The Maze");
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            Parent root = fxmlLoader.load(getClass().getResource("Start.fxml").openStream());
//            StartSceneController startController = fxmlLoader.getController();
//            Scene scene = new Scene(root,407,400);
//            startController.setResizeEvent(scene);
//            stage.setScene(scene);
//            //stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mazeDisplayer.redraw();
            }
        });
    }
    public void reStartView(ActionEvent actionEvent){
        mazeDisplayer.redraw();
    }

    public void openSettingsScene(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            stage.setTitle("Settings");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Settings.fxml").openStream());
            SettingsSceneController settingsSceneController=fxmlLoader.getController();
            Scene scene = new Scene(root,614,400);
            settingsSceneController.setResizeEvent(scene);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            Stage currentStage = (Stage) buttonSolveMaze.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHelpScene(ActionEvent actionEvent){
        try {
            //viewModel.stopServers();
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("HelpScene.fxml").openStream());
            //SettingsSceneController settingsSceneController=fxmlLoader.getController();
            Scene scene = new Scene(root,600,400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mouseScrolled(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown()){
            if(scrollEvent.getY() < 0){
                //mazeDisplayer.zoomOut();
            }
            else {
               // mazeDisplayer.zoomIn();
            }
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if(mouseEvent.isControlDown()){
            if(startedDragging){
                mazeDisplayer.changeCursersPlace((mouseEvent.getX()-dragAndCtrlPreviousX)/dragAndCtrlPreviousX, (mouseEvent.getY()-dragAndCtrlPreviousY)/dragAndCtrlPreviousY);
                dragAndCtrlPreviousX = mouseEvent.getX();
                dragAndCtrlPreviousY = mouseEvent.getY();
            }
            else {
                if(startedDraggingWithCtrl){
                    if(enoughForMovement(mouseEvent,startX, startY)){
                        viewModel.moveCharacter(mouseEvent, startX, startY);
                        startX = mouseEvent.getX();
                        startY = mouseEvent.getY();
                    }
                }
            }
        }
    }

    private boolean enoughForMovement(MouseEvent mouseEvent, double startX, double startY) {
        boolean leftOrRight = Math.abs(mouseEvent.getX()-startX)>= mazeDisplayer.getCellWidth();
        boolean upOrDown = Math.abs(mouseEvent.getY()-startY) >= mazeDisplayer.getCellHeight();
        return leftOrRight || upOrDown;
    }

    public void dragDetected(MouseEvent mouseEvent) {
        if(mouseEvent.isControlDown()){
            dragAndCtrlPreviousX = mouseEvent.getX();
            dragAndCtrlPreviousY = mouseEvent.getY();
            startDragging = true;
        }
        else{
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
            startedDraggingWithCtrl = true;
        }
    }
}



