package com.example.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.FXMLLoader;
import javafx.scene.Parent;
import javafx.geometry.Insets  //Serve per definire margini di un contenitore grafico
import javafx.scene.layout.FlowPane;  //Layout che dispone gli elementi uno dopo l'altro in riga o colonna
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;  //Contenitore di layout che dispone i componenti in colonna, uno sotto l'altro
import javafx.scene.layout.HBox;  //Contenitore di layout che dispone i componenti in riga, uno di fianco all'altro
import javafx.scene.control.Label;
import javafx.scene.layout.Region;  //Classe base di JavaFX per tutti i nodi di layout che hanno una larghezza e altezza impostabili
import javafx.scene.paint.Color;  //Classe usata per definire i colori
import javafx.scene.layout.Background;  //Classe che gestisce lo sfondo dei vari pannelli di layout
import javafx.scene.layout.BackGroundFill;  //Definisce come riempire lo sfondo di un pannello
import javafx.scene.layout.CornerRadii;  //Specifica il raggio di curvatura degli angoli di un componente
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;  //Contenitore di layout che divide la finestra in 5 aree principali: top, bottom, left, right e center
import javafx.animation.KeyFrame;  //Definisce quale azione deve accadere in un preciso istante di tempo
import javafx.animation.Timeline;  //Timer che esegue una o più azioni a intervalli di tempi definiti
import javafx.util.Duration;  //Rappresenta una quantità di tempo usata per stabilire la durata dell'animazione o la frequenza del timer
import com.example.business.exception.BusinessException;
import com.example.domain.Brano;
import com.example.domain.StatoRiproduzione;
import com.example.domain.GenereMusicale;
import com.example.domain.Utente;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileAlbumServiceImpl;
import java.util.*;
import javafx.scene.Node;  //Superclasse di quasi tutti gli elementi visivi in JavaFX
import javafx.scene.image.Image;  //Rappresenta il file dell'immagine 
import javafx.scene.image.ImageView;  //Componente grafico che mostra l'oggetto Image sullo schermo
import java.io.File;
import javafx.scene.layout.StackPane;  //Contenitore di layout che sovrappone gli elementi l'uno sopra l'altro
import javafx.scene.layout.BackgroundImage;  //Definisce l'immagine da usare come sfondo
import javafx.scene.layout.BackgroundPosition;  //Determina la posizione iniziale dell'immagine di sfondo all'interno del pannello
import javafx.scene.layout.BackgroundRepeat;  //Decide se l'immagine di sfondo deve ripetersi o rimanere singola se il pannello si ingrandisce
import javafx.scene.layout.BackgroundSize;  //Controlla la dimensione dello sfondo
import javafx.scene.control.ScrollPane;  //Pannello che racchiude un contenuto e mostra automaticamente le barrie di scorrimento (verticale od orizzontale) se il contenuto è troppo grande rispetto allo spazio disponibile nella finestra
public class MainController {
  @FXML
  private Label titoloLabel;
  @FXML
  private VBox contenutoCentrale;
  @FXML
  private HBox miniPlayerContenitore;
  @FXML
  private Label branoMiniPlayer;
  @FXML
  private Slider barraMiniPlayer;
  @FXML
  private Button miniPlayPausa;
  @FXML
  private Button miniStop;
  @FXML
  private Button miniAvanti;
  @FXML
  private Button miniIndietro;
  @FXML
  private BorderPane rootPrincipale;
  @FXML
  private ComboBox<String> selettoreTema;
  @FXML
  private VBox barraLaterale;
  @FXML
  private Label nomeProgettoLabel;
  @FXML
  private Label nomeGruppoLabel;
  @FXML
  private Label titoloMiniPlayer;
  @FXML
  private Label artistaMiniPlayer;
  @FXML
  private HBox contenitoreInformazioniMiniPlayer;
  @FXML
  private VBox contenitoreControlliMiniPlayer;
  @FXML
  private ImageView copertinaMiniPlayer;
  private Timeline miniTimeline;
  private Timeline sincronizzazioneMiniPlayer;
  private boolean miniTrascinamento;
  private StatisticheController controllerStatisticheCorrente;
  private PlayerController controllerPlayerCorrente;
  private UtenteController controllerUtenteCorrente;
  private boolean ripristinoNavigazione=false;
  private String ultimaVista=null;
  @FXML
  private void initialize() { titoloLabel.setText("UnivaqSound");
    selettoreTema.getItems().setAll("Tema chiaro", "Tema scuro");
    selettoreTema.setValue(TemaVisivo.isScuro() ? "Tema scuro" : "Tema chiaro");
    applicaTemaAllaHome();
    Brano brano=PlayerStato.getBranoCorrente();
    aggiornaInformazioniMiniPlayer(brano);
    aggiornaVisibilitaMiniPlayer();
    if (brano!=null) { titoloMiniPlayer.setText(brano.getTitolo());
      barraMiniPlayer.setDisable(false); 
      barraMiniPlayer.setValue(PlayerStato.getProgressoCorrente()/brano.getDurata());  //Calcola e imposta la percentuale di avanzamento iniziale della barra
      if (PlayerStato.getCoda().getStato()==StatoRiproduzione.IN_RIPRODUZIONE) { miniPlayPausa.setText("⏸️");
        if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();  //Avvia il timer per far scorrere il tempo del brano in background
        avviaMiniTimer();
      } else miniPlayPausa.setText("▶️");
    } else { titoloMiniPlayer.setText("Nessun brano in riproduzione");
            barraMiniPlayer.setValue(0);
            barraMiniPlayer.setDisable(true);  //Blocca la barra per impedire modifiche
            miniPlayPausa.setText("▶️"); 
           }
    barraMiniPlayer.valueProperty().addListener((obs, vecchioValore, nuovoValore) -> { aggiornaStileBarraMini(); });  //Quando la barra del mini player si muove, aggiorna visivamente il colore e stile dello slider
    avviaSincronizzazioneMiniPlayer();
    Navigazione.impostaHome(() -> { miniPlayerContenitore.setVisible(true); //Configura la logica di navigazione per il ritorno alla schermata Home
      miniPlayerContenitore.setManaged(true);  //Indica a JavaFX di considerare lo spazio del mini player nel layout
      applicaTemaAllaHome();
      apriHome();
    });
    apriHome();  //Carica la schermata iniziale all'avvio dell'applicazione
  }
  private void aggiornaStileBarraMini() {
    if (barraMiniPlayer==null) return;
    double percentuale=Math.max(0, Math.min(100, barraMiniPlayer.getValue() * 100));
    String coloreRiempimento=TemaVisivo.getColoreBarra();
    barraMiniPlayer.applyCss();
    javafx.scene.Node track=barraMiniPlayer.lookup(".track");
    if (track!=null) track.setStyle("-fx-background-color: linear-gradient(to right, " + coloreRiempimento + " " + percentuale + "%, #808080 " + percentuale + "%);" + "-fx-background-radius: 5;");
    javafx.scene.Node thumb=barraMiniPlayer.lookup(".thumb");
    if (thumb!=null) thumb.setStyle("-fx-background-color: " + coloreRiempimento + ";" + "-fx-background-radius: 10;");
  }
  @FXML
  private void cambiaTema() {
    if (selettoreTema==null) return; 
    String scelta=selettoreTema.getValue(); 
    if ("Tema chiaro".equals(scelta)) TemaVisivo.impostaTema(TemaVisivo.Tema.CHIARO);
    else if ("Tema scuro".equals(scelta)) TemaVisivo.impostaTema(TemaVisivo.Tema.SCURO);
    applicaTemaAllaHome();
    RicercaController.aggiornaTemaCronologiaVisibile();
    if (contenutoCentrale!=null) {  //Se è presente una schermata centrale attiva, aggiorna ricorsivamente tutti i suoi elementi figli
      for (Node nodo : contenutoCentrale.getChildren()) {
        TemaVisivo.applicaTema(nodo);
        if (nodo instanceof Region) ((Region) nodo).setBackground(new Background(new BackgroundFill(Color.web(TemaVisivo.getColoreSfondo()), CornerRadii.EMPTY, Insets.EMPTY)));  //Se il nodo è un pannello (Region), sovrascrive lo sfondo col nuovo colore del tema
      }
      contenutoCentrale.setStyle("-fx-background-color: " + TemaVisivo.getColoreSfondo() + ";");
      contenutoCentrale.applyCss();
    }
    if (controllerPlayerCorrente!=null) controllerPlayerCorrente.aggiornaTemaPlayer();
    if (controllerStatisticheCorrente!=null) controllerStatisticheCorrente.aggiornaTemaStatistiche();
    if (controllerUtenteCorrente !=null) controllerUtenteCorrente.aggiornaTemaUtente();
    aggiornaStileBarraMini();
  }
  private void applicaTemaAllaHome() { boolean scuro=TemaVisivo.isScuro(); 
    if (barraLaterale!=null) barraLaterale.setStyle("-fx-background-color: " + (scuro ? "#191414" : "#f5f5f5") + ";");
    if (titoloMiniPlayer!=null) titoloMiniPlayer.setStyle("-fx-font-size: 14px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + TemaVisivo.getColoreTesto() + ";" + "-fx-cursor: hand;");
    if (artistaMiniPlayer!=null) artistaMiniPlayer.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: " + TemaVisivo.getColoreSecondario() + ";" + "-fx-cursor: hand;");
    if (nomeProgettoLabel!=null) nomeProgettoLabel.setStyle("-fx-text-fill: " + (scuro ? "white" : "#111111") + ";" + "-fx-font-size: 26px;" + "-fx-font-weight: bold;");
    if (nomeGruppoLabel!=null) nomeGruppoLabel.setStyle("-fx-text-fill: " + (scuro ? "#aaaaaa" : "#111111") + ";" + "-fx-font-size: 26px" + ";");
    if (rootPrincipale!=null) rootPrincipale.setStyle("-fx-background-color: " + TemaVisivo.getColoreSfondo() + ";");
    if (contenutoCentrale!=null) contenutoCentrale.setStyle("-fx-background-color: " + TemaVisivo.getColoreSfondo() + ";");
    if (miniPlayerContenitore!=null) miniPlayerContenitore.setStyle("-fx-background-color: " + (scuro ? #191919" : "#e5e5e5") + ";");
    if (titoloLabel!=null) titoloLabel.setStyle("-fx-font-size: 36px;" + "-fx-font-weight: bold;" + "-fx-text-fill: " + TemaVisivo.getColoreTesto() + ";");
    if (branoMiniPlayer!=null) branoMiniPlayer.setStyle("-fx-font-size: 14px;" + "-fx-text-fill: " + TemaVisivo.getColoreTesto() + ";");
    if (barraMiniPlayer!=null) TemaVisivo.applicaStileSlider(barraMiniPlayer);
  }
  @FXML
  private void apriHome() { controllerPlayerCorrente=null;
    miniPlayerContenitore.setVisible(true);
    miniPlayerContenitore.setManaged(true);
    contenutoCentrale.setSpacing(22);  //Configura la spaziatura verticale interna
    contenutoCentrale.setPadding(new Insets(28, 30, 28, 30));  //Configura i margini esterni
    contenutoCentrale.getChildren().clear();  //Svuota tutti gli elementi grafici presenti nella schermata centrale per caricarne dei nuovi
    mostraConsigliati();
  }
  private void mostraConsigliati() { Label titoloSezione=new Label("Consigliati per te");  //Configurazione dei layout e contenitori grafici
    titoloSezione.setTextFill(javafx.scene.paint.Color.web(TemaVisivo.getColoreTesto()));
    VBox intestazione= new VBox(6);
    intestazione.getChildren().add(titoloSezione);
    VBox contenutoHome=new VBox(22);
    contenutoHome.setFillWidth(true);
    contenutoHome.getChildren().add(intestazione);
    FlowPane grigliaBrani=new FlowPane();  //Crea una griglia dove le schede dei brani si dispongono
    grigliaBrani.setHgap(18);  //Spazio orizzontale tra le schede
    grigliaBrani.setVgap(18);  //Spazio verticale tra le schede
    grigliaBrani.setPrefWrapLength(750);  //Imposta la lunghezza in pixel oltre la quale il contenitore va a capo per disporre gli elementi sulla riga o colonna successiva
    grigliaBrani.setMaxWidth(Double.MAX_VALUE);
    contenutoHome.getChildren().add(grigliaBrani);
    ScrollPane scrollHome=new ScrollPane(contenutoHome);  //Inserisce tutto in un pannello a scorrimento per gestre gli elenchi lunghi
    scrollHome.setFitToWidth(true);
    scrollHome.setFitToHeight(false);
    scrollHome.setHBarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  //Nasconde la barra orizzontale 
    scrollHome.setVBarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  //Mostra la barra verticale solo se serve 
    ScrollHome.setStyle("-fx-background-color: transparent;" + "-fx-background: transparent;");
    VBox.setVgrow(grigliaBrani, Priority.ALWAYS);
    contenutoCentrale.getChildren().add(scrollHome);  //Inserisce il pannello di scorrimento nella UI
    try { FileBranoServiceImpl branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");  //Caricamento dati da file 
         List<Brano> brani=branoService.findAllBrani();
         FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
         List<Album> albumDisponibili=albumService.findAllAlbum();
         Utente utente=UtenteAttivo.getUtente();  //Filtraggio dei brani in base alle preferenze utente 
         Set<GenereMusicale> preferenze=null;
         if (utente!=null) preferenze=utente.getPreferenzeMusicali();
         List<Brano> consigliati=new ArrayList<>();
         for (Brano brano : brani) {
           if (brano==null) continue;
           if (preferenze!=null && !preferenze.isEmpty()) {  //Se l'utente ha espresso delle preferenze, tiene solo i brani che corrispondono ai suoi generi
             boolean corrisponde=false;
             for (GenereMusicale genere : brano.getGenereBrano()) {
               if (preferenze.contains(genere)) { corrisponde=true;  
                 break;
               }
             }
             if (!corrisponde) continue;  //Salta il brano se non rispecchia i gusti dell'utente
           }
           consigliati.add(brano);
         }
         if (consigliati.isEmpty()) { Label nessunBrano=new Label("Non ci sono ancora brani da consigliarti."); //Se non ci sono brani consigliati, mostra un messaggio testuale di avviso
           nessunBrano.setStyle("-fx-font-size: 15px;" + "-fx-text-fill: #b3b3b3;");
           grigliaBrani.getChildren().add(nessunBrano);
           return;
         }
         int limite=consigliati.size();  //Costruzione dinamica delle schede dei brani (UI)
         for (int i=0; i < limite; i++) {
           Brano brano=consigliati.get(i);
           VBox contenutoScheda=new VBox(12);  //Contenitore per i testi e i pulsanti della scheda
           contenutoScheda.setPadding(new Insets(18));
           contenutoScheda.setPrefWidth(220);
           contenutoScheda.setMinHeight(210);
           contenutoScheda.setMaxWidth(250);
           contenutoScheda.setStyle("-fx-background-color: transparent;");
           StackPane scheda= new StackPane();  //Pannello a livelli per sovrapporre testi all'immagine di copertina
           scheda.setPrefWidth(220);
           scheda.setMinWidth(220);
           scheda.setMinHeight(210);
           scheda.setPrefHeight(210);
           scheda.setMaxWidth(250);
           Album albumBrano=brano.getAlbumBrano();  //Cerca l'album a cui appartiene il brano
           if (albumBrano==null) {
             for (Album album : albumDisponibili) {
               if (album==null || album.getListaBrani()==null) continue;
               for (Brano branoAlbum : album.getListaBrani()) {
                 if (branoAlbum!=null && branoAlbum.getTitolo()!=null && brano.getTitolo()!=null && branoAlbum.getTitolo().trim().equalsIgnoreCase(brano.getTitolo().trim())) { albumBrano=album;
                   brano.setAlbumBrano(album);
                   break;
                 }
               }
               if (albumBrano!=null) break;
             }
           }
           Image immagineCopertina=null;  //Carica la copertina dell'album
           if (albumBrano!=null && albumBrano.getCopertina()!=null && !albumBrano.getCopertina().trim().isEmpty()) {
             File fileCopertina=new File("src/main/resources/dati/" + albumBrano.getCopertina().trim());
             if (fileCopertina.isFile()) { Image immagine=new Image(fileCopertina.toURI().toString());
               if (!immagine.isError()) immagineCopertina=immagine;
             }
           }
           if (immagineCopertina!=null) { BackgroundImage sfondoCopertina=new BackgroundImage(immagineCopertina, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, true, true, false, true)); //Imposta la copertina come sfondo, mantenendo le proporzioni e ritagliando l'eventuale parte in eccesso
             scheda.setBackground(new Background(sfondoCopertina)); 
           } else scheda.setBackground(new Background(new BackgroundFill(Color.web("#242424"), new CornerRadii(12), Insets.EMPTY)));
           Region oscuramento=new Region();   //Oscura leggermente l'immagine per rendere leggibili i testi
           oscuramento.setMouseTransparent(true);
           oscuramento.setStyle("-fx-background-color: rgba(0, 0, 0, 0.58);" + "-fx-background-radius: 12;");
           oscuramento.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
           Label icona=new Label("♫");   //Simbolo musicale
           icona.setStyle("-fx-font-size: 28px;" + "-fx-text-fill: #1ed760;");
           Label titoloBrano=new Label(brano.getTitolo());   
           titoloBrano.setWrapText(true);
           titoloBrano.setMaxWidth(190);
           titoloBrano.setStyle("-fx-font-size: 17px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");
           String generi=brano.getGenereBrano().isEmpty() ? "Genere non specificato" : brano.getGenereBrano().toString().replace("[","").replace("]","");
           Label genereBrano=new Label(generi);
           genereBrano.setWrapText(true);
           genereBrano.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: white;");
           int durata=brano.getDurata();
           String durataFormattata=String.format("%02d:%02d", durata / 60, durata % 60);  //Formatta i secondi della durata nel formato mm:ss
           Label durataBrano=new Label(durataFormattata);
           durataBrano.setStyle("-fx-font-size: 12px;" + "-fx-text-fill: #1ed760;");
           Button pulsantePlay=new Button("▶️Riproduci");
           pulsantePlay.setMaxWidth(Double.MAX_VALUE);
           pulsantePlay.setStyle("-fx-background-color: #1ed760;" + "-fx-text-fill: black;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;" + "-fx-cursor: hand;");
           pulsantePlay.setOnAction(event -> avviaBranoConsigliato(brano));  //Avvia la riproduzione al clic
           Region spazio=new Region();
           VBox.setVgrow(spazio, Priority.ALWAYS);  //Spinge la durata e il pulsante verso il basso della scheda
           contenutoScheda.getChildren().addAll(icona, titoloBrano, genereBrano, spazio, durataBrano, pulsantePlay);  //Ordine dei livelli: sfondo, oscuramento, contenuto
           scheda.getChildren().addAll(oscuramento, contenutoScheda);  
           grigliaBrani.getChildren().add(scheda);  //Aggiunge la scheda finita alla griglia generale
         }
        } catch(BusinessException e) { Label errore=new Label("Impossibile caricare i suggerimenti musicali.");
              errore.setStyle("-fx-text-fill: #ff7777;");
              grigliaBrani.getChildren().add(errore);
            }
      }
  private void avviaBranoConsigliato(Brano brano) {
    if (brano==null) return;
    try { if (miniTimeline!=null) { miniTimeline.stop();  //Ferma il timer grafico del mini player           
            miniTimeline=null;
        }
      FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resorces/dati/album.txt");
      List<Album> album=albumService.findAllAlbum();
      brano.setAlbumBrano(null);
      for (Album a : album) {
        for (Brano branoAlbum : a.getListaBrani()) {
          if (branoAlbum!=null && branoAlbum.getTitolo()!=null && brano.getTitolo()!=null && branoAlbum.getTitolo().trim().equalsIgnoreCase(brano.getTitolo().trim())) { brano.setAlbumBrano(a);
            break;
          }
        }
        if (brano.getAlbumBrano()!=null) break;
      }
      PlayerStato.avviaBranoDaHome(brano);
      titoloMiniPlayer.setText(brano.getTitolo());
      barraMiniPlayer.setValue(0);
      barraMiniPlayer.setDisable(false);
      miniPlayPausa.setText("⏸️");
      avviaMiniTimer();
      } catch(Exception e) { e.getMessage(); }
  }
  @FXML
  private void apriRicerca() throws IOException { mostraVista("/viste/RicercaView.fxml"); }
  @FXML
  private void apriLibreria() throws IOException { mostraVista("/viste/LibreriaView.fxml"); }
  @FXML
  private void apriPlaylist() throws IOException { mostraVista("/viste/PlaylistView.fxml"); }
  @FXML
  private void apriArtisti() throws IOException { mostraVista("/viste/ArtistaForm.fxml"); }
  @FXML
  private void apriAlbum() throws IOException { mostraVista("/viste/AlbumForm.fxml"); }
  @FXML
  private void apriBrani() throws IOException { mostraVista("/viste/BranoForm.fxml"); }
  @FXML
  private void apriPlayer() throws IOException { miniPlayerContenitore.setVisible(false);
    miniPlayerContenitore.setManaged(false);
    mostraVista("/viste/PlayerView.fxml");
  }
  @FXML
  private void apriPlayerDaArtista() {
    try { apriPlayer(); 
        } catch(IOException e) { e.getMessage(); }
  }
  @FXML
  private void apriPlayerDaTitolo() {
    try { apriPlayer();
        } catch(IOException e) { e.getMessage(); }
  }
  private void avviaMiniTimer() {
    if (miniTimeline!=null) { miniTimeline.stop(); //Se c'è già un timer attivo, lo ferma e lo resetta per evitare che si sovrappongano più thread
      miniTimeline=null;
    }
    miniTimeline=new Timeline(new KeyFrame(Duration.seconds(1), e -> {  //Crea un nuovo ciclo temporale che esegue il codice ogni secondo
      Brano brano=PlayerStato.getBranoCorrente();
      if (brano==null) { miniTimeline.stop();
        return;
      }
      double progresso=PlayerStato.getProgressoCorrente();
      double durata= brano.getDurata();
      if (durata > 0 && !miniTrascinamento) barraMiniPlayer.setValue(progresso / durata);  //Aggiorna la posizione della barra solo se la canzone ha una durata valida e se l'utente non sta attivamente trascinando lo slider con il mouse
      titoloMiniPlayer.setText(brano.getTitolo());  //Tiene aggiornato il testo con il titolo del brano corrente 
    }));
    miniTimeline.setCycleCount(Timeline.INDEFINITE);  //Imposta il timer affinché si ripeta all'infinito e lo fa partire 
    miniTimeline.play();
  }
  @FXML
  private void miniPlayPausa() {
    try { Brano brano=PlayerStato.getBranoCorrente();
         if (brano==null) return;
         if (PlayerStato.getCoda().getStato()==StatoRiproduzione.IN_RIPRODUZIONE) {  //Se la musica è attiva, allora viene messa in pausa
           PlayerStato.getCodaService().pausa(PlayerStato.getCoda());
           PlayerStato.fermaTimerRiproduzione();
           if (miniTimeline!=null) { miniTimeline.stop();  //Arresta e distrugge il timer grafico per fermare l'avanzamento della barra            
             miniTimeline=null;
           }
           miniPlayPausa.setText("▶️");  //Cambia l'icona mostrando il simbolo del Play
         } else {  //Se la musica è in pausa o ferma, allora si avvia la riproduzione 
           PlayerStato.getCodaService().play(PlayerStato.getCoda());
           if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();  //Riattiva i timer di sistema e quelli grafici per far muovere nuovamente la barra
           miniPlayPausa.setText("⏸️");  //Cambia l'icona mostrando il simbolo della Pausa
           avviaMiniTimer();  //Fa ripartire l'aggiornamento grafico al secondo
         }
        } catch(BusinessException e) { e.getMessage(); }
  }
  @FXML
  private void miniStop() {
    try {  //Ferma i timer di sistema della riproduzione e salva lo stop nella cronologia d'ascolto
      PlayerStato.fermaTimerRiproduzione();
      PlayerStato.terminaSessioneCronologiaConStop();
      PlayerStato.getCodaService().stop(PlayerStato.getCoda());  //Invia il comando di interruzione definitiva al motore audio di background
      PlayerStato.setProgressoCorrente(0);  //Resetta i dati logici del brano e del tempo trascorso all'interno dello stato del player
      PlayerStato.setBranoCorrente(null);
      if (branoMiniPlayer!=null) branoMiniPlayer.setText("Nessun Brano in riproduzione");  //Ripristina i testi della UI allo stato iniziale predefinito
      if (barraMiniPlayer!=null) { barraMiniPlayer.setValue(0);  //Azzera la barra di avanzamento e la blocca per impedire interazioni con il mouse       
        barraMiniPlayer.setDisable(true);
      }
      if (miniPlayPausa!=null) miniPlayPausa.setText("▶");  //Imposta nuovamente il pulsante principale con l'icona del Play
      if (miniTimeline!=null) { miniTimeline.stop();  //Ferma e distrugge la Timeline per bloccare definitivamente l'aggiornamento automatico della barra       
        miniTimeline=null;
      }
    } catch (BusinessException e) { e.getMessage(); }
  }
  @FXML
  private void miniAvanti() {
    try {
      if (PlayerStato.getCodaService().isAvantiDisabilitato(PlayerStato.getCoda())) return;
      //Ferma il timer grafico del mini-player
      if (miniTimeline!=null) { miniTimeline.stop();
        miniTimeline=null;
      }
      //Passa al brano successivo
      PlayerStato.getCodaService().avanti(PlayerStato.getCoda());
      Brano brano=PlayerStato.getCodaService().getBranoCorrente(PlayerStato.getCoda());
      if (brano!=null) { PlayerStato.setBranoCorrente(brano);
        PlayerStato.setProgressoCorrente(0);
        branoMiniPlayer.setText(brano.getTitolo());
        barraMiniPlayer.setValue(0);
        barraMiniPlayer.setDisable(false);
        PlayerStato.getCodaService().play(PlayerStato.getCoda());
        if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();
        miniPlayPausa.setText("⏸");
        avviaMiniTimer();
      } else {
        //La coda è terminata: puliamo il mini-player
        PlayerStato.fermaTimerRiproduzione();
        PlayerStato.setBranoCorrente(null);
        PlayerStato.setProgressoCorrente(0);
        titoloMiniPlayer.setText("Nessun brano in riproduzione");
        artistaMiniPlayer.setText("");
        copertinaMiniPlayer.setImage(null);
        barraMiniPlayer.setValue(0);
        barraMiniPlayer.setDisable(true);
        miniPlayPausa.setText("▶");
      }
    } catch (BusinessException e) { e.getMessage(); }
  }
  @FXML
  private void miniIndietro() {
    try { Brano branoCorrente=PlayerStato.getBranoCorrente();
      if (branoCorrente==null) return;
      double progresso=PlayerStato.getProgressoCorrente();
      //Se sono trascorsi almeno 5 secondi, riavviamo il brano corrente
      if (progresso>=5) { riavviaBranoMiniPlayer();
        return;
      }
      //Nei primi 5 secondi torniamo al precedente se disponibile
      List<Brano> braniCoda=PlayerStato.getCodaService().getCoda(PlayerStato.getCoda());
      int indiceCorrente=braniCoda.indexOf(branoCorrente);
      if (indiceCorrente<=0) { riavviaBranoMiniPlayer();
        //In questo caso non esiste un precedente quindi riavviamo il brano corrente    
        return;
      }
      if (miniTimeline!=null) { miniTimeline.stop()
        miniTimeline=null;
      }
      PlayerStato.salvaSessioneCronologia();
      PlayerStato.getCodaService().indietro(PlayerStato.getCoda());
      Brano branoPrecedente=PlayerStato.getCodaService().getBranoCorrente(PlayerStato.getCoda());
      if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();
      miniPlayPausa.setText("⏸");
      avviaMiniTimer();
      aggiornaPulsanteMiniIndietro();
    } catch (BusinessException e) { e.getMessage(); }
  }
  private void riavviaBranoMiniPlayer() throws BusinessException {
    if (miniTimeline!=null) { miniTimeline.stop();
      miniTimeline=null;
    }
    PlayerStato.salvaSessioneCronologia();
    PlayerStato.setProgressoCorrente(0);
    PlayerStato.iniziaNuovoAscolto();
    barraMiniPlayer.setValue(0);
    PlayerStato.getCodaService().play(PlayerStato.getCoda());
    if (!PlayerStato.isTimerRiproduzioneAttivo()) PlayerStato.avviaTimerRiproduzione();
    miniPlayPausa.setText("⏸");
    avviaMiniTimer();
    aggiornaPulsanteMiniIndietro();
  }
  private void aggiornaPulsanteMiniIndietro() {
    if (miniIndietro!=null) miniIndietro.setDisable(PlayerStato.getBranoCorrente()==null);
  }
  @FXML
  private void miniInizioTrascinamento() { miniTrascinamento=true;
    if (miniTimeline!=null) miniTimeline.pause();
  }
  @FXML
  private void miniFineTrascinamento() { miniTrascinamento=false;  //Segnala che l'utente ha terminato di spostare la abrra con il mouse
    try { Brano brano=PlayerStato.getBranoCorrente();
      if (brano==null) return;
      double durataSecondi=brano.getDurata();
      double nuovoProgresso=barraMiniPlayer.getValue()*durataSecondi;  //Calcola il punto esatto della canzone moltiplicando la percentuale dello slider per la durata totale 
      PlayerStato.setProgressoCorrente(nuovoProgresso);  //Aggiorna lo stato globale portando l'audio al secondo selezionato dall'utente
      if (miniTimeline!=null) { miniTimeline.stop();  //Ferma temporaneamente il timer grafico per resettarlo senza problemi       
        miniTimeline=null;
      }
      if (PlayerStato.getCoda().getStato()==StatoRiproduzione.IN_RIPRODUZIONE) avviaMiniTimer();  //Se il brano era attivamente in riproduzione, fa ripartire subito il timer dei secondi
    } catch (Exception e) { e.getMessage(); }
  }
  @FXML
  private void apriStatistiche() throws IOException { mostraVista("/viste/StatisticheView.fxml"); }
  @FXML
  private void apriUtente() throws IOException { mostraVista("/viste/UtenteForm.fxml"); }
  private void mostraVista(String percorso) throws IOException { Navigazione.svuotaCronologia();
    boolean mostraMiniPlayer=!percorso.equals("/viste/PlayerView.fxml");
    miniPlayerContenitore.setVisible(mostraMiniPlayer);
    miniPlayerContenitore.setManaged(mostraMiniPlayer);
    FXMLLoader loader=new FXMLLoader(getClass().get(Resource(percorso));  //Inizializza il caricatore FXML puntando al percorso della risorsa passata come parametro
    Parent vista=loader.load();  //Carica l'interfaccia grafica
    if (percorso.equals("/viste/StatisticheView.fxml")) controllerStatisticheCorrente=loader.getController();  //Associa il controller corretto in base alla schermata aperta e azzera gli altri per liberare memoria
    else controllerStatisticheCorrente=null;
    if (percorso.equals("/viste/PlayerView.fxml")) controllerPlayerCorrente=loader.getController();
    else controllerPlayerCorrente=null;
    if (percorso.equals("/viste/UtenteForm.fxml")) controllerUtenteCorrente=loader.getController();
    else controllerUtenteCorrente=null;
    if (percorso.equals("/viste/PlayerView.fxml")) { contenutoCentrale.setPadding(Insets.EMPTY); //Rimuove i margini e gli spazi se siamo nel player a schermo intero, altrimenti imposta i margini standard della Home      
      contenutoCentrale.setSpacing(0);
    } else { contenutoCentrale.setPadding(new Insets(28,30,28,30));
      contenutoCentrale.setSpacing(22);
    }
    contenutoCentrale.getChildren().setAll(vista);  //Inserisce la nuova interfaccia grafica nel pannello centrale sostituendo quella vecchia
    VBox.setVgrow(vista, Priority.ALWAYS);  //Fa in modo che la vista occupi tutto lo spazio verticale disponibile
    TemaVisivo.applicaTema(vista);  //Applica le regole CSS del tema corrente (chiaro/scuro) alla nuova finestra appena caricata
    if (vista instanceof Region) ((Region vista).setBackground(new Background(new BackgroundFill(Color.web(TemaVisivo.getColoreSfondo()), CornerRadii.EMPTY, Insets.EMPTY)));
    if (percorso.equals("/viste/UtenteForm.fxml")) { UtenteController controllerUtente= loader.getController();  //Se è stata aperta la schermata utente, forza l'aggiornamento grafica del suo tema   
      controllerUtente.aggiornaTemaUtente();
    }
    if (controllerPlayerCorrente!=null) controllerPlayerCorrente.aggiornaTemaPlayer();  //Allinea e aggiorna lo stile visivo dei singoli controller attivi (Player e Statistiche)
    if (controllerPlayerCorrente!=null) controllerPlayerCorrente.aggiornaTemaStatistiche();
  }
  private void tornaAllaVistaPrecedente() { boolean tornatoIndietro=Navigazione.indietro();
    if (!tornatoIndietro) apriHome();
  }
  private String ottieniNomeArtistaMiniPlayer(Brano brano) {
    if (brano==null||brano.getAlbumBrano()==null) return "";
    Artista artista=brano.getAlbumBrano().getArtista();
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "";
  }
  private void aggiornaInformazioniMiniPlayer(Brano brano) {
    if (brano==null) { titoloMiniPlayer.setText("");  //Se nessun brano è in esecuzione, allora si svuotano tutti i campi del mini player
      artistaMiniPlayer.setText("");
      copertinaMiniPlayer.setImage(null);
      return;
    }
    titoloMiniPlayer.setText(brano.getTitolo());  //Se il brano è presente, allora nel mini player vengono mostrati il titolo e il nome dell'artista o del gruppo
    artistaMiniPlayer.setText(ottieniNomeArtistaMiniPlayer(brano));
    copertinaMiniPlayer.setImage(null);  //Reset preventivo della vecchia copertina
    if (brano.getAlbumBrano()==null) return;  //Se il brano non appartiene a un album, interrompe il metodo
    String nomeFile=brano.getAlbumBrano().getCopertina();  //Recupera il nome del file dell'immagine e verifica che non ci sia un testo vuoto
    if (nomeFile==null||nomeFile.trim().isEmpty()) return;
    nomeFile=nomeFile.trim();
    File file=new File("src/main/resources/dati/" + nomeFile.trim());  //Si cerca di caricare la copertina cercandola direttamente nel percorso della cartella
    if (file.isFile()) { Image immagine=new Image(file.toURI().toString());
      if(!immagine.isError()) copertinaMiniPlayer.setImage(immagine);  //Imposta la foto se non ci sono errori
    }
    var risorsa=getClass().getResource("/dati/" + nomeFile);  //Cerca l'immagine all'interno del file di build compilato per evitare errori 
    if (risorsa!=null) { Image immagine=new Image(risorsa.toExternalForm());
      if (!immagine.isError()) copertinaMiniPlayer.setImage(immagine);
    }
  }
  private void aggiornaPulsanteMiniAvanti() {
    if (miniAvanti==null) return;
    try {miniAvanti.setDisable(PlayerStato.getCodaService().isAvantiDisabilitato(PlayerStato.getCoda()));  //Viene chiesto al servizio della coda se il comando "Mini Avanti" deve essere disabilitato o meno
        } catch (BusinessException e) { e.getMessage(); }
  }
  private void avviaSincronizzazioneMiniPlayer() { sincronizzazioneMiniPlayer=new Timeline(new Keyframe(Duration.millis(250), evento -> {  //Inizializza una Timeline con polling a 250 millisecondi per sincronizzare costantemente la UI e verificare eventualmente nuovi dati o eventi
      Brano brano=PlayerStato.getBranoCorrente();
      if (miniIndietro!=null) miniIndietro.setDisable(brano==null);
      aggiornaInformazioniMiniPlayer(brano);  //Aggiorna i dati del mini player
      aggiornaVisibilitaMiniPlayer();  //Controlla se mostrare o nascondere il pannello del player
      if (brano==null) { titoloMiniPlayer.setText("Nessun brano in riproduzione"); //Se la riproduzione è vuota, resetta i componenti grafici di default    
        barraMiniPlayer.setValue(0);
        barraMiniPlayer.setDisable(true);
        miniPlayPausa.setText("▶");
        miniAvanti.setDisable(true);
        return;
      }
      aggiornaPulsanteMiniAvanti();  //Se è presente un brano attivo, aggiorna lo stato dei controlli e viene valutato se abilitare o meno il tasto "Avanti"
      titoloMiniPlayer.setText(brano.getTitolo());
      barraMiniPlayer.setDisable(false);  //Sblocca lo slider
      if (brano.getDurata()>0) { double progresso=PlayerStato.getProgressoCorrente()/brano.getDurata();
        barraMiniPlayer.setValue(Math.max(0, Math.min(1, progresso)));  //Assicura che la barra rimanga sempre entro i limiti corretti (tra lo 0% e il 100%)
      }
      if (PlayerStato.getCoda().getStato()==StatoRiproduzione.IN_RIPRODUZIONE) miniPlayPausa.setText("⏸");  //Cambia l'icona del pulsante (Play o Pausa) in base allo stato della musica
      else miniPlayPausa.setText("▶");
    }));
    sincronizzazioneMiniPlayer.setCycleCount(Timeline.INDEFINITE);  //Imposta il timer in modo che continui a ripetersi all'infinito
    sincronizzazioneMiniPlayer.play();  //Fa partire il timer per iniziare ad aggiornare la grafica
  }
  private void aggiornaVisibilitaMiniPlayer() { boolean branoPresente=PlayerStato.getBranoCorrente()!=null;
    contenitoreInformazioniMiniPlayer.setVisible(branoPresente);  //Mostra o nasconde il testo e la copertina del brano
    contenitoreInformazioniMiniPlayer.setManaged(branoPresente);  //Adatta il testo e la copertina del brano rispetto allo spazio nella schermata
    contenitoreControlliMiniPlayer.setVisible(branoPresente);  //Mostra o nasconde i pulsanti di controllo
    contenitoreControlliMiniPlayer.setManaged(branoPresente);
  }
}
