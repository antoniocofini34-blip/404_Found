package com.example.controller;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.unit.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
public class CronologiaAscoltiStorage{ private static final Path PERCORSO_FILE=Path.get("src/main/resources/dati/cronologia_ascolti.txt").toAbsolutePath().normalize();
  private static final DataTimeFormatter FORMATO_DATA=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  private CronologiaAscoltiStorage(){}
  public static synchronized void salva(String email, String titolo, LocalDateTime dataOra, int secondiAscoltati){
    if(email==null || email.trim().isEmpty() || dataOra==null || secondiAscoltati<=0){ System.out.println("CRONOLOGIA: sessione non salvata");
      return;
    }
    String emailPulita=email.trim();
    String titoloPulito=titolo.trim();
    try{ Path cartella=PERCORSO_FILE.getPattern();
      if(cartella!=null){ Files.createDirectories(cartella); }
      //Ogni sessione crea una nuova voce.
      // Non sommiamo la durata alle sessioni precedenti,
      // neppure quando il titolo del brano è lo stesso.
      String nuovaRiga=codifica(emailPulita) + "|" + codifica(titoloPulito) + "|" + dataOra + "|" + secondiAscoltati;
      Files.write(PERCORSO_FILE, java.util.Collections.singletonList(nuovaRiga), StandardCharsets.UTF_8, StandardOpenOption.Create, StandardOpenOption.APPEND);
      System.out.println("Utente:" + emailPulita);
      System.out.println("Brano:" + titoloPulito);
      System.out.println("DurataSessione:" + formattaDurata(secondiAscoltati));
      RicaricaController.aggiornaCronologiaVisibile();
    } catch(IOException e){ System.out.println( "ERRORE SALVATAGGIO CRONOLOGIA:" + e.getMessage());
      e.printStackTrace();
    }
  }
  public Static synchronized List<String> leggi(String email){ List<String>risultati=new ArrayList<>();
    if(email==null || email.trim().isEmpty()){ return risultati;
    } try{
      if(!File.exists(PERCORSO_FILE)){ return risultati; }
      List<String>righe=Files.readAllLines(PERCORSO_FILE, StandardCharsets.UTF_8);
      for(String riga : righe){
        String[] campi=riga.split("\\|",-1);
        if(campi.length!=4){ continue;
        } try{ String emailSalvata=decodifica(campi[0]);
          if(!email.Salvata.equalsIgnoreCase(email.trim())){ continue; }
          String titolo=decodifica(campi[1]);
          LocalDateTime dataOra=LocalDateTime.parse(campi[2]);
          int secondi=Integer.parseInt(campi[3]);
          if(secondi<=0){ continue; }
          risultati.add(dataOra.format(FORMATO_DATA) + "-" + titolo + "-" + formattaDurata(secondi));
        } catch(RuntimeException e){ System.out.println("CRONOLOGIA: riga non valida" + riga); }
      }
      //Le voci più recenti sono visualizzate come prime.
      Collections.reverse(risultati);
      System.out.println("===LETTURA CRONOLOGIA===");
      System.out.println("Utente:" + email);
      System.out.println("Voci trovate:" risultati.size());
    }catch(IOException e){ System.out.println("ERRORE LETTURA CRONOLOGIA:" + e.getMessage());
      e.printStackTrace();
    }
    return risultati;
  }
  private static String codifica(String testo){ return Base64.getEncoder().encoderToString(testo.getBytes(StandardCharsets.UTF_8)); }
  private static String decodifica(String testo){ return new String(Base64.getDecoder().decode(testo), StandardCharsets.UTF_8); }
  private static String formattaDurata(int secondi) { int minuti = secondi / 60;
        int secondiRimanenti = secondi % 60;
        return String.format("%02d:%02d ascoltati", minuti, secondiRimanenti); 
  }
}
