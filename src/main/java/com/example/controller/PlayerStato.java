package com.example.controller;
import com.example.business.exception.BusinessException;
import java.timeLocalDateTime;
import java.util.*;
import com.example.business.impl.file.FileBranoService;
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
  private static final RAMCodaRiproduzioneService codaService=new RAMCodaRiproduzioneServiceImpl();
  private static final FileBranoServiceImpl branoService=new FileBranoServiceImpl("src/main/resources/dati/brani.txt");
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
  static {
    try {  Path percorso=Paths.get(FILE_TEMPI);
           if (Files.exists(percorso)) {
             try (InputStream input=Files.newInputStream(percorso)) {
               tempiAscolto.load(input);
             }
           }
        } catch(IOException e) { e.getMessage(); }
  }
  public static RAMCodaRiproduzioneServiceImpl getCodaService() { return codaService; }
  public static CodaRiproduzione getCoda() { return coda; }
  public static Brano getBranoCorrente() { return branoCorrente; }
  public static void setBranoCorrente(Brano brano) {
    String nuovoTitolo=brano==null ? null : brano.getTitolo();
    boolean branoDiverso=titoloBranoConteggiato==null ? nuovoTitolo!=null : nuovoTitolo==null || !titoloBranoConteggiato.equalsIgnoreCase(nuovoTitolo);
    if (branoDiverso) {
      salvaSessioneCronologia();
      azzeraConteggioAscolto();
      titoloBranoConteggiato=nuovoTitolo;
    }
    branoCorrente=brano;
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
    String chiave=creaChiave(utente.getEmail(), brano.getTitolo(), "ascolti");
    int ascolti=Integer.parseInt(tempiAscolto.getProperty(chiave, "0"));
    tempiAscolto.setProperty(chiave, String.valueOf(ascolti + 1));
    salvaTempiAscolto();
    ascoltoRegistrato=true;
    System.out.println("Ascolto personale registrato per " + utente.getEmail() + ": " + brano.getTitolo());
  }
  public static void avviaTimerRiproduzione() {
    if (timerRiproduzione!=null) {
      timerRiproduzione.stop();
      timerRiproduzione=null;
    }
    if (inizioSessione==null) iniziaSessioneCronologia();
    Timeline nuovoTimer=new Timeline(new KeyFrame(Duration.seconds(1), e -> {
      if (coda.getStato()!=StatoRiproduzione.IN_RIPRODUZIONE || branoCorrente==null) {
        if (timerRiproduzione!=null) {
          timerRiproduzione.stop();
          timerRiproduzione=null;
        }
        return;
      }
      secondiSessione++;
      if (!ascoltoRegistrato) {
        secondiAscoltoCorrente++;
        if (secondiAscoltoCorrente >= 30) registraAscolto(branoCorrente);
      }
      registraSecondoAscolto(branoCorrente);
      int durata=branoCorrente.getDurata();
      if (progressoCorrente < durata) progressoCorrente++;
      if (progressoCorrente >= durata) {
        progressoCorrente=durata;
        if (timerRiproduzione!=null) {
          timerRiproduzione.stop();
          timerRiproduzione=null;
        }
        try { salvaSessioneCronologia();
              codaService.branoTerminato(coda);
              Brano nuovoBrano=codaService.getBranoCorrente(coda);
              if (nuovoBrano!=null && coda.getStato()==StatoRiproduzione.IN_RIPRODUZIONE) {
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
    timerRiproduzione=nuovoTimer;
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
    String chiave=creaChiave(email, titolo, "secondi");
    int secondi=Integer.paerseInt(tempiAscolto.getProperty(chiave, "0"));
    tempiAscolto.setProperty(chiave, String.valueOf(secondi + 1));
    salvaTempiAscolto();
  }
  private static String creaChiave(String email, String titolo, String tipo) {
    String emailNormalizzata=email.trim().toLowerCase();
    String titoloNormalizzato=titolo.trim().toLowerCase();
    return emailNormalizzata+"|"+titoloNormalizzato+"|"+tipo;
  }
  public static int getTempoAscoltoUtente(String email, String titolo) {
    if (email==null||titolo==null) return 0;
    return Integer.parseInt(tempiAscolto.getProperty(creaChiave(email.titolo,"secondi"), "0"));
  }
  public static int getNumeroAscoltiUtente(String email, String titolo) {
    if (email==null||titolo==null) return 0;
    return Integer.parseInt(tempiAscolto.getProperty(creaChiave(email.titolo,"ascolti"), "0"));
  }
  private static void salvaTempiAscolto() {
    try{
      Path percorso=Paths.get(FILE_TEMPI);
      Path cartella=percorso.getParent();
      if (cartella!=null) Files.createDirectories(cartella);
      try (OutputStream output=Files.newOutputStream(percorso)) { tempiAscolto.store(output, "Statistiche personali degli utenti");
      } 
    } catch (IOException e) { e.getMessage(); }
  }
  private static void iniziaSessioneCronologia() {
    if (!cronologiaAbilitata||branoCorrente==null||branoCorrente.getTitolo()==null) return;
    Utente utente=UtenteAttivo.getUtente();
    if (utente==null||utente.getEmail()==null||utente.getEmail().trim().isEmpty()) return;
    emailUtenteSessione=utente.getEmail().trim();
    titoloSessione=branoCorrente.getTitolo();
    inizioSessione=LocalDateTime.now();
    secondiSessione=0;
  }
  public static void salvaSessioneCronologia() {
    if (cronologiaAbilitata&&emailUtenteSessione!=null&&titoloSessione!=null&&inizioSessione!=null) {
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
    if (brani==null||brani.isEmpty()) throw new BusinessException("La playlist è vuota");
    if (indiceIniziale<0||inidiceIniziale>=brani.size()) throw new BusinessException("Seleziona un brano valido");
    List<Brano> braniPlaylist=new ArrayList<>(brani);
    for (Brano brano:braniPlaylist) {
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
    iniziaNuovoAscoltoì();
    codaService.play(coda);
    avviaTimerRiproduzione();
  }
  public static void fermaRiproduzionePerCambioUtente() {
    //Fermiamo subito il timer globale
    fermaTimerRiproduzione();
    //salviamo la cronologia della sessione dell'utente precedente
    salvaSessioneCronologia();
    //ora disabilitiamo la registrazione della cronologia della vecchia sessione 
    cronologiaAbilitata=false;
    //andiamo ad azzerare lo stato della coda condivisa
    try{
      codaService.stop(coda);
      codaService.suotaCoda(coda);
    } catch(BusinessException e) { e.getMessage(); }
    //adesso azzeriamo il brano e il progresso globali
    branoCorrente=null;
    progressoCorrente=0;
    //successivamente azzeriamo i dati dell'ascolto in corso
    titoloBranoConteggiato=null;
    secondiAscoltoCorrente00;
    ascoltoRegistrato=false;
    //infine azzeriamo i dati della sessione di cronologia
    emailUtenteSessione=null;
    titoloSessione=null;
    inizioSessione=null;
    secondiSessione=0;
  }
}
