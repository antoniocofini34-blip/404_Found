package com.example.business;
import java.util.*;
import com.example.business.exception.BusinessException;
import com.example.domain.Utente;
import com.example.domain.CodaRiproduzione; 
public interface UtenteService {
  void creaUtente(Utente utente) throws BusinessException; 
  void modificaUtente(Utente utente) throws BusinessException; 
  void eliminaUtente(Utente utente) throws BusinessException; 
  List<Utente> findAllUtenti() throws BusinessException; 
  Utente findUtenteByEmail(String email) throws BusinessException; 
  CodaRiproduzione getCodaRiproduzione(Utente utente) throws BusinessException;
  void setCodaRiproduzione(Utente utente, CodaRiproduzione coda) throws BusinessException; 
}
