import java.util.*;
package com.example.domain;

public class CodaRiproduzione implements Riproducibile {
  private List<Brano> lista_coda_brani;
  private boolean riproduzione_casuale;
  private Modalita_Repeat modalita_repeat;
  private Stato_Riproduzione stato;
  public CodaRiproduzione(List<Brano> lista_coda_brani, boolean riproduzione_casuale, Modalita_Repeat modalita_repeat, Stato_Riproduzione stato) {
    this.lista_coda_brani=new ArrayList<>();
    this.riproduzione_casuale=riproduzione_casuale;
    this.modalita_repeat=modalita_repeat;
    this.stato=stato;
  }
  public List<Brano> getListaCodaBrani() {
    return new ArrayList<>(this.lista_coda_brani); 
  }
  public boolean getCasuale() {
    return riproduzione_casuale;
  }
  public Modalita_Repeat getRepeat() {
    return modalita_repeat;
  }
  public Stato_Riproduzione getStato() {
    return stato;
  }
  public void setListaCodaBrani(List<Brano> lista_coda_brani) {
    if (lista_coda_brani!=null) this.lista_coda_brani=new ArrayList<>(lista_coda_brani); 
    else this.lista_coda_brani=new ArrayList<>(); 
  }
  public void setShuffle(boolean shuffle) {
    this.riproduzione_casuale=shuffle;
  }
  public void setModalitaRepeat(Modalita_Repeat modalitaRepeat) {
    this.modalita_repeat=modalitaRepeat;
  }
  public void setStato(Stato_Riproduzione stato) {
    this.stato=stato;
  }
}
    





    

