package com.example.business.impl.file;
import java.util.*;
import com.example.business.BusinessException;
import com.example.business.StatisticheService;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.Ascolto;
import com.example.domain.Brano;
import com.example.domain.GenereMusicale;
import com.example.domain.Utente;
import com.example.business.impl.ram.RAMCronologiaAscoltiServiceImpl;
public class FileStatisticheServiceImpl implements StatisticheService {
  private FileBranoServiceImpl branoService;
  private FileArtistaServiceImpl artistaService;
  private RAMCronologiaAscoltiServiceImpl cronologiaService;
  public FileStatisticheServiceImpl(FileBranoServiceImpl branoService, FileArtistaServiceImpl artistaService, RAMCronologiaAscoltiServiceImpl cronologiaService) {
    if (branoService==null) throw new IllegalArgumentException("Il servizio dei brani non può essere null");
    if (artistaService==null) throw new IllegalArgumentException("Il servizio degli artisti non può essere null");
    if (cronologiaService==null) throw new IllegalArgumentException("Il servizio della cronologia non può essere null");
    this.branoService=branoService;
    this.artistaService=artistaService;
    this.cronologiaService=cronologiaService;
  }
  @Override
  public List<Brano> ottieniBraniPiuAscoltati(Utente utente, int limite) throws BusinessException {
    verificaUtente(utente);
    if (limite<=0) throw new OperazioneNonValidaException("Il limite deve essere maggiore di 0");
    List<Ascolto> ascolti= cronologiaService.getAscolti(utente.getCronologiaAscolti());
    Map<Brano, Integer> conteggi=new HashMap<>();
    for (Ascolto ascolto:ascolti) {
      if (ascolto!=null && ascolto.getBrano()!=null) {
        Brano brano=ascolto.getBrano();
        conteggi.put(brano, conteggi.getOrDefault(brano, 0) +1);
      }
    }
    List<Brano> risultati=new ArrayList<>(conteggi.keySet());
    List<Brano> ordinati=new ArrayList<>();
    while (!risultati.isEmpty() && ordinati.size()<limite) {
      Brano piuAscoltato=risultati.get(0);
      for (Brano brano:risultati) {
        if (conteggi.get(brano)>conteggi.get(piuAscoltato)) piuAscoltato=brano;
      }
      ordinati.add(piuAscoltato);
      risultati.remove(piuAscoltato);
    }
    return ordinati;
  }
  @Override
  public List<Artista> ottieniArtistiPiuAscoltati(Utente utente) throws BusinessException {
    verificaUtente(utente);
    List<Ascolto> ascolti=cronologiaService.getAscolti(utente.getCronologiaAscolti());
    Map<Artista, Integer> conteggi=new HashMap<>();
    for (Ascolto ascolto:ascolti) {
      if (ascolto==null || ascolto.getBrano()==null) continue;
      Brano brano=ascolto.getBrano();
      Album album=brano.getAlbumBrano();
      if (album==null) continue;
      for (Artista artista:artistaService.findAllArtisti()) {
        List<Album> albumArtista=artistaService.findAlbumArtista(artista);
        if (albumArtista.contains(album)) {
          conteggi.put(artista, conteggi.getOrDefault(artista, 0) +1);
          break;
        }
      }
    }
    List<Artista> risultati=new ArrayList<>(conteggi.keySet());
    List<Artista> ordinati=new ArrayList<>();
    while (!risultati.isEmpty()) {
      Artista piuAscoltato=risultati.get(0);
      for (Artista artista:risultati) {
        if (conteggi.get(artista)>conteggi.get(piuAscoltato)) piuAscoltato=artista;
      }
      ordinati.add(piuAscoltato);
      risultati.remove(piuAscoltato);
    }
    return ordinati;
  }
  @Override
  public int calcolaTempoTotaleAscolto(Utente utente) throws BusinessException {
    verificaUtente(utente);
    return cronologiaService.calcolaTempoTotaleAscolto(utente.getCronologiaAscolti());
  }
  @Override
  public GenereMusicale ottieniGenereMusicalePiuAscoltato(Utente utente) throws BusinessException {
    verificaUtente(utente;
    List<Ascolto> ascolti=cronologiaService.getAscolti(utente.getCronologiaAscolti());
    if (ascolti.isEmpty()) return null;
    Map<GenereMusicale, Integer> conteggi=new HashMap<>();
    for (Ascolto ascolto:ascolti) {
      if (ascolto==null||ascolto.getBrano()==null) continue;
      Brano brano=ascolto.getBrano();
      for (GenereMusicale genere:brano.getGenereBrano()) {
        if (genere!=null) conteggi.put(genere, conteggi.getOrDefault(genere, 0) +1);
      }
    }
    if (conteggi.isEmpty()) return null;
    GenereMusicale generePiuAscoltato=null;
    int massimo=-1;
    for (Map.Entry<GenereMusicale, Integer> entry: conteggi.entrySet()) {
      if (entry.getValue() > massimo) {
        massimo=entry.getValue();
        generePiuAscoltato=entry.getKey();
      }
    }
    return generePiuAscoltato;
  }
  @Override
  public List<Brano> suggerisciBrani(Utente utente) throws BusinessException {
    verificaUtente(utente);
    GenereMusicale generePreferito=ottieniGenereMusicalePiuAscoltato(utente);
    if (generePreferito==null) return new ArrayList<>()
    Set<Brano> braniAscoltati=new HashSet<>();
    List<Ascolto> ascolti=cronologiaService.getAscolti(utente.getCronologiaAscolti());
    for (Ascolto ascolto:ascolti) {
      if (ascolto!=null && ascolto.getBrano()!=null) braniAscoltati.add(ascolto.getBrano());
    }
    List<Brano> suggerimenti=new ArrayList<>();
    for (Brano brano:branoService.findAllBrani()) {
      if (brano==null) continue;
      if (braniAscoltati.contains(brano)) continue;
      if (brano.getGenereBrano().contains(generePreferito)) suggerimenti.add(brano);
    }
    return suggerimenti;
  }
  private void verificaUtente(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (utente.getCronbologiaAscolti()==null) throw new OperazioneNonValidaException("La cronologia dell'utente non può essere null");
  }
}
