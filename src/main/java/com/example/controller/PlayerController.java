package com.example.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import com.example.business.exception.BusinessException;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileAlbumServiceImpl;
import com.example.business.impl.ram.RAMCodaRiproduzioneServiceImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import com.example.domain.Brano;
import com.example.domain.CodaRiproduzione;
import com.example.domain.ModalitaRepeat;
import com.example.domain.StatoRiproduzione;
import com.example.domain.Artista;
import com.example.domain.ArtistaSolista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.Album;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.util.*;
public class PlayerController{
  @FXML
  private Label titoloBrano;
  @FXML
  private Label artistaBrano;
  @FXML
  private Label albumBrano;
  @FXML
  private Slider barraAvanzamento;
  @FXML
  private  Region overlayCopertina;
  @FXML
  private Label tempoTrascorso
  @FXML
  private Label tempoRimanente;
  @FXML
  private Button bottoneIndietro;
  @FXML
  private Button bottonePlay;
  @FXML
  private Button bottonePausa;
  @FXML
  private Button bottoneStop;
  @FXML
  private Button bottoneAvanti;
  @FXML
  private ImageView copertinaAlbum;
  @FXML
  private StackPane rootPlayer;
  @FXML
  private ToggleButton bottoneShuffle;
  @FXML
  private ToggleButton bottoneRepeat;
  private RAMCodaRiproduzioneServiceImpl CodaRiproduzione;
  private CodaRiproduzione coda;
  private FileBranoServiceImpl branoService;
  private Timeline timeline;
  private double progressoCorrente;
  private List<Brano> ordineOriginale;
  private boolean trascinamentoBarra;
  private Brano branoAscoltatoRegistrato;
  private Brano branoMonitorato;
  private int secondiAscoltoCorrente=0;
  private boolean riproduzioneIniziale=false;
  private boolean apertoDaPlaylist=false;
  private Stage finestraTesto;
  private Label titoloFinestraTesto;
  private Label messaggioTesto;
  private VBox contenitoreRigheTesto;
  private ScrollPanel scorrimentoTesto;
  private String titoloTestoCercato; 
  @FXML
  private void initialize() { codaService = PlayerStato.getCodaService();
   branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
   coda = PlayerStato.getCoda();
   copertinaAlbum.fitWidthProperty().bind(rootPlayer.widthProperty());
   copertinaAlbum.fitHeightProperty().bind(rootPlayer.heightProperty());
   Brano branoCorrente = PlayerStato.getBranoCorrente();
   if (branoCorrente != null) { aggiornaCopertinaAlbum(branoCorrente);
      titoloBrano.setText(branoCorrente.getTitolo());
      if (branoCorrente.getAlbumBrano() != null) { albumBrano.setText(branoCorrente.getAlbumBrano().getTitolo());
         if (branoCorrente.getAlbumBrano().getArtista() != null) { Artista artista = branoCorrente.getAlbumBrano().getArtista();
             if (artista instanceof ArtistaSolista) { artistaBrano.setText(((ArtistaSolista) artista).getNomeArte());
             } else if (artista instanceof ArtistaGruppo) { artistaBrano.setText(((ArtistaGruppo) artista).getNomeGruppo());
          }
       }
    }
    progressoCorrente = PlayerStato.getProgressoCorrente();
    aggiornaTempiBrano(branoCorrente);
    barraAvanzamento.setValue(progressoCorrente / branoCorrente.getDurata());
    Platform.runLater(this::aggiornaStileBarraAvanzamento);
    barraAvanzamento.setDisable(false);
    if (coda.getStato() == StatoRiproduzione.IN_RIPRODUZIONE) avviaTimer(branoCorrente); }
        else aggiornaTempiBrano(null);
        if (coda.getListaCodaBrani().isEmpty()) {
            try { FileAlbumServiceImpl albumService = new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
                List<Album> album = albumService.findAllAlbum();
                List<Brano> brani = new ArrayList<>();
                for (Album a : album) {
                    brani.addAll(a.getListaBrani());
                }
                ordineOriginale = new ArrayList<>(brani);
                for (Brano brano : brani) {
                    codaService.aggiungiBranoCoda(coda, brano);
                }
            } catch (BusinessException e) { System.out.println("Errore nel caricamento del brano " + e.getMessage()); }
        } else ordineOriginale = new ArrayList<>(coda.getListaCodaBrani());
        barraAvanzamento.valueProperty().addListener((obs, vecchioValore, nuovoValore) -> aggiornaStileBarraAvanzamento());
        aggiornaTemaPlayer();
        aggiornaPulsantiRiproduzione();
     }
     public void aggiornaTemaPlayer() {
        if (titoloBrano == null || titoloBrano.getScene() == null) { return; }
        Parent radice = titoloBrano.getScene().getRoot();
        TemaVisivo.applicaTema(radice);
        radice.setStyle("-fx-background-color: " + TemaVisivo.getColoreSfondo() + ";");
        String coloreTesto = TemaVisivo.getColoreTesto();
        if (TemaVisivo.isScuro()) { copertinaAlbum.setEffect(null);
            // Tema scuro: copertina originale e overlay scuro.
            overlayCopertina.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        } else { copertinaAlbum.setEffect(new GaussianBlur(1));
            overlayCopertina.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            titoloBrano.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.68);");
            artistaBrano.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.68);");
            albumBrano.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.68);");
            tempoTrascorso.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.68);");
            tempoRimanente.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.68);");
        }
        String coloreEtichette;
        if (apertoDaPlaylist) { coloreEtichette = "white";
        } else { coloreEtichette = TemaVisivo.isScuro() ? "white" : (PlayerStato.getBranoCorrente() == null ? "black" : "white"); }
        titoloBrano.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + coloreEtichette + ";");
        artistaBrano.setStyle("-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + coloreEtichette + ";");
        albumBrano.setStyle("-fx-font-size: 20px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + coloreEtichette + ";");
        tempoTrascorso.setStyle("-fx-font-size: 12px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + coloreEtichette + ";");
        tempoRimanente.setStyle("-fx-font-size: 12px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + coloreEtichette + ";");
        Platform.runLater(this::aggiornaStileBarraAvanzamento);
    }
  private void aggiornaStileBarraAvanzamento() {
        if (barraAvanzamento == null) { return; }
        double percentuale = Math.max(0, Math.min(100, barraAvanzamento.getValue() * 100));
        String coloreRiempimento = TemaVisivo.getColoreBarra();
        barraAvanzamento.applyCss();
        javafx.scene.Node track = barraAvanzamento.lookup(".track");
        if (track != null) { track.setStyle("-fx-background-color: linear-gradient(to right, " + coloreRiempimento + " " + percentuale + "%, #808080 " + percentuale + "%);" + "-fx-background-radius: 5;"); }
        javafx.scene.Node thumb = barraAvanzamento.lookup(".thumb");
        if (thumb != null) { thumb.setStyle("-fx-background-color: " + coloreRiempimento + ";" + "-fx-background-radius: 10;"); }
    }
      private void aggiornaPulsanteAvanti() {
        if (bottoneAvanti == null || codaService == null || coda == null) { return; }
        try { bottoneAvanti.setDisable(codaService.isAvantiDisabilitato(coda));
        } catch (BusinessException e) { System.out.println("Errore nell'aggiornamento del pulsante Avanti: " + e.getMessage());
        }
    }
    private void aggiornaPulsantiRiproduzione() {
        if (bottoneIndietro == null || bottonePlay == null || bottonePausa == null || bottoneStop == null || bottoneAvanti == null || bottoneShuffle == null || bottoneRepeat == null || codaService == null || coda == null) { return; }
        boolean nessunBrano = PlayerStato.getBranoCorrente() == null;
        bottoneIndietro.setDisable(nessunBrano);
        bottonePlay.setDisable(nessunBrano);
        bottonePausa.setDisable(nessunBrano);
        bottoneStop.setDisable(nessunBrano);
        bottoneShuffle.setDisable(nessunBrano);
        bottoneRepeat.setDisable(nessunBrano);
        aggiornaPulsanteAvanti();
    }
}
