package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
public class DuplicatoException extends BusinessException {
  public DuplicatoException() { super(); }
  public DuplicatoException(String message) { super(message); }
  public DuplicatoException(String message, Throwable cause) { super(message, cause); }
  public DuplicatoException(Throwable cause) { super(cause); }
}
