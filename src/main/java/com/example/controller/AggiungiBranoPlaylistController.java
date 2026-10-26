package com.example.controller; 
import com.example.business.exception.BusinessException; 
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FilePlaylistServiceImpl;
import com.example.domain.Brano;
import com.example.domain.Playlist; 
import javafx.beans.property.SimpleStringProperty; //Gestisce stringhe reattive per il Data Binding
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;  //Importa la classe Button che rappresenta un pulsante cliccabile all'interno dell'interfaccia grafica
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;  //Serve a importare la classe FXMLLoader che viene usata per leggere file FXML
import javafx.scene.Parent;  //Superclasse di tutti i nodi di JavaFX che possono contenere elementi figli, rappresentando quindi la root
import javafx.scene.Scene;  //Rappresenta il contenitore finale di tutti i contenuti visivi della finestra
import javafx.stage.Stage;  //Importa la classe Stage che rappresenta la finestra principale dell'applicazione
import java.io.IOException;  //Rappresenta eventuali eccezioni di Input/Output che potrebbero verificarsi durante le operazioni di lettura o scrittura dei dati
import java.util.*; 
public class AggiungiBranoPlaylistController {
  @FXML
  private TableView<Brano> tabellaBrani;
  @FXML
  private TableColumn<Brano, String> colonnaTitoloBrano;
  @FXML
  private TableColumn<Brano, String> colonnaDurataBrano;
  @FXML
  private TableColumn<Brano, String> colonnaAlbumBrano;
  @FXML
  private Button bottoneConferma;
  @FXML
  private Label messaggioLabel;
  private FileBranoServiceImpl branoService;
  private FilePlaylistServiceImpl playlistService;
  private Playlist playlistSelezionata;
  @FXML
  private void initialize() {
    branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    playlistService=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt"); 
    configuraTabella(); 
    caricaBrani(); 
  }
  private void configuraTabella() {
    colonnaTitoloBrano.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getTitolo())); //Collega la colonna della tabella al campo "titolo" del brano
    colonnaDurataBrano.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getDurata()))); //Converte la durata del brano in testo e la mostra nella colonna tramite Lambda
    colonnaAlbumBrano.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getAlbumBrano()==null ? "" : cellData.getValue().getAlbumBrano().getTitolo())); //Mostra il titolo dell'album nella colonna, gestendo con un operatore ternario i brani senza album per evitare errori
  }
  private void caricaBrani() {
    try { List<Brano> brani=branoService.findAllBrani();
         tabellaBrani.setItems(FXCollections.observableArrayList(brani)); //Converte la lista dei brani in una lista osservabile in tempo reale
        } catch(BusinessException e) { messaggioLabel.setText("Errore nel caricamento dei brani:" + e.getMessage());
            }
        }
  public void impostaPlaylist(Playlist playlist) {
    playlistSelezionata=playlist;
  }
  @FXML
  private void confermaAggiunta() throws IOException {
    Brano branoSelezionato=tabellaBrani.getSelectionModel().getSelectedItem(); //Recupera l'oggetto Brano corrispondente alla riga attualmente selezionata nella tabella
    if (branoSelezionato==null) {
      messaggioLabel.setText("Seleziona un brano da aggiungere");
      return;
    }
    if (playlistSelezionata==null) {
      messaggioLabel.setText("Nessuna playlist selezionata");
      return;
    }
    try { playlistService.aggiungiBrano(playlistSelezionata, branoSelezionato);
         tornaAlDettaglio();
        } catch(BusinessException e) { messaggioLabel.setText("Errore:" + e.getMessage());
            }
        }
  private void tornaAlDettaglio() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/PlaylistDettaglioView.fxml")); //Prepara il caricamento del file FXML che definisce il layout della vista di dettaglio
    Parent root=loader.load();
    PlaylistDettaglioController controller=loader.getController(); //Recupera il controller della nuova schermata per poterle passare i dati
    controller.impostaPlaylist(playlistSelezionata); //Passa la playlist selezionata al controller della nuova schermata per visualizzarla
    Stage stage=(Stage) bottoneConferma.getScene().getWindow(); //Recupera la finestra (Stage) corrente dal pulsante di conferma per poterla gestire o chiudere
    stage.setScene(new Scene(root, 1000, 700)); //Imposta la nuova schermata nella finestra corrente con le suddette dimensioni
  }
  @FXML
  private void annulla() throws IOException {
    tornaAlDettaglio();
  }
}
