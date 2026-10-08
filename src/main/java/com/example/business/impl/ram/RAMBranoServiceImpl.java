package com.example.business.impl.ram; 
import java.util.*; 
import com.example.business.BranoService; 
import com.example.business.BusinessException; 
import com.example.business.exception.BranoNonTrovatoException;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.domain.Album; 
import com.example.domain.Brano; 
import com.example.domain.GenereMusicale;
public class RAMBranoServiceImpl implements BranoService { private List<Brano> brani; 
  public RAMBranoServiceImpl() { this.brani=new ArrayList<>(); }
  @Override
  public List<Brano> findAllBrani() throws Business Exception { return new ArrayList<>(this.brani); }
  @Override 
  public Brano findBranoByTitolo(String titolo) throws BusinessException {
    if (titolo==null || titolo.trim().isEmpty()) throw new OperazioneNonValidaException("Il titolo del brano non è valido"); 
    for (Brano brano : this.brani) {
      if (brano.getTitolo().equalsIgnoreCase(titolo.trim())) return brano;
    }
    throw new BranoNonTrovatoException("Brano non trovato:" + titolo); 
  }
  @Override 
  public void creaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (brano.getTitolo()==null || brano.getTitolo().trim().isEmpty()) throw new OperazioneNOnValidaException("Il titolo del brano non può essere vuoto"); 
    if (brano.getDurata()<=0) throw new OperazioneNonValidaException("La durata del brano deve essere maggiore di zero"); 
    if (brano.getAlbumBrano()==null) throw new OperazioneNonValidaException("Ogni brano deve appartenere a un album"); 
    for (Brano b : this.brani) {
    if (b.getTitolo().equalsIgnoreCase(brano.getTitolo().trim())) throw new DuplicatoException("Esiste già un brano con il titolo:" + brano.getTitolo());
    }
  this.brani.add(brano); 
 }
  @Override
  public void modificaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null"); 
    if (brano.getTitolo()==null || brano.getTitolo().trim().isEmpty())  throw new OperazioneNonValidaException("Il titolo del brano non può essere vuoto"); 
    if (brano.getDurata()<=0)  throw new OperazioneNonValidaException("La durata del brano deve essere maggiore di zero"); 
    if (brano.getAlbumBrano()==null) throw new OperazioneNonValidaException("Ogni brano deve appartenere a un album"); 
    boolean trovato=false; 
    for (int i=0; i<this.brani.size(); i++) {
      Brano branoEsistente=this.brani.get(i); 
      if (branoEsistente==brano) { this.brani.set(i, brano); 
        trovato=true; 
        break;
      }
    }
    if (!trovato) throw new BranoNonTrovatoException("Brano da modificare non trovato"); 
  }
  @Override 
  public void eliminaBrano(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null");
    if (!this.brani.remove(brano)) throw new BranoNonTrovatoException("Brano da eliminare non trovato"); 
  }
  @Override 
  public void assegnaAlbum(Brano brano, Album album) throws BusinessException {
    if (brano==null) throw new OperazioneNOnValidaException("Il brano non può essere null"); 
    if (album==null) throw new OperazioneNonValidaException("L'album non può essere null"); 
    if (!this.brani.contains(brano)) throw new BranoNonTrovatoException("Brano non trovato"); 
    brano.setAlbumBrano(album); 
  }
  @Override 
  public void modificaGenere(Brano brano, Set<GenereMusicale> generi) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null"); 
    if (!this.brani.contains(brano)) throw new BranoNonTrovatoException("Brano non trovato"); 
    if (generi==null) throw new OperazioneNonValidaException("L'insieme dei generi non può essere null"); 
    brano.setGenereBrano(new HashSet<>(generi)); 
  }
  @Override 
  public void aggiornaNumeroAscolti(Brano brano) throws BusinessException {
    if (brano==null) throw new OperazioneNonValidaException("Il brano non può essere null"); 
    if (!this.brani.contains(brano)) throw new BranoNonTrovatoException("Brano non trovato"); 
    int numeroAscolti=brano.getNumeroAscolti(); 
    brano.setNumeroAscolti(numeroAscolti+1); 
  }
}
