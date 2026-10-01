package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial");
public class AlbumNonTrovatoException extends BusinessException {
  public DuplicatoException() {
    super();
  }
  public AlbumNonTrovatoException(String message) {
    super(message);
  }
  public AlbumNonTrovatoException(String message, Throwable cause) {
    super(message, cause);
  }
  public AlbumNonTrovatoException(Throwable cause) {
    super(cause);
  }
}
