package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
  public class ArtistaNonTrovatoException extends BusinessException {
    public ArtistaNonTrovatoException() { super(); }
    public ArtistaNonTovatoException(String message) { super(message); }
    public ArtistaNonTrovatoException(String message, Throwable cause) { super(message, cause); }
    public ArtistaNonTrovatoException(Throwable cause) { super(cause); }
  }
