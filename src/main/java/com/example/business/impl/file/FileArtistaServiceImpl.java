package com.example.business.impl.file;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.example.business.ArtistaService;
import com.example.business.BusinessException;
import com.example.business.exception.ArtistaNonTrovatoException;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
import com.example.domain.GenereMusicale;
public class FileArtistaServiceImpl implements ArtistaService {
  private String fileName;
  public FileArtistaServiceImpl(String filename) {
    this.filename=filename;
  }
  @Override
  public List<Artista> findAllArtisti() throws BusinessException {
    try {
      FileData data= leggiDati();
      List<Artista> artisti=new ArrayList<>();
      for (String[] riga: data.getRighe()) {
        Artista artista= creaArtistaDaRiga(riga);
        if(artista!=null) artista.add(artista);
      }
      collegaComponentiGruppi(artisti);
      return artisti;
    } catch (IOException e) {
      throw new BusinessException("Errore durante la lettura degli artisti", e);
    }
  }
  @Override
  public Artista findArtistaByNome(String nome) throws BusinessException {
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non è valido");
    List<Artista> artisti=findAllArtisti();
    for(Artista artista: artisti) {
      String nomeArtista= ottieniNome(artista);
      if(nomeArtista!=null && nomeArtista.equalsIgnoreCase(nome.trim())) return artista;
    }
    throw new ArtistaNonTrovatoException("Artista non trovato: " + nome);
  }
  @Override
  public void creaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere null");
    String nome=ottieniNome(artista);
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto");
    List<Artista> artisti= findAllArtisti();
    for (Artista artistaEsistente: artisti) {
      String nomeEsistente=ottieniNome(artistaEsistente);
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) throw new DuplicatoException("Esiste già un artista con il nome: " + nome);
    }
    artisti.add(artista);
    scriviArtisti(artisti);
  }
  @Override
  public void modificaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere null");
    String nome=ottieniNome(artista);
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto");
    List<Artista> artisti=findAllArtisti();
    boolean trovato= false;
    for (int i=0; i<artisti.size(); i++) {
      String nomeEsistente=ottieniNome(artisti.get(i));
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) {
        artisti.set(i, artista);
        trovato=true;
        break;
      }
    }
    if (!trovato) throw new ArtistaNonTrovatoException("Artista da modificare non trovato");
    scriviArtisti(artisti);
  }
  @Override
  public void eliminaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere null");
    String nome=ottieniNome(artista);
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto");
    List<Artista> artisti= findAllArtisti();
    boolean rimosso=false;
    for (int i=0; i<artisti.size(); i++) {
      String nomeEsistente=ottieniNome(artisti.get(i));
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) {
        artisti.remove(i);
        rimosso=true;
        break;
      }
    }
    if (!rimosso) throw new ArtistaNonTrovatoException("Artista da eliminare non trovato");
    scriviArtisti(artisti);
  }
  @Override
  public List<Album> findAlbumArtista(Artista artista) throws BusinessException{
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere null");
    String nome=ottieniNome(artista);
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non è valido");
    Artista artistaFile=findArtistaByNome(nome);
    return new ArrayList<>(artistaFile.getDiscografia());
  }
  @Override
  public void aggiungiComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException {
    if (gruppo==null) throw new OperazioneNonValidaException("Il gruppo non può essere null");
    if (componente==null) throw new OperazioneNonValidaException("Il componente non può essere null");
    if (gruppo==componente) throw new OperazioneNonValidaException("Un gruppo non può essere componente di se stesso");
    Artista artista=findArtistaByNome(gruppo.getNomeGruppo());
    if(!(artistaGruppo instanceof ArtistaGruppo)) throw new OperazioneNonValidaException("L'artista indicato non è un gruppo");
    ArtistaGruppo gruppoFile= (ArtistaGruppo) artistaGruppo;
    String nomeComponente=ottieniNome(componente);
    if(nomeComponente==null || nomeComponente.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome del componente non è valido");
    List<Artista> componenti=gruppoFile.getListaMembri();
    for (Artista membro:componenti) {
      String nomeMembro=ottieniNome(membro);
      if(nomeMembro!=null && nomeMembro.equalsIgnoreCase(nomeComponente.trim())) throw new DuplicatoException("Il componente è già presente nel gruppo");
    }
    Artista componenteFile=findArtistaByNome(nomeComponente);
    componenti.add(componenteFile);
    gruppoFile.setListaMembri(componenti);
    modificaArtista(gruppoFile);
  }
  @Override
  public void rimuoviComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException {
    if (gruppo==null) throw new OperazioneNonValidaException("Il gruppo non può essere null");
    if (componente==null) throw new OperazioneNonValidaException("Il componente non può essere null");
    Artista artistaGruppo=findArtistaByNome(gruppo.getNomeGruppo());
    if(!(artistaGruppo instanceof ArtistaGruppo)) throw new OperazioneNonValidaException("L'artista indicato non è un gruppo");
    ArtistaGruppo= gruppoFile=(ArtistaGruppo) artistaGruppo;
    String nomeComponente=ottieniNome(componente);
    List<Artista> componenti=gruppoFile.getListaMembri();
    boolean rimosso=false;
    for (int i=0; i<componenti.size(); i++) {
      String nomeMembro=ottieniNome(componenti.get(i));
      if (nomeMembro!=null && nomeMembro.equalsIgnoreCase(nomeComponente)) {
        componenti.remove(i);
        rimosso=true;
        break;
      }
    }
    if(!rimosso) throw new OperazioneNonValidaexception("Il componente non è presente nel gruppo");
    gruppoFile.setListaMembri(componenti);
    modificaArtista(gruppoFile);
  }
  @Override
  public List<Artista> findComponenti(ArtistaGruppo gruppo) throws BusinessException {
    if (gruppo==null) throw new OperazioneNonValidaException("Il gruppo non può essere null");
    Artista artistaGruppo=findArtistaByNome(gruppo.getNomeGruppo());
    if(!(artistaGruppo instanceof ArtistaGruppo)) throw new OperazioneNonValidaException("L'artista indicato non è un gruppo");
    ArtistaGruppo gruppoFile=(ArtistaGruppo) artistaGruppo;
    return new ArrayList<>(gruppoFile.getListaMembri());
  }
  private FileData leggiDati() throws IOException {
    File file= new File(this.filename);
    if(!file.exists()) {
      FileData data=new FileData();
      data.setContatore(0);
      data.setRighe(new ArrayList<>());
      return data;
    }
    return Utility.readAllRows(this.filename);
  }
  private Artista creaArtistaDaRiga(String[] riga) throws BusinessException {
    if (riga==null || riga.length<5) return null;
    String tipo=riga[0];
    String nome=riga[1];
    String biografia=riga[2];
    GenereMusicale generePrincipale;
    try {
      generePrincipale=GenereMusicale.valueOf(riga[3].trim());
    } catch (IllegalArgumentException e) {
      throw new BusinessException("Genere musicale non valido: " + riga[3]);
    }
    Set<GenereMusicale> generiSecondari=new HashSet<>();
    if (!riga[4].trim().isEmpty()) {
      String[] generi=riga[4].split("\\|");
      for (String genere: generi) {
        try {
          generiSecondari.add(GenereMusicale.valueOf(genere.trim()));
        } catch (IllegalArgumentException e) {
          throw new BusinessException("Genere musicale non valido: " + genere);
        }
      }
    }
    if ("SOLISTA".equalsIgnoreCase(tipo)) {
      return new ArtistaSolista(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome);
    }
    if("GRUPPO".equalsIgnoreCase(tipo)) {
      return new ArtistaGruppo(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome, new ArrayList<Artista>());
    }
    return null;
  }
  private void collegaComponentiGruppi(List<Artista> artisti) {
    for(String[] riga: getRigheFile()) {
      













  
