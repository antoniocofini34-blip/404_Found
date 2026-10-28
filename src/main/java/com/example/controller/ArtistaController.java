package com.example.controller;
import com.example.business.exception.BusinessException; 
import com.example.business.impl.file.FileArtistaServiceImpl;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
import com.example.domain.Componente;
import com.example.domain.GenereMusicale;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;  //Rappresenta un'area di testo multilinea, permettendo di gestire grandi blocchi di testo su più righe (a differenza di TextField che invece permette l'inserimento di una sola riga)
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import java.util.*;
public class ArtistaController {
  @FXML
  private TableView<Artista> tabellaArtisti;
  @FXML
  private TableColumn<Artista, String> colonnaNome;
  @FXML
  private TableColumn<Artista, String> colonnaTipo;
  @FXML
  private TableColumn<Artista, String> colonnaGenere;
  @FXML
  private ComboBox<String> campoTipo;
  @FXML
  private Label etichettaNome;
  @FXML
  private VBox contenitoreNome;
  @FXML
  private TextField campoNome;
  @FXML
  private TextArea campoBiografia;
  @FXML
  private ComboBox<GenereMusicale> campoGenerePrincipale;
  @FXML
  private TextField campoGeneriSecondari;
  @FXML
  private VBox areaComponenti;
  @FXML
  private TableView<Componente> tabellaComponenti;
  @FXML
  private TableColumn<Componente, String> colonnaNomeComponente;
  @FXML
  private TableColumn<Componente, String> colonnaCognomeComponente;
  @FXML
  private TableColumn<Componente, String> colonnaRuoloComponente;
  @FXML
  private Button pulsanteAggiungiComponente;
  @FXML
  private Button pulsanteRimuoviComponente;
  @FXML
  private Label messaggio;
  private FileArtistaServiceImpl artistaService; 
  private Artista artistaSelezionato;
  private List<Componente> componentiTemporanei=new ArrayList<>();
  @FXML
  private void initialize() {
    artistaService=new FileArtistaServiceImpl("src/main/resources/dati/artisti.txt"); 
    campoTipo.setItems(FXCollections.observableArrayList("SOLISTA", "GRUPPO")); 
    campoGenerePrincipale.setItems(FXCollections.observableArrayList(GenereMusicale.values())); //Riempie la ComboBox del genere musicale con tutti i valori dell'enum dei generi musicali
    configuraTabella();
    configuraComponenti();
    campoTipo.valueProperty().addListener((osservabile, precedente, selezionato) -> aggiornaVisibilitaCampi());
    aggiornaVisibilitaCampi();
    caricaArtisti();
    tabellaArtisti.getSelectionModel().selectedItemProperty().addListener((osservabile, precedente, selezionato) -> { //Listener lambda per aggiornare i dettagli dell'artista selezionato in tempo reale
      if (selezionato!=null) mostraDatiArtista(selezionato);
    });
  }
  private void configuraTabella() {
    colonnaNome.setCellValueFactory(cella -> new SimpleStringProperty(ottieniNome(cella.getValue()))); //Estrae il nome dell'artista
    colonnaTipo.setCellValueFactory(cella -> {  //Controlla se l'oggetto è istanza di ArtistaSolista o ArtistaGruppo
      String tipo=cella.getValue() instanceof ArtistaSolista ? "SOLISTA" : "GRUPPO";
      return new SimpleStringProperty(tipo);
    });
    colonnaGenere.setCellValueFactory(cella -> {  //Mostra il genere musicale principale, inserendo un testo vuoto se non è specificato
      GenereMusicale genere=cella.getValue().getGenerePrincipale();
      return new SimpleStringProperty(genere==null ? "" : genere.toString());
    });
  }
  private void configuraComponenti() {
    colonnaNomeComponente.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getNome()));
    colonnaCognomeComponente.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getCognome()));
    colonnaRuoloComponente.setCellValueFactory(cella -> new SimpleStringProperty(cella.getValue().getRuolo()));
  }
private void aggiornaVisibilitaCampi() {
  String tipo=campoTipo.getValue();
  boolean solista="SOLISTA".equals(tipo);
  boolean gruppo="GRUPPO".equals(tipo);
  contenitoreNome.setVisible(solista || gruppo);
  contenitoreNome.setManaged(solista || gruppo);
  if (solista) {
    etichettaNome.setText("Nome d'arte");
    campoNome.setPromptText("Inserisci il nome d'arte");
  } else if (gruppo) {
    etichettaNome.setText("Nome gruppo");
    campoNome.setPromptText("Inserisci il nome del gruppo");
  } else campoNome.clear();
  areaComponenti.setVisible(gruppo);
  areaComponenti.setManaged(gruppo);
}  
  private void caricaArtisti() {
    try { List<Artista> artisti=artistaService.findAllArtisti();  //Recupera la lista di tutti gli artisti
         tabellaArtisti.setItems(FXCollections.observableArrayList(artisti));  //Riempie la tabella convertendo i dati in una lista osservabile
         messaggio.setText("Artisti caricati:" + artisti.size());
        } catch(BusinessException e) { mostraErrore("Errore durante il caricamento:" + e.getMessage());
              }
        }
  private void mostraDatiArtista(Artista artista) {
    artistaSelezionato=artista;
    campoNome.setText(ottieniNome(artista));
    campoNome.setEditable(false);  //Dopo aver inserito il nome nel campo di testo, lo si blocca per impedire modifiche accidentali
    campoBiografia.setText(artista.getBiografia()==null ? "" : artista.getBiografia()); //Mostra la biografia gestendo il valore nullo con una stringa vuota
    campoGenerePrincipale.setValue(artista.getGenerePrincipale()); //Imposta il genere principale nel menù a tendina(ComboBox)
    campoTipo.setValue(artista instanceof ArtistaSolista ? "SOLISTA" : "GRUPPO");
    Set<GenereMusicale> generi=artista.getGeneriSecondari();  //Recupera i generi secondari e li concatena in una stringa separata da virgole
    StringBuilder testoGeneri=new StringBuilder();
    for (GenereMusicale genere : generi) {
      if (testoGeneri.length() > 0) testoGeneri.append(",");
      testoGeneri.append(genere.name());
    }
    campoGeneriSecondari.setText(testoGeneri.toString());
    componentiTemporanei.clear();
    if (artista instanceof ArtistaGruppo) {
      ArtistaGruppo gruppo=(ArtistaGruppo) artista;
      componentiTemporanei.addAll(gruppo.getListaMembri());
      aggiornaListaComponenti();
    } else tabellaComponenti.getItems().clear();
    messaggio.setText("Artista selezionato: " + ottieniNome(artista));
  }
  @FXML
  private void creaArtista() {
    try { Artista artista=costruisciArtista();
         artistaService.creaArtista(artista);  //Invia l'oggetto al livello di business per validarlo e salvarlo nel database
         caricaArtisti();  //Aggiorna l'elenco visibile nella tabella
         pulisciCampi();  //Reset del form di inserimento
         mostraMessaggio("Artista creato e salvato correttamente:");  //Feedback di successo
        } catch(IllegalArgumentException | BusinessException e) { mostraErrore(e.getMessage());
              }
        }
  @FXML
  private void modificaArtista() {
    if (artistaSelezionato==null) {
      mostraErrore("Seleziona un artista dalla tabella.");
      return;
    }
    try { Artista aggiornato=costruisciArtista();
         artistaService.modificaArtista(aggiornato);
         caricaArtisti();
         pulisciCampi();
         mostraMessaggio("Artista modificato e salvato correttamente.");
        } catch(IllegalArgumentException | BusinessException e) { mostraErrore(e.getMessage());
              }
        }
  @FXML
  private void eliminaArtista() {
    if (artistaSelezionato==null) {
      mostraErrore("Seleziona un artista dalla tabella.");
      return;
    }
    Alert conferma=new Alert(Alert.AlertType.CONFIRMATION);
    conferma.setTitle("Conferma eliminazione");
    conferma.setHeaderText("Eliminare l'artista?");
    conferma.setContentText("Vuoi eliminare " + ottieniNome(artistaSelezionato) + "?");
    Optional<ButtonType> risposta=conferma.showAndWait();
    if (risposta.isPresent() && risposta.get()==ButtonType.OK) {
      try { artistaService.eliminaArtista(artistaSelezionato);
           caricaArtisti();
           pulisciCampi();
           mostraMessaggio("Artista eliminato correttamente.");
          } catch(BusinessException e) { mostraErrore(e.getMessage());
                }
          }
      }
  @FXML
  private void aggiungiComponente() {
    if(!"GRUPPO".equals(campoTipo.getValue())) {
      mostraErrore("Seleziona la tipologia GRUPPO prima di aggiungere componenti.");
      return;
    }
    Dialog<Componente> dialog=new Dialog<>();
    dialog.setTitle("Aggiungi componente");
    dialog.setHeaderText("Inserisci i dati del componente");
    ButtonType pulsanteConferma=new ButtonType("Aggiungi", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(pulsanteConferma, ButtonType.CANCEL);
    TextField campoNomeComponente=new TextField();
    TextField campoCognomeComponente=new TextField();
    TextField campoRuoloComponente=new TextField();
    campoNomeComponente.setPromptText("Nome");
    campoCognomeComponente.setPromptText("Cognome");
    campoRuoloComponente.setPromptText("Ruolo");
    GridPane griglia=new GridPane();
    griglia.setHgap(10);
    griglia.setVgap(10);
    griglia.setPadding(new Insets(15));
    griglia.add(new Label("Nome"), 0, 0);
    griglia.add(campoNomeComponente, 1, 0);
    griglia.add(new Label("Cognome"), 0, 1);
    griglia.add(campoCognomeComponente, 1, 1);
    griglia.add(new Label("Ruolo"), 0, 2);
    griglia.add(campoNomeComponente, 1, 2);
    dialog.getDialogPane().setContent(griglia);
    Button bottoneAggiungi=(Button) dialog.getDialogPane().lookupButton(pulsanteConferma);
    bottoneAggiungi.addEventFilter(javafx.event.ActionEvent.ACTION, evento -> {
      if (campoNomeComponente.getText().trim().isEmpty() || campoCognomeComponente.getText().trim().isEmpty() || campoRuoloComponente.getText().trim().isEmpty()) {
        mostraErrore("Inserisci nome, cognome e ruolo del componente.");
        evento.consume();
      }
    });
    dialog.setResultConverter(pulsante -> {
      if (pulsante==pulsanteConferma) return new Componente(campoNomeComponente.getText().trim(), campoCognomeComponente.getText().trim(), campoRuoloComponente.getText().trim());
      return null;
    });
    Optional<Componente> risultato=dialog.showAndWait();
    if (!risultato.isPresent()) return;
    Componente componente=risultato.get();
    try { if (artistaSelezionato instanceof ArtistaGruppo) {
            ArtistaGruppo gruppo=(ArtistaGruppo) artistaService.findArtistaByNome(ottieniNome(artistaSelezionato));
            artistaService.aggiungiComponente(gruppo, componente);
            aggiornaArtistaSelezionato(gruppo.getNomeGruppo());
    }  else {
         for (Componente esistente : componentiTemporanei) {
           if (esistente.getNome().equalsIgnoreCase(componente.getNome()) && esistente.getCognome().equalsIgnoreCase(componente.getCognome())) throw new IllegalArgumentException("Il componente è già presente nella band.");
             }
      componentiTemporanei.add(componente);
      aggiornaListaComponenti();
        }
      } catch (BusinessException | IllegalArgumentException e) { mostraErrore(e.getMessage()); }
  }
  @FXML
  private void rimuoviComponente() {
    if (artistaSelezionato==null) {
      mostraErrore("Impossibile rimuovere componenti durante la creazione della band.");
      return;
    }
    if (!(artistaSelezionato instanceof ArtistaGruppo)) { 
      mostraErrore("Seleziona una band della tabella.");
      return;
    }
    Componente componente=tabellaComponenti.getSelectionModel().getSelectedItem();
    if (componente==null) {
      mostraErrore("Seleziona il componente da rimuovere.");
      return;
    }
    try {  ArtistaGruppo gruppo=(ArtistaGruppo) artistaService.findArtistaByNome(ottieniNome(artistaSelezionato));
           artistaService.rimuoviComponente(gruppo, componente);
           aggiornaArtistaSelezionato(gruppo.getNomeGruppo());
           mostraMessaggio("Componente rimosso correttamente.");
        } catch(BusinessException e) { mostraErrore(e.getMessage()); }
  }
  @FXML
  private void aggiornaArtistaSelezionato(String nomeGruppo) throws BusinessException {
    Artista aggiornato=artistaService.findArtistaByNome(nomeGruppo);
    caricaArtisti();
    artistaSelezionato=aggiornato;
    tabellaArtisti.getSelectionModel().select(tabellaArtisti.getItems().stream().filter(a -> ottieniNome(a).equalsIgnoreCase(nomeGruppo)).findFirst().orElse(null));
    mostraDatiArtista(aggiornato);
  }
  private void aggiornaListaComponenti() { tabellaComponenti.setItems(FXCollections.observableArrayList(componentiTemporanei)); }                                                              
  private Artista costruisciArtista() {
    String nome=campoNome.getText()==null ? "" : campoNome.getText().trim();
    String biografia=campoBiografia.getText()==null ? "" : campoBiografia.getText().trim();
    String tipo=campoTipo.getValue();
    GenereMusicale genere=campoGenerePrincipale.getValue();
    if (nome.isEmpty()) throw new IllegalArgumentException("Inserire il nome dell'artista.");
    if (tipo==null) throw new IllegalArgumentException("Selezionare la tipologia dell'artista.");
    if (genere==null) throw new IllegalArgumentException("Selezionare il genere principale.");
    Set<GenereMusicale> generiSecondari=leggiGeneriSecondari(campoGeneriSecondari.getText());
    if (generiSecondari.contains(genere)) throw new IllegalArgumentException("Errore: non si può ripetere il genere principale tra i generi secondati.");
    List<Album> discografia=artistaSelezionato==null ? new ArrayList<>() : artistaSelezionato.getDiscografia();
    if (tipo.equals("SOLISTA")) return new ArtistaSolista(biografia, genere, generiSecondari, discografia, nome);
    return new ArtistaGruppo(biografia, genere, generiSecondari, artistaSelezionato==null ? new ArrayList<>() : artistaSelezionato.getDiscografia(), nome, componentiTemporanei);
  }
  private Set<GenereMusicale> leggiGeneriSecondari(String testo) {
    Set<GenereMusicale> generi=new HashSet<>();
    if (testo==null || testo.trim().isEmpty()) return generi;
    String[] valori=testo.split(",");
    for (String valore : valori) {
      String nomeGenere=valore.trim();
      if (nomeGenere.isEmpty()) continue;
      try { generi.add(GenereMusicale.valueOf(nomeGenere.toUpperCase()));
          } catch(IllegalArgumentException e) { throw new IllegalArgumentException("Genere secondario non valido:" + nomeGenere + ". Usa i nomi dell'elenco dei generi.");
                }
            }
          return generi;
      }
  private String ottieniNome(Artista artista) {
    if (artista instanceof ArtistaSolista) return ((ArtistaSolista) artista).getNomeArte();
    if (artista instanceof ArtistaGruppo) return ((ArtistaGruppo) artista).getNomeGruppo();
    return "";
  }
  @FXML
  private void pulisciCampi() {
    artistaSelezionato=null;
    componentiTemporanei.clear();
    tabellaArtisti.getSelectionModel().clearSelection();  //Deseleziona la riga nella tabella
    campoNome.clear();
    campoNome.setEditable(true);  //Riabilita la modifica del nome per i nuovi inserimenti
    campoBiografia.clear();
    campoTipo.setValue(null);  //Resetta i menù a tendina
    campoGenerePrincipale.setValue(null);
    campoGeneriSecondari.clear();
    tabellaComponenti.getItems().clear();
    aggiornaVisibilitaCampi();
    messaggio.setText("Inserisci campi del nuovo artista.");
  }
  private void mostraMessaggio(String testo) {
    messaggio.setText(testo);
    Alert alert=new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("UnivaqSound");  //Imposta il titolo della finestra
    alert.setHeaderText(null);  //Rimuove l'intestazione interna per rendere il popup più pulito
    alert.setContentText(testo);  //Imposta il corpo del messaggio nel popup
    alert.showAndWait();
  }
  private void mostraErrore(String testo) {
    messaggio.setText("Errore:" + testo);
    Alert alert=new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Errore");
    alert.setHeaderText("Operazione non completata");
    alert.setContentText(testo);
    alert.showAndWait();
  }
}
