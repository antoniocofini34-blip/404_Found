package com.example.controller;
import com.example.business.exception.BusinessException;
import com.example.business.RicercaService;
import com.example.business.impl.file.FIleAlbumServiceImpl;
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.business.impl.file.FileBranoServiceImpl;
import com.example.business.impl.file.FileRicercaServiceImpl;
import com.example.business.impl.file.FilePlaylistService;
import com.example.domain.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
  private TextField durataMassima;
  @FXML
  private TextField annoPubblicazione;
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
    FileAlbumServiceImpl albumService=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
    FileArtistaServiceImpl artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artista.txt");
    ricercaService=new FileRicercaServiceImpl(albumService, artistaService);
    colonnaTipo.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getTipo()));  //Colonne della tabella unica
    colonnaTitolo.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getTitolo()));
    colonnaDurata.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getDurata()));
    colonnaGenere.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getGenere()));
    colonnaRiproduci.setCellFactory(colonna -> new TableCell<>() {
      private final Button bottone=new Button("Riproduci");
      private final Button bottonePlaylist=new Button("+ Playlist");
      private final HBox contenitore=new HBox(5, bottone, bottonePlaylist); //Mette i due buttoni vicini
        { 
          bottone.SetOnAction(event -> {  //Azione del tasto "Riproduci": fa partire il brano selezionato
            RisultatoRicerca risultato=getTableView().getItems().get(getIndex());
            Brano brano= risultato.brano();
            if (brano!=null) riproduciBrano(brano);
          });
          bottonePlaylist.setOnAction(event -> {  //Azione del tasto "+ playlist": aggiunge il brano alla playlist dell'utente
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
          setGraphic(null);  //Svuota la cella se la riga della tabella è vuota
          return;
          }
        RisultatoRicerca risultato=getTableView().getItems().get(getIndex());
        boolean eBrano=risultato.brano()!=null;  //I pulsanti devono apparire solo se la riga attuale è un brano
        setGraphic(eBrano ? contenitore : null);
        }
      });
    colonnaDataPubblicazione.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAnnoPubblicazione()).asObject());  //Mostra l'anno di pubblicazione convertendo il numero in una proprietà leggibile da JavaFX
    colonnaNumeroAscolti.setCellValueFactory(cellData -> {
      RisultatoRicerca risultato=cellData.getValue();
      Utente utente=UtenteAttivo.getUtente();
      int numeroAscolti=0;
      if (utente!=null && utente.getEmail()!=null) {  //Calcola gli ascolti solo se l'utente è attualmente loggato nel sistema
        String email=utente.getEmail();
        Brano brano=risultato.brano();
        if (brano!=null) numeroAscolti=PlayerStato.getNumeroAscoltiUtente(email, brano.getTitolo());  //Se la riga è un brano singolo, prende direttamente il suo numero di ascolti
        else { Artista artista=risultato.artista();  //Se la riga è un artista, somma gli ascolti di tutti i suoi brani
          if (artista!=null) {
            try {  FileBranoServiceImpl servizioBrani=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
                   FileAlbumServiceImpl servizioAlbum=new FileAlbumServiceImpl("src/main/resources/dati/album.txt");
                   List<Brano> tuttiBrani=servizioBrani.findAllBrani();
                   List<Album> tuttiAlbum=servizioAlbum.findAllAlbum();
                   String nomeArtista=null;
                   if (artista instanceof ArtistaSolita) nomeArtista=((ArtistaSolista) artista).getNomeArte();
                   else if (artista instanceof ArtistaGruppo) nomeArtista=((ArtistaGruppo) artista).getNomeGruppo();
                   if (nomeArtista!=null) {
                     for (Brano branoCatalogo : tuttiBrani) {  //Gira su tutti i brani del catalogo per calcolare la somma totale degli ascolti dell'artista
                       if (branoCatalogo==null || branoCatalogo.getTitolo()==null) continue;
                       int ascolti=PlayerStato.getNumeroAscoltiUtente(email, branoCatalogo.getTitolo());
                       if (ascolti <= 0) continue;  //Salta i brani mai ascoltati
                       for (Album album : tuttiAlbum) {  //Verifica se il brano in analisi appartiene a un album di questo specifico artista
                         if (album==null || album.getArtista()==null || album.getListaBrani()==null) continue;
                         Artista artistaAlbum=album.getArtista();
                         String nomeAlbumArtista=null;
                          if (artistaAlbum instanceof ArtistaSolita) nomeAlbumArtista=((ArtistaSolista) artistaAlbum).getNomeArte();
                          else if (artistaAlbum instanceof ArtistaGruppo) nomeAlbumArtista=((ArtistaGruppo) artistaAlbum).getNomeGruppo();
                       if (nomeAlbumArtista==null || !nomeAlbumArtista.trim().equalsIgnoreCase(nomeArtista.trim())) continue;
                       boolean branoAppartieneAdAlbum=album.getListaBrani().stream().anyMatch(b -> b!=null && b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(branoCatalogo.getTitolo()));  //Se il brano fa parte dell'album dell'artista, aggiunge i suoi ascolti al totale
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
  aggiornaCronologia();  //Aggiorna la cronologia delle ricerche effettuate e applica il tema scuro
  applicaTemaCronologiaScuro();
}
 public static void aggiornaCronologiaVisibile(){
   Platform.runLater(()->{ if(istanza!=null){  //runLater assicura l'aggiornamento grafico sicuro in background
     istanza.aggiornaCronologia();  //Ricarica la cronologia sull'istanza attiva del controller
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
    String testoDurata=durataMassima.getText().trim();
    String testoAnno=annoPubblicazione.getText().trim();
    Integer durataMassima=null, annoPubblicazione=null;
    try {
      if (!testoDurata.isEmpty()) {
        durataMassima=Integer.parseInt(testoDurata);
        if (durataMassima<0) throw new NumberFormatException();
      }
      if (!testoAnno.isEmpty()) {
        annoPubblicazione=Integer.parseInt(testoAnno);
        if (annoPubblicazione<=0) throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      Alert alert=new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Filtri non validi");
      alert.setHeaderText("Controlla i valori inseriti");
      alert.setContentText("La durata deve essere un numero di secondi non negativo "+ "e l'anno deve essere un numero intero positivo.");
      alert.showAndWait();
      return;
    }
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
      List<Artista>artistiTrovati=new ArrayList<>(); 
      if(testo.isEmpty()){  //Se il testo cercato è vuoto, carica tutto il catalogo
        artistiTrovati.addAll(serviziArtista.findAll(servizioArtista.findAll());
        albumTrovati.addAll(tuttiAlbum);
        for(Album album : tuttiAlbum){  //Estrae tutti i brani da ciascun album e imposta il legame bidirezionale brano-album
          for(Brano brano : album.getBrani()){
            brano.setAlbumBrano(album);
            brani.add(brano);
          }
        }
        brani=rimuoviDuplicati(brani);
      } else{  //Se c'è un testo, esegue la ricerca filtrata
        brani.addAll(ricercaService.cercaBrani(testo)); //Interroga il servivio di ricerca per trovare brani, album e artisti che contengono il testo
        albumTrovati.addAll(ricercaService.cercaAlbum(testo));
        artistiTrovati.addAll(ricercaService.cercaArtista(testo));
        for(Artista artista : artistiTrovati){  //Per ogni artista trovato, include nella ricerca anche i suoi album e tutti i suoi brani
          String nomeCercato=ottieniNomeArtistaRicerca(artista);
          if(nomeCercato==null) continue;
          for(Album album : tuttiAlbum){
            Artista artistaAlbum=album.getArtista();
            if(nomeArtistaAlbum!=null && nome.ArtistaAlbum.equalsIgnoreCase(nomeCercato)){  //Se l'album appartiene all'artista cercato, lo aggiunge ai risultati insieme ai suoi brani
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
            if(brano.getGenereBrano().contains(genereScelto) braniFiltrati.add(brano);
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
          artistiTrovati.removeIf(artista->!genereScelto.equals(artista.getGenerePrincipale()) && !artista.getGenereSecondario().contains(genereScelto));
        }
        GenereMusicale genereSelezionato=filtroGenere.getValue();
        if(genereSelezionato!=null){
          brani.removeIf(brano->brano.getGenereBrano==null || !brano.getGenereBrano().contains(genereSelezionato));
        }
        final Integer durataMassimaFiltro=durataMassima;
        if (durataMassima!=null) brani.removeIf(brano->brano.getDurata()>durataMassimaFiltro);
        final Integer annoPubblicazioneFiltro=annoPubblicazione;
        if (annoPubblicazione!=null) {
          albumTrovati.removeIf(elemento->elemento.getAnnoPubblicazione()!=annoPubblicazioneFiltro);
          brani.removeIf(brano->brano.getAlbumBrano()==null||brano.getAlbumBrano().getAnnoPubblicazione()!=annoPubblicazioneFiltro);
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
        tabellaBrani.getItems().setAll(risultati);  //Mostra i risultati a schermo e aggiorna i contatori delle label della UI
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
            List<String> nomiPlaylist=new ArrayList<>();  //Estrae i soli nomi testuali delle playlist per poterli inserire nel menù del popup
            for (Playlist playlist : playlistDisponibili) {
              nomiPlaylist.add(playlist.getNome());
            }
            ChoiceDialog<String> dialogo=new ChoiceDialog<>(nomiPlaylist.get(0), nomiPlaylist);  //Crea una finestra di dialogo (ChoiceDialog) mostrando l'elenco dei nomi delle playlist
            dialogo.setTitle("Aggiungi alla playlist");
            dialogo.setHeaderText("Brano: " + brano.getTitolo());
            dialogo.setContentText("Scegli la playlist: ");
            Optional<String> scelta=dialogo.showAndWait();  //Mostra il popup a schermo e attende in modo bloccante la scelta dell'utente (l'oggetto Optional<T> è un contenitore che può contenere un valore non nullo oppure può essere vuoto) 
            if (!scelta.isPresent()) return;  //Se l'utente chiude il popup o preme "Annulla", interrompe l'operazione
            for (Playlist playlist : playlistDisponibili) {  //Cerca la playlist corrispondente al nome selezionato per associarle il nuovo brano
              if (playlist.getNome().equals(scelta.get())) {
                servizioPlaylist.aggiungiBrano(playlist, brano);  //Salva la modifica sul file di testo
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
    Stage stage=(Stage) campoRicerca.getScene().getWindow();  //Recupera la finestra attuale (Stage) partendo dal campo di ricerca presente nella UI
    stage.setScene(new Scene(root, 1000, 700));  //Sostituisce la scena corrente impostando la schermata principale con dimensioni 1000x700 pixel
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
    cronologiaListView.setStyle("-fx-control-inner-background: " + sfondo + ";" + "-fx-border-color:" + bordo + ";" + "-fx-text-fill: " + testo + ";");  //Applica i colori di sfondo, bordo e testo alla struttura principale della ListaView
    cronologiaListView.setCellFactory(lista -> new ListCell<String>() {  //Configura lo stile delle singole celle per fare in modo che abbiano i colori corretti
      @Override
      protected void updateItem(String elemento, boolean vuoto) {
        super.updateItem(elemento, vuoto);
        if (vuoto || elemento==null) setText(null);
        else setText(elemento);
        setTextFill(javafx.scene.paint.Color.web(testo));  //Colora il testo e lo sfondo della singola riga a schermo
        setStyle("-fx-background-color: " + sfondo + ";" + "-fx-text-fill: " + testo + ";");
      }
    });
    javafx.application.Platform.runLater(() -> {  //Esegue una pulizia grafica sul thread principale per eliminare gli sfondi grigi ereditati da JavaFX
      cronologiaListView.applyCss();  //Forza il ricalcolo degli stili grafici
      String[] selettori= {".virtual-flow", ".clipped-container", ".sheet", ".viewport"};
      for (String selettore : selettori) {
        for (javafx.scene.Node nodo : cronologiaListView.lookupAll(selettore)) {  //Cerca questi elementi nascosti dentro la lista e ne colora lo sfondo
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
