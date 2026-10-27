package com.example.business.impl.file;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class FileCodaPersonaleStorage{ private static final String CARTELLA="src/main/resource/dati/code_percorso";
  public List<String>leggiTitoli(String emailUtente){ Path file=percorsoFile(emailUtente);
    if(!Files.exists(file)){ return new ArrayList<>();
    } try { return Files.readAllLines(file, StandardCharsets.UTF_8);
    } catch(IOException e){ throw new IllegalStateException("Impossibile leggere la Coda personale:", e); }
  }
  public void salvaTitoli(String emailUtente, List<String>titoli){ Path file=percorsoFile(emailUtente);
    try{ Files.createDierectories(file.getParent());
    Files.write(file, titoli, StandardCharsets.UTF_8);
    } catch (IOException e){ throw new IllegalStateException("Impossibile salvare la Coda personale:", e); }
  }
  public void aggiungiTitolo(String emailUtente, String titolo){
    if(titolo==null || titolo.isBlank()){ throw new IllegalArgumentException("Il titolo del brano non puù essere vuoto"); }
    List<String>titoli=leggiTitoli(emailUtente);
    for(String titoloEsistente : titoli){
      if(titoloEsistente.equalsIgnoreCase(titolo.trim())) return;
    }
    titolo.add(titolo.trim());
    salvaTitoli(emailUtente, titoli);
  }
  public void rimuoviTitolo(String emailUtente, String titolo){ List<String>titoli=leggiTitoli(emailUtente);
    titoli.remove(t->t.equalsIgnoreCase(titolo));
    salvaTitoli(emailUtente, titoli);
  }
  private Path percorsoFile(String emailUtente){
    if(emailUtente==null || emailUtente.isBlank()){ throw new IllegalArgumentException("L'email dell'utente non può essere vuota"); }
    String nomeFile=emailUtente.trim().replaceAll("[^a-zA-Z0-9.-]","") + ".txt";
    return Paths.get(CARTELLA, nomeFile);
  }
}
