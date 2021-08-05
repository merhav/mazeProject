package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.management.relation.RoleList;
//import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpSceneController implements Initializable {

    private javafx.scene.image.ImageView imageViewHelp;
    @FXML
    private javafx.scene.control.Button buttonHelp;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Image image = new Image(this.getClass().getResourceAsStream("/help sign.png"));
            imageViewHelp.setImage(image);    
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
         
    }
    
    public void openHelpScreen2(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("HelpScene2.fxml").openStream());
            Scene scene = new Scene(root,670,400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            Stage currentStage = (Stage) buttonHelp.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
