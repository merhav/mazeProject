package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpScene2Controller implements Initializable {

    private ImageView imageTortuga;
    private ImageView imageGoomba;
    @FXML
    private javafx.scene.control.Button buttonHelp2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Image tortuga = new Image(this.getClass().getResourceAsStream("/tortuga_left_01.png"));
            imageTortuga.setImage(tortuga);
            Image goomba = new Image(this.getClass().getResourceAsStream("/goomba_left_01.png"));
            imageGoomba.setImage(goomba);
        } catch (Exception e){
            //System.out.println(e.getMessage());
        }
    }


    public void openHelpScreen1(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("HelpScene.fxml").openStream());
            Scene scene = new Scene(root,600,400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            Stage currentStage = (Stage) buttonHelp2.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
