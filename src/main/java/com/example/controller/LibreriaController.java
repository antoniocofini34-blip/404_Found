package com.example.controller; 
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo; 
import com.example.domain.ArtistaSolista;
import com.example.domain.Brano;
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
  private TableView<Album> tabellaAlbum;
  @FXML
  private TableColumn<Album, String> colonnaTitoloAlbum;
  @FXML
  private TableColumn<Album, String> colonnaAnnoAlbum;
  @FXML
  private TableColumn<Album, String> colonnaArtistaAlbum;
  @FXML
  private TableView<Brano> tabellaBrani;
  @FXML
  private TableColumn<Brano, String> colonnaTitoloBrano;
  @FXML
  private TableColumn<Brano, String> colonnaDurataBrano;
  @FXML
  private TableColumn<Brano, String> colonnaAlbumBrano;
  @FXML
  private Button bottoneIndietro;
  private FileArtistaServiceImpl artistaService;
  private FileAlbumServiceImpl albumService;
  private FileBranoServiceImpl branoService;
  private List<Artista> tuttiGliArtisti;
  private List<Album> tuttiGliAlbum;
  private List<Brano> tuttiIBrani;
  @FXML
  private void initialize() {
    //i servizi li abbiamo collegati ai file di testo che contengono i dati di artisti, album e brani
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    //qui andiamo a definire come vengono mostrati i dati nelle tabelle e poi carichiamo i contenuti così da attivare le iterazioni
    configuraTabellaArtisti();
    configuraTabellaAlbum();
    configuraTabellaBrani();
    caricaArtisti();
    caricaAlbum();
    caricaBrani();
    configuraSelezioneArtista();
    configuraSelezioneAlbum();
    configuraFiltro();
  }  
  private void configuraTabellaArtisti() {
    //qui andiamo a usare delle lambda che trasformano ogni artista in un valore testuale così da poterlo visualizzare nella rispettiva colonna 
    colonnaNome.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(ottieniNomeArtista(cellData.getValue())));
    colonnaGenere.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(ottieniGenereArtista(cellData.getValue())));
    colonnaTipo.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(ottieniTipoArtista(cellData.getValue())));
  }
  private void configuraTabellaAlbum() {
    //le lambda estraggono dall'album il titolo, l'anno convertito in testo e il nome del suo artista
    colonnaTitoloAlbum.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitolo()));
    colonnaAnnoAlbum.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getAnnoPubblicazione())));
    colonnaArtistaAlbum.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(ottieniNomeArtista(cellData.getValue().getArtista())));
  }
  private void configuraTabellaBrani() {
    //le lambda estraggono dal brano il titolo, la durata convertita in testo e il titolo del suo album
    colonnaTitoloBrano.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitolo()));
    colonnaDurataBrano.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getDurata())));
    colonnaAlbumBrano.setCellValueFactory(cellData-> new javafx.beans.property.SimpleStringProperty(ottieniTitoloAlbum(cellData.getValue().getAlbumBrano())));
  }
  private void caricaArtisti() {
    try { //qui andiamo a recuperare tutti gli artisti dal file e li convertiamo in una lista osservabile così da aggiornare la tabella di JAVAFX
      tuttiGliArtisti=artistaService.findAllArtisti();
      tabellaArtisti.setItems(FXCollections.observableArrayList(tuttiGliArtisti)); //se il servizio non riesce a recuperare gli artisti, viene stampato un messaggio di errore
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli artisti: " + e.getMessage()); }
  }
  private void caricaAlbum() {
    try {//stesso concetto degli artisti anche qui
      tuttiGliAlbum=albumService.findAllAlbum();
      tabellaAlbum.setItems(FXCollections.observableArrayList(tuttiGliAlbum));
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli album: " + e.getMessage()); }
  }
  private void caricaBrani() {
    try { //in questo caso carichiamo tutti i brani e gli album così da ricostruire il collegamento tra ciascun brano e il suo album
      tuttiIBrani=branoService.findAllBrani();
      tuttiGliAlbum=albumService.findAllAlbum();
      for (Brano brano:tuttiIBrani) {//azzeriamo l'album associato al brano così da evitare di mantenere un collegamento non corretto
        brano.setAlbumBrano(null);
        for (Album album:tuttiGliAlbum) {
          for (Brano branoAlbum:album.getListaBrani()) {//si confrontano i titolo ignorando maiuscole e minuscole dopo aver verficiato per entrambi che non siano null
            if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
              brano.setAlbumBrano(album);
              break;
            }
          } //trovato l'album del brano, interrompiamo anche la ricerca negli album successivi
          if (brano.getAlbumBrano()!=null) break;
        }
      } //trasformiamo la lista completa dei brani in una lista osservabile
      tabellaBrani.setItems(FXCollections.observableArrayList(tuttiIBrani));
    } catch (BusinessException e) {System.out.println("Errore nel caricamento dei brani: " + e.getMessage()); }
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista==null) return "";
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    else if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "";
  }
  private String ottieniGenereArtista(Artista artista) {
    if (artista!=null&&artista.getGenerePrincipale()!=null) return artista.getGenerePrincipale().toString();
    return "";
  }
  private String ottieniTipoArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) return "SOLISTA";
    else if (artista instanceof ArtistaGruppo) return "GRUPPO";
    return "";
  }
  private String ottieniTitoloAlbum(Album album) {
    if (album==null) return "";
    return album.getTitolo();
  }
  private void configuraSelezioneArtista() {
    //il listener rileva ogni cambiamento dell'artista selezionato così da aggiornare gli album e i brani da mostrare
    tabellaArtisti.getSelectionModel().selectedItemProperty().addListener((observable, vecchioArtista, nuovoArtista) -> {
      if (nuovoArtista==null) {
        tabellaAlbum.getItems().clear();
        tabellaBrani.getItems().clear();
        return;
      }
      caricaAlbumArtista(nuovoArtista); });
  }
  private void configuraSelezioneAlbum() {
    //qui il listener aggiorna la tabella dei brani ogni volta che cambia l'album selezionato
    tabellaAlbum.getSelectionModel().selectedItemProperty().addListener((observable, vecchioAlbum, nuovoAlbum) -> {
      if (nuovoAlbum==null) {
        tabellaBrani.getItems().clear();
        return;
      }
      caricaBraniAlbum(nuovoAlbum); });
  }
  private void caricaBraniAlbum(Album album) {
    if (album==null) { //in questo caso la tabella viene svuotata assegnandole una lista osservabile vuota
      tabellaBrani.setItems(FXCollections.observableArrayList());
      return;
    }
    List<Brano> braniAlbum=album.getListaBrani();
    if (braniAlbum==null) braniAlbum=new ArrayList<>();
    for (Brano brano:braniAlbum) brano.setAlbumBrano(album);
    tabellaBrani.setItems(FXCollections.observableArrayList(braniAlbum));
  }
  private void caricaAlbumArtista(Artista artista) {
    try {
      List<Album> tuttiGliAlbum=albumService.findAllAlbum();
      List<Album> albumArtista=new ArrayList<>();
      for (Album album: tuttiGliAlbum) {
        Artista artistaAlbum=album.getArtista();
        //confrontiamo tipo e nome dell'artista per associare gli album corretti anche quando gli oggetti sono instanze diverse
        if (artistaAlbum!=null&&ottieniTipoArtista(artistaAlbum).equals(ottieniTipoArtista(artista))&&ottieniNomeArtista(artistaAlbum).equalsIgnoreCase(ottieniNomeArtista(artista))) 
          albumArtista.add(album);
      }
      tabellaAlbum.setItems(FXCollections.observableArrayList(albumArtista));
      tabellaBrani.getItems().clear();
    } catch (BusinessException e) {System.out.println("Errore nel caricamento degli album dell'artista: " + e.getMessage());}
  }
  private void configuraFiltro() {
    //il listener intercetta ogni modifica del testo digitato e applica immediatamente il filtro alle tabelle
    campoFiltro.textProperty().addListener((observable, vecchioTesto, nuovoTesto)->{applicaFiltro(nuovoTesto);});
  }
  private void applicaFiltro(String testo) {
    if (testo==null) return;
    String filtro=testo.trim().toLowerCase();
    List<Artista> artistiFiltrati=new ArrayList<>();
    List<Album> albumFiltrati=new ArrayList<>();
    List<Brano> braniFiltrati=new ArrayList<>();
    if (filtro.isEmpty()) {
      tabellaArtisti.setItems(FXCollections.observableArrayList(tuttiGliArtisti));
      tabellaAlbum.setItems(FXCollections.observableArrayList(tuttiGliAlbum));
      tabellaBrani.setItems(FXCollections.observableArrayList(tuttiIBrani));
      return;
    }
    for (Artista artista:tuttiGliArtisti) {
      String nome=ottieniNomeArtista(artista);
      if(nome!=null&&nome.toLowerCase().contains(filtro)) {
        artistiFiltrati.add(artista);
        for (Album album:tuttiGliAlbum) {
          if (album.getArtista()!=null&&ottieniNomeArtista(album.getArtista()).equalsIgnoreCase(nome)) {
            albumFiltrati.add(album);
            //andiamo a cercare tra tutti i brani quelli presenti nell'elenco dell'album, evitando di inserire due volte lo stesso elemento 
            for(Brano brano:tuttiIBrani) {
              for (Brano branoAlbum: album.getListaBrani()) {
                if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
                  if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
                  break;
                }
              }
            }
          }
        }
      }
    }
    for (Album album:tuttiGliAlbum) {
      String titolo=album.getTitolo();
      if(titolo!=null&&titolo.toLowerCase().contains(filtro)) {
        if(!albumFiltrati.contains(album)) albumFiltrati.add(album);
        if (album.getArtista()!=null&&!artistiFiltrati.contains(album.getArtista())) artistiFiltrati.add(album.getArtista());
        for (Brano branoAlbum: album.getListaBrani()) {
          //individuiamo i brani dell0album nella lista generale e li si aggiunge senza duplicati
          for (Brano brano:tuttiIBrani) {
            if (brano.getTitolo()!=null&&branoAlbum.getTitolo()!=null&&brano.getTitolo().equalsIgnoreCase(branoAlbum.getTitolo())) {
              if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
              break;
            }
          }
        }
      }
    }
    for (Brano brano:tuttiIBrani) {
      String titolo=brano.getTitolo();
      if (titolo!=null&&titolo.toLowerCase().contains(filtro)) {
        if (!braniFiltrati.contains(brano)) braniFiltrati.add(brano);
        for (Album album:tuttiGliAlbum) {
          for (Brano branoAlbum: album.getListaBrani()) {
            if (branoAlbum.getTitolo()!=null&&titolo.equalsIgnoreCase(branoAlbum.getTitolo())) {
              //se l'album contiene il brano cercato, aggiungiamo anche l'album e il suo artista se non sono già presenti
              if (!albumFiltrati.contains(album)) albumFiltrati.add(album);
              if (album.getArtista()!=null&&!artistiFiltrati.contains(album.getArtista())) artistiFiltrati.add(album.getArtista());
              break;
            }
          }
        }
      }
    }
    //alla fine di tutto, le tre tabelle le aggiorniamo con i rispettivi risultati filtrati
    tabellaArtisti.setItems(FXCollections.observableArrayList(artistiFiltrati));
    tabellaAlbum.setItems(FXCollections.observableArrayList(albumFiltrati));
    tabellaBrani.setItems(FXCollections.observableArrayList(braniFiltrati));
  }
  @FXML
  private void tornaIndidetro() throws IOException { //qui carichiamo il file FXML della schermata principale per poter tornare alla vista iniziale dell'applicazione
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    //qui invece recuperiamo la finestra attuale dal pulsante premuto per poi sostituire la scena con quella principale
    Stage stage=(Stage) bottoneIndietro.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
}
