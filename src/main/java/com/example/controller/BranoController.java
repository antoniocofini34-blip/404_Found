package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.domain.Album;
import com.example.domain.Brano;
import com.example.domain.GenereMusicale;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import java.util.*;
public class BranoController {
  @FXML private TableView<Brano> tabellaBrani;
  @FXML private TableColumn<Brano, String> colonnaTitolo;
  @FXML private TableColumn<Brano, String> colonnaDurata;
  @FXML private TableColumn<Brano, String> colonnaGeneri;
  @FXML private TableColumn<Brano, String> colonnaAlbum;
  @FXML private TableColumn<Brano, String> colonnaAscolti;
  @FXML private TextField campoTitolo;
  @FXML private TextField campoDurata;
  @FXML private TextField campoGeneri;
  @FXML private ComboBox<Album> campoAlbum;
  @FXML private TextArea campoTesto;
  @FXML private Label messaggio;
  private FileBranoServiceImpl branoService;
  private FileAlbumServiceImpl albumService;
  private Brano branoSelezionato;
  private Album albumOriginale;
  @FXML
  private void initialize() { //inizializziamo i servizi indicando i file di testo da cui leggere e su cui salvare album e artisti
    albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    configuraTabella();
    configuraComboAlbum();
    caricaAlbum();
    caricaBrani();
  }
  private void configuraTabella() {
    colonnaTitolo.setCellValueFactory(cella->new SimpleStringProperty(cella.getValue().getTitolo()));
    colonnaDurata.setCellValueFactory(cella->new SimpleStringProperty(String.valueOf(cella.getValue().getDurata())));
    colonnaGeneri.setCellValueFactory(cella->new SimpleStringProperty(formattaGeneri(cella.getValue().getGenereBrano())));
    colonnaAlbum.setCellValueFactory(cella->new SimpleStringProperty(trovaAlbumDelBrano(cella.getValue())==null ? "Non assegnato" : trovaAlbumDelBrano(cella.getValue()).getTitolo()));
    colonnaAscolti.setCellValueFactory(cella->new SimpleStringProperty(String.valueOf(cella.getValue().getNumeroAscolti())));
    tabellaBrani.getSelectionModel().selectedItemProperty().addListener((osservabile, precedente, selezionato)->{
      if(selezionato!=null) mostraDatiBrano(selezionato);
    });
  }
  private void configuraComboAlbum() {
    campoAlbum.setConverter(new StringConverter<Album>() {
      @Override
      public String toString(Album album) { return album==null? "": album.getTitolo(); }
      @Override
      public Album fromString(String testo) { return null; }
    });
  }
  private void caricaAlbum() {
    try {campoAlbum.setItems(FXCollections.observableArrayList(albumService.findAllAlbum()));
        } catch (BusinessException e) { mostraErrore("Impossibile caricare gli album: " + e.getMessage()); }
  }
  private void caricaBrani() {
    try {tabellaBrani.setItems(FXCollections.observableArrayList(branoService.findAllBrani()));
         messaggio.setText("Brani caricati: " + tabellaBrani.getItems().size());
        } catch (BusinessException e) { mostraErrore("Impossibile caricare i brani: " + e.getMessage()); }
  }
  private void mostraDatiBrano(Brano brano) {
    branoSelezionato=brano;
    albumOriginale=trovaAlbumDelBrano(brano);
    campoTitolo.setText(brano.getTitolo());
    campoTitolo.setEditable(false);
    campoDurata.setText(String.valueOf(brano.getDurata()));
    campoDurata.setText(brano.getTesto()==null? "": brano.getTesto());
    campoGeneri.setText(formattaGeneri(brano.getGenereBrano()));
    campoAlbum.setValue(albumOriginale);
    messaggio.setText("Brano selezionato: " + brano.getTitolo());
  }
  @FXML
  private void creaBrano() {
    try {
      Brano brano=costruisciBrano();
      //prima salviamo il brano nel file dei brani
      branoService.creaBrano(brano);
      //poi andiamo ad aggiornare anche l'elenco dei brani dell'album
      albumService.aggiungiBrano(campoAlbum.getValue(), brano);
      caricaAlbum();
      caricaBrani();
      pulisciCampi();
      mostraMessaggio("Brano creato e associato all'album");
    } catch (BusinessException | IllegalArgumentException e) { mostraErrore(e.getMessage()); }
  }
  @FXML
  private void modificaBrano() {
    if (branoSelezionato==null) {
      mostraErrore("Seleziona prima un brano dalla tabella");
      return;
    }
    try {
      Brano aggiornato=costruisciBrano();
      Album nuovoAlbum=campoAlbum.getValue();
      if (albumOriginale!=null&&!albumOriginale.getTitolo().equalsIgnoreCase(nuovoAlbum.getTitolo())) albumService.rimuoviBrano(albumOriginale, aggiornato);
      branoService.modificaBrano(aggiornato);
      if (albumOriginale==null||!albumOriginale.getTitolo().equalsIgnoreCase(nuovoAlbum.getTitolo())) albumService.aggiungiBrano(nuovoAlbum, aggiornato);
      caricaAlbum();
      caricaBrani();
      pulisciCampi();
      mostraMessaggio("Brano modificato correttamente");
    } catch (BusinessException | IllegalArgumentException e) { mostraErrore(e.getMessage()); }
  }
  @FXML
  private void eliminaBrano() {
    if (branoSelezionato==null) {
      mostraErrore("Seleziona prima un brano dalla tabella");
      return;
    }
    Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
    conferma.setTitle("Conferma eliminazione");
    conferma.setHeaderText("Eliminare il brano?");
    conferma.setContentText("Vuoi eliminare \"" + branoSelezionato.getTitolo() + "\"? L'operazione non può essere annullata");
    Optional<ButtonType> risposta=conferma.showAndWait();
    if (risposta.isPresent()&&risposta.get()==ButtonType.OK) {
      try {
        if (albumOriginale!=null) albumService.rimuoviBrano(albumOriginale, branoSelezionato);
        branoService.eliminaBrano(branoSelezionato);
        caricaAlbum();
        caricaBrani();
        pulisciCampi();
        mostraMessaggio("Brano eliminato correttamente");
      } catch (BusinessException e) { mostraErrore(e.getMessage()); }
    }
  }
  private Brano costruisciBrano() {
    String titolo=campoTitolo.getText()==null ? "": campoTitolo.getText().trim();
    if (titolo.isEmpty()) throw new IllegalArgumentException("Inserisci il titolo del brano");
    int durata;
    try { durata=Integer.parseInt(campoDurata.getText().trim());
        } catch (Exception e) { throw new IllegalArgumentException("Inserisci una durata in secondi valida"); }
    if (durata<=0) throw new IllegalArgumentException("La durata deve essere maggiore di zero");
    Album album=campoAlbum.getValue();
    if (album==null) throw new IllegalArgumentException("Seleziona l'album a cui associare il brano");
    Set<GenereMusicale> generi=leggiGeneri(campoGeneri.getText());
    String testo=campoTesto.getText()==null ? "" : campoTesto.getText();
    int ascolti=branoSelezionato==null ? 0 : branoSelezionato.getNumeroAscolti();
    return new Brano(titolo, durata, testo, generi, album, ascolti);
  }
  private Set<GenereMusicale> leggiGeneri(String testo) {
    Set<GenereMusicale> generi=new HashSet<>();
    if (testo==null||testo.trim().isEmpty()) return generi;
    for (String valore:testo.split(",")) {
      String nome=valore.trim();
      if (nome.isEmpty()) continue;
      try { generi.add(GenereMusicale.valueOf(nome.toUpperCase()));
          } catch (IllegalArgumentException e) { throw new IllegalArgumentException("Genere non valido: " + nome + ". Usa ROCK, POP, JAZZ, CLASSICA, " + "BLUES, ELETTRONICA, COUNTRY, " + "REGGAETON, METAL, RAP," + "ROMANTICO, LATINA o TRAP"); }
    }
    return generi;
  }
  private String formattaGeneri(Set<GenereMusicale> generi) {
    if(generi==null||generi.isEmpty()) return "";
    StringBuilder risultato=new StringBuilder();
    for (GenereMusicale genere:generi) {
      if (risultato.length()>0) risultato.append(",");
      risultato.append(genere.name());
    }
    return risultato.toString();
  }
  //dato che il servizio dei brani ricostruisce il campo album come null, andiamo a cercare l'associazione leggendo gli album salvati
  private Album trovaAlbumDelBrano(Brano brano) {
    try {
      List<Album> album=albumService.findAllAlbum();
      for (Album elemento:album) {
        for (Brano branoAlbum:elemento.getListaBrani()) {
          if (branoAlbum.getTitolo()!=null&&branoAlbum.getTitolo().equalsIgnoreCase(brano.getTitolo())) return elemento;
        }
      }
    } catch (BusinessException e) { messaggio.setText("Impossibile verificare l'album del brano"); } 
    return null;
  }
  @FXML
  private void pulisciCampi() {
    branoSelezionato=null;
    albumOriginale=null;
    tabellaBrani.getSelectionModel().clearSelection();
    campoTitolo.clear();
    campoTitolo.setEditable(true);
    campoDurata.clear();
    campoGeneri.clear();
    campoTesto.clear();
    campoAlbum.setValue(null);
    messaggio.setText("Inserisci campi del nuovo brano");
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
