package com.example.business.impl.file;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.example.business.BranoService;
import com.example.business.exception.BusinessException;
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
    } catch (IOException e) { throw new BusinessException("Si è verificato un errore nella lettura dei brani", e); }
  }
  @Override
  public Brano findBranoByTitolo(String titolo) throws BusinessException {
    if (titolo==null||titolo.trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non è valido");
    List<Brano> brani=findAllBrani();
    for (Brano brano:brani) {
      if(brano.getTitolo()!=null && brano.getTitolo().equalsIgnoreCase(titolo.trim())) return brano;
    }
    throw new BranoNonTrovatoException("Il seguente brano non è stato trovato: " + titolo);
  }
  @Override
  public void creaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
    if (brano.getTitolo()==null || brano.getTitolo().trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non può essere vuoto");
    if (brano.getDurata()<0) throw new OperazioneNonValidaException("La durata del brano non può essere negativa");
    List<Brano> brani=findAllBrani();
    for (Brano b:brani) {
      if (b.getTitolo()!=null && b.getTitolo().equalsIgnoreCase(brano.getTitolo().trim())) throw new DuplicatoException("Brano già esistente con il seguente titolo: " + brano.getTitolo());
    }
    brani.add(brano);
    scriviBrani(brani);
  }
  @Override
  public void modificaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
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
    if (!trovato) throw new BranoNonTrovatoException("Il brano da modificare non è stato trovato");
    scriviBrani(brani);
  }
  @Override
  public void eliminaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
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
    if (!rimosso) throw new BranoNonTrovatoException("il brano da eliminare non è stato trovato");
    scriviBrani(brani);
  }
  @Override
  public void aggiornaNumeroAscolti(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere vuoto");
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
    if (riga==null||riga.length<5) return null;
    String titolo=riga[0];
    int durata;
    try { durata=Integer.parseInt(riga[1].trim());
    } catch (NumberFormatException e) { throw new BusinessException("La durata non è valida: " + riga[1]); }
    String testo=decodificaTesto(riga[2]);
    Set<GenereMusicale> generi=new HashSet<>();
    if (!riga[3].trim().isEmpty()) { String[] generiString=riga[3].split("\\|");
      for (String genere:generiString) {
        try { generi.add(GenereMusicale.valueOf(genere.trim()));
        } catch (IllegalArgumentException e) { throw new BusinessException("Il genere musicale non è valido: " + genere); }
      }
    }
    int numeroAscolti;
    try { numeroAscolti=Integer.parseInt(riga[4].trim());
    } catch (NumberFormatException e) { throw new BusinessException("il numero degli ascolti non è valido: " + riga[4]); }
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
      if (out.checkError()) throw new BusinessException("Si è verificato un errore durante il salvataggio dei brani");
    } catch (IOException e) { throw new BusinessException("Errore durante il salvataggio dei brani", e); }
  }
  private String codificaTesto(String testo) {
    if (testo==null||testo.isEmpty()) return "";
    return testo.replace("\\", "\\\\").replace("\r","\\r").replace("\n","\\n").replace(",","\\c");
  }
  private String decodificaTesto(String testo) {
    if (testo==null||testo.isEmpty()) return "";
    StringBuilder risultato=new StringBuilder();
    for (int i=0;i<testo.length();i++) {
      char carattere=testo.charAt(i);
      if (carattere=='\\' && i+1<testo.length()) {
        char successivo=testo.charAt(i+1);
        switch (successivo) {
          case '\\':
            risultato.append('\\');
            i++;
            break;
          case 'r':
            risultato.append('\r');
            i++;
            break;
          case 'n':
            risultato.append('\n');
            i++;
            break;
          case 'c':
            risultato.append(',');
            i++;
            break;
          default:
          risultato.append(carattere);
        }
    }  else risultato.append(carattere);
  }
    return risultato.toString();
}
  private String convertiBranoInRiga(Brano brano) { StringBuilder riga=new StringBuilder();
    riga.append(brano.getTitolo());
    riga.append(",");
    riga.append(brano.getDurata());
    riga.append(",");
    riga.append(codificaTesto(brano.getTesto()));
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
