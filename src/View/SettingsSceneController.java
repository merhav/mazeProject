package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsSceneController implements Initializable {

    private MyViewModel myViewModel;
    @FXML
    private ImageView settings;
    @FXML
    private javafx.scene.layout.AnchorPane ancorPane;
    public javafx.scene.control.RadioButton RadioButtonBestAlgorithm;
    public javafx.scene.control.RadioButton RadioButtonSimpleAlgorithm;
    public javafx.scene.control.RadioButton RadioButtonBreadthFirdtSearch;
    public javafx.scene.control.RadioButton RadioButtonDepthFirstSearch;
    public javafx.scene.control.RadioButton RadioButtonBestFirstSearch;
    private ToggleGroup generatingAlgorithmGroup;
    private ToggleGroup solvingAlgorithmGroup;
    @FXML
    private javafx.scene.control.TextField textFiledNumOfThreads;
    @FXML
    private javafx.scene.control.Button buttonSaveSettings;


    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            generatingAlgorithmGroup = new ToggleGroup();
            solvingAlgorithmGroup = new ToggleGroup();
            Image image = new Image(getClass().getResourceAsStream("/settings.jpg"));
            settings.setImage(image);
            settings.fitHeightProperty().bind(ancorPane.heightProperty().divide(1.8));
            settings.fitWidthProperty().bind(ancorPane.widthProperty().divide(1.5));
            settings.xProperty().bind(ancorPane.widthProperty().divide(20));
            settings.yProperty().bind(ancorPane.heightProperty().divide(20));

            RadioButtonBestAlgorithm.setToggleGroup(generatingAlgorithmGroup);
            RadioButtonBestAlgorithm.setUserData("MyMazeGenerator");
            RadioButtonSimpleAlgorithm.setToggleGroup(generatingAlgorithmGroup);
            RadioButtonSimpleAlgorithm.setUserData("SimpleMazeGenerator");
            IMazeGenerator currGenerateAlgorithm = MyViewModel.getConfigurationGeneratingAlgorithm();
            if(currGenerateAlgorithm instanceof MyMazeGenerator){
                RadioButtonBestAlgorithm.setSelected(true);
            }
            else if (currGenerateAlgorithm instanceof SimpleMazeGenerator){
                RadioButtonSimpleAlgorithm.setSelected(true);
            }
            RadioButtonBreadthFirdtSearch.setToggleGroup(solvingAlgorithmGroup);
            RadioButtonBreadthFirdtSearch.setUserData("BreathFirstSearch");
            RadioButtonDepthFirstSearch.setToggleGroup(solvingAlgorithmGroup);
            RadioButtonDepthFirstSearch.setUserData("DepthFirstSearch");
            RadioButtonBestFirstSearch.setToggleGroup(solvingAlgorithmGroup);
            RadioButtonBestFirstSearch.setUserData("BestFirstSearch");
            for (Toggle toggle : solvingAlgorithmGroup.getToggles()){
                if(toggle.getUserData().toString().equals(currGenerateAlgorithm))
                    toggle.setSelected(true);
            }
            int amountOfThreads = MyViewModel.getConfigurationNumberOfThreads();
            textFiledNumOfThreads.setText(amountOfThreads+"");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStartPage(){
        try {
            Stage stage = new Stage();
            MyModel myModel = new MyModel();
            MyViewModel myViewModel = new MyViewModel(myModel);
            stage.setTitle("The Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
            Scene scene = new Scene(root,614,400);
            stage.setScene(scene);
            MyViewController view=fxmlLoader.getController();
            view.setResizeEvent(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            Stage currentStage = (Stage) buttonSaveSettings.getScene().getWindow();
            currentStage.close();
            SetStageCloseEvent(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SetStageCloseEvent(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    System.exit(0);//user chose OK
                }
                event.consume();//user chose to cancel
            }
        });
    }


    public void saveSettingsClicked(ActionEvent actionEvent){
        String selectedGeneratingAlgorithm = generatingAlgorithmGroup.getSelectedToggle().getUserData().toString();
        String selectedSolvingAlgorithm = solvingAlgorithmGroup.getSelectedToggle().getUserData().toString();
        String amountOfThreads = textFiledNumOfThreads.getText();
        boolean validText = true;
        int numOfThreads = 2;
        try {
            numOfThreads = Integer.parseInt(amountOfThreads);
            if (numOfThreads < 1 || numOfThreads > 20) {
                validText = false;
                showAlert("Amoun of threads must be minimum 1 and maximum 20");
            }
        } catch (Exception e){
            validText = false;
            showAlert("Amount of Threads must be an Integer");
        }
        if (validText){
            myViewModel.setConfig(numOfThreads,selectedGeneratingAlgorithm,selectedSolvingAlgorithm);
            showAlert("Settings have been changed successfully! go back to main menu");
        }
    }


    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(s);
        alert.setOnCloseRequest(event -> openStartPage());
        alert.show();
    }

    public void CancelSettingsClicked(ActionEvent actionEvent){
        showAlert("Settings not saved");
    }



}
