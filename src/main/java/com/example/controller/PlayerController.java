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
  @FXML
    private void play() {
        try {
            Brano branoPrima = PlayerStato.getBranoCorrente();
            boolean ripresa = coda.getStato() == StatoRiproduzione.IN_PAUSA;
            codaService.play(coda);
            Brano brano=codaService.getBranoCorrente(coda);
            if (brano!=null) {
                boolean nuovoBrano=branoPrima==null || !branoPrima.getTitolo().equalsIgnoreCase(brano.getTitolo());
                if (!ripresa && (nuovoBrano || !riproduzioneIniziata)) {
                    azzeraConteggioAscolto();
                    riproduzioneIniziata=true;
                }
                barraAvanzamento.setDisable(false);
                PlayerStato.setBranoCorrente(brano);
                progressoCorrente = PlayerStato.getProgressoCorrente();
                barraAvanzamento.setValue(progressoCorrente/brano.getDurata());
                titoloBrano.setText(brano.getTitolo());
                aggiornaCopertinaAlbum(brano);
                if(brano.getAlbumBrano()!=null) albumBrano.setText(brano.getAlbumBrano().getTitolo());
                else albumBrano.setText("");
                if (brano.getAlbumBrano()!=null && brano.getAlbumBrano().getArtista()!=null)
                    artistaBrano.setText(ottieniNomeArtista(brano.getAlbumBrano().getArtista()));
                else artistaBrano.setText("");
                if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();
            avviaTimer(brano);
            aggiornaPulsantiRiproduzione();
            }
        } catch (BusinessException e) {
            System.out.println("Errore durante la riproduzione: " + e.getMessage());
        }
    }
    @FXML
    private void pausa() {
       try { codaService.pausa(coda);
          PlayerStato.fermaTimerRiproduzione();
          if (timeline!=null) { timeline.stop();
             timeline=null;
          }
          System.out.println("Riproduzione in pausa");
        } catch (BusinessException e) { System.out.println("Errore durante la pausa: " + e.getMessage()); }
    }
    @FXML
    private void stop() {
      try { PlayerStato.fermaTimerRiproduzione();
          PlayerStato.terminaSessioneCronologiaConStop();
          if (timeline != null) { timeline.stop();
             timeline=null;
          }
          codaService.stop(coda);
            copertinaAlbum.setImage(null);
            progressoCorrente=0;
            barraAvanzamento.setValue(0);
            PlayerStato.setProgressoCorrente(0);
            azzeraConteggioAscolto();
            riproduzioneIniziata=false;
            PlayerStato.setBranoCorrente(null);
            apertoDaPlaylist = false;
            titoloBrano.setText("Nessun brano in riproduzione");
            artistaBrano.setText("");
            albumBrano.setText("");
            aggiornaTempiBrano(null);
            aggiornaTemaPlayer();
            barraAvanzamento.setDisable(true);
            aggiornaPulsantiRiproduzione();
            System.out.println("Riproduzione stoppata");
        } catch (BusinessException e) { System.out.println("Errore durante lo stop: " + e.getMessage()); }
    }
  @FXML
  private void avanti() {
      try {
          // Se siamo sull'ultimo brano e Repeat è disattivato,
          // non modificare la coda e non fermare i timer.
          if (codaService.isAvantiDisabilitato(coda)) { aggiornaPulsanteAvanti();
              return;
          }
          // Ferma il timer grafico.
          if (timeline != null) { timeline.stop();
              timeline = null;
          }
          // Salva la sessione prima di cambiare brano.
          PlayerStato.fermaTimerRiproduzione();
          codaService.avanti(coda);
          Brano brano = codaService.getBranoCorrente(coda);
          if (brano != null) { aggiornaCopertinaAlbum(brano);
              titoloBrano.setText(brano.getTitolo());
              progressoCorrente = 0;
              barraAvanzamento.setValue(0);
              barraAvanzamento.setDisable(false);
              PlayerStato.setProgressoCorrente(0);
              PlayerStato.setBranoCorrente(brano);
              PlayerStato.iniziaNuovoAscolto();
              if (brano.getAlbumBrano() != null) { albumBrano.setText(brano.getAlbumBrano().getTitolo());
                } else { albumBrano.setText(""); }
                if (brano.getAlbumBrano() != null && brano.getAlbumBrano().getArtista() != null) {
                    artistaBrano.setText(ottieniNomeArtista(brano.getAlbumBrano().getArtista()));
                } else { artistaBrano.setText(""); }
                codaService.play(coda);
                PlayerStato.avviaTimerRiproduzione();
                avviaTimer(brano);
                System.out.println("Brano successivo: " + brano.getTitolo());
            } else { titoloBrano.setText("Nessun brano in riproduzione");
                artistaBrano.setText("");
                albumBrano.setText("");
                copertinaAlbum.setImage(null);
                aggiornaTempiBrano(null);
                progressoCorrente = 0;
                barraAvanzamento.setValue(0);
                barraAvanzamento.setDisable(true);
                PlayerStato.setProgressoCorrente(0);
                PlayerStato.setBranoCorrente(null);
                System.out.println("Fine della coda.");
            }
            aggiornaPulsanteAvanti();
        } catch (BusinessException e) { System.out.println("Errore durante il passaggio al brano successivo: " + e.getMessage());
        }
    }
  @FXML
  private void indietro() {
     try {
         if (timeline != null) { timeline.stop();
                timeline = null;
            }
            PlayerStato.fermaTimerRiproduzione();
            if (progressoCorrente >= 5) { Brano branoCorrente = codaService.getBranoCorrente(coda);
                if (branoCorrente != null) { PlayerStato.salvaSessioneCronologia();
                    progressoCorrente = 0;
                    barraAvanzamento.setValue(0);
                    PlayerStato.setProgressoCorrente(0);
                    PlayerStato.setBranoCorrente(branoCorrente);
                    PlayerStato.iniziaNuovoAscolto();
                    codaService.play(coda);
                    PlayerStato.avviaTimerRiproduzione();
                    avviaTimer(branoCorrente);
                    System.out.println("Brano riavviato.");
                }
                return;
            }
            PlayerStato.salvaSessioneCronologia();
            codaService.indietro(coda);
            Brano brano = codaService.getBranoCorrente(coda);
            progressoCorrente = 0;
            barraAvanzamento.setValue(0);
            PlayerStato.setProgressoCorrente(0);
            if (brano != null) { PlayerStato.setBranoCorrente(brano);
                PlayerStato.iniziaNuovoAscolto();
                titoloBrano.setText(brano.getTitolo());
                aggiornaCopertinaAlbum(brano);
                if (brano.getAlbumBrano() != null) { albumBrano.setText(brano.getAlbumBrano().getTitolo());
                } else { albumBrano.setText(""); }
                if (brano.getAlbumBrano() != null && brano.getAlbumBrano().getArtista() != null) { artistaBrano.setText(ottieniNomeArtista(brano.getAlbumBrano().getArtista()));
                } else { artistaBrano.setText(""); }
                barraAvanzamento.setDisable(false);
                codaService.play(coda);
                PlayerStato.avviaTimerRiproduzione();
                avviaTimer(brano);
                System.out.println("Brano precedente: " + brano.getTitolo());
            } else { titoloBrano.setText("Nessun brano in riproduzione");
                artistaBrano.setText("");
                albumBrano.setText("");
                copertinaAlbum.setImage(null);
                barraAvanzamento.setDisable(true);
                PlayerStato.setBranoCorrente(null);
                System.out.println("Nessun brano precedente.");
            }
            aggiornaPulsanteAvanti();
        } catch (BusinessException e) { System.out.println("Errore nel ritornare al brano precedente: " + e.getMessage()); }
    }
  
}
