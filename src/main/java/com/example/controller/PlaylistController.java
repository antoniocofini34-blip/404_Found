package com.example.controller; 
import com.example.business.BusinessException; 
import com.example.business.impl.file.FilePlaylistServiceImpl; 
import com.example.domain.Playlist; 
import javafx.collections.FXCollections;
import javafx.fxml.FXML; 
import javafx.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import java.scene.control.TableView;
import javafx.stage.Stage; 
import java.io.IOException; 
import java.util.*; 

public class PlaylistController {
  @FXML
  private TableView<Playlist> tabellaPlaylist; 
  @FXML
  private TableColumn<Playlist, String> colonnaNomePlaylist; 
  @FXML
  private TableColumn<Playlist, String> colonnaDescrizionePlaylist;
  @FXML
  private TableColumn<Playlist, String> colonnaDataCreazionePlaylist; 
  @FXML
  private Button bottoneIndietro; 

  private FilePlaylistServiceImpl playlistService; 

  @FXML
  private void initialize() {
    playlistService=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt"); 
    configuraTabella(); 
    caricaPlaylist(); 
  }

  private void configuraTabella() {
    colonnaNomePlaylist.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
    colonnaDescrizionePlaylist.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescrizione()));
    colonnaDataCreazionePlaylist.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDataCreazione())));
  }
  private void caricaPlaylist() {
    try {
      List<Playlist> playlist=playlistService.findAllPlaylist();
      tabellaPlaylist.setItems(FXCollections.observableArrayList(playlist)); 
    }
    catch(BusinessException e) {
      System.out.println("Errore nel caricamento della playlist:" + e.getMessage()); 
    }
  }
  @FXML
  private void tornaIndietro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load(); 
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow(); 
    stage.setScene(new Scene(root, 1000, 700)); 
  }
}
  
