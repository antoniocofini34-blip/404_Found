package com.example.controller;
import com.example.domain.Utente;
public class UtenteAttivo {
  private static Utente utente;
  private static boolean amministratore=false;
  private static final String EMAIL_AMMINISTRATORE="admin@univaqsound.it";
  private static final String PASSWORD_AMMINISTRATORE="404Found";
  public UtenteAttivo() {}
  public static Utente getUtente() { return utente; }
  public static void setUtente(Utente nuovoUtente) { utente=nuovoUtente; }
  public static boolean isAmministratore() { return amministratore; }
  public static boolean accediAmministratore(String email, String password) {
    if (email==null||password==null) return false;
    if (EMAIL_AMMINISTRATORE.equalsIgnoreCase(email.trim())&&PASSWORD_AMMINISTRATORE.equals(password)) {
      utente=null;
      amministratore=true;
      return true;
    }
    return false;
  }
  public static void accediComeUtente(Utente nuovoUtente) {
    amministratore=false;
    utente=nuovoUtente;
  }
}
