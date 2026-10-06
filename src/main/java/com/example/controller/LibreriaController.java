package com.example.controller; 
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.sceneParent; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.stage.Stage; 
import java.io.IOException; 

public class LibreriaController {
  @FXML
  private Button bottoneIndietro; 
  @FXML
  private void tornaIndietro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml")); 
    Parent root=loader.load(); 
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow(); 
    stage.setScene(new Scene(root, 1000, 700)); 
  }
}
