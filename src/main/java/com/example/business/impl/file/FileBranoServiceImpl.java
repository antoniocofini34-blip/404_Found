package com.example.business.impl.file;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.example.business.BranoService;
import com.example.business.BusinessException;
import com.example.business.exception.BranoNonTrovatoException;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album;
import com.example.domain.Brano;
import com.example.domain.GenereMusicale;
public class FileBranoServiceImpl implements BranoService { private String filename;
  public FileBranoServiceImpl(String filename) { this.filename=filename;}
  @Override
  public List<Brano> findAllBrani() throws BusinessException {
    try { FileData data=leggiDati();
      return convertiRighe(data);
    } catch (IOException e) { throw new BusinessException("Errore durante la lettura dei brani", e); }
  }
  @Override
  public Brano findBranoByTitolo(String titolo) throws BusinessException {
    if (titolo==null||titolo.trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non è valido");
    List<Brano> brani=findAllBrani();
    for (Brano brano:brani) {
      if(brano.getTitolo()!=null && brano.getTitolo().equalsIgnoreCase(titolo.trim())) return brano;
    }
    throw new BranoNonTrovatoException("Brano non trovato: " + titolo);
  }
  @Override
  public void creaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (brano.getTitolo()==null || brano.getTitolo().trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non può essere vuoto");
    if (brano.getDurata()<0) throw new OperazioneNonValidaException("La durata del brano non può essere negativa");
    List<Brano> brani=findAllBrani();
    for (Brano b:brani) {
      if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo().trim())) throw new DuplicatoException("Esiste già un brano con il titolo: " + brano.getTitolo());
    }
    brani.add(brano);
    scriviBrani(brani);
  }
  @Override
  public void modificaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (brano.getTitolo()==null||brano.getTitolo().trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non può essere vuoto");
    List<Brano> brani=findAllBrani();
    boolean trovato=false;
    for (int i=0;i<brani.size();i++) {
      Brano branoEsistente=brani.get(i);
      if (branoEsistente.getTitolo()!=null && branoEsistente.getTitolo().equalsIgnoreCase(brano.getTitolo().trim())) { brani.set(i, brano);
        trovato=true;
        break;
      }
    }
    if (!trovato) throw new BranoNonTrovatoException("Brano da modificare non trovato");
    scriviBrani(brani);
  }
  @Override
  public void eliminaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (brano.getTitolo()==null||brano.getTitolo().trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non è valido");
    List<Brano> brani=findAllBrani();
    boolean rimosso=false;
    for (int i=0;i<brani.size();i++) {
      Brano branoEsistente=brani.get(i);
      if (branoEsistente.getTitolo()!=null && branoEsistente.getTitolo().equalsIgnoreCase(brano.getTitolo().trim())) { brani.remove(i);
        rimosso=true;
        break;
      }
    }
    if (!rimosso) throw new BranoNonTrovatoException("Brano da eliminare non trovato");
    scriviBrani(brani);
  }
  @Override
  public void assegnaAlbum(Album album, Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (album==null) throw new OperazioneNonValidaException("L'album non può essere null");
    brano.setAlbumBrano(album);
    modificaBrano(brano);
  }
  @Override
  public void modificaGenere(Brano brano, Set<GenereMusicale> generi) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (generi==null) throw new OperazioneNonValidaException("I generi non possono essere null");
    brano.setGenereBrano(generi);
    modificaBrano(brano);
  }
  @Override
  public void aggiornaNumeroAscolti(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    brano.setNumeroAscolti(brano.getNumeroAscolti() +1);
    modificaBrano(brano);
  }
  private FileData leggiDati() throws IOException {
    File file = new File(this.filename);
    if (!file.exists()) { FileData data= new FileData();
      data.setContatore(0);
      data.setRighe(new ArrayList<>());
      return data;
    }
    return Utility.readAllRows(this.filename);
  }
  private List<Brano> convertiRighe(FileData data) throws BusinessException {
    List<Brano> brani = new ArrayList<>();
    for (String[] riga: data.getRighe()) {
      Brano brano=creaBranoDaRiga(riga);
      if (brano!=null) brani.add(brano);
    }
    return brani;
  }
  private Brano creaBranoDaRiga(String[] riga) throws BusinessException {
    if (riga==null||riga.length<6) return null;
    String titolo=riga[0];
    int durata;
    try { durata=Integer.parseInt(riga[1].trim());
    } catch (NumberFormatException e) { throw new BusinessException("Durata non valida: " + riga[1]); }
    String testo=riga[2];
    Set<GenereMusicale> generi=new HashSet<>();
    if (!riga[3].trim().isEmpty()) { String[] generiString=riga[3].split("\\|");
      for (String genere:generiString) {
        try { generi.add(GenereMusicale.valueOf(genere.trim()));
        } catch (IllegalArgumentException e) { throw new BusinessException("Genere musicale non valido: " + genere); }
      }
    }
    int numeroAscolti;
    try { numeroAscolti=Integer.parseInt(riga[4].trim());
    } catch (NumberFormatException e) { throw new BusinessException("Numero ascolti non valido: " + riga[4]); }
    Brano brano = new Brano(titolo, durata, testo, generi, null, numeroAscolti);
    return brano;
  }
  private void scriviBrani(List<Brano> brani) throws BusinessException {
    File file = new File(this.filename);
    File cartella=file.getParentFile();
    if (cartella!=null && !cartella.exists()) cartella.mkdirs();
    try (PrintWriter out= new PrintWriter(file)) { out.println(brani.size());
      for (Brano brano:brani) {
        out.println(convertiBranoInRiga(brano));
      }
    } catch (IOException e) { throw new BusinessException("Errore durante il salvataggio dei brani", e); }
  }
  private String convertiBranoInRiga(Brano brano) { StringBuilder riga=new StringBuilder();
    riga.append(brano.getTitolo());
    riga.append(",");
    riga.append(brano.getDurata());
    riga.append(",");
    riga.append(brano.getTesto());
    riga.append(",");
    String generi="";
    for (GenereMusicale genere: brano.getGenereBrano()) {
      if(generi.isEmpty()) generi+="|";
      generi+=genere.name();
    }
    riga.append(generi);
    riga.append(",");
    riga.append(brano.getNumeroAscolti());
    riga.append(",");
    if (brano.getAlbumBrano()!=null) riga.append(brano.getAlbumBrano().getTitolo());
    return riga.toString();
  }
}
