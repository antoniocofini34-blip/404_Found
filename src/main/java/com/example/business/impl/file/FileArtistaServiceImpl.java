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
public class FileArtistaServiceImpl implements ArtistaService{ private String filename;
  public FileArtistaServiceImpl(String filename) { this.filename=filename; }
  @Override
  public List<Artista> findAllArtisti() throws BusinessException {
    try { FileData data= leggiDati();
      return convertiRighe(data);
    } catch (IOException e) { throw new BusinessException("Errore durante la lettura degli artisti", e); }
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
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) { artisti.set(i, artista);
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
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) { artisti.remove(i);
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
    Artista artistaGruppo=findArtistaByNome(gruppo.getNomeGruppo());
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
    ArtistaGruppo gruppoFile=(ArtistaGruppo) artistaGruppo;
    String nomeComponente=ottieniNome(componente);
    if (nomeComponente==null||nomeComponente.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome del componente non è valido");
    List<Artista> componenti=gruppoFile.getListaMembri();
    boolean rimosso=false;
    for (int i=0; i<componenti.size(); i++) {
      String nomeMembro=ottieniNome(componenti.get(i));
      if (nomeMembro!=null && nomeMembro.equalsIgnoreCase(nomeComponente)) { componenti.remove(i);
        rimosso=true;
        break;
      }
    }
    if(!rimosso) throw new OperazioneNonValidaException("Il componente non è presente nel gruppo");
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
  private FileData leggiDati() throws IOException { File file= new File(this.filename);
    if(!file.exists()) { FileData data=new FileData();
      data.setContatore(0);
      data.setRighe(new ArrayList<>());
      return data;
    }
    return Utility.readAllRows(this.filename);
  }
  private List<Artista> convertiRighe(FileData data) throws BusinessException {
    List<Artista> artisti= new ArrayList<>();
    for(String[] riga:data.getRighe()) {
      Artista artista=creaArtistaDaRiga(riga);
      if(artista!=null) artisti.add(artista);
    }
    collegaComponentiGruppi(artisti);
    return artisti;
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
        try { generiSecondari.add(GenereMusicale.valueOf(genere.trim()));
        } catch (IllegalArgumentException e) { throw new BusinessException("Genere musicale non valido: " + genere);
        }
      }
    }
    if ("SOLISTA".equalsIgnoreCase(tipo)) { return new ArtistaSolista(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome); }
    if("GRUPPO".equalsIgnoreCase(tipo)) { return new ArtistaGruppo(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome, new ArrayList<Artista>()); }
    return null;
  }
  private void collegaComponentiGruppi(List<Artista> artisti) {
    for(String[] riga: getRigheFile()) {
      if(riga.length<6) continue;
      if(!"GRUPPO".equalsIgnoreCase(riga[0])) continue;
      String nomeGruppo=riga[1];
      Artista gruppoTrovato=trovaArtistaPerNome(artisti,nomeGruppo);
      if(!(gruppoTrovato instanceof ArtistaGruppo)) continue;
      ArtistaGruppo gruppo=(ArtistaGruppo) gruppoTrovato;
      List<Artista> componenti=new ArrayList<>();
      if(!riga[5].trim().isEmpty()) {
        String[] nomi=riga[5].split("\\|");
        for(String nomeComponente:nomi) {
          Artista componente=trovaArtistaPerNome(artisti, nomeComponente.trim());
          if(componente!=null) componenti.add(componente);
        }
      }
      gruppo.setListaMembri(componenti);
    }
  }
  private List<String[]> getRigheFile() {
    try { FileData data=leggiDati();
      return data.getRighe();
    } catch (IOException e) { return new ArrayList<>(); }
  }
  private Artista trovaArtistaPerNome(List<Artista> artisti, String nome) {
    for (Artista artista: artisti) {
      String nomeArtista=ottieniNome(artista);
      if(nomeArtista!=null && nomeArtista.equalsIgnoreCase(nome.trim())) return artista;
    }
    return null;
  }
  private void scriviArtisti(List<Artista> artisti) throws BusinessException { File file=new File(this.filename);
    File cartella=file.getParentFile();
    if(cartella!=null && !cartella.exists()) cartella.mkdirs();
    try (PrintWriter out=new PrintWriter(file)) { out.println(artisti.size());
      for (Artista artista:artisti) { out.println(convertiArtistaInRiga(artista)); }
    } catch (IOException e) { throw new BusinessException("Errore durante il salvataggio degli artisti", e); }
  }
  private String convertiArtistaInRiga(Artista artista) { String tipo;
    String nome;
    if (artista instanceof ArtistaSolista) { tipo="SOLISTA";
      ArtistaSolista solista=(ArtistaSolista) artista;
      nome=solista.getNomeArte();
    }
    else { tipo="GRUPPO";
      ArtistaGruppo gruppo=(ArtistaGruppo) artista;
      nome=gruppo.getNomeGruppo();
    }
    String biografia=artista.getBiografia();
    String generePrincipale=artista.getGenerePrincipale().name();
    String generiSecondari="";
    for (GenereMusicale genere:artista.getGeneriSecondari()) {
      if(!generiSecondari.isEmpty()) generiSecondari+="|";
      generiSecondari+=genere.name();
    }
    StringBuilder riga=new StringBuilder();
    riga.append(tipo);
    riga.append(",");
    riga.append(nome);
    riga.append(",");
    riga.append(biografia);
    riga.append(",");
    riga.append(generePrincipale);
    riga.append(",");
    riga.append(generiSecondari);
    if (artista instanceof ArtistaGruppo) { ArtistaGruppo gruppo= (ArtistaGruppo) artista;
      riga.append(",");
      List<Artista> componenti=gruppo.getListaMembri();
      for (int i=0; i<componenti.size(); i++) {
        if (i>0) riga.append("|");
        riga.append(ottieniNome(componenti.get(i)));
      }
    }
    return riga.toString();
  }
  private String ottieniNome(Artista artista) {
  if (artista instanceof ArtistaSolista) { ArtistaSolista solista=(ArtistaSolista) artista;
      return solista.getNomeArte();
    } 
    if (artista instanceof ArtistaGruppo) { ArtistaGruppo gruppo=(ArtistaGruppo) artista;
      return gruppo.getNomeGruppo();
    }
    return null;
  }
}
