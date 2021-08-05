package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.scenario.Settings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartSceneController implements Initializable {


    @FXML
    private javafx.scene.control.Button buttonStart = new Button();
    @FXML
    private javafx.scene.layout.AnchorPane anchorPane;
    private DoubleProperty textSize = new SimpleDoubleProperty();
    @FXML
    private javafx.scene.image.ImageView imageViewSettings;
    @FXML
    private javafx.scene.image.ImageView imageViewBackground;


    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                buttonStart.setLayoutX(anchorPane.getWidth());
                buttonStart.setFont(new Font(buttonStart.getFont().getName(),textSize.doubleValue()));
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                buttonStart.setLayoutX(anchorPane.getHeight());
                buttonStart.setFont(new Font(buttonStart.getFont().getName(),textSize.doubleValue()));
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputStream is = this.getClass().getResourceAsStream("/logo.png");
        Image background = new Image(is);
        imageViewBackground.setImage(background);
        Image settings = new Image(this.getClass().getResourceAsStream("/settings.jpg"));
        imageViewSettings.setImage(settings);
        imageViewBackground.fitHeightProperty().bind(anchorPane.heightProperty());
        imageViewBackground.fitWidthProperty().bind(anchorPane.widthProperty());
        imageViewSettings.fitHeightProperty().bind(anchorPane.heightProperty().divide(8));
        imageViewSettings.fitWidthProperty().bind(anchorPane.widthProperty().divide(8));

        buttonStart.prefHeightProperty().bind(anchorPane.heightProperty().divide(10));
        buttonStart.prefWidthProperty().bind(anchorPane.widthProperty().divide(3));
        textSize.bind(buttonStart.heightProperty().divide(2));

    }

    public void openSettings(){
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
            Stage currentStage = (Stage) buttonStart.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openViewScene(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            stage.setTitle("The Maze");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            double setX = (primScreenBounds.getWidth()-stage.getWidth());
            double setY = (primScreenBounds.getHeight()-stage.getHeight());
            //SettingsSceneController settingsSceneController=fxmlLoader.getController();
            Scene scene = new Scene(root,1400,1000);
            //settingsSceneController.setResizeEvent(scene);
            scene.getStylesheets().add(getClass().getResource("").toExternalForm());
            stage.setScene(scene);

            MyModel myModel = new MyModel();
            MyViewModel myViewModel = new MyViewModel(myModel);
            myModel.addObserver(myViewModel);
            MyViewController viewController = fxmlLoader.getController();
            viewController.setResizeEvent(scene);
            viewController.setViewModel(myViewModel);
            setStageCloseEvent(stage,viewController);
            stage.show();
            final Node source = (Node)actionEvent.getSource();
            final Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStageCloseEvent(Stage stage, MyViewController viewController) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    viewController.closeScene(null);
                    System.exit(0);//user chose OK
                }
                event.consume();//user chose to cancel
            }

        });
    }


}
