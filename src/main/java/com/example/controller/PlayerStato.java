package com.example.controller;
import com.example.business.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.*;
import com.example.business.impl.ram.RAMCodaRiproduzioneServiceImpl;
import com.example.domain.Brano;
import com.example.domain.CodaRiproduzione;
import com.example.domain.ModalitaRepeat;
import com.example.domain.StatoRiproduzione;
import com.example.domain.Utente;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.*;
public class PlayerStato {
  private static final RAMCodaRiproduzioneServiceImpl codaService=new RAMCodaRiproduzioneServiceImpl();
  private static final CodaRiproduzione coda=new CodaRiproduzione(new ArrayList<>(), false, ModalitaRepeat.NESSUNA, StatoRiproduzione.FERMO);
  private static Brano branoCorrente;
  private static double progressoCorrente=0;
  private static Timeline timerRiproduzione;
  private static String emailUtenteSessione;
  private static String titoloSessione;
  private static LocalDateTime inizioSessione;
  private static int secondiSessione;
  private static boolean cronologiaAbilitata=false;
  private static String titoloBranoConteggiato;
  private static int secondiAscoltoCorrente=0;
  private static boolean ascoltoRegistrato=false;
  private static final String FILE_TEMPI= "src/main/resources/dati/tempi_ascolto.properties";
  private static final Properties tempiAscolto=new Properties();
  static {  //Inizializza le variabili contrassegnate come static, eseguendo questo blocco una sola volta all'avvio della classe per caricare i dati in memoria 
    try {  Path percorso=Paths.get(FILE_TEMPI);  //Verifica se il file delle impostazioni dei temi esiste fisicamente nel computer
           if (Files.exists(percorso)) {
             try (InputStream input=Files.newInputStream(percorso)) {  //Il try interno (try with resources) gestisce l'apertura e chiusura automatica del canale di lettura (Stream)
               tempiAscolto.load(input);  //Carica in memoria i dati salvati nel file .properties
             }
           }
        } catch(IOException e) { e.getMessage(); }  //Intercetta eventuali errori di input/output sul file system
  }
  public static RAMCodaRiproduzioneServiceImpl getCodaService() { return codaService; }
  public static CodaRiproduzione getCoda() { return coda; }
  public static Brano getBranoCorrente() { return branoCorrente; }
  public static void setBranoCorrente(Brano brano) {
    String nuovoTitolo=brano==null ? null : brano.getTitolo();
    boolean branoDiverso=titoloBranoConteggiato==null ? nuovoTitolo!=null : nuovoTitolo==null || !titoloBranoConteggiato.equalsIgnoreCase(nuovoTitolo);  //Restituisce true se il nuovo brano è diverso da quello ascoltato precedentemente
    if (branoDiverso) {  //Se l'utente ha cambiato canzone, si salvano i vecchi dati e si resettano i contatori
      salvaSessioneCronologia();  //Registra l'ascolto appena terminato
      azzeraConteggioAscolto();  //Resetta a zero il timer della nuova canzone
      titoloBranoConteggiato=nuovoTitolo;  
    }
    branoCorrente=brano; //Imposta il brano passato come traccia corrente 
  }
  public static double getProgressoCorrente() { return progressoCorrente; }
  public static void setProgressoCorrente(double progresso) { progressoCorrente=progresso; }
  private static void azzeraConteggioAscolto() {
    secondiAscoltoCorrente=0;
    ascoltoRegistrato=false;
  }
  private static void registraAscolto(Brano brano) { 
    if (brano==null || brano.getTitolo()==null) return;
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) return;
    String chiave=creaChiave(utente.getEmail(), brano.getTitolo(), "ascolti");  //Crea la chiave combinata univoca per salvare i dati
    int ascolti=Integer.parseInt(tempiAscolto.getProperty(chiave, "0"));  //Legge il numero di ascolti precedenti dal file delle impostazioni e, se non c'è nulla, parte da 0
    tempiAscolto.setProperty(chiave, String.valueOf(ascolti + 1));  //Aumenta di 1 il numero di ascolti e ggiorna il valore in memoria
    salvaTempiAscolto();
    ascoltoRegistrato=true;
    System.out.println("Ascolto personale registrato per " + utente.getEmail() + ": " + brano.getTitolo());
  }
  public static void avviaTimerRiproduzione() {
    if (timerRiproduzione!=null) {  //Se c'è un timer attivo in background, lo ferma e lo cancella per evitare conflitti
      timerRiproduzione.stop();
      timerRiproduzione=null;
    }
    if (inizioSessione==null) iniziaSessioneCronologia();
    Timeline nuovoTimer=new Timeline(new KeyFrame(Duration.seconds(1), e -> {  //Crea un nuovo timer che esegue questo blocco di codice ogni secondo
      if (coda.getStato()!=StatoRiproduzione.IN_RIPRODUZIONE || branoCorrente==null) {  //Se la musica viene messa in pausa o il brano sparisce, spegne il timer
        if (timerRiproduzione!=null) {
          timerRiproduzione.stop();
          timerRiproduzione=null;
        }
        return;
      }
      secondiSessione++;
      if (!ascoltoRegistrato) {  //Se l'ascolto non è stato ancora registrato, aumenta i secondi di ascolto continui e, raggiunti i 30 secondi, scatta il salvataggio automatico dell'ascolto personale
        secondiAscoltoCorrente++;
        if (secondiAscoltoCorrente >= 30) registraAscolto(branoCorrente);
      }
      registraSecondoAscoltato(branoCorrente);  //Salva i secondi totali nel file delle impostazioni
      int durata=branoCorrente.getDurata();  //Fino a quando la canzone non è finita, sposta in avanti di 1 secondo la barra del tempo
      if (progressoCorrente < durata) progressoCorrente++;
      if (progressoCorrente >= durata) {
        progressoCorrente=durata;
        if (timerRiproduzione!=null) {  //Ferma il timer corrente poiché la canzone è giunta alla fine
          timerRiproduzione.stop();
          timerRiproduzione=null;
        }
        try { salvaSessioneCronologia();  
              codaService.branoTerminato(coda);  //Chiede al servizio della coda di passare al brano successivo
              Brano nuovoBrano=codaService.getBranoCorrente(coda);
              if (nuovoBrano!=null && coda.getStato()==StatoRiproduzione.IN_RIPRODUZIONE) {  //Se c'è un'altra canzone pronta in coda e siamo ancora in riproduzione, la fa partire subito
                branoCorrente=nuovoBrano;
                progressoCorrente=0;
                iniziaNuovoAscolto();
                avviaTimerRiproduzione();
              } else {
                branoCorrente=null;
                progressoCorrente=0;
              }
            } catch(Exception ex) { ex.getMessage(); }
      }
    }));
    timerRiproduzione=nuovoTimer;  //Salva il timer appena creato nella variabile globale, lo imposta all'infinito e lo fa partire
    nuovoTimer.setCycleCount(Timeline.INDEFINITE);
    nuovoTimer.play();
  }
  public static void fermaTimerRiproduzione() {
    if (timerRiproduzione!=null) {
      timerRiproduzione.stop();
      timerRiproduzione=null;
    }
  }
  public static void annullaSessioneCronologia() {
    emailUtenteSessione=null;
    titoloSessione=null;
    inizioSessione=null;
    secondiSessione=0;
    cronologiaAbilitata=false;
  }
  public static void terminaSessioneCronologiaConStop() {
    if (cronologiaAbilitata) salvaSessioneCronologia();
    else annullaSessioneCronologia();
    cronologiaAbilitata=false;
  }
  public static boolean isTimerRiproduzioneAttivo() { return timerRiproduzione!=null && timerRiproduzione.getStatus()==javafx.animation.Animation.Status.RUNNING; }
  private static void registraSecondoAscoltato(Brano brano) {
    if (brano==null || brano.getTitolo()==null) return;
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) return;
    String email=utente.getEmail();
    String titolo=brano.getTitolo();
    String chiave=creaChiave(email, titolo, "secondi");  //Crea una chiave unica per salvare i secondi di questa specifica canzone
    int secondi=Integer.parseInt(tempiAscolto.getProperty(chiave, "0"));  
    tempiAscolto.setProperty(chiave, String.valueOf(secondi + 1));  //Incrementa il contatore di 1 secondo e aggiorna il valore in memoria 
    salvaTempiAscolto();
  }
  private static String creaChiave(String email, String titolo, String tipo) {
    String emailNormalizzata=email.trim().toLowerCase();
    String titoloNormalizzato=titolo.trim().toLowerCase();
    return emailNormalizzata + "|" + titoloNormalizzato + "|" + tipo;
  }
  public static int getTempoAscoltoUtente(String email, String titolo) {  //Ritorna il tempo totale che un utente ha speso ascoltando una determinata canzone
    if (email==null || titolo==null) return 0;
    return Integer.parseInt(tempiAscolto.getProperty(creaChiave(email, titolo,"secondi"), "0"));  //Cerca i dati in memoria usando la chiave dei secondi e li converte in numero intero
  }
  public static int getNumeroAscoltiUtente(String email, String titolo) {  //Ritorna il numero di riproduzioni totali (quante volte il brano è stato ascoltato per almeno 30 secondi)
    if (email==null || titolo==null) return 0;
    return Integer.parseInt(tempiAscolto.getProperty(creaChiave(email, titolo,"ascolti"), "0"));  
  }
  private static void salvaTempiAscolto() {  //Salva fisicamente e permanentemente le modifiche nel file sul computer
    try{
      Path percorso=Paths.get(FILE_TEMPI);
      Path cartella=percorso.getParent();
      if (cartella!=null) Files.createDirectories(cartella);
      try (OutputStream output=Files.newOutputStream(percorso)) { tempiAscolto.store(output, "Statistiche personali degli utenti");  //Apre il file per la scrittura e lo chiude in automatico alla fine (try with resources) e scrive le informazioni all'interno del file .properties inserendo un'intestazione di commento iniziale
      } 
    } catch (IOException e) { e.getMessage(); }
  }
  private static void iniziaSessioneCronologia() {
    if (!cronologiaAbilitata || branoCorrente==null || branoCorrente.getTitolo()==null) return;
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null || utente.getEmail()==null || utente.getEmail().trim().isEmpty()) return;
    emailUtenteSessione=utente.getEmail().trim();
    titoloSessione=branoCorrente.getTitolo();
    inizioSessione=LocalDateTime.now();
    secondiSessione=0;
  }
  public static void salvaSessioneCronologia() {
    if (cronologiaAbilitata&&emailUtenteSessione!=null && titoloSessione!=null && inizioSessione!=null) {
      try{
        int secondiDaSalvare=Math.max(1, secondiSessione);
        CronologiaAscoltiStorage.salva(emailUtenteSessione,titoloSessione,inizioSessione,secondiDaSalvare);
      } catch (Exception e) { e.getMessage(); }
    }
    emailUtenteSessione=null;
    titoloSessione=null;
    inizioSessione=null;
    secondiSessione=0;
  }
  public static void iniziaNuovoAscolto() {
    secondiAscoltoCorrente=0;
    ascoltoRegistrato=false;
    if (branoCorrente!=null) {
      titoloBranoConteggiato=branoCorrente.getTitolo();
      iniziaSessioneCronologia();
    } else titoloBranoConteggiato=null;
  }
  public static void avviaPlaylist(List<Brano> brani, int indiceIniziale) throws BusinessException {
    avviaRiproduzione(brani, indiceIniziale, false);
  }
  public static void avviaBranoDaHome(Brano brano) throws BusinessException {
    if (brano==null) throw new BusinessException("Nessun brano selezionato");
    avviaRiproduzione(Collections.singletonList(brano),0,false);
  }
  public static void avviaBranoDaRicerca(Brano brano) throws BusinessException {
    if (brano==null) throw new BusinessException("Nessun brano selezionato");
    avviaRiproduzione(Collections.singletonList(brano),0,true);
  }
  private static void avviaRiproduzione(List<Brano> brani,int indiceIniziale, boolean registraCronologia) throws BusinessException {
    if (brani==null || brani.isEmpty()) throw new BusinessException("La playlist è vuota");
    if (indiceIniziale <0 || indiceIniziale >= brani.size()) throw new BusinessException("Seleziona un brano valido");
    List<Brano> braniPlaylist=new ArrayList<>(brani);
    for (Brano brano : braniPlaylist) {
      if (brano==null) throw new BusinessException("La playlist contiene un brano non valido");
    }
    fermaTimerRiproduzione();
    salvaSessioneCronologia();
    cronologiaAbilitata=registraCronologia;
    codaService.stop(coda);
    codaService.svuotaCoda(coda);
    for (Brano brano:braniPlaylist) codaService.aggiungiBranoCoda(coda,brano);
    codaService.impostaIndiceCorrente(coda, indiceIniziale);
    branoCorrente=braniPlaylist.get(indiceIniziale);
    progressoCorrente=0;
    iniziaNuovoAscolto();
    codaService.play(coda);
    avviaTimerRiproduzione();
  }
  public static void fermaRiproduzionePerCambioUtente() {
    fermaTimerRiproduzione();  //Fermiamo subito il timer globale
    salvaSessioneCronologia();  //salviamo la cronologia della sessione dell'utente precedente
    cronologiaAbilitata=false;  //Ora disabilitiamo la registrazione della cronologia della vecchia sessione 
    try{  //Andiamo ad azzerare lo stato della coda condivisa
      codaService.stop(coda);
      codaService.svuotaCoda(coda);
    } catch(BusinessException e) { e.getMessage(); }
    branoCorrente=null;  //Adesso azzeriamo il brano e il progresso globali
    progressoCorrente=0;
    titoloBranoConteggiato=null;  //Successivamente azzeriamo i dati dell'ascolto in corso
    secondiAscoltoCorrente=0;
    ascoltoRegistrato=false;
    emailUtenteSessione=null;  //Infine azzeriamo i dati della sessione di cronologia
    titoloSessione=null;
    inizioSessione=null;
    secondiSessione=0;
  }
}
