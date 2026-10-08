package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
public class AlbumNonTrovatoException extends BusinessException {
  public AlbumNonTrovatoException() { super(); }
  public AlbumNonTrovatoException(String message) { super(message); }
  public AlbumNonTrovatoException(String message, Throwable cause) { super(message, cause); }
  public AlbumNonTrovatoException(Throwable cause) { super(cause); }
}
