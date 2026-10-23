package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaSolista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.GenereMusicale;
import javafx.beans.property.SimpleStringProperty; //uso la classe SimpleStringProperty che si trova dentro quel preciso pacchetto
import javafx.collections.FXCollections;//fornisce i metodi per creare intere liste di elementi che aggiornano tabelle o liste a scorrimento
import javafx.fxml.FXML; //collego l'interfaccia grafica con il codice Java
import javafx.scene.control.Alert; //serve per mostrare finestre di avviso o messaggi di errore a schermo
import javafx.scene.control.ButtonType; //serve per definire i tipi di pulsanti all'interno degli avvisi 
import javafx.scene.control.ComboBox; //serve per creare menu a tendina con una lista di opzioni selezionabili
import javafx.scene.control.Label; //serve per visualizzare un semplice testo fisso o un'etichetta deescrittiva sulla User Interface 
import javafx.scene.control.TableColumn; //serve per definire e gestire una colonna all'interno di una tabella
import javafx.scene.control.TableView; //serve per creare la struttura principale di una tabella
import javafx.scene.control.TextField; //serve per creare una casella di testo in cui l'utente può digitare parole o numeri
import javafx.util.StringConverter; //serve per convertire un oggetto in testo (e viceversa) dentro le ComboBox per esempio
import java.time.Year;
import java.util.*;
public class AlbumController {
  @FXML private TableView<Album> tabellaAlbum;
  @FXML private TableColumn<Album, String> colonnaTitolo;
  @FXML private TableColumn<Album, String> colonnaAnno;
  @FXML private TableColumn<Album, String> colonnaArtista;
  @FXML private TextField campoTitolo;
  @FXML private TextField campoAnno;
  @FXML private ComboBox<Artista> campoArtista;
  @FXML private TextField campoGeneri;
  @FXML private TextField campoCopertina;
  @FXML private Label messaggio;
  private FileAlbumServiceImpl albumService;
  private FileArtistaServiceImpl artistaService;
  private Album albumSelezionato;
  @FXML
  private void initialize() {
    //inizializziamo i servizi indicando i file di testo da cui leggere e su cui salvare album e artisti
    albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    configuraTabella();
    configuraCmboArtisti();
    caricaArtisti();
    caricaAlbum();
  }
  private void configuraTabella() {
    //colleghiamo ogni colonna al valore dell'album corrispondente, convertendolo in una proprietà testuale visualizzabile
    colonnaTitolo.setCellValueFactory(cella->new SimpleStringProperty(cella.getValue().getTitolo()));
    colonnaAnno.setCellValueFactory(cella->new SimpleStringProperty(String.valueOf(cella.getValue().getAnnoPubblicazione())));
    //mostriamo nella tabella il nome leggibile dell'artista anzichè l'oggetto Artista completo
    colonnaArtista.setCellValueFactory(cella->new SimpleStringProperty(ottieniNomeArtista(cella.getValue().getArtista())));
    //visualizziamo i dati dell'album soltanto quando ne viene selezionato uno
    tabellaAlbum.getSelectionModel().selectedItemProperty().addListener((osservabile, precedente, selezionato) -> {
      if (selezionato!=null) mostraDatiAlbum(selezionato);
    });
  }
  private void configuraComboArtisti() {
    //definiamo come convertire gli oggetti Artista in testo da mostrare poi nella ComboBox
  campoArtista.setConverter(new StringConverter<Artista>() {
    @Override
    public String toString(Artista artista) { return artista==null ? "": ottieniNomeArtista(artista); }
    @Override
    public String fromString(String testo) { return null; } //è null perchè la ComboBox utilizza gli oggetti già caricati
  });
  }
  private void caricaArtisti() {
    try {
      List<Artista> artisti=artistaService.findAllArtisti();
      //convertiamo la lista ottenuta dal servizio in una raccolta osservabil e utilizzabile dalla ComboBox di JavaFX
      campoArtista.setItems(FXCollections.observableArrayList(artisti));
      messaggio.setText("Artisti disponibili: " + artisti.size());
    } catch (BusinessException e) { mostraErrore("Errore nel caricamento degli artisti: " + e.getMessage()); }
  }
  private void caricaAlbum() {
    try {
      List<Album> album=albumService.findAllAlbum();
      //trasformiamo gli album letti dal servizio in una lista osservabile che JavaFX può visualizzare nella tabella
      tabellaAlbum.setItems(FXCollections.observableArrayList(album));
      messaggio.setText("Album caricati: " + album.size());
    } catch (BusinessException e) { mostraErrore("Errore nel caricamento degli albumn" + e.getMessage()); }
  }
  private void mostraDatiAlbum(Album album) {
    //memorizziamo l'album selezionato per identificare quello da modificare o eliminare
    albumSelezionato=album;
    campoTitolo.setText(album.getTitolo());
    //ora andiamo a bloccare la modifica del titolo durante l'aggiornamento, lasciando invariato il criterio identificativo dell'album
    campoTitolo.setEditable(false);
    campoAnno(setText(String.valueOf(album.getAnnoPubblicazione())));
    campoCopertina.setText(album.getCopertina()==null ? "":album.getCopertina());
    StringBuilder generi=new StringBuilder();
    //costruiamo una stringa con tutti i generi dell'album separandoli con una virgola così da mostrarli nel campo di testo
    for (GenereMusicale genere:album.getGenere()) {
      if(generi.length()>0) generi.append(",");
      generi.append(genere.name());
    }
    campoGeneri.setText(generi.toString());
    String nomeArtista=ottieniNomeArtista(album.getArtista());
    //ora andiamo a cercare nella ComboBox l'oggetto artista corrispondente al nome dell'album e lo selezioniamo senza distinzione tra maiuscole e minuscole
    for (Artista artista:campoArtista.getItems()) {
      if (ottieniNomeArtista(artista).equalsIgnoreCase(nomeArtista)) {
        campoArtista.setValue(artista);
        break;
      }
    }
    messaggio.setText("Album selezionato: " + album.getTitolo());
  }
  @FXML
  private void creaAlbum() {
    try {
      //qui raccogliamo i dati inseriti nel'interfaccia per poi convertirli in un oggetto Album dopo esserci assicurati la sua validità
      Album album=costruisciAlbum();
      albumService.creaAlbum(album);
      caricaAlbum();
      pulisciCampi();
      mostraMessaggio("Album creato e salvato correttamente");
    } catch (BusinessException | IllegalArgumentException e) { mostraErrore(e.getMessage()); }  //gestiamo sia errori di servizio sia dati errati forniti dall'utente
  }
  @FXML
  private void modificaAlbum() {
    if (albumSelezionato==null) {
      mostraErrore("Seleziona prima un album dalla tabella");
      return;
    }
    try {
      //in questo caso ricostruiamo l'album partendo dai dati aggiornati per poi andare a salvare le modifiche
      Album aggiornato=costruisciAlbum();
      albumService.modificaAlbum(aggiornato);
      caricaAlbum();
      pulisciCampi();
      mostraMessaggio("Album modificato e salvato correttamente");
    } catch (BusinessException | IllegalArgumentException e) { mostraErrore(e.getMessage()); }
  }
  @FXML
  private void eliminaAlbum() {
    if (albumSelezionato==null) {
      mostraErrore("Seleziona prima un album dalla tabella");
      return;
    }
    Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
    conferma.setTitle("Conferma eliminazione");
    conferma.setHeaderText("Eliminare l'album?");
    conferma.setContentText("Vuoi eliminare l'album \"" + albumSelezionato.getTitolo() + "\"?");
    //attendiamo cosa vuole fare l'utente così da poi procedere con l'eliminazione dell'album
    Optional<ButtonType> risposta=conferma.showAndWait();
    //eliminiamo l'album solo se l'utente ha cliccato OK
    if (risposta.isPresent()&&risposta.get()==ButtonType.OK) {
      try {
        albumService.eliminaAlbum(albumSelezionato);
        caricaAlbum();
        pulisciCampi();
        mostraMessaggio("Album eliminato correttamente");
      } catch (BusinessException e) { mostraErrore(e.getMessage()); }
    }
  }
  private Album costruisciAlbum() {
    //qui andiamo a rimuovere gli spazi iniziali e finali dal titolo e usiamo una stringa vuota se il campo non contiene testo
    String titolo=campoTitolo.getText()==null ? "":campoTitolo.getText().trim();
    if (titolo.isEmpty()) throw new IllegalArgumentException("Inserisci il titolo dell'album");
    int anno;
    try {//convertiamo l'anno inserito in un intero fermando gli errori se il testo non rappresenta un numero valido
      anno=Integer.parseInt(campoAnno.getText().trim()); 
    } catch (Exception e) { throw new IllegalArgumentException("Inserisci un anno valido"); }
    //non possiamo inserire anni non positivi oppure anni successivi all'anno corrente
    if (anno<=0||anno>Year.now().getValue()) throw new IllegalArgumentException("L'anno deve essere positivo e non successivo" + "all'anno corrente");
    Artista artista=campoArtista.getValue();
    if (artista==null) throw new IllegalArgumentException("Seleziona l'artista dell'album");
    Set<GenereMusicale> generi=leggiGeneri(campoGeneri.getText());
    String copertina=campoCopertina.getText()==null? "":campoCopertina.getText().trim();
    //manteniamo i brani associati all'album per la modifica e creiamo una lista vuota per la creazione dell'album
    List<com.example.domain.Brano> brani=albumSelezionato==null ?  new ArrayList<>():albumSelezionato.getListaBrani();
    return new Album(brani, artista, titolo, anno, generi, copertina);
  }
  private Set<GenereMusicale> leggiGeneri(String testo) {
    //utilizziamo un HashSet che contiene i generi senza avere duplicazioni
    Set<GenereMusicale> generi=new HashSet<>();
    //se il campo è nullo o vuoto, restituiamo un insieme vuoto senza elaborare il testo
    if (testo==null||testo.trim().isEmpty()) return generi;
    //ora dividiamo il testo in corrispondenza delle virgole, cosi da poter inserire più generi nello stesso campo
    for (String valore:testo.split(",")) {
      String nomeGenere=valore.trim();
      //ignoriamo elementi vuoti (per esempio due virgole consecutive)
      if (nomeGenere.isEmpty()) continue;
      try {//convertiamo il testo nel valore corrispondente dell'enum ignorando differenze tra maiuscole e minuscole
        generi.add(GenereMusicale.valueOf(nomeGenere.toUpperCase()));
      } catch (IllegalArgumentException e) { throw new IllegalArgumentException("Genere non valido: " + nomeGenere + ". Valori ammessi: ROCK, POP, JAZZ, " + "CLASSICA, BLUES, ELETTRONICA, COUNTRY, " + "REGGAETON, METAL, RAP, ROMANTICO, " + "LATINA, TRAP."); }
    }
    return generi;
  }
  private String ottieniNomeArtista(Artista artista) {
    //qui verifichiamo il tipo dell'artista così da recuperare il nome specifico
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "";
  }
  @FXML
  private void pulisciCampi() {
    //puliamo il riferimento all'album corrente così da poter far reinserire informazioni per un nuovo inserimento
    albumSelezionato=null;
    tabellaAlbum.getSelectionModel().clearSelection();
    campoTitolo.clear();
    //riabilitiamo il titolo che avevamo bloccato nella selezione dell'album da modificare
    campoTitolo.setEditable(true);
    campoAnno.clear();
    campoArtista.setValue(null);
    campoGeneri.clear();
    messaggio.setText("Pronto per inserire un nuovo album.");
  }
  private void mostraMessaggio(String testo) {
    messaggio.setText(testo);
    Alert alert=new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("UnivaqSound");
    alert.setHeaderText(null);
    alert.setContentText(testo);
    alert.showAndWait();
  }
  private void mostraErrore(String testo) {
    messaggio.setText("Errore: " + testo);
    Alert alert=new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Errore");
    alert.setHeaderText("Operazione non completata");
    alert.setContentText(testo);
    alert.showAndWait();
  }
}
