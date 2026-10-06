package com.example.controller; 
import com.example.domain.Artista;
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.control.TableColumn; 
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
import java.io.IOException; 

public class LibreriaController {
  @FXML
  private TextField campoFiltro; 
  @FXML
  private TableView<Artista> tabellaArtisti; 
  @FXML
  private TableColumn<Artista, String> colonnaNome; 
  @FXML
  private TableColumn<Artista, String> colonnaGenere;
  @FXML
  private TableColumn<Artista, String> colonnaTipo;
  
  private Button bottoneIndietro; 
  @FXML
  private void initialize() {
    System.out.println("LIBRERIA CARICATA");
  }  
  private void tornaIndietro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml")); 
    Parent root=loader.load(); 
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow(); 
    stage.setScene(new Scene(root, 1000, 700)); 
  }
}
