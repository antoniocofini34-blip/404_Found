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
      protected void updateItem(Void item, boolean empty) {
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
      if (utente!=null && utente.getEmail()!=null) {
        String email=utente.getEmail();
        Brano brano=risultato.brano();
        if (brano!=null) numeroAscolti=PlayerStato.getNumeroAscoltiUtente(email, brano.getTitolo());  //Per un brano si mantiene il conteggio personale già funzionante
        else { Artista artista=risultato.artista();
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
 public static void aggiornaCronologiaVisibile(){
   Platform.runLate(()->{ if(istanza!=null){
     istanza.aggiornaCronologia();
   }
   });
 }
  private void riproduciBrano(Brano brano){
    try{
      if(brano==null){
        risultatoLabel.setText("Nessun brano selezionato");
        return;
      }
      PlayerStato.avvioBranoDaRicerca((brano);
      aggiornaCronologia();
      risultatoLabel.setText("In riproduzione:" + brano.getTitolo());
    } catch(Exception e){
      risultatoLabel.setText("Errore durante la riproduzione:" + e.getMessage());
    }
  }   
  @FXML
  private void cerca(){
    String testo=campoRicerca.getText();
    if(testo==null){
      testo="";
    }
    testo=testo.trim();
    try{
      FileAlbumServiceImpl servizioAlbum=new FileAlbumServiceImpl("src/main/resource/dati/album.txt");
      List<Album>tuttiAlbum=servizioAlbum.findAllAlbum();
      FileArtistaServiceImpl servizioArtista= new FileArtistaServiceImpl("src/main/resource/dati/artista.txt");
      List<Brano>brani=new ArrayList<>();
      List<Album>albumTrovati=new ArrayList<>();
      List<Artista>artistiTrovati=new ArrayList<>(); //Se il testo è vuoto, la ricerca parte da tutti i brani.
      if(testo.isEmpty()){
        artistiTrovati.addAll(serviziArtista.findAll(servizioArtista.findAll());
        albumTrovati.addAll(tuttiAlbum);
        for(Album album : tuttiAlbum){
          for(Brano brano : album.getBrani()){
            brano.setAlbumBrano(album);
            brani.add(brano);
          }
        }
        brani=rimuoviDuplicati(brani);
      } else{
        brani.addAll(ricercaService.cercaBrani(testo)); //Ricerca testuale dei brani.
        albumTrovati.addAll(ricercaService.cercaAlbum(testo));
        artistiTrovati.addAll(ricercaService.cercaArtista(testo));
        for(Artista artista : artistiTrovati){
          String nomeCercato=ottieniNomeArtistaRicerca(artista);
          if(nomeCercato==null) continue;
          for(Album album : tuttiAlbum){
            Artista artistaAlbum=album.getArtista();
            if(nomeArtistaAlbum!=null && nome.ArtistaAlbum.equalsIgnoreCase(nomeCercato)){
              if(!albumTrovati.contains(album)){
                if(!albumTrovati.add(album)){ albumTrovati.add(album) }
                for(Brano brano : album.getListaBrani()){
                  brani.setAlbumBrano(album);
                  if(!brani.contains(brano)){
                    brani.add(brano);
                  }  
                }
              }
            }
          }
          brani=rimuoviDuplicati(brani);
        }
        GenereMusicale genereScelto=filtroGenere.getValue();
        if(genereScelto!=null){
          List<Brano>braniFiltrati=new ArrayList<>();
          for(Brano brano : brani){
            if(brano.getGenereBrano().contains(genereScelto)){
              braniFiltrati.add(brano);
            }
          }
          brani=braniFiltrati;
          List<Album> albumFiltrati=new ArrayList<>();
          for(Album album : albumTrovati){
            if(album.getGenere().contains(genereScelto)){
              albumFiltrati.add(album);
            }
          }
          albumTrovati=albumFiltrati;
        }
        if(genereScelto!=null){
          artistiTrovati.removeIf(artista->!genereScelto.equals(artista.getGenerePrincipale()) && !arista.getGenereSecondario().contains(genereScelto));
        }
        GenereMusicale genereSelezionato=filtroGenere.getValue();
        if(genereSelezionato!=null){
          brani.removeIf(brano->brano.getGenereBrano==null || !brano.getGenereBrano().contains(genereSelezionato));
        }
        List<RisultatoRicerca>risultati=new ArrayList<>();
        for(Brano brano : brani){
          risultati.add(new RisultatoRicerca(brano));
        }
        for(Album album : albumTrovati){
          risultati.add(new RisultatoRicerca(album));
        }
        for(Artista artista : artistiTrovati){
          risultati.add(new RisultatoRicerca(artista));
        }
        tabellaBrani.getItems().setAll(risultati);
        risultatoBraniLabel.setText("Brani:" + (brani.size()));
        risultatoAlbumLabel.setText("Album:" + (albumTrovati.size()));
        risultatoArtistiLabel.setText("Artisti:" + (artistiTrovati.size()));
         risultatoLabel.setText(risultati.isEmpty() ? "Nessun risultato trovato con i criteri selezionati." : "Risultati trovati: " + risultati.size());
        } catch (BusinessException e) {
            risultatoLabel.setText("Errore nella ricerca: " + e.getMessage());
        }
    }
  private List<Brano> rimuoviDuplicati(List<Brano> brani) {
    List<Brano> risultati=new ArrayList<>();
    for (Brano brano : brani) {
      if (!risultati.contains(brano)) risultati.add(brano);
    }
    return risultati;
  }
  private void aggiungiAllaPlaylist(Brano brano) {
    try { FilePlaylistServiceImpl servizioPlaylist=new FilePlaylistServiceImpl("src/main/resources/dati/playlist.txt");
          List<Playlist> playlistDisponibili=servizioPlaylist.findAllPlaylist();
          if(playlistDisponibili.isEmpty()) {
            risultato.Label.setText("Non esistono playlist.");
            return;
          }
            List<String> nomiPlaylist=new ArrayList<>();
            for (Playlist playlist : playlistDisponibili) {
              nomiPlaylist.add(playlist.getNome());
            }
            ChoiceDialog<String> dialogo=new ChoiceDialog<>(nomiPlaylist.get(0), nomiPlaylist);
            dialogo.setTitle("Aggiungi alla playlist");
            dialogo.setHeaderText("Brano: " + brano.getTitolo());
            dialogo.setContentText("Scegli la playlist: ");
            Optional<String> scelta=dialogo.showAndWait();
            if (!scelta.isPresent()) return;
            for (Playlist playlist : playlistDisponibili) {
              if (playlist.getNome().equals(scelta.get())) {
                servizioPlaylist.aggiungiBrano(playlist, brano);
                risultatoLabel.setText("\"" + brano.getTitolo() + "\" aggiunto a \"" + playlist.getNome() + "\".");
                return;
              }
            }
            risultatoLabel.setText("Playlist non trovata.");
          } catch(BusinessException e) { risultato.Label.setText("Errore durante l'aggiunta: " + e.getMessage()); }
  }
  @FXML
  private void tornaIndietro() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml");
    Parent root=loader.load();
    Stage stage=(Stage) campoRicerca.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
  private String ottieniNomeArtistaRicerca(Artista artista) {
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return null;
  }
  private void applicaTemaCronologiaScuro() {
    if (cronologiaListView==null) return;
    boolean scuro=TemaVisivo.isScuro();
    String sfondo=scuro ? "#242424" : "#ffffff";
    String testo=scuro ? "#ffffff" : "#111111";
    String bordo=scuro ? "#454545" : "#cccccc";
    cronologiaListView.setStyle("-fx-control-inner-background: " + sfondo + ";" + "-fx-border-color:" + bordo + ";" + "-fx-text-fill: " + testo + ";");
    cronologiaListView.setCellFactory(lista -> new ListCell<String>() {
      @Override
      protected void updateItem(String elemento, boolean vuoto) {
        super.updateItem(elemento, vuoto);
        if (vuoto || elemento==null) setText(null);
        else setText(elemento);
        setTextFill(javafx.scene.paint.Color.web(testo));
        setStyle("-fx-background-color: " + sfondo + ";" + "-fx-text-fill: " + testo + ";");
      }
    });
    javafx.application.Platform.runLater(() -> {
      cronologiaListView.applyCss();
      String[] selettori= {".virtual-flow", ".clipped-container", ".sheet", ".viewport"};
      for (String selettore : selettori) {
        for (javafx.scene.Node nodo : cronologiaListView.lookupAll(selettore)) {
          nodo.setStyle("-fx-background-color: " + sfondo + ";");
        }
      }
    });
  }
  private void aggiornaCronologia() {
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) {
      utenteCronologiaLabel.setText("Nessun utente selezionato.");
      cronologiaListView.getItems().clear();
      return;
    }
    String email=utente.getEmail().trim();
    List<String> ascolti=CronologiaAscoltiStorage.leggi(email);
    cronologiaListView.getItems().setAll(ascolti);
    if (ascolti.isEmpty()) utenteCronologiaLabel.setText("Nessun ascolto registrato per " + email);
    else utenteCronologiaLabel.setText("Cronologia di " + email + "-" + ascolti.size() + "sessioni");
  }
