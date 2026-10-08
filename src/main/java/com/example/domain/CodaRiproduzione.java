package com.example.domain;
import java.util.*;
public class CodaRiproduzione implements Riproducibile {
  private List<Brano> lista_coda_brani;
  private boolean shuffle;
  private ModalitaRepeat modalita_repeat;
  private StatoRiproduzione stato;
  public CodaRiproduzione(List<Brano> lista_coda_brani, boolean shuffle, ModalitaRepeat modalita_repeat, StatoRiproduzione stato) {
    if (lista_coda_brani!=null) this.lista_coda_brani=new ArrayList<>(lista_coda_brani);
    else this.lista_coda_brani=new ArrayList<>(); 
    this.shuffle=shuffle;
    this.modalita_repeat=modalita_repeat; 
    this.stato=stato; 
  }
  public List<Brano> getListaCodaBrani() { return new ArrayList<>(this.lista_coda_brani); }
  public boolean getShuffle() { return shuffle; }
  public ModalitaRepeat getRepeat() { return modalita_repeat; }
  public StatoRiproduzione getStato() { return stato; }
  public void setListaCodaBrani(List<Brano> lista_coda_brani) {
    if (lista_coda_brani!=null) this.lista_coda_brani=new ArrayList<>(lista_coda_brani); 
    else this.lista_coda_brani=new ArrayList<>(); 
  }
  public void setShuffle(boolean shuffle) { this.shuffle=shuffle; }
  public void setModalitaRepeat(ModalitaRepeat modalita_repeat) { this.modalita_repeat=modalita_repeat; }
  public void setStato(StatoRiproduzione stato) { this.stato=stato; }
}
