package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileCodaPersonaleStorage;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.domain.Brano;
import com.example.domain.Album;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import java.io.File;
import javafx.scene.control.Alert;
import java.util.*;
public class CodaController {
  @FXML
  private Label etichettaNumeroBrani;
  @FXML
  private Label etichettaCodaVuota;
  @FXML
  private FlowPane contenitoreBrani;
  private final FileCodaPersonaleStorage storage=new FileCodaPersonaleStorage();
  private final FileBranoServiceImpl branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
  private final FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
  @FXML
  public void initialize() { aggiornaCoda(); }
  public void aggiornaCoda() {
    contenitoreBrani.getChildren().clear();  //Svuota il contenitore grafico da eventuali brani visualizzati in precedenza
    if (UtenteAttivo.getUtente()==null || UtenteAttivo.getUtente().getEmail()==null) {
      mostraCodaVuota("Accedi per visualizzare la tua coda.");
      return;
    }
    String email=UtenteAttivo.getUtente().getEmail();
    List<String> titoli=storage.leggiTitoli(email);
    List<Brano> braniCatalogo;
    List<Album> albumDisponibili;
    try { braniCatalogo=branoService.findAllBrani();
          albumDisponibili=albumService.findAllAlbum();
        } catch(BusinessException e) { mostraCodaVuota("Impossibile caricare i brani o gli album.");
                return;
          }
    List<Brano> braniCoda=new ArrayList<>();  //Ricostruisce gli oggetti Brano completi partendo dai soli titoli testuali in coda
    for (String titolo : titoli) {
      for (Brano brano : braniCatalogo) {
        if (brano.getTitolo()!=null && brano.getTitolo().equalsIgnoreCase(titolo)) {  //Cerca la corrispondenza del titolo nel catalogo
          for(Album album : albumDisponibili) {  //Associa l'album corretto al brano trovato scorrendo gli album disponibili
            if (album==null || album.getListaBrani()==null) continue;
            for (Brano branoAlbum : album.getListaBrani()) {  //Verifica se il brano corrente fa parte della lista dei brani di questo album
              if (branoAlbum!=null && branoAlbum.getTitolo()!=null && brano.getTitolo()!=null && branoAlbum.getTitolo().trim().equalsIgnoreCase(brano.getTitolo().trim())) {
                brano.setAlbumBrano(album);  //Collega l'album trovato al brano e interrompe la ricerca per questo album
                break;
              }
            }
            if (brano.getAlbumBrano()!=null) break;  //Se l'album è stato assegnato con successo, interrompe il ciclo degli album
          }
          braniCoda.add(brano);  //Aggiunge il brano completo (ora con l'album associato) alla lista della coda
          break;
        }
      }
    }
    etichettaNumeroBrani.setText(braniCoda.size() + (braniCoda.size()==1 ? " brano" : " brani"));  //Aggiorna il contatore testuale dei brani gestendo il singolare e il plurale
    boolean vuota=braniCoda.isEmpty();  //Gestisce la visibilità dell'interfaccia nel caso in cui la coda sia vuota
    etichettaCodaVuota.setVisible(vuota);
    etichettaCodaVuota.setManaged(vuota);  //Rilascia lo spazio nel layout se non è visibile
    if (vuota) {
      etichettaCodaVuota.setText("La tua coda è vuota. Aggiungi i brani che vuoi ascoltare.");
      return;
    }
    for (int i=0; i < braniCoda.size(); i++) {  //Scorre la lista dei brani validati e aggiunge le schede grafiche al FlowPane
      Brano brano=braniCoda.get(i);
      contenitoreBrani.getChildren().add(creaScheda(brano, braniCoda, i));  //Crea la scheda visiva passando il brano attuale, l'intera coda e la posizione 
    }
  }
  private StackPane creaScheda(Brano brano, List<Brano> braniCoda, int indice) {
    VBox contenuto=new VBox(8);  //Configurazione del contenitore verticale (VBox) per i testi e i pulsanti
    contenuto.setAlignment(javafx.geometry.Pos.TOP_LEFT);
    contenuto.setPrefSize(190, 180);
    contenuto.setStyle("-fx-background-color: rgba(0, 0, 0, 0.58);" + "-fx-padding: 12;");
    Label nota=new Label("♫");
    nota.setStyle("-fx-font-size: 28px; -fx-text-fill: #1ed760;");
    Label titolo=new Label(brano.getTitolo());  //Configurazione dell'etichetta del titolo (con gestione del testo a capo se troppo lungo)
    titolo.setWrapText(true);
    titolo.setMaxWidth(190); 
    titolo.setStyle("-fx-font-size: 17px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");
    //Formattazione e configurazione dell'etichetta dei generi musicali e rimuove le parentesi quadre tipiche del toString() delle liste
    String testoGeneri=brano.getGenereBrano()==null || brano.getGenereBrano().isEmpty() ? "Genere non specificato" : brano.getGenereBrano().toString().replace("[", "").replace("]", "");  
    Label generi=new Label(testoGeneri);
    generi.setWrapText(true);
    generi.setMaxWidth(190);
    generi.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: white;");
    Label durata=new Label(formattaDurata(brano.getDurata()));  //Configurazione dell'etichetta della durata (formattata tramite metodo di utilità)
    durata.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: #1ed760;");
    Button riproduci=new Button("▶ Riproduci");
    riproduci.setMaxWidth(Double.MAX_VALUE);
    riproduci.setStyle("-fx-background-color: #1ed760;" + "-fx-text-fill: black;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;" + "-fx-cursor: hand;");
    riproduci.setOnAction(event -> {
      try { PlayerStato.avviaPlaylist(braniCoda, indice);  //Avvia la riproduzione della coda partendo dall'indice di questo brano
          } catch(BusinessException e) { Alert avviso=new Alert(Alert.AlertType.ERROR);
                  avviso.setTitle("Errore di riproduzione");
                  avviso.setHeaderText(null);
                  avviso.setContentText("Impossibile avviare la riproduzione del brano.");
                  avviso.showAndWait();
                }
    });
    Button rimuovi=new Button("Rimuovi");
    rimuovi.setMaxWidth(Double.MAX_VALUE);
    rimuovi.setStyle("-fx-background-color: #444444;" + "-fx-text-fill: white;" + "-fx-background-radius: 20;" + "-fx-cursor: hand;");
    rimuovi.setOnAction(event -> {
      String email=UtenteAttivo.getUtente().getEmail();  //Elimina il titolo dal file di memorizzazione della coda e ridisegna la schermata
      storage.rimuoviTitolo(email, brano.getTitolo());
      aggiornaCoda();
    });
    contenuto.getChildren().addAll(nota, titolo, generi, durata, riproduci, rimuovi);  //Assemblaggio di tutti i componenti visivi all'interno del contenitore verticale
    StackPane scheda=new StackPane(contenuto);  //Creazione del pannello a strati (StackPane) principale della scheda
    if (brano.getAlbumBrano()!=null && brano.getAlbumBrano().getCopertina()!=null && !brano.getAlbumBrano().getCopertina().trim().isEmpty()) {  //Gestione e caricamento dell'immagine di copertina dell'album come sfondo della scheda
      File fileCopertina=new File ("src/main/resources/dati/" + brano.getAlbumBrano().getCopertina().trim());
      if (fileCopertina.isFile()) {  //Verifica che il file esista fisicamente sul disco 
        Image immagineCopertina=new Image(fileCopertina.toURI().toString());
        if (!immagineCopertina.isError()) {  
          ImageView immagineSfondo=new ImageView(immagineCopertina);
          immagineSfondo.setFitWidth(210);
          immagineSfondo.setFitHeight(210);
          immagineSfondo.setPreserveRatio(false);  //Forza il riempimento del quadrato 210x210
          immagineSfondo.setSmooth(true);  //Applica il filtro per una qualità migliore
          scheda.getChildren().add(0, immagineSfondo);  //Inserisce l'immagine al livello 0 dello StackPane(sotto al testo e ai pulsanti)
          Rectangle ritaglio=new Rectangle(210, 210);  //Taglia l'immagine per adattarla perfettamente alla scheda ed evitare che esca fuori
          immagineSfondo.setClip(ritaglio);
        }
      }
    }
    scheda.setStyle("-fx-background-radius: 12;");
    scheda.setPrefSize(210, 210);
    return scheda;
  }
  private String formattaDurata(int secondi) {
    int minuti=secondi / 60;
    int restoSecondi=secondi % 60;
    return String.format("%02d:%02d", minuti, restoSecondi);
  }
  private void mostraCodaVuota(String messaggio) {
    contenitoreBrani.getChildren().clear();
    etichettaNumeroBrani.setText("0 brani");  //Resetta il contatore testuale dei brani portandolo a 0
    etichettaCodaVuota.setText(messaggio);  //Imposta il messaggio personalizzato passato in input
    etichettaCodaVuota.setVisible(true);  //Rende visibile l'etichetta del messaggio di stato vuoto 
    etichettaCodaVuota.setManaged(true);  //Dice al layout di riservare lo spazio per questa etichetta all'interno della schermata
  }
  public void aggiornaTemaCoda() { aggiornaCoda(); }
}
