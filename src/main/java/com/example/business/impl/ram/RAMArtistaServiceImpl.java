package com.example.business.impl.ram; 
import java.util.*; 
import com.example.business.ArtistaService; 
import com.example.business.exception.BusinessException;
import com.example.business.exception.ArtistaNonTrovatoException; 
import com.example.business.exception.DuplicatoException; 
import com.example.business.exception.OperazioneNonValidaException;  
import com.example.domain.Artista; 
import com.example.domain.ArtistaGruppo; 
import com.example.domain.ArtistaSolista;
import com.example.domain.Componente;
public class RAMArtistaServiceImpl implements ArtistaService { private List<Artista> artisti; 
  public RAMArtistaServiceImpl() { this.artisti=new ArrayList<>(); }
  @Override 
  public List<Artista> findAllArtisti() { return new ArrayList<>(this.artisti); }
  @Override 
  public Artista findArtistaByNome(String nome) throws BusinessException {
  if (nome==null || nome.trim().isEmpty()) { throw new OperazioneNonValidaException("Il nome dell'artista non è valido"); }
    for (Artista artista : this.artisti) {
      if (artista instanceof ArtistaSolista) { ArtistaSolista solista=(ArtistaSolista) artista; 
        if (solista.getNomeArte().equalsIgnoreCase(nome.trim())) { return solista; }
      }
      else if (artista instanceof ArtistaGruppo) { ArtistaGruppo gruppo=(ArtistaGruppo) artista; 
        if (gruppo.getNomeGruppo().equalsIgnoreCase(nome.trim())) { return gruppo; }
      }
    }
    throw new ArtistaNonTrovatoException("Artista non trovato:" + nome); 
  }
  @Override 
  public void creaArtista(Artista artista) throws BusinessException {
    if (artista==null) { throw new OperazioneNonValidaException("L'artista non può essere null"); }
    String nome=ottieniNome(artista);   
    if (nome==null || nome.trim().isEmpty()) { throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto"); }
    for (Artista a : this.artisti) {
      String nomeEsistente=ottieniNome(a); 
      if (nomeEsistente!=null && nomeEsistente.equalsIgnoreCase(nome.trim())) { throw new DuplicatoException("Esiste già un artista con il nome:" + nome); }
    }
    this.artisti.add(artista); 
  }
  @Override 
  public void modificaArtista(Artista artista) throws BusinessException {
    if (artista==null) { throw new OperazioneNonValidaException("L'artista non può essere null"); }
    String nome=ottieniNome(artista); 
    if (nome==null || nome.trim().isEmpty()) { throw new OperazioneNonValidaException("Il nome dell'artista non può essere vuoto"); }
    boolean trovato=false; 
    for (int i=0; i<this.artisti.size(); i++) {
      Artista artistaEsistente=this.artisti.get(i); 
      if (artistaEsistente==artista) { this.artisti.set(i, artista); 
        trovato=true; 
        break; 
      }
    }
    if (!trovato) { throw new ArtistaNonTrovatoException("Artista da modificare non trovato"); }
  }
  @Override 
  public void eliminaArtista(Artista artista) throws BusinessException {
    if (artista==null) { throw new OperazioneNonValidaException("L'artista non può essere null"); }
    if (!this.artisti.remove(artista)) { throw new ArtistaNonTrovatoException("Artista da eliminare non trovato"); }
  }
  @Override 
  public void aggiungiComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException {
    if (gruppo==null) { throw new OperazioneNonValidaException("Il gruppo non può essere null"); }
    if (componente==null) { throw new OperazioneNonValidaException("Il componente non può essere null"); }
    if (!this.artisti.contains(gruppo)) { throw new ArtistaNonTrovatoException("Gruppo non trovato"); }
    if (componente.getNome()==null || componente.getNome().trim().isEmpty() || componente.getCognome()==null || componente.getCognome().trim().isEmpty() || componente.getRuolo()==null || componente.getRuolo().trim().isEmpty()) throw new OperazioneNonValidaException("Nome, cognome e ruolo del componente sono obbligatori.");
    List<Componente> componenti=gruppo.getListaMembri();
    for (Componente esistente : componenti) {
      if (esistente.getNome().equalsIgnoreCase(componente.getNome().trim()) && esistente.getCognome().equalsIgnoreCase(componente.getCognome().trim())) throw new DuplicatoException("Il componente è già presente nel gruppo.");
    }
    componenti.add(componente); 
    gruppo.setListaMembri(componenti);
  }
  @Override 
  public void rimuoviComponente(ArtistaGruppo gruppo, Componente componente) throws BusinessException {
    if (gruppo==null) { throw new OperazioneNonValidaException("Il gruppo non può essere null"); }
    if (componente==null) { throw new OperazioneNonValidaException("Il componente non può essere null"); }
    if (this.artisti.contains(gruppo)) { throw new ArtistaNonTrovatoException("Gruppo non trovato"); }
    List<Componente> componenti=gruppo.getListaMembri(); 
    if (!componenti.remove(componente)) { throw new OperazioneNonValidaException("Il componente non è presente nel gruppo"); }
    gruppo.setListaMembri(componenti); 
  }
  private String ottieniNome(Artista artista) {
    if (artista instanceof ArtistaSolista) { ArtistaSolista solista=(ArtistaSolista) artista; 
      return solista.getNomeArte(); 
    }
    else if (artista instanceof ArtistaGruppo) { ArtistaGruppo gruppo=(ArtistaGruppo) artista; 
      return gruppo.getNomeGruppo(); 
    }
    return null; 
  }
}   
