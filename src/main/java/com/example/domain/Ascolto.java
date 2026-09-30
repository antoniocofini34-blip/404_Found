import java.util.*;
import java.time.*;
package com.example.domain;

public class Ascolto{
  private Brano brano_Ascoltato;
  private LocalDateTime istanteAscolto;
  private int tempo_brano_ascoltato;
    public Ascolto(Brano brano, LocalDateTime istanteAscolto, int tempo_brano_ascoltato){
    this.brano=new Brano();
    this.istanteAscolto=istanteAscolto;
    this.tempo_brano_ascoltato=tempo_brano_ascoltato;
  }
  public String getBrano(){
    return brano_Ascoltato;
  }
  public LocalDateTime getIstanteAscolto(){
    return istanteAscolto;
  }
  public int getTempoBranoAscoltato(){
    return tempo_brano_ascoltato;
  }
  public void setBrano(Brano brano){
    this.brano_Ascoltato=brano;
  }
  public void setIstanteAscolto(LocalDateTime istanteAscolto) {
    this.istanteAscolto=istanteAscolto; 
  }
  public void setTempoBranoAscoltato(int tempo_brano_ascoltato) {
    this.tempo_brano_ascoltato=tempo_brano_ascoltato;
  }
}
