package com.example.business.exception;
import com.example.business.BusinessException;
@SuppressWarnings("serial")
  public class PlaylistVuotaException extends BusinessException {
    public PlaylistVuotaException() { super(); }
    public PlaylistVuotaException(String message) { super(message); }
    public PlaylistVuotaException(String message, Throwable cause) { super(message, cause); }
    public PlaylistVuotaException(Throwable cause) { super(cause); }
  }
