 package com.example.busines;
 import java.util.*;
 import com.example.domain.CodaRiproduzione;
  public void aggiungiBranoCoda(Brano brano) {
    this.lista_coda_brani.add(brano);
  }
  public void rimuoviBranoCoda(Brano: brano) {
    this.lista_coda_brani.remove(brano);
  }
  public boolean svuotaCoda() {
    this.lista_coda_brani.clear();
  }
  public void impostaRepeatCoda(Modalita_Repeat modalita_repeat) {
    this.modalita_repeat = modalita_repeat;
  }
  public void branoTerminato() {
    if (modalita_repeat == Modalita_Repeat.RIPETI_BRANO) {
        play();
    } else {
        avanti();
    }
}
