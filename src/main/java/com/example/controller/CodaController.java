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
    contenitoreBrani.getChildren().clear();
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
    List<Brano> braniCoda=new ArrayList<>();
    for (String titolo : titoli) {
      for (Brano brano : braniCatalogo) {
        if (brano.getTitolo()!=null && brano.getTitolo().equalsIgnoreCase(titolo)) {
          for(com.example.domain.Album album : albumDisponibili) {
            if (album==null || album.getListaBrani()==null) continue;
            for (Brano branoAlbum : album.getListaBrani()) {
              if (branoAlbum!=null && branoAlbum.getTitolo()!=null && brano.getTitolo()!=null && branoAlbum.getTitolo().trim().equalsIgnoreCase(brano.getTitolo().trim())) {
                brano.setAlbumBrano(album);
                break;
              }
            }
            if (brano.getAlbum()!=null) break;
          }
          braniCoda.add(brano);
          break;
        }
      }
    }
    etichettaNumeroBrani.setText(braniCoda.size() + (braniCoda.size()==1 ? " brano" : "brani"));
    boolean vuota=braniCoda.isEmpty();
    etichettaCodaVuota.setVisible(vuota);
    etichettaCodaVuota.setManaged(vuota);
    if (vuota) {
      etichettaCodaVuota.setText("La tua coda è vuota. Aggiungi i brani che vuoi ascoltare.");
      return;
    }
    for (int i=0; i < braniCoda.size(); i++) {
      Brano brano=braniCoda.get(i);
      contenitoreBrani.getChildren().add(creaScheda(brano, braniCoda, i));
    }
  }
  private StackPane creaScheda(Brano brano, List<Brano> braniCoda, int indice) {
    VBox contenuto=new VBox(0);
    contenuto.setAlignment(javafx.geometry.Pos.TOP_LEFT);
    contenuto.setPrefSize(190, 180);
    contenuto.setSyle("-fx-background-color: rgba(0, 0, 0, 0.50);" + "-fx-padding: 12;");
    Label nota=new Label("♫");
    nota.setStyle("-fx-font-size: 28px; -fx-text-fill: #1ed760;");
    Label titolo=new Label(brano.getTitolo());
    titolo.setWrapText(true);
    titolo.setMaxWidth(190); 
    titolo.setStyle("-fx-font-size: 17px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");
    String testoGeneri=brano.getGenereBrano()==null || brano.getGenereBrano().isEmpty() ? "Genere non specificato" : brano.getGenereBrano().toString().replace("[", "").replace("]", "");
    Label generi=new Label(testoGeneri);
    generi.setWrapText(true);
    generi.setMaxWidth(190);
    generi.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: white;");
    Label durata=new Label(formattaDurata(brano.getDurata()));
    durata.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: #1ed760;");
    Button riproduci=new Button("▶ Riproduci");
    riproduci.setMaxWidth(Double.MAX_VALUE);
    riproduci.setStyle("-fx-background-color: #1ed760;" + "-fx-text-fill: black;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;" + "-fx-cursor: hand;");
    riproduci.setOnAction(event -> {
      try { PlayerStato.avviaPlaylist(braniCoda, indice);
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
      String email=UtenteAttivo.getUtente().getEmail();
      storage.rimuoviTitolo(email, brano.getTitolo());
      aggiornaCoda();
    });
    contenuto.getChildren().addAll(nota, titolo, generi, durata, riproduci, rimuovi);
    StackPane scheda=new StackPane(contenuto);
    if (brano.getAlbumBrano()!=null && brano.getAlbumBrano().getCopertina()!=null && !brano.getAlbumBrano().getCopertina().trim().isEmpty()) {
      File fileCopertina=new File ("src/main/resources/dati/" + brano.getAlbumBrano().getCopertina().trim());
      if (fileCopertina.isFile()) {
        Image immagineCopertina=new Image(fileCopertina.toURI().toString());
        if (!immagineCopertina.isError()) {
          ImageView immagineSfondo=new ImageView(immagineCopertina);
          immagineSfondo.setFitWidth(210);
          immagineSfondo.setFitHeight(210);
          immagineSfondo.setPreserveRatio(false);
          immagineSfondo.setSmooth(true);
          scheda.getChildren().add(0, immagineSfondo);
          Rectangle ritaglio=new Rectangle(210, 210);
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
    etichettaNumeroBran.setText("0 brani");
    etichettaCodaVuota.setText(messaggio);
    etichettaCodaVuota.setVisible(true);
    etichettaCodaVuota.setManaged(true);
  }
  public void aggiornaTemaCoda() { aggiornaCoda(); }
}
