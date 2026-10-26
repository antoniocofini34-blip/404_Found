package com.example.business.impl.ram;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.business.UtenteService;
import com.example.business.exception.DuplicatoException;
import com.example.business.exception.OperazioneNonValidaException;
import com.example.business.exception.UtenteNonTrovatoException;
import com.example.domain.GenereMusicale;
import com.example.domain.Playlist;
import com.example.domain.Utente;
import com.example.domain.CodaRiproduzione; 
public class RAMUtenteServiceImpl implements UtenteService{ private List<Utente> utenti;
  public RAMUtenteServiceImpl() { this.utenti=new ArrayList<>(); }
  @Override
  public void creaUtente(Utente utente) throws BusinessException {
    if (utente == null) throw new OperazioneNonValidaException("L'utente non può essere null");
    if (utente.getNome() == null || utente.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'utente non può essere vuoto");
    if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) throw new OperazioneNonValidaException("Il cognome dell'utente non può essere vuoto"); 
    if (utente.getEmail() == null || utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota"); 
    if (utente.getDataDiNascita() == null) throw new OperazioneNonValidaException("La data di nascita dell'utente non può essere vuota");
    for (Utente u : this.utenti) {
      if(u.getEmail().equalsIgnoreCase(utente.getEmail().trim())) throw new DuplicatoException("Esiste già un utente con l'email: " + utente.getEmail());
    }
    this.utenti.add(utente);
  }
  @Override
  public void modificaUtente(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere vuoto");
    if (utente.getNome() == null || utente.getNome().trim().isEmpty()) throw new OperazioneNonValidaException("Il nome dell'utente non può essere vuoto");
    if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) throw new OperazioneNonValidaException("Il cognome dell'utente non può essere vuoto"); 
    if (utente.getEmail() == null || utente.getEmail().trim().isEmpty()) throw new OperazioneNonValidaException("L'email dell'utente non può essere vuota"); 
    if (utente.getDataDiNascita() == null) throw new OperazioneNonValidaException("La data di nascita dell'utente non può essere vuota");
    boolean trovato = false;
    for (int i=0; i<this.utenti.size(); i++) {
      Utente utenteEsistente = this.utenti.get(i);
      if(utenteEsistente == utente) { this.utenti.set(i, utente);
        trovato = true;
        break;
      }
    }
    if (!trovato) throw new UtenteNonTrovatoException("L'utente da modificare non è stato trovato");
  }
  @Override
  public void eliminaUtente(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere vuoto");
    if (!this.utenti.remove(utente)) throw new UtenteNonTrovatoException("L'utente da eliminare non è stato trovato");
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
  public CodaRiproduzione getCodaRiproduzione(Utente utente) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere vuoto"); 
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato"); 
    return utente.getCodaRiproduzione(); 
  }
  @Override 
  public void setCodaRiproduzione(Utente utente, CodaRiproduzione coda) throws BusinessException {
    if (utente==null) throw new OperazioneNonValidaException("L'utente non può essere vuoto"); 
    if (!this.utenti.contains(utente)) throw new UtenteNonTrovatoException("Utente non trovato"); 
    if (coda==null) throw new OperazioneNonValidaException("La coda di riproduzione non può essere vuota"); 
    utente.setCodaRiproduzione(coda);     
  }  
}
