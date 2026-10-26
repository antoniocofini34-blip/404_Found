package com.example.controller;
import java.io.IOException;
import java.nio.charset.StandardCharsets;  //Permette di specificare la codifica del testo standard UTF_8 per gestire accenti e caratteri speciali
import java.nio.file.Files;  //Serve per lavorare con i file e contiene metodi per svolgere qualsiasi operazione
import java.nio.file.Path;  //È un'interfaccia che rappresenta il percorso dove si trova il file
import java.nio.file.Paths;  //È una classe di supporto (utility) per creare oggetti Path
import java.nio.file.StandardOpenOption;  //È un elenco di opzioni (enumerazione) che specifica in che modo si vuole aprire un file quando si decide di scriverci dentro
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;  //Permette di codificare e decodificare dati in formato Base64
import java.util.*;
public class CronologiaAscoltiStorage{ private static final Path PERCORSO_FILE=Paths.get("src/main/resources/dati/cronologia_ascolti.txt").toAbsolutePath().normalize();
  private static final DateTimeFormatter FORMATO_DATA=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");    //Definisce il formato standard per mostrare o leggere data e ora
  private CronologiaAscoltiStorage(){}
  public static synchronized void salva(String email, String titolo, LocalDateTime dataOra, int secondiAscoltati){
    if(email==null || email.trim().isEmpty() || dataOra==null || secondiAscoltati<=0){ System.out.println("CRONOLOGIA: sessione non salvata");
      return;
    }
    String emailPulita=email.trim();
    String titoloPulito=titolo.trim();
    try{ Path cartella=PERCORSO_FILE.getParent();
      if(cartella!=null){ Files.createDirectories(cartella); }
      //Ogni sessione crea una nuova voce.
      // Non sommiamo la durata alle sessioni precedenti,
      // neppure quando il titolo del brano è lo stesso.
      String nuovaRiga=codifica(emailPulita) + "|" + codifica(titoloPulito) + "|" + dataOra + "|" + secondiAscoltati;
      Files.write(PERCORSO_FILE, java.util.Collections.singletonList(nuovaRiga), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);  //Scriviamo sul file il percorso del file su disco. Trasforma la singola riga in una lista compatibile. Applica la codifica per lettere accentate e speciali. Crea il file da 0, se non esiste ancora. Aggiunge il testo in fondo (in coda) se esiste già
      RicercaController.aggiornaCronologiaVisibile();
    } catch(IOException e){ e.getMessage(); }
  }
  public static synchronized List<String> leggi(String email){ List<String>risultati=new ArrayList<>();
    if(email==null || email.trim().isEmpty()){ return risultati;
    } try{
      if(!Files.exists(PERCORSO_FILE)){ return risultati; }
      List<String>righe=Files.readAllLines(PERCORSO_FILE, StandardCharsets.UTF_8);
      for(String riga : righe){
        String[] campi=riga.split("\\|",-1);
        if(campi.length!=4){ continue;
        } try{ String emailSalvata=decodifica(campi[0]);
          if(!emailSalvata.equalsIgnoreCase(email.trim())){ continue; }
          String titolo=decodifica(campi[1]);
          LocalDateTime dataOra=LocalDateTime.parse(campi[2]);
          int secondi=Integer.parseInt(campi[3]);
          if(secondi<=0){ continue; }
          risultati.add(dataOra.format(FORMATO_DATA) + "-" + titolo + "-" + formattaDurata(secondi));
        } catch(RuntimeException e){ System.out.println("CRONOLOGIA: riga non valida" + riga); }
      }
      //Le voci più recenti sono visualizzate come prime.
      Collections.reverse(risultati);
    }catch(IOException e){ e.getMessage(); }
    return risultati;
  }
  public static synchronized boolean svuota(String email) {
    if (email==null || email.trim().isEmpty()) return false;
    try { if (!Files.exists(PERCORSO_FILE)) return true;
          List<String> righe=Files.readAllLines(PERCORSO_FILE, StandardCharsets.UTF_8);
          List<String> righeDaConservare=new ArrayList<>();
          for (String riga : righe) {
            String[] campi=riga.split("\\|", -1);  
            if (campi.length!=4) {  //Conserva le righe che non hanno il formato atteso
              righeDaConservare.add(riga);
              continue;
            }
            try { String emailSalvata=decodifica(campi[0]);
                  if (!emailSalvata.equalsIgnoreCase(email.trim())) righeDaConservare.add(riga);  //Conserva tutti gli ascolti degli altri utenti
                }  catch(IllegalArgumentExceptione e) { righedaConservare.add(riga); }  //Non elimina righe che non riesce a interpretare
          }
         //Scrive una collezione di righe in un file, gestendo la codifica dei caratteri in UTF_8 e la modalità di apertura. CREATE: crea il file se non esiste già; TRUNCATE_EXISTING: se il file esiste già, azzera il suo contenuto; WRITE: apre il file in modalità di scrittura
         Files.write(PERCORSO_FILE, righeDaConservare, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
         return true;
        } catch(IOException e) { e.getMessage(); 
                return false;
          }
  }    
  private static String codifica(String testo){ return Base64.getEncoder().encodeToString(testo.getBytes(StandardCharsets.UTF_8)); }  //Converte un testo normale in una stringa codificata in Base64 sicura per il trasferimento dati
  private static String decodifica(String testo){ return new String(Base64.getDecoder().decode(testo), StandardCharsets.UTF_8); }  //Converte una stringa in Base64 nel suo testo originale leggibile
  private static String formattaDurata(int secondi) { int minuti = secondi / 60;
        int secondiRimanenti = secondi % 60;
        return String.format("%02d:%02d ascoltati", minuti, secondiRimanenti); 
  }
}
