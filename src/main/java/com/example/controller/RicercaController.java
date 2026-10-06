package com.example.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java-io-IOException;

public class RicercaController{
  @FXML
  private TextField campoRicerca;
  @FXML
  private Label risultatoLabel;
  @FXML
  private void(){
    String testo=campoRicerca.getText();
    if(testo==null||testo.trim().isEmpty()){
      risultatoLabel.setText("Inserisci qualcosa da cercare");
      return;
    }
    risultatoLabel.setText("Hai cercato:" + testo);
  }
  @FXML
  private void tornaIndietro() throws IOException{
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml");
    Parent root=loader.load();
    Stage stage=(Stage)campoRicerca.getScene().getWindow();
    stage.setScene(newScene(root, 1000, 700));
  }
}
