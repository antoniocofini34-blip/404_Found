package com.example.controller;
import com.example.domain.Utente;
public class UtenteAttivo {
  private static Utente utente;
  public static Utente getUtente() { return utente; }
  public static void setUtente(Utente nuovoUtente) { utente=nuovoUtente; }
  public static void rimuoviUtente() { utente=null; }
}
