package com.example.controller; 
import com.example.business.exception.BusinessException; 
import com.example.business.impl.file.FilePlaylistServiceImpl; 
import com.example.domain.Playlist; 
import com.example.domain.Brano;
import com.example.domain.Utente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML; 
import javafx.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage; 
import com.example.controller.Navigazione;
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
  private TableColumn<Playlist, String> colonnaDurataTotalePlaylist;
  @FXML
  private Button bottoneIndietro; 
  @FXML
  private Button bottoneNuovaPlaylist;
  @FXML
  private Button bottoneModificaPlaylist;
  @FXML
  private Button bottoneEliminaPlaylist;
  @FXML
  private Button bottoneDettagliPlaylist;
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
    colonnaDurataTotalePlaylist.setCellValueFactory(cellData -> {
      Playlist playlist=cellData.getValue();
      int durataTotale=0;
      try { List<Brano> brani=playlistService.getBrani(playlist);
            for (Brano brano : brani) {
              durataTotale +=brano.getDurata();
            }
          } catch(BusinessException e) { e.getMessage(); }
      return new javafx.beans.property.SimpleStringProperty(String.valueOf(durataTotale));
    });      
  }
  private void caricaPlaylist() {
    try {  Utente utente=UtenteAttivo.getUtente();
           if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) {
             tabellaPlaylist.setItems(FXCollections.observableArrayList());
             return;
           }
         String emailUtente=utente.getEmail().trim();
         List<Playlist> tutteLePlaylist=playlistService.findAllPlaylist();
         List<Playlist> playlistUtente=new ArrayList<>();
         for (Playlist playlist : tutteLePlaylist) {
           String proprietario=playlist.getProprietarioEmail();
           if (proprietario!=null && proprietario.trim().equalsIgnoreCase(emailUtente)) playlistUtente.add(playlist);
         }
         tabellaPlaylist.setItems(FXCollections.observableArrayList(playlistUtente));
        } catch(BusinessException e) { e.getMessage(); }
  }
  @FXML
  private void nuovaPlaylist() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/PlaylistForm.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage) bottoneNuovaPlaylist.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
  @FXML
  private void modificaPlaylist() throws IOException {
    Playlist playlistSelezionata=tabellaPlaylist.getSelectionModel().getSelectedItem();
    if (playlistSelezionata==null) {
      System.out.println("Seleziona una playlist da modificare.");
      return;
    }
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/PlaylistForm.fxml"));
    Parent root=loader.load();
    PlaylistFormController controller=loader.getController();
    controller.impostaPlaylist(playlistSelezionata);
    Stage stage=(Stage) bottoneModificaPlaylist.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
  @FXML
  private void eliminaPlaylist() {
    Playlist playlistSelezionata=tabellaPlaylist.getSelectionModel().getSelectedItem();
    if (playlistSelezionata==null) {
      System.out.println("Seleziona una playlist da eliminare.");
      return;
    }
    Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
    conferma.setTitle("Conferma eliminazione");
    conferma.setHeaderText("Eliminazione playlist");
    conferma.setContentText("Sei sicuro di voler eliminare la playlist \"" + playlistSelezionata.getNome() + "\"?");
    Optional<ButtonType> risultato=conferma.showAndWait();
    if (risultato.isPresent() && risultato.get()==ButtonType.OK) {
      try { playlistService.eliminaPlaylist(playlistSelezionata);
           caricaPlaylist();
          } catch(BusinessException e) { e.getMessage(); }
    }
  }
  @FXML
  private void visualizzaBrani() throws IOException {
    Playlist playlistSelezionata=tabellaPlaylist.getSelectionModel().getSelectedItem();
    if (playlistSelezionata==null) {
      System.out.println("Seleziona una playlist.");
      return;
    }
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/PlaylistDettaglioView.fxml"));
    Parent root=loader.load();
    PlaylistDettaglioController controller=loader.getController();
    controller.impostaPlaylist(playlistSelezionata);
    Stage stage=(Stage) bottoneDettagliPlaylist.getScene().getWindow();
    Scene scenaPrecedente=stage.getScene();
    Navigazione.salvaSchermataPrecedente(() -> stage.setScene(scenaPrecedente));
    stage.setScene(new Scene(root, 1000, 700));
  }
  @FXML
  private void tornaIndietro() {
    Navigazione.indietroHome();
  }
}
