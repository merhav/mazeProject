package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutSceneController implements Initializable {
    @FXML
    private javafx.scene.control.Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void close() {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }
}
