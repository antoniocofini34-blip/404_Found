import java.util.*;
import java.time.*;
package com.example.domain;

public class Ascolto {
  private Brano brano_ascoltato;
  private LocalDateTime istante_ascolto;
  private int tempo_brano_ascoltato;
  public Ascolto(Brano brano_ascoltato, LocalDateTime istante_ascolto, int tempo_brano_ascoltato){
    this.brano_ascoltato=brano_ascoltato;
    this.istante_ascolto=istante_ascolto;
    this.tempo_brano_ascoltato=tempo_brano_ascoltato;
  }
  public String getBrano(){
    return brano_ascoltato;
  }
  public LocalDateTime getIstanteAscolto(){
    return istante_ascolto;
  }
  public int getTempoBranoAscoltato(){
    return tempo_brano_ascoltato;
  }
  public void setBrano(Brano brano_ascoltato){
    this.brano_ascoltato=brano_ascoltato;
  }
  public void setIstanteAscolto(LocalDateTime istante_ascolto) {
    this.istante_ascolto=istante_ascolto; 
  }
  public void setTempoBranoAscoltato(int tempo_brano_ascoltato) {
    this.tempo_brano_ascoltato=tempo_brano_ascoltato;
  }
}
