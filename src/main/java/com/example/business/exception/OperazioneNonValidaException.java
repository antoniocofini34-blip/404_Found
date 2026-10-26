package com.example.business.exception;
@SuppressWarnings("serial")
public class OperazioneNonValidaException extends BusinessException {
  public OperazioneNonValidaException() { super(); }
  public OperazioneNonValidaException(String message) { super(message); }
  public OperazioneNonValidaException(String message, Throwable cause) { super(message, cause); }
  public OperazioneNonValidaException(Throwable cause) { super(cause); }
}
