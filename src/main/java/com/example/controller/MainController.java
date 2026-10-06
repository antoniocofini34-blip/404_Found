package com.example.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException; 
public class MainController {
  @FXML
  private Label titoloLabel;
  @FXML
  private void initialize() {
    titoloLabel.setText("UnivaqSound");
  }
  @FXML
  private void apriRicerca() throws IOException {
    FXMLLoader loader=new FXML(getClass().getResource("/viste/RicercaView.fxml"));
    Parent root=loader.load(); 
    Stage stage=(Stage) titoloLabel.getScene().getWindow(); 
    stage.setScene(new Scene(root, 1000, 700)); 
  }
}
