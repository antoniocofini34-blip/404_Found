package com.example.business;
import java.util.*;
import java.time.*;
import com.example.domain.Utente;
import com.example.domain.Playlist; 
import com.example.domain.GenereMusicale;
import com.example.domain.CodaRiproduzione; 
public interface UtenteService {
  void creaUtente(Utente utente) throws BusinessException; 
  void modificaUtente(Utente utente) throws BusinessException; 
  void eliminaUtente(Utente utente) throws BusinessException; 
  List<Utente> findAllUtenti() throws BusinessException; 
  Utente findUtenteByEmail(String email) throws BusinessException; 
  void aggiungiPlaylist(Utente utente, Playlist playlist) throws BusinessException; 
  void rimuoviPlaylist(Utente utente, Playlist playlist) throws BusinessException; 
  List<Playlist> getPlaylist(Utente utente) throws BusinessException; 
  void aggiungiPreferenza(Utente utente, GenereMusicale genere) throws BusinessException; 
  void rimuoviPreferenza(Utente utente, GenereMusicale genere) throws BusinessException; 
  Set<GenereMusicale> getPreferenze(Utente utente) throws BusinessException; 
  CodaRiproduzione getCodaRiproduzione(Utente utente) throws BusinessException; 
  void setCodaRiproduzione(Utente utente, CodaRiproduzione coda) throws BusinessException; 
}
