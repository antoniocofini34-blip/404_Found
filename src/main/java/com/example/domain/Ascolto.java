package com.example.domain;
import java.time.*;
public class Ascolto {
  private Brano brano_Ascoltato;
  private LocalDateTime istante_ascolto;
  private int tempo_brano_ascoltato;
  public Ascolto(Brano brano_ascoltato, LocalDateTime istante_ascolto, int tempo_brano_ascoltato){
    this.brano_ascoltato=brano_ascoltato;
    this.istante_ascolto=istante_ascolto;
    this.tempo_brano_ascoltato=tempo_brano_ascoltato;
  }
  public Brano getBrano() { return brano_ascoltato; }
  public void setBrano(Brano brano_ascoltato) { this.brano_ascoltato=brano_ascoltato; }
}
