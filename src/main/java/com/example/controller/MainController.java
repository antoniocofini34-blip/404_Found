package com.example.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class MainController {
  @FXML
  private Label titoloLabel;
  @FXML
  private void initialize() {
    titoloLabel.setText("UnivaqSound");
  }
}
