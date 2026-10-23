package com.example.controller; 
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileStatisticheServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
import com.example.domain.Brano;
import com.example.domain.Utente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;  //Serve a includere la classe astratta InputStream che rappresenta il flusso di dati in ingresso sotto forma di byte
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*; 
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class StatisticheController {
  @FXML
  private Label tempoTotaleLabel;
  @FXML
  private ListView<String> braniListView;
  @FXML
  private ListView<String> artistiListView;
  @FXML
  private Label messaggioLabel;
  @FXML
  private HBox intestazioneStatistiche;
  @FXML
  private VBox contenitoreStatistiche;
  @FXML
  private VBox riquadroTempoTotale;
  @FXML
  private Label titoloStatisticheLabel;
  @FXML
  private Label titoloTempoTotaleLabel;
  @FXML
  private Label titoloTopBraniLabel;
  @FXML
  private Label titoloTopArtistiLabel;
  @FXML
  private VBox riquadroTopBrani;
  @FXML
  private VBox riquadroTopArtisti;
  private FileArtistaServiceImpl artistaService;
  private FileAlbumServiceImpl albumService;
  private FileBranoServiceImpl branoService;
  private FileStatisticheServiceImpl statisticheService;
  private Timeline timerStatistiche;
  @FXML
  private void initialize() {
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt");
    albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
    applicaStileStatistiche();
    caricaStatistiche();
    avviaAggiornamentoStatistiche();
  }
  private void caricaStatistiche() {
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null || utente.getEmail()==null) {
      braniListView.getItems().clear();
      artistiListView.getItems().clear();
      tempoTotaleLabel.setText("0 secondi");
      messaggioLabel.setText("Selezionare un utente nella vista UTENTE.");
      return;
    }
    try { List<Brano> brani=branoService.findAllBrani();
          List<Album> album=albumService.findAllAlbum();
          List<Artista> artisti=artistaService.findAllArtisti();
          caricaTopBraniUtente(brani, utente.getEmail());
          caricaTopArtistiUtente(brani, album, artisti, utente.getEmail());
          aggiornaTempoTotaleUtente(utente.getEmail());
          messaggioLabel.setText("Statistiche di " + utente.getNome() + " " + "utente.getCognome()");
        } catch(BusinessException e) { messaggioLabel.setText("Errore nel caricamento delle statistiche: " + e.getMessage()); }
  }
  private int calcolaTempoTotale(List<Brano> brani) {
    int tempoTotale=0;
    for (Brano brano : brani) {
      if (brano!=null) tempoTotale +=brano.getDurata() * brano.getNumeroAscolti();
    }
    return tempoTotale;
  }
  private void caricaTopBraniUtente(List<Brano> brani, String email) {
    List<Brano> ordinati=new ArrayList<>(brani);
    ordinati.sort(Comparator.comparingInt((Brano b) -> PlayerStato.getNumeroAscoltiUtente(email, b.getTitolo())).reversed());
    List<String> risultati=new ArrayList<>();
    int posizione=1;
    for (Brano brano : ordinati) {
      int ascolti=PlayerStato.getNumeroAscoltiUtente(email, brano.getTitolo());
      if (ascolti <= 0) continue;
      risultati.add(posizione + "." + brano.getTitolo() + "-" + ascolti + " ascolti");
      if (++posizione > 10) break;
    }
    if (risultati.isEmpty()) risultati.add("Nessun brano ascoltato.");
    braniListView.setItems(FXCollections.observableArrayList(risultati));
  }
  private void caricaTopArtistiUtente(List<Brano> brani, List<Album> album, List<Artista> artisti, String email) {
    Map<String, Integer> conteggi=new HashMap<>();
    Map<String, String> nomi=new HashMap<>();
    for (Brano brano : brani) {
      if (brano==null || brano.getTitolo()==null) continue;
      int ascolti=PlayerStato.getNumeroAscoltiUtente(email, brano.getTitolo());
      if (ascolti <= 0) continue; 
      for (Album a : album) {
        if (a==null || a.getListaBrani()==null || a.getArtista()==null) continue;
        boolean presente=a.getListaBrani().stream().anyMatch(b -> b!=null && b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo()));
        if (presente) {
          String nome=ottieniNomeArtista(a.getArtista());
          if (nome!=null) {
            String chiave=nome.trim().toLowerCase();
            nomi.putIfAbsent(chiave, nome.trim());
            conteggi.merge(chiave, ascolti, Integer::sum);
          }
        }
      }
    }
    List<Map.Entry<String, Integer>> classifica=new ArrayList<>(conteggi.entrySet());
    classifica.sort(Map.Entry.<String, Integer> comparingByValue().reversed());
    List<String> risultati=new ArrayList<>();
    int posizione=1;
    for (Map.Entry<String, Integer> entry : classifica) {
      if (posizione > 10) break;
      risultati.add(posizione++ + "." + nomi.get(entry.getKey()) + "-" + entry.getValue() + " ascolti");
    }
    if (risultati.isEmpty()) risultati.add("Nessun artista ascoltato.");
    artistiListView.setItems(FXCollections.observableArrayList(risultati));
  }
  private String ottieniNomeArtista(Artista artista) {
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "Artista sconosciuto";
  }
  private String formattaTempo(int secondi) {
    int ore=secondi / 3600;
    int minuti=(secondi % 3600) / 60;
    int secondiRimanenti=secondi % 60;
    if (ore > 0) return ore + " ore, " + minuti + " minuti e " + secondiRimanenti + " secondi";
    if (minuti > 0) return minuti + " minuti e " + secondiRimanenti + " secondi";
    return secondiRimanenti + " secondi";
  }
  private int calcolaTempoTotaleAscoltato() {
    Properties tempi=new Properties(); 
    Path percorso=Paths.get("src/main/resources/dati/tempi_ascolto.properties");
    if (!Files.exists(percorso)) return 0;
    try(InputStream input=Files.newInputStream(percorso)) {
      tempi.load(input);
      int totale=0;
      for (String valore : tempi.stringPropertyNames()) {
        totale +=Integer.parseInt(tempi.getProperty(valore, "0"));
      }
      return totale;
    } catch(IOException | NumberFormatException e) { e.getMessage();
            return 0;
           }
  }
  private void aggiornaTempoTotaleUtente(String email) {
    int totale=0;
    try { List<Brano> brani=branoService.findAllBrani();
          for (Brano brano : brani) {
            if (brano!=null) totale +=PlayerStato.getTempoAscoltoUtente(email, brano.getTitolo());
          }
         tempoTotaleLabel.setText(formattaTempo(totale));
        } catch(BusinessException e) { tempoTotaleLabel.setText("Tempo non disponibile"); }
  }
  private void avviaAggiornamentoStatistiche() {
    if (timerStatistiche!=null) timerStatistiche.stop();
    timerStatistiche=new Timeline(new KeyFrame(Duration.seconds(1), e -> caricaStatistiche()));
    timerStatistiche.setCycleCount(Timeline.INDEFINITE);
    timerStatistiche.play();
  }
  @FXML
  private void tornaHome() throws IOException {
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/viste/MainView.fxml"));
    Parent root=loader.load();
    Stage stage=(Stage) messaggioLabel.getScene().getWindow();
    stage.setScene(new Scene(root, 1000, 700));
  }
  public void aggiornaTemaStatistiche() { applicaStileStatistiche(); }
  private void applicaStileStatistiche() {
    boolean scuro=TemaVisivo.isScuro();
    String sfondo=scuro ? "#121212" : "#ffffff";
    String sfondoIntestazione=scuro ? "#191414":"#ffffff";
    String sfondoRiquadro=scuro ? "#242424":"#ffffff";
    String coloreTesto=scuro ? "#ffffff":"#111111";
    String sfondoLista=scuro ? "#242424":"#ffffff";
    intestazioneStatistiche.setStyle("-fx-background-color: "+(scuro ? "#191414": "#ffffff")+";");
    contenitoreStatistiche.setStyle("-fx-background-color: "+sfondo+";");
    riquadroTempoTotale.setStyle("-fx-background-color: "+sfondoRiquadro+";"+"-fx-background-radius:10;");
    String sfondoRiquadriClassifica=scuro ? "#242424":"#ffffff";
    riquadroTopBrani.setStyle("-fx-background-color: "+sfondoRiquadriClassifica+";"+"-fx-background-radius:10;");
    riquadroTopArtisti.setStyle("-fx-background-color: "+sfondoRiquadriClassifica+";"+"-fx-background-radius:10;");
    titoloStatisticheLabel.setStyle("-fx-font-size:28px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+coloreTesto+";");
    titoloTempoTotaleLabel.setStyle("-fx-font-size:18px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+coloreTesto+";");
    tempoTotaleLabel.setStyle("-fx-font-size: 24px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+coloreTesto+";");
    titoloTopBraniLabel.setStyle("-fx-font-size:18px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+coloreTesto+";");
    titoloTopArtistiLabel.setStyle("-fx-font-size:18px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+coloreTesto+";");
    messaggioLabel.setStyle("-fx-text-fill: "+coloreTesto+";");
    applicaStileLista(braniListView, sfondoLista, coloreTesto);
    applicaStileLista(artistiListView, sfondoLista, coloreTesto);
  }
  private void applicaStileLista(ListView<String> lista, String sfondo, String coloreTesto) {
    lista.setStyle("-fx-control-inner-background: "+sfondo+";"+"-fx-background-color: "+sfondo+";"+"-fx-text-fill: "+coloreTesto+";");
    lista.setCellFactory(lv->new ListCell<String>() {
      @Override
      protected void updateItem(String elemento, boolean vuoto) {
        super.updateItem(elemento, boolean vuoto);
        if (vuoto||elemento==null) setText(null);
        else setText(elemento);
        setTextFill(Color.web(coloreTesto));
        setStyle("-fx-background-color: "+sfondo+";"+"-fx-text-fill: "+coloreTesto+";");
      }
    });
  }
}
