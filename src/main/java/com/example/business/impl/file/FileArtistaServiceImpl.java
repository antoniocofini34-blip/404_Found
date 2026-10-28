package com.example.business.impl.file;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.example.business.ArtistaService;
import com.example.business.exception.BusinessException;
import com.example.business.exception.ArtistaNonTrovatoException;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Artista;
import com.example.domain.ArtistaGruppo;
import com.example.domain.ArtistaSolista;
import com.example.domain.Componente;
import com.example.domain.GenereMusicale;
public class FileArtistaServiceImpl implements ArtistaService{ private String filename;
  public FileArtistaServiceImpl(String filename) { this.filename=filename; }
  @Override
  public List<Artista> findAllArtisti() throws BusinessException {
    try { FileData data= leggiDati();
      return convertiRighe(data);
    } catch (IOException e) { throw new BusinessException("Errore nella lettura degli artisti", e); }
  }
  @Override
  public Artista findArtistaByNome(String nome) throws BusinessException {
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Nome artista non valido"); 
    List<Artista> artisti=findAllArtisti();
    for(Artista artista: artisti) {
      String nomeArtista= ottieniNome(artista);
      if(nomeArtista!=null && nomeArtista.equalsIgnoreCase(nome.trim())) return artista;
    }
    throw new ArtistaNonTrovatoException("Artista non trovato: " + nome);
  }
  @Override
  public void creaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere vuoto");
    String nome=ottieniNome(artista);
    if (nome==null || nome.trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto");
    List<Artista> artisti= findAllArtisti();
    for (Artista artistaEsistente: artisti) {
      String nomeEsistente=ottieniNome(artistaEsistente);
      if(nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) throw new DuplicatoException("Artista esistente con il seguente nome: " + nome);
    }
    artisti.add(artista);
    scriviArtisti(artisti);
  }
  @Override
  public void modificaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere vuoto");
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
    if (!trovato) throw new ArtistaNonTrovatoException("Non è stato possibile trovare l'artista da modificare");
    scriviArtisti(artisti);
  }
  @Override
  public void eliminaArtista(Artista artista) throws BusinessException {
    if (artista==null) throw new OperazioneNonValidaException("L'artista non può essere vuoto");
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
  public void aggiungiComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException {
    if (gruppo==null) throw new OperazioneNonValidaException("Il gruppo non può essere vuoto");
    validaComponente(componente);
    Artista artistaGruppo=findArtistaByNome(gruppo.getNomeGruppo());
    if(!(artistaGruppo instanceof ArtistaGruppo)) throw new OperazioneNonValidaException("L'artista indicato non è un gruppo");
    ArtistaGruppo gruppoFile= (ArtistaGruppo) artistaGruppo;
    List<Componente> componenti=gruppoFile.getListaMembri();
    for (Componente membro : componenti) {
      if (stessoComponente(membro, componente)) throw new DuplicatoException("Il componente è già presente nel gruppo.");
    }    
    componenti.add(componente);
    gruppoFile.setListaMembri(componenti);
    modificaArtista(gruppoFile);
  }
  @Override
  public void rimuoviComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException {
    if (gruppo==null) throw new OperazioneNonValidaException("Il gruppo non può essere vuoto");
    if (componente==null) throw new OperazioneNonValidaException("Il componente non può essere vuoto");
    Artista artistaGruppo=findArtistaByNome(gruppo.getNomeGruppo());
    if(!(artistaGruppo instanceof ArtistaGruppo)) throw new OperazioneNonValidaException("L'artista indicato non è un gruppo");
    ArtistaGruppo gruppoFile=(ArtistaGruppo) artistaGruppo;
    List<Componente> componenti=gruppoFile.getListaMembri();
    boolean rimosso=false;
    for (int i=0; i < componenti.size(); i++) {
      if (stessoComponente(componenti.get(i), componente)) {
        componenti.remove(i);
        rimosso=true;
        break;
      }
    }    
    if(!rimosso) throw new OperazioneNonValidaException("Il componente non è presente nel gruppo");
    gruppoFile.setListaMembri(componenti);
    modificaArtista(gruppoFile);
  }
  private void validaComponente(Componente componente) throws OperazioneNonValidaException {
    if (componente==null) throw new OperazioneNonValidaException("Il componente non può essere vuoto.");
    if (componente.getNome()==null || componente.getNome().trim().isEmpty() || componente.getCognome()==null || componente.getCognome().trim().isEmpty() || componente.getRuolo()==null || componente.getRuolo().trim().isEmpty()) throw new OperazioneNonValidaException("Nome, cognome e ruolo del componente sono obbligatori.");
  }
  private boolean stessoComponente(Componente primo, Componente secondo) {
    return primo.getNome().equalsIgnoreCase(secondo.getNome().trim()) && primo.getCognome().equalsIgnoreCase(secondo.getCognome().trim());
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
    } catch (IllegalArgumentException e) {throw new BusinessException("Genere musicale non valido: " + riga[3]);}
    Set<GenereMusicale> generiSecondari=new HashSet<>();
    if (!riga[4].trim().isEmpty()) {
      String[] generi=riga[4].split("\\|");
      for (String genere: generi) {
        try { generiSecondari.add(GenereMusicale.valueOf(genere.trim()));
        } catch (IllegalArgumentException e) { throw new BusinessException("Genere musicale non valido: " + genere); }
      }
    }
    if ("SOLISTA".equalsIgnoreCase(tipo)) return new ArtistaSolista(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome);
    if("GRUPPO".equalsIgnoreCase(tipo)) return new ArtistaGruppo(biografia, generePrincipale, generiSecondari, new ArrayList<Album>(), nome, new ArrayList<Componente>()); 
    return null;
  }
  private void collegaComponentiGruppi(List<Artista> artisti) {
    for(String[] riga: getRigheFile()) {
      if(riga.length<6 || !"GRUPPO".equalsIgnoreCase(riga[0])) continue;
      Artista artistaTrovato=trovaArtistaPerNome(artisti, riga[1]);
      if (!(artistaTrovato instanceof ArtistaGruppo)) continue;
      ArtistaGruppo gruppo=(ArtistaGruppo) artistaTrovato;
      List<Componente> componenti=new ArrayList<>();
      if(!riga[5].trim().isEmpty()) {
        String[] datiComponenti=riga[5].split("\\|");
        for (String dati : datiComponenti) {
          String[] campi=dati.split("~", -1);
          if (campi.legth==3) componenti.add(new Componente(campi[0], campi[1], campi[2]));
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
    StringBuilder generiSecondari=new StringBuilder();
    for (GenereMusicale genere:artista.getGeneriSecondari()) {
      if(generiSecondari.length() > 0) generiSecondari.append("|");
      generiSecondari.append(genere, name());
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
      List<Componente> componenti=gruppo.getListaMembri();
      for (int i=0; i<componenti.size(); i++) {
        if (i>0) riga.append("|");
        Componente componente=componenti.get(i);
        riga.append(componente.getNome()).append("~");
        riga.append(componente.getCognome()).append("~");
        riga.append(componente.getRuolo());
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
