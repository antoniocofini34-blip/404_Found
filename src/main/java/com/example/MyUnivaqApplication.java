package com.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MyUnivaqApplication etends Application {
  @Override
    //Questo metodo l'abbiamo definito per far inizializzare ed eseguire automaticamente da JavaFX la finestra principale della nostra applicazione
    public void start(Stage stage) throws Exception {
    FXMLLoader loader=new FXMLLoader(MyUnivaqApplication.class.getResource("/viste/MainView.fxml")); //carichiamo il file FXML che contiene la struttura grafica della schermata principale
    Scene scene=new Scene(loader.load()); //carichiamo la schermata definita nel FXML e la inseriamo in una scena JavaFX
    stage.setTitle("UnivaqSound");
    stage.setScene(scene); //associamo alla finestra la scena che contiene l'interfaccia grafica appena caricata
    stage.setWidth(1000);
    stage.setHeight(700);
    //rendiamo visibile la finestra principale all'utente
    stage.show();
  }
  public static void main(String[] args) { launch(args); } //infine avviamo l'applicazione JavaFX che richiama il metodo start per inizializzare l'interfaccia
