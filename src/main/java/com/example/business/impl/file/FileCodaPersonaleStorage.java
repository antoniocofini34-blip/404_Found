package com.example.business.impl.file;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class FileCodaPersonaleStorage{ private static final String CARTELLA="src/main/resource/dati/code_percorso";
  public List<String>leggiTitoli(String emailUtente){ Path file=percorsoFile(emailUtente);  //Recupera il percorso del file associato all'email dell'utente
    if(!Files.exists(file)){ return new ArrayList<>();
    } try { return Files.readAllLines(file, StandardCharsets.UTF_8);  //Legge tutte le righe del file (ogni riga rappresenta il titolo di un brano) usando la codifica UTF-8
    } catch(IOException e){ throw new IllegalStateException("Impossibile leggere la Coda personale:", e); }
  }
  public void salvaTitoli(String emailUtente, List<String>titoli){ Path file=percorsoFile(emailUtente);  //Sovrascrive il file dell'utente salvando la lista completa dei titoli forniti
    try{ Files.createDirectories(file.getParent());  //Crea automaticamente le cartelle di destinazione se non esistono ancora sul disco
    Files.write(file, titoli, StandardCharsets.UTF_8);  //Scrive la lista di titoli nel file, sovrascrivendo l'eventuale contenuto precedente
    } catch (IOException e){ throw new IllegalStateException("Impossibile salvare la Coda personale:", e); }
  }
  public void aggiungiTitolo(String emailUtente, String titolo){
    if(titolo==null || titolo.isBlank()){ throw new IllegalArgumentException("Il titolo del brano non puù essere vuoto"); }
    List<String>titoli=leggiTitoli(emailUtente);
    for(String titoloEsistente : titoli){
      if(titoloEsistente.equalsIgnoreCase(titolo.trim())) return;
    }
    titoli.add(titolo.trim());
    salvaTitoli(emailUtente, titoli);
  }
  public void rimuoviTitolo(String emailUtente, String titolo){ List<String>titoli=leggiTitoli(emailUtente);
    titoli.removeIf(t->t.equalsIgnoreCase(titolo));
    salvaTitoli(emailUtente, titoli);
  }
  private Path percorsoFile(String emailUtente){
    if(emailUtente==null || emailUtente.isBlank()){ throw new IllegalArgumentException("L'email dell'utente non può essere vuota"); }
    String nomeFile=emailUtente.trim().replaceAll("[^a-zA-Z0-9.-]","") + ".txt";  //Pulisce l'email rimuovendo tutti i caratteri non validi o pericolosi per un nome di file, lasciando intatti solo lettere, numeri, punti e trattini
    return Paths.get(CARTELLA, nomeFile);  //Unisce la cartella principale col nome del file generato per ottenere il percorso finale 
  }
}
