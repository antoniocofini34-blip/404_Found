package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
  public class BranoNonTrovatoException extends BusinessException {
    public BranoNonTrovatoException() {
      super();
    }
    public BranoNonTrovatoException(String message) {
      super(message);
    }
    public BranoNonTrovatoException(String message, Throwable cause) {
      super(message, cause);
    }
    public BranoNonTrovatoException(Throwable cause) {
      super(cause);
    }
  }
