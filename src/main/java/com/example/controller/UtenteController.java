package com.example.controller;
import java.time.LocalDate;
import java.util.*;
import com.example.business.impl.file.FileUtenteServiceImpl;
import com.example.business.exception.BusinessException;
import com.example.domain.GenereMusicale;
import com.example.domain.Utente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
public class UtenteController {
  @FXML
  private BorderPane rootUtente;
  @FXML
  private ComboBox<Utente> utenteComboBox;
  @FXML
  private TextField nomeField;
  @FXML
  private TextField cognomeField;
  @FXML
  private TextField emailField;
  @FXML
  private DatePicker dataNascitaPicker;
  @FXML
  private FlowPane preferenzeBox;
  @FXML
  private Label messaggioLabel;
  private final FileUtenteServiceImpl utenteService=new FileUtenteServiceImpl("src/main/resources/dati/utenti.txt");
  private final List<CheckBox> genereCheckBoxes=new ArrayList<>();
  private Utente utenteSelezionato;
  public void aggiornaTemaUtente() {
    if (rootUtente==null) return;
    boolean scuro=TemaVisivo.isScuro();
    String sfondo=scuro? "#151515" : "#ffffff";
    String testo=scuro? "#ffffff" : "#111111";
    String secondario=scuro? "#bbbbbb" : "#111111";
    String sfondoCampo=scuro? "#242424" : "#ffffff";
    String bordoCampo=scuro? "#454545" : "#cccccc";
    rootUtente.setStyle("-fx-background-color: " +sfondo+";"); //verifichiamo che il componente centrale del BorderPane è un'istanza di VBox
    if (rootUtente.getCenter() instanceof VBox) { VBox centro=(VBox) rootUtente.getCenter(); //applichiamo il colore di sfondo dinamico al VBox centrale tramite CSS
      centro.setStyle("-fx-background-color: "+sfondo+";");
    }
    if (rootUtente.getTop()!=null) rootUtente.getTop().setStyle("-fx-background-color: "+sfondo+";"); //applichiamo lo stesso colore di sfondo dinamico al componente superiore se quest'ultimo non è nullo //aggiorniamo le etichette della schermata
    aggiornaColoriEtichette(rootUtente,testo,secondario); //aggiorniamo i campi anagrafici
    applicaStileCampo(nomeField,testo,secondario,sfondoCampo,bordoCampo);
    applicaStileCampo(cognomeField,testo,secondario,sfondoCampo,bordoCampo);
    applicaStileCampo(emailField,testo,secondario,sfondoCampo,bordoCampo); //successivamente aggiorniamo il DatePicker senza perdere il suo valore
    if (dataNascitaPicker!=null) dataNascitaPicker.setStyle("-fx-background-color: "+sfondoCampo+";" + "-fx-control-inner-background: "+sfondoCampo+";" + "-fx-text-fill: "+testo+";" + "-fx-border-color: "+bordoCampo+";"); //dopo andiamo ad aggiornare i testi delle caselle dei generi
    for (CheckBox checkBox:genereCheckBoxes) checkBox.setStyle("-fx-text-fill: "+testo+";"); //ripristiniamo colore e grassetto del messaggio di stato
    if (messaggioLabel!=null) { String messaggio=messaggioLabel.getText();
      if(messaggio!=null&&!messaggio.isEmpty()) { boolean errore=messaggio.contains("Errore")||messaggio.contains("non valida")||messaggio.contains("Dati mancanti")||messaggio.contains("Nessun utente");
        String colore=errore ?"#ff8585":(TemaVisivo.isScuro() ?"#ffffff":"#111111");
        messaggioLabel.setTextFill(Color.web(colore));
        messaggioLabel.setStyle("-fx-text-fill: " + colore + ";" + "-fx-font-weight:bold;");
      }
    }
  }
  private void aggiornaColoriEtichette(Parent parent, String testo, String secondario) { //iteriamo su tutti i nodi figli del parent in modalità di sola lettura per sicurezza
    for (Node nodo:parent.getChildrenUnmodifiable()) {
      if (nodo instanceof Label) { Label label=(Label) nodo;
        String contenuto=label.getText();
        if (contenuto==null) continue;
        if (contenuto.equals("Gestisci i tuoi dati e le preferenze musicali")||contenuto.equals("Seleziona i generi che preferisci")) {
          label.setTextFill(Color.web(secondario));
          label.setStyle("-fx-font-size:18px;"+"-fx-text-fill: "+secondario+";" + "-fx-font-weight:normal;");
        } else if (contenuto.equals("PROFILO UTENTE")||contenuto.equals("DATI ANAGRAFICI")||contenuto.equals("PREFERENZE MUSICALI")) {
          label.setTextFill(Color.web(testo));
          label.setStyle("-fx-font-size:24px;"+"-fx-font-weight:bold;"+"-fx-text-fill: "+testo+";");
        } else if (contenuto.equals("Nome")||contenuto.equals("Cognome")||contenuto.equals("Email")||contenuto.equals("Data di nascita")||contenuto.equals("Utente salvato:")) {
          label.setTextFill(Color.web(testo));
          label.setStyle("-fx-text-fill: " + testo + ";" + (contenuto.equals("Utente salvato: ")? "-fx-font-weight:bold;":""));
        }
      }
      if (nodo instanceof Parent) aggiornaColoriEtichette((Parent) nodo,testo,secondario);
    }
  }
  private void applicaStileCampo(TextField campo, String testo, String secondario, String sfondo, String bordo) {
    if (campo==null) return;
    campo.setStyle("-fx-control-inner-background: "+sfondo+";" + "-fx-background-color: "+sfondo+";" + "-fx-text-fill: "+testo+";" + "-fx-prompt-text-fill: "+secondario+";" + "-fx-border-color: "+bordo+";");
  }
  @FXML
  private void initialize() { aggiornaTemaUtente();
    creaCheckboxGeneri();
    aggiornaElencoUtenti();
    Utente attivo=UtenteAttivo.getUtente();
    if (attivo!=null) selezionaUtenteNellElenco(attivo.getEmail());
    else nuovoUtente();
    aggiornaTemaUtente();
  }
  private void creaCheckboxGeneri() {  preferenzeBox.getChildren().clear(); //dopo svuotiamo i contenitori precedenti per poi andare ad applicare lo stile visivo corrente del testo//qui andiamo a creare le CheckBox per ogni genere musicale disponibile
    genereCheckBoxes.clear();
    String testo=TemaVisivo.getColoreTesto(); //recuperiamo il colore dle testo corrente dal tema visivo impostato
    for (GenereMusicale genere:GenereMusicale.values()) {
      CheckBox checkBox=new CheckBox(genere.name());//creiamo una nuova CheckBox con il nome del genere come etichetta
      checkBox.setStyle("-fx-text-fill: "+testo+";");//per poi applicare il colore del testo tramite CSS di JavaFX
      genereCheckBoxes.add(checkBox); 
      preferenzeBox.getChildren().add(checkBox); //infine aggiungiamo la CheckBox sia alla lista così da poter recuperare il suo stato in seguito, sia al contenitore grafico così da mostrarla a schermo 
    }
  }
  @FXML
  private void aggiornaElencoUtenti() {
    try{ List<Utente> utenti=utenteService.findAllUtenti();
      utenteComboBox.setItems(FXCollections.observableArrayList(utenti));
      utenteComboBox.setConverter(new StringConverter<>() {
        @Override
        public String toString(Utente utente) {
          if (utente==null) return "";
          return utente.getNome()+" "+utente.getCognome()+" (" +utente.getEmail()+")";
        }
        @Override
        public Utente fromString(String string) {return null;}
      });
      mostraMessaggio("Elenco utenti aggiornato");
    } catch (BusinessException e) {mostraErrore("Impossibile caricare gli utenti",e.getMessage()); }
  }
  @FXML
  private void selezionaUtente() { Utente selezionato=utenteComboBox.getValue();
    if(selezionato==null) return;
    try{ utenteSelezionato=utenteService.findUtenteByEmail(selezionato.getEmail());
      Utente utentePrecedente=UtenteAttivo.getUtente();
      boolean cambioUtente=utentePrecedente!=null && !utentePrecedente.getEmail().equalsIgnoreCase(utenteSelezionato.getEmail());
      if (cambioUtente) PlayerStato.fermaRiproduzionePerCambioUtente();
      UtenteAttivo.setUtente(utenteSelezionato);      
      nomeField.setText(utenteSelezionato.getNome());
      cognomeField.setText(utenteSelezionato.getCognome());
      emailField.setText(utenteSelezionato.getEmail());
      dataNascitaPicker.setValue(utenteSelezionato.getDataDiNascita());
      aggiornaCheckBoxPreferenze();      
      mostraMessaggio("Utente caricato");
    } catch(BusinessException e) {mostraErrore("Impossibile selezionare l'utente", e.getMessage());}
  }
  @FXML
  private void nuovoUtente() { utenteSelezionato=null;
    utenteComboBox.getSelectionModel().clearSelection();
    nomeField.clear();
    cognomeField.clear();
    emailField.clear();
    dataNascitaPicker.setValue(null);
    for (CheckBox checkBox:genereCheckBoxes) checkBox.setSelected(false);
    mostraMessaggio("Inserisci i dati del nuovo utente");
  }
  @FXML
  private void creaUtente() {
    try {
      if (!datiAnagraficiValidi()) return;
      Utente nuovo=new Utente(nomeField.getText().trim(), cognomeField.getText().trim(), emailField.getText().trim(), dataNascitaPicker.getValue(), null, generaPreferenzeSelezionate(), null);
      utenteService.creaUtente(nuovo);
      aggiornaElencoUtenti();
      selezionaUtenteNellElenco(nuovo.getEmail());
      mostraMessaggio("Utente creato e salvato correttamente");
    } catch (BusinessException e) {mostraErrore("Errore durante la creazione",e.getMessage());}
  }
  @FXML
  private void modificaUtente() {
    if (utenteSelezionato==null) { mostraErrore("Nessun utente selezionato", "Seleziona un utente esistente prima di salvare le modifiche");
      return;
    }
    try{
      if (!datiAnagraficiValidi()) return;
      String emailOriginale=utenteSelezionato.getEmail();
      String nuovaEmail=emailField.getText().trim();
      if (!emailOriginale.equalsIgnoreCase(nuovaEmail)) { mostraErrore("Modifica email non supportata", "Il servizio attuale identifica l'utente tramite email. "+"Per evitare di perdere l'utente, non cambiare l'email "+"da questa schermata");
        emailField.setText(emailOriginale);
        return;
      }
      utenteSelezionato.setNome(nomeField.getText().trim());
      utenteSelezionato.setCognome(cognomeField.getText().trim());
      utenteSelezionato.setDataDiNascita(dataNascitaPicker.getValue());
      utenteService.modificaUtente(utenteSelezionato);
      aggiornaElencoUtenti();
      selezionaUtenteNellElenco(emailOriginale);
      mostraMessaggio("Modifiche salvate coorrettamente");
    } catch (BusinessException e) {mostraErrore("Nessun utente selezionato", "Seleziona prima l'utente da eliminare");}
  }
  @FXML
  private void eliminaUtente() {
    if (utenteSelezionato==null) { mostraErrore("Nessun utente selezionato","Seleziona prima l'utente da eliminare");
      return;
    }
    Alert conferma=new Alert(Alert.AlertType.CONFIRMATION, "Vuoi eliminare l'utente "+utenteSelezionato.getEmail()+"?",ButtonType.YES, ButtonType.NO);
    conferma.setTitle("Conferma eliminazione");
    conferma.setHeaderText("Eliminazione utente");
    if (conferma.showAndWait().orElse(ButtonType.NO)!=ButtonType.YES) return;
    try { utenteService.eliminaUtente(utenteSelezionato);
      nuovoUtente();
      aggiornaElencoUtenti();
      mostraMessaggio("Utente eliminato");
    } catch (BusinessException e) {mostraErrore("Errore durante l'eliminazione", e.getMessage());}
  }
  @FXML
  private void salvaPreferenze() {
    if (utenteSelezionato==null) { mostraErrore("Nessun utente selezionato", "Crea o seleziona un utente prima di salvare le preferenze");
      return;
    }
    try{ Set<GenereMusicale> preferenze=generaPreferenzeSelezionate();
      utenteSelezionato.setPreferenzeMusicali(preferenze);
      utenteService.modificaUtente(utenteSelezionato);
      mostraMessaggio("Preferenze musicali salvate");
    } catch(BusinessException e) {mostraErrore("Errore durante il salvataggio",e.getMessage());}
  }
  private void aggiornaCheckBoxPreferenze() { Set<GenereMusicale> preferenze=utenteSelezionato.getPreferenzeMusicali();
    for (CheckBox checkBox:genereCheckBoxes) checkBox.setSelected(preferenze.contains(GenereMusicale.valueOf(checkBox.getText())));
  }
  private Set<GenereMusicale> generaPreferenzeSelezionate() { Set<GenereMusicale> preferenze=new HashSet<>();
    for (CheckBox checkBox:genereCheckBoxes) {
      if (checkBox.isSelected()) preferenze.add(GenereMusicale.valueOf(checkBox.getText()));
    }
    return preferenze;
  }
  private boolean datiAnagraficiValidi() {
    if (nomeField.getText()==null||nomeField.getText().trim().isEmpty()||cognomeField.getText()==null||emailField.getText()==null||emailField.getText().trim().isEmpty()||dataNascitaPicker.getValue()==null) { mostraErrore("Dati mancanti", "Compila nome,cognome,email e data di nascita");
      return false;
    }
    if (!emailField.getText().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) { mostraErrore("Email non valida", "Inserisci un indirizzo email valido");
      return false;
    }
    if (dataNascitaPicker.getValue().isAfter(LocalDate.now())) { mostraErrore("Data di nascita non valida", "La data di nascita non può essere nel futuro");
      return false;
    }
    return true;
  }
  private void selezionaUtenteNellElenco(String email) {
    for (Utente utente:utenteComboBox.getItems()) {
      if (utente.getEmail()!=null&&utente.getEmail().equalsIgnoreCase(email)) { utenteComboBox.getSelectionModel().select(utente);
        selezionaUtente();
        return;
      }
    }
  }
  private void mostraMessaggio(String messaggio) { messaggioLabel.setText(messaggio);
    aggiornaStileMessaggio();
  }
  private void aggiornaStileMessaggio() {
    if (messaggioLabel==null) return;
    String colore=TemaVisivo.isScuro()?"#ffffff":"#111111";
    messaggioLabel.setTextFill(Color.web(colore));
    messaggioLabel.setStyle("-fx-text-fill: " + colore + ";" + "-fx-font-weight:bold;");
  }
  private void mostraErrore(String titolo, String messaggio) { String testoErrore=titolo+": "+messaggio;
    messaggioLabel.setText(testoErrore);
    messaggioLabel.setTextFill(Color.web("#ff8585"));
    messaggioLabel.setStyle("-fx-text-fill: #ff8585;"+"-fx-font-weight:bold;");
    Alert alert=new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titolo);
    alert.setHeaderText(titolo);
    alert.setContentText(messaggio);
    alert.showAndWait();
  }
}
