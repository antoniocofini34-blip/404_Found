package com.example.domain;
public class Ascolto { private Brano brano_ascoltato;
  public Ascolto(Brano brano_ascoltato){ this.brano_ascoltato=brano_ascoltato; }
  public Brano getBrano() { return brano_ascoltato; }
  public void setBrano(Brano brano_ascoltato) { this.brano_ascoltato=brano_ascoltato; }
}
