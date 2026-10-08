package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
public class UtenteNonTrovatoException extends BusinessException {
  public UtenteNonTrovatoException() { super(); }
  public UtenteNonTrovatoException(String message) { super(message); }
  public UtenteNonTrovatoException(String message, Throwable cause) { super(message, cause); }
  public UtenteNonTrovatoException(Throwable cause) { super(cause); }
}
