package com.example.controller; 
import com.example.business.BusinessException; 
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo; 
import com.example.domain.ArtistaSolista; 
import javafx.collections.FXCollections;
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
import java.util.*; 

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
  @FXML
  private Button bottoneIndietro; 
  private FileArtistaServiceImpl artistaService;
  @FXML
  private void initialize() {
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    colonnaNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(ottieniNomeArtista(cellData.getValue())));
    colonnaGenere.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(ottieniGenereArtista(cellData.getValue())));
    colonnaTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(ottieniTipoArtista(cellData.getValue())));
    caricaArtisti();
  }  
  privaye void caricaArtisti() {
  try {
    List<Artista> artisti=artistaService.findAllArtisti(); 
    tabellaArtisti.setItems(FXCollections.observableArrayList(artisti));
  }
    catch(BusinessException e) {
      System.out.println("Errore nel caricamento degli artisti: " + e.getMessage());
    }
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) {
      return ((ArtistaSolista) artista).getNomeArte();
    }
    else if (artista instanceof ArtistaGruppo) {
      return ((ArtistaGruppo) artista).getNomeGruppo();
    }
    return "";
  }
  private String ottieniGenereArtista(Artista artista) {
    if (artista.getGenerePrincipale()!=null) {
      return artista.getGenerePrincipale().toString();
    }
    return "";
  }
  private String ottieniTipoArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) {
      return "SOLISTA";
    }
    else if (artista instanceof ArtistaGruppo) {
      return "GRUPPO";
    }
    return "";
  }    
  private void tornaIndietro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml")); 
    Parent root=loader.load(); 
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow(); 
    stage.setScene(new Scene(root, 1000, 700)); 
  }
}
