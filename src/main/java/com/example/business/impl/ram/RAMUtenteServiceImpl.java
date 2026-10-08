package com.example.business.impl.ram;
import java.util.*;
import com.example.business.BusinessException;
import com.example.business.UtenteService;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.business.exception..UtenteNonTrovatoException;
import com.example.domain.GenereMusicale;
import com.example.domain.Playlist;
import com.example.domain.Utente;
import com.example.domain.CodaRiproduzione; 
public class RAMUtenteServiceImpl implements UtenteService{
  private List<Utente> utenti;
  public RAMUtenteServiceImpl() { this.utenti=new ArrayList<>(); }
  @Override
  public void creaUtente(Utente utente) throws BusinessException {
    if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (utente.getNome() == null || utente.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'utente non può essere vuoto");
    if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) throw new OperazioneNonValidaException("Il cognome dell'utente non può essere vuoto"); 
    if (utente.getEmail() == null || utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota"); 
    if (utente.getDataDiNascita() == null) throw new OperazioneNonValidaException("La data di nascita dell'utente non può essere null");
    for (Utente u: this.utenti) {
      if(u.getEmail().equalsIgnoreCase(utente.getEmail().trim()) throw new DuplicatoException("Esiste già un utente con l'email: " + utente.getEmail());
    }
    this.utenti.add(utente);
  }
  @Override
  public void modificaUtente(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (utente.getNome() == null || utente.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'utente non può essere vuoto");
    if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) throw new OperazioneNonValidaException("Il cognome dell'utente non può essere vuoto"); 
    if (utente.getEmail() == null || utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota"); 
    if (utente.getDataDiNascita() == null) throw new OperazioneNonValidaException("La data di nascita dell'utente non può essere null");
    boolean trovato = false;
    for (int i=0; i<this.utenti.size(); i++) {
      Utente utenteEsistente = this.utente.get(i);
      if(utenteEsistente == utente) { this.utenti.set(i, utente);
        trovato = true;
        break;
      }
    }
    if (!trovato) throw new UtenteNonTrovatoException("Utente da modificare non trovato");
  }
  @Override
  public void eliminaUtente(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (!this.utenti.remove(utente)) throw new UtenteNonTrovatoException("Utente da eliminare non trovato");
  }
  @Override
  public List<Utente> findAllUtenti() throws BusinessException { return new ArrayList<>(this.utenti); }
  @Override
  public Utente findUtenteByEmail(String email) throws BusinessException {
    if (email == null || email.trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non è valida");
    for (Utente utente: this.utenti) {
      if (utente.getEmail().equalsIgnoreCase(email.trim())) return utente;
    }
    throw new UtenteNonTrovatoException("Utente non trovato: " + email);
  }
  @Override
  public void aggiungiPlaylist(Utente utente, Playlist playlist) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (playlist==null) throw new OperazioneNonValidaException("La playlist non può essere null");
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
    List<Playlist> playlistUtente= utente.getPlaylistCreate();
    if(playlistUtente.contains(playlist)) throw new DuplicatoException("La playlist è già presente nell'utente");
    playlistUtente.add(playlist);
    utente.setPlaylistCreate(playlistUtente);
  }
  @Override public void rimuoviPlaylist(Utente utente, Playlist playlist) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (playlist==null) throw new OperazioneNonValidaException("La playlist non può essere null");
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
    List<Playlist> playlistUtente=utente.getPlaylistCreate();
    if(!playlistUtente.remove(playlist)) throw new OperazioneNonValidaException("La playlist non è presente nell'utente");
    utente.setPlaylistCreate(playlistUtente);
  }
  @Override
  public List<Playlist> getPlaylist(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
    return new ArrayList<>(utente.getPlaylistCreate());
  }
  @Override
  public void aggiungiPreferenza(Utente utente, GenereMusicale genere) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (genere==null) throw new OperazioneNonValidaException("Il genere musicale non può essere null");
    if(!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
    Set<GenereMusicale> preferenze = utente.getGeneriMusicali();
    if(preferenze.contains(genere)) throw new DuplicatoException("Il genere è già presente nelle preferenze");
    preferenze.add(genere);
    utente.setPreferenzeMusicali(preferenze);
  }
  @Override
  public void rimuoviPreferenza(Utenter utente, GenereMusicale genere) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (genere==null) throw new OperazioneNonValidaException("Il genere musicale non può essere null");
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
    Set<GeenereMusicale> preferenze=utente.getGeneriMusicali();
    if(!preferenze.remove(genere)) throw new OperazioneNonValidaException("Il genere non è presente nelle preferenze");
    utente.setPreferenzeMusicali(preferenze);
  }
  @Override
  public Set<GenereMusicale> getPreferenze(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato");
      return new HashSet<>(utente.getGeneriMusicali());
    }
  @Override
  public CodaRiproduzione getCodaRiproduzione(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null"); 
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato"); 
    return utente.getCodaRiproduzione(); 
  }
  @Override 
  public void setCodaRiproduzione(Utente utente, CodaRiproduzione coda) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere null"); 
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato"); 
    if (coda==null) throw new OperazioneNonValidaException("La coda di riproduzione non può essere null"); 
    utente.setCodaRiproduzione(coda);     
  }  
}
