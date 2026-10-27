package com.example.business.exception;
@SuppressWarnings("serial")
  public class ArtistaNonTrovatoException extends BusinessException {
    public ArtistaNonTrovatoException() { super(); }
    public ArtistaNonTrovatoException(String message) { super(message); }
    public ArtistaNonTrovatoException(String message, Throwable cause) { super(message, cause); }
    public ArtistaNonTrovatoException(Throwable cause) { super(cause); }
  }
