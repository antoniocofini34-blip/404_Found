import java.util.*;
package com.example.domain;

public class CodaRiproduzione implements Riproducibile {
  private List<Brano> lista_coda_brani;
  private boolean riproduzione_casuale;
  private ModalitaRepeat modalita_repeat;
  private StatoRiproduzione stato;
  public CodaRiproduzione(List<Brano> lista_coda_brani, boolean riproduzione_casuale, ModalitaRepeat modalita_repeat, StatoRiproduzione stato) {
    this.lista_coda_brani=new ArrayList<>();
    this.riproduzione_casuale=riproduzione_casuale;
    this.modalita_repeat=new ModalitaRepeat();
    this.stato=new StatoRiproduzione();
  }
  public List<Brano> getListaCodaBrani() {
    return new ArrayList<>(this.lista_coda_brani); 
  }
  public boolean getCasuale() {
    return riproduzione_casuale;
  }
  public ModalitaRepeat getRepeat() {
    return modalita_repeat;
  }
  public StatoRiproduzione getStato() {
    return stato;
  }
  public void setListaCodaBrani(List<Brano> lista_coda_brani) {
    if (lista_coda_brani!=null) this.lista_coda_brani=new ArrayList<>(lista_coda_brani); 
    else this.lista_coda_brani=new ArrayList<>(); 
  }
  public void setShuffle(boolean shuffle) {
    this.riproduzione_casuale=shuffle;
  }
  public void setModalitaRepeat(ModalitaRepeat modalitaRepeat) {
    this.modalita_repeat=modalitaRepeat;
  }
  public void setStato(StatoRiproduzione stato) {
    this.stato=stato;
  }
}
    





    

