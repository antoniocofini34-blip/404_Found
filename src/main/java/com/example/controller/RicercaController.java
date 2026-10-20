package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.RicercaService;
import com.example.business.impl.file.FIleAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileRicercaServiceImpl;
import com.example.business.impl.file.FilePlaylistService;
import com.example.domain.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.*; 
public class RicercaController{
  @FXML
  private TextField campoRicerca;
  @FXML
  private Label risultatoLabel;
  @FXML
  private Label risultatoBraniLabel;
  @FXML
  private Label risultatoBraniLabel;
  @FXML
  private Label risultatoArtistiLabel;
  @FXML
  private ComboBox<GenereMusicale> filtroGenere;
  @FXML
  private TableView<RisultatoRicerca> tabellaBrani;
  @FXML
  private TableColumn<RisultatoRicerca, String> colonnaTipo;
  @FXML
  private TableColumn<RisultatoRicerca, String> colonnaTitolo;
  @FXML
  private TableColumn<RisultatoRicerca, String> colonnaDurata;
  @FXML
  private TableColumn<RisultatoRicerca, String> colonnaGenere;
  @FXML
  private TableColumn<RisultatoRicerca, Void> colonnaRiproduci;
  @FXML
  private TableColumn<RisultatoRicerca, Integer> colonnaDataPubblicazione;
  @FXML
  private TableColumn<RisultatoRicerca, Integer> colonnaNumeroAscolti; 
  @FXML
  private Label utenteCronologiaLabel;
  @FXML
  private ListView<String> cronologiaListView;
  private RicercaService ricercaService;
  private static RicercaController istanza;
  @FXML
  private void initialize(){
    filtroGenere.getItems().setAll(GenereMusicale.values()); 
    istanza=this;
    FileBranoServiceImpl branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    FileArtistaServiceImpl artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artista.txt");
    ricercaService=new FileRicercaServiceImpl(branoService, albumService, artistaService);
    colonnaTipo.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getTipo()));  //Colonne della tabella unica
    colonnaTitolo.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getTitolo()));
    colonnaDurata.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getDurata()));
    colonnaGenere.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getGenere()));
    colonnaRiproduci.setCellFactory(colonna -> new TableCell<RisultatoRicerca, Void>() {
      private final Button bottone=new Button("Riproduci");
      private final Button bottonePlaylist=new Button("+ Playlist");
      private final HBox contenitore=new HBox(5, bottone, bottonePlaylist); 
        { 
          bottone.SetOnAction(event -> {
            RisultatoRicerca risultato=getTableView().getItems().get(getIndex());
            Brano brano= risultato.brano();
            if (brano!=null) riproduciBrano(brano);
          });
          bottonePlaylist.setOnAction(event -> {
            RisultatoRicerca risultato=getTableView().getItems().get(getIndex());
            Brano brano=risultato.brano();
            if (brano!=null) aggiungiAllaPlaylist(brano);
            }  
          });
        }
      @Override 
      protected void updateIte(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
          return;
          }
        RisultatoRicerca risultato=getTableView().getItems().get(getIndex());
        boolean eBrano=risultato.brano()!=null;
        setGraphic(eBrano ? contenitore : null);
        }
      });
    colonnaDataPubblicazione.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAnnoPubblicazione()).asObject());
    colonnaNumeroAscolti.setCellValueFactory(cellData -> {
      RisultatoRicerca risultato=cellData.getValue();
      Utente utente=UtenteAttivo.getUtente();
      int numeroAscolti=0;
      if (uente!=null && utente.getEmail()!=null) {
        String email=utente.getEmail();
        Brano brano=risultato.brano();
        if (brano!=null) numeroAscolti=PlayerStato.getNumeroAscoltiUtente(email, brano.getTitolo());  //Per un brano si mantiene il conteggio personale già funzionante
        else {
          Artista artista=risultato.artista();
          if (artista!=null) {
            try {  FileBranoServiceImpl servizioBrani=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
                   FileAlbumServiceImpl servizioAlbum=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
                   List<Brano> tuttiBrani=servizioBrani.findAllBrani();
                   List<Album> tuttiAlbum=servizioAlbum.findAllAlbum();
                   String nomeArtista=null;
                   if (artista instanceof ArtistaSolita) nomeArtista=((ArtistaSolista) artista).getNomeArte();
                   else if (artista instanceof ArtistaGruppo) nomeArtista=((ArtistaGruppo) artista).getNomeGruppo();
                   if (nomeArtista!=null) {
                     for (Brano branoCatalogo : tuttiBrani) {
                       if (branoCatalogo==null || branoCatalogo.getTitolo()==null) continue;
                       int ascolti=PlayerStato.getNumeroAscoltiUtente(email, branoCatalogo.getTitolo());
                       if (ascolti > 0) continue;
                       for (Album album : tuttiAlbum) {
                         if (album==null || album.getArtista()==null || album.getListaBrani()==null) continue;
                         Artista artistaAlbum=album.getArtista();
                         String nomeAlbumArtista=null;
                          if (artistaAlbum instanceof ArtistaSolita) nomeAlbumArtista=((ArtistaSolista) artistaAlbum).getNomeArte();
                          else if (artistaAlbum instanceof ArtistaGruppo) nomeAlbumArtista=((ArtistaGruppo) artistaAlbum).getNomeGruppo();
                       }
                       if (nomeAlbumArtista==null || !nomeAlbumArtista.trim().equalsIgnoreCase(nomeArtista.trim())) continue;
                       boolean branoAppartieneAdAlbum=album.getListaBrani().stream().anyMatch(b -> b!=null && b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(branoCatalogo.getTitolo()));
                       if (branoAppartieneAdAlbum) {
                         numeroAscolti += ascolti;
                         break;
                       }
                     }
                   }
                }
          } catch(BusinessException e) { e.getMessage(); }
      }
    }
  }
        return new javafx.beans.property.SimpleIntegerProperty(numeroAscolti).asObject();
  });
  aggiornaCronologia();
  applicaTemaCronologiaScuro();
}
  
                                             




      
  @FXML
  private void cerca(){
    String testo=campoRicerca.getText();
    if(testo==null||testo.trim().isEmpty()){
      risultatoLabel.setText("Inserisci qualcosa da cercare");
      return;
    }
    try{
      List<Brano>brani=ricercaService.cercaBrani(testo);
      List<Album>album=ricercaService.cercaAlbum(testo);
      List<Artista>artista=ricercaServic.cercaArtista(testo);
      StringBuilder risultato=new StringBuilder();
      risultato.append("Risultati trovati:\n\n");
      risultato.append("Brani: ").append(brani.size()).append("\n");
      for (Brano brano : brani) {
        risultato.append("- ").append(brano.getTitolo()).append("\n");
      }
      risultato.append("\nAlbum: ").append(album.size()).append("\n");
      for (Album a : album) {
        risultato.append("- ").append(a.getTitolo()).append("\n");
      }
      risultato.append("\nArtisti: ").append(artisti.size()).append("\n");
      for (Artista artista : artisti) {
        risultato.append("- Artista trovato\n");
      }
        risultatoLabel.setText(risultato.toString());
      } catch (BusinessException e) {
      risultatoLabel.setText("Errore nella ricerca: " + e.getMessage());
      }
  }
  @FXML
  private void tornaIndietro() throws IOException{
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage)campoRicerca.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
}
