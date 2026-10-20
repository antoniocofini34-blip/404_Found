package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FilePlaylistService;
import com.example.domain.Brano;
import com.example.domain.Playlist;
import com.example.business.impl.file.FileAlbumService;
import com.example.domain.Album;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tableview;
import javafx.stage.Stage
import javafx.application.Platform;
import com.example.controller.Naavigazione;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class PlaylistDettaglioController{
  @FXML
  private Label titoloPlaylist;
  @FXML
  private Label descrizionePlaylist;
  @FXML
  private TableView<Brano> tabellaBrani;
  @FXML
  private TableColumn<Brano, String> colonnaTitoloBrano;
  @FXML
  private TableColumn<Brano, String> colonnaDurataBrano;
  @FXML
  private Button bottoneIndietro;
  @FXML
  private Button bottoneAggiungiBrano;
  @FXML
  private Button bottoneRimuoviBrano;
  @FXML
  private Button bottoneSpostaSu;
  @FXML
  private Button bottoneSpostaGiu;
  private FilePlaylistServiceImpl playlistService;
  private Playlist playlistSelezionata;
  @FXML
  private void inizialize(){ playlistService=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt");
    configuraTabella();
    Platform.runLater(this::aggiornaTemaPlaylist);  //Sincronizza l'aggiornamento grafico del tema sul thread principale di JavaFX per evitare errori
  }
    private void configuraTabella(){ colonnaTitoloBrano.setCellValueFactory(cellData->new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitolo()));
      colonnaDurataBrano.setCellValueFactory(cellData->new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getTitolo())));
    }
    public void ImpostaPlaylist(Playlist playlist){ playlistSelezionata=playlist;
      titoloPlaylist.setText(playlist.getNome());
      descrizionePlaylist.setText(playlist.getDescriziono());
      try{ List<Brano> brani= playlistService.getBrani(playlist);
        tabellaBrani.setItems(FXCollections.observableArrayList(brani));
      } catch(BusinessException e){ e.getMessage(); }
    }
  @FXML
  private void rimuoviBrano() { Brano branoSelezionato = tabellaBrani.getSelectionModel().getSelectedItem();  //Recupera l'oggetto Brano selezionato dall'utente nella tabella
        if (branoSelezionato == null) { System.out.println("Seleziona un brano da rimuovere.");
            return;
        }
        try { playlistService.rimuoviBrano(playlistSelezionata, branoSelezionato);
            impostaPlaylist(playlistSelezionata);
        } catch (BusinessException e) { e.getMessage(); }
    }
    @FXML
    private void spostaSu() { int indice = tabellaBrani.getSelectionModel().getSelectedIndex();  //Recupera l'indice numerico della riga selezionata nella tabella
        if (indice <= 0) return;
        try { playlistService.riordinaBrani(playlistSelezionata, indice, indice - 1);
            impostaPlaylist(playlistSelezionata);
            tabellaBrani.getSelectionModel().select(indice - 1);
        } catch (BusinessException e) { e.getMessage(); }
    }
    @FXML
    private void spostaGiu() { int indice = tabellaBrani.getSelectionModel().getSelectedIndex();
        if (indice < 0 || indice >= tabellaBrani.getItems().size() - 1) return;
        try { playlistService.riordinaBrani(playlistSelezionata, indice, indice + 1);
           impostaPlaylist(playlistSelezionata);
           tabellaBrani.getSelectionModel().select(indice + 1);
        } catch (BusinessException e) { System.out.println("Errore durante lo spostamento: " + e.getMessage()); }
    }
    @FXML
    private void tornaIndietro() { boolean tornatoIndietro = Navigazione.indietro();
        if (!tornatoIndietro) { System.out.println("Nessuna schermata precedente registrata."); }
    }
  @FXML
  private void aggiungiBrano() throws IOException{ FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/AggiungiBranoPLaylistView.fxml"));
     Parent root=loader.load();
     AggiungiBranoPlaylistController controller=loader.getController();
     controller.impostaPlaylist(playlistSelezionata);
     Stage stage=(Stage) bottoneAggiungiBrano.getScene().getWindow();
     stage.setScene(new Scene(root, 1000, 700));
  }
  private void associaAlbumAiBrani(List<Brano> brani) throws BusinessException{ FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    List<Album> albumDisponibili=albumService.findAllAlbum();
    for(Brano brano : brani){
      if(brano==null || brano.getTitolo()==null) continue;
      brano.setAlbumBrano(null);  //Elimina eventuali associazioni mancanti o obsolete.
      for(Album album : albumDisponibile){
        if(album==null) continue;
        for(Brano branoAlbum : album.getListaBrani()){
          if(branoAlbum!=null && branoAlbum.getTitolo()!=null && branoAlbum.getTitolo.trim().equalsIgnoreCase(brano.getTitolo().trim())){ brano.setAlbumBrano(album);
          break;
          } 
        }
        if(brano.getAlbumBrano()!=null) break;
      }
  }
  @FXML
  private void riproduciPlaylistDaSelezionato() {
        Brano selezionato = tabellaBrani.getSelectionModel().getSelectedItem();
        int indice = tabellaBrani.getSelectionModel().getSelectedIndex();
        if (playlistSelezionata == null || selezionato == null || indice < 0) { mostraMessaggio("Seleziona un brano da cui iniziare.");
            return;
        }
        try { List<Brano> brani = playlistService.getBrani(playlistSelezionata);
            // Associa l'album a tutti i brani prima di avviare la coda.
            associaAlbumAiBrani(brani);
            PlayerStato.avviaPlaylist(brani, indice);
            apriPlayer();
        } catch (BusinessException | IOException e) { mostraMessaggio("Errore durante l'avvio della playlist: " + e.getMessage()); }
    }
    @FXML
    private void riproduciSoloBranoSelezionato() { Brano selezionato = tabellaBrani.getSelectionModel().getSelectedItem();
        if (selezionato == null) { mostraMessaggio("Seleziona un brano da riprodurre.");
            return;
        }
        try { List<Brano> braniDaAssociare = new java.util.ArrayList<>();
            braniDaAssociare.add(selezionato);
            associaAlbumAiBrani(braniDaAssociare); // Recupera album e artista anche per il singolo brano.
            PlayerStato.avviaSoloBrano(selezionato);
            apriPlayer();
        } catch (BusinessException | IOException e) { mostraMessaggio("Errore durante la riproduzione: " + e.getMessage()); }
    }
  private void apriPlayer() throws IOException{ FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/PlayerView.fxml"));
    Parent root=loader.load();
    TemaVisivo.applicaTema(root);
    root.setStyle("-fx.backgr-color:" + TemaVisivo.getColoreSfondo() + ";");
    PlayerController controller = loader.getController();
    Stage stage = (Stage) bottoneIndietro.getScene().getWindow();
    Scene scenaPrecedente = stage.getScene();ù
    Navigazione.salvaSchermataPrecedente(() -> stage.setScene(scenaPrecedente));
    stage.setScene(new Scene(root, 1000, 700));
    controller.impostaAperturaDaPlaylist(true);
    }
    private void mostraMessaggio(String messaggio) { Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Riproduzione");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    private void aggiornaTemaPlaylist() {
        if (titoloPlaylist == null || titoloPlaylist.getScene() == null) return;
        Parent radice = titoloPlaylist.getScene().getRoot();
        TemaVisivo.applicaTema(radice);
        radice.setStyle("-fx-background-color: " + TemaVisivo.getColoreSfondo() + ";");
    }    
}
