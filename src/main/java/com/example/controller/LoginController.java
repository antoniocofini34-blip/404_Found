package com.example.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class LoginController {
  @FXML
  private TextField campoEmail;
  @FXML
  private PasswordField campoPassword;
  @FXML
  private Label messaggioErrore;
  @FXML
  private void accedi() {
    String email=campoEmail.getText();
    String password=campoPassword.getText();
    if (UtenteAttivo.accediAmministratore(email,password)) {
      Stage finestra=(Stage) campoEmail.getScene().getWindow();
      finestra.close();
    } else {
      messaggioErrore.setText("Email o password non corrette");
      campoPassword.clear();
    }
  }
  @FXML
  private void annulla() {
    Stage finestra=(Stage) campoEmail.getScene().getWindow();
    finestra.close();
  }
}
