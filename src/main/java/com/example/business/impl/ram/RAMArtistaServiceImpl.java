package com.example.business.impl.ram; 
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

public class RAMArtistaServiceImpl implements ArtistaService {
  private List<Artista> artisti; 
  public RAMArtistaServiceImpl() {
    this.artisti=new ArrayList<>(); 
  }

  @Override 
  public List<Artista> findAllArtisti() throws BusinessException {
    return new ArrayList<>(this.artisti); 
  }
  @Override 
  public Artista findArtistaByNome(String nome) throws BusinessException {
  if (nome==null || nome.trim().isEmpty()) {
  throw new OperazioneNonValidaException("Il nome dell'artista non è valido"); 
    }
    for (Artista artista : this.artisti) {
      if (artista instanceof ArtistaSolista) {
        ArtistaSolista solista=(ArtistaSolista) artista; 
        if (solista.getNomeArte().equalsIgnoreCase(nome.trim())) {
          return solista; 
        }
      }
      else if (artista instanceof ArtistaGruppo) {
        ArtistaGruppo gruppo=(ArtistaGruppo) artista; 
        if (gruppo.getNomeGruppo().equalsIgnoreCase(nome.trim())) {
          return gruppo; 
        }
      }
    }
    throw new ArtistaNonTrovatoException("Artista non trovato:" + nome); 
  }
  @Override 
  public void creaArtista(Artista artista) throws BusinessException {
    if (artista==null) {
      throw new OperazioneNonValidaException("L'artista non può essere null"); 
    }
    String nome=ottieniNome(artista);   
    if (nome==null || nome.trim().isEmpty()) {
      throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto"); 
    }
    for (Artista a : this.artisti) {
      String nomeEsistente=ottieniNome(a); 
      if (nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) {
        throw new DuplicatoException("Esiste già un artista con il nome:" + nome); 
      }
    }
    this.artisti.add(artista); 
  }
  @Override 
  public void modificaArtista(Artista artista) throws BusinessException {
    if (artista==null) {
      throw new OperazioneNonValidaException("L'artista non può essere null"); 
    }
    String nome=ottieniNome(artista); 
    if (nome==null || nome.trim().isEmpty()) {
      throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto"); 
    }
    boolean trovato=false; 
    for (int i=0; i<this.artisti.size(); i++) {
      Artista artistaEsistente=this.artisti.get(i); 
      if (artistaEsistente==artista) {
        this.artisti.set(i, artista); 
        trovato=true; 
        break; 
      }
    }
    if (!trovato) {
      throw new ArtistaNonTrovatoException("Artista da modificare non trovato"); 
    }
  }
  @Override 
  public void eliminaArtista(Artista artista) throws BusinessException {
    if (artista==null) {
      throw new OperazioneNonValidaException("L'artista non può essere null");
    }
    if (!this.artisti.remove(artista)) {
      throw new ArtistaNonTrovatoException("Artista da eliminare non trovato"); 
    }
  }
  @Override 
  public List<Album> findAlbumArtista(Artista artista) throws BusinessException {
    if (artista==null) {
      throw new OperazioneNonValidaException("L'artista non può essere null"); 
    }
    if (!this.artisti.contains(artista)) {
      throw new ArtistaNonTrovatoException("Artista non trovato"); 
    }
    return new ArrayList<>(artista.getDiscografia()); 
  }
  @Override 
  public void aggiungiComponente(ArtistaGruppo gruppo, Artista componente) throws BusinessException {
    if (gruppo==null) {
      throw new OperazioneNonValidaException("Il gruppo non può essere null"); 
    }
    if (componente==null) {
      throw new OperazioneNonValidaException("Il componente non può essere null"); 
    }
    if (!this.artisti.contains(gruppo)) {
      throw new ArtistaNonTrovatoException("Gruppo non trovato"); 
    }
    if (gruppo==componente) {
      throw new OperazioneNonValidaException("Un gruppo non può essere componente di sè stesso"); 
    }
    if (gruppo.getListaMembri().contains(componente)) {
      throw new DuplicatoException("Il componente è già presente nel gruppo"); 
    }
    List<Artista> componenti=gruppo.getListaMembri(); 
    componenti.add(componente); 
    gruppo.setListaMembri(componenti); 
  }
  @Override 
  public void rimuoviComponente(Artista Gruppo, Artista componente) throws BusinessException {
    if (gruppo==null) {
      throw new OperazioneNonValidaException("Il gruppo non può essere null"); 
    }
    if (componente==null) {
      throw new OperazioneNonValidaException("Il componente non può essere null"); 
    }
    if (!this.artisti.contains(gruppo)) {
      throw new ArtistaNonTrovatoException("Gruppo non trovato"); 
    }
    List<Artista> componenti=gruppo.getListaMembri(); 
    if (!componenti.remove(componente)) {
      throw new OperazioneNonValidaException("Il componente non è presente nel gruppo"); 
    }
    gruppo.setListaMembri(componenti); 
  }
  @Override 
  public List<Artista> findComponenti(ArtistaGruppo gruppo) throws BusinessException {
    if (gruppo==null) {
      throw new OperazioneNonValidaException("Il gruppo non può essere null"); 
    }
    if (!this.artisti.contains(gruppo)) {
      throw new ArtistaNonTrovatoException("Gruppo non trovato"); 
    }
    return new ArrayList<>(gruppo.getListaMembri()); 
  }
  private String ottieniNome(Artista artista) {
    if (artista instanceof ArtistaSolista) {
      ArtistaSolista solista=(ArtistaSolista) artista; 
      return solista.getNomeArte(); 
    }
    else if (artista instanceof ArtistaGruppo) {
      ArtistaGruppo gruppo=(ArtistaGruppo) artista; 
      return gruppo.getNomeGruppo(); 
    }
    return null; 
  }
}
    
    
    

  
    
    
    

 
    
    

    

    
  
  


