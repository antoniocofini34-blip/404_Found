package com.example.business.impl.ram; 
import java.util.*; 
import com.example.business.BusinessException; 
import com.example.business.CodaRiproduzioneService; 
import com.example.business.exception.OperazioneNonValidaException; 
import com.example.domain.Brano;
import com.example.domain.CodaRiproduzione; 
import com.example.domain.ModalitaRepeat; 
import com.example.domain.StatoRiproduzione; 
public class RAMCodaRiproduzioneServiceImpl implements CodaRiproduzioneService {
  private Map<CodaRiproduzione, Integer> indiciCorrenti; 
  public RAMCodaRiproduzioneServiceImpl() { this.indiciCorrenti=new HashMap<>(); }
  private void verificaCoda(CodaRiproduzione coda) throws BusinessException {
    if (coda==null) { throw new OperazioneNonValidaException("La coda non può essere null"); }
    if (!this.indiciCorrenti.containsKey(coda)) { this.indiciCorrenti.put(coda, -1); }
  }
  private void verificaCodaNonVuota(CodaRiproduzione coda) throws BusinessException { verificaCoda(coda); 
    if (coda.getListaCodaBrani().isEmpty()) { throw new OperazioneNonValidaException("La coda di riproduzione è vuota"); }
  }
  @Override 
  public void aggiungiBranoCods(CodaRiproduzione coda, Brano brano) throws BusinessException { verificaCoda(coda); 
    if (brano==null) { throw new OperazioneNonValidaException("Il brano non può essere null"); }
    List<Brano> brani=coda.getListaCodaBrani(); 
    brani.add(brano); 
    coda.setListaCodaBrani(brani); 
    if (this.indiciCorrenti.get(coda)==1) { this.indiciCorrenti.put(coda, 0); }
  }
  @Override 
  public void rimuoviBranoCoda(CodaRiproduzione coda, Brano brano) throws BusinessException {
    verificaCoda(coda); 
    verificaCoda(coda); 
      if (brano==null) { throw new OperazioneNonValidaException("Il brano non può essere null"); }
    List<Brano> brani=coda.getListaCodaBrani(); 
    int indiceRimosso=brani.indexOf(brano); 
    if (indiceRimosso==-1) { throw new OperazioneNonValidaException("Il brano non è presente nella coda"); }
    int indiceCorrente=this.indiciCorrenti.get(coda); 
    brani.remove(indiceRimosso); 
    coda.setListaCodaBrani(brani); 
    if (brani.isEmpty()) { this.indiciCorrenti.put(coda, -1); 
      coda.setStato(StatoRiproduzione.FERMO); 
    }
    else if (indiceRimosso<indiceCorrente) { this.indiciCorrenti.put(coda, indiceCorrente-1); 
    }
    else if (indiceRimosso==indiceCorrente) {
      if (indiceCorrente >= brani.size()) { this.indiciCorrenti.put(coda, brani.size()-1); }
    }
  }
  @Override 
  public void svuotaCoda(CodaRiproduzione coda) throws BusinessException { verificaCoda(coda); 
    coda.setListaCodaBrani(new ArrayList<>()); 
    this.indiciCorrenti.put(coda, -1); 
    coda.setStato(StatoRiproduzione.FERMO); 
  }
  @Override
  public void play(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    int indice=this.indiciCorrenti.get(coda); 
    if (indice==-1) { indice=0;
      this.indiciCorrenti.put(coda, indice); 
    }
    coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
  }
  @Override 
  public void pausa(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    coda.setStato(StatoRiproduzione.IN_PAUSA); 
  }
  @Override 
  public void stop(CodaRiproduzione coda) throws BusinessException { verificaCoda(coda); 
    coda.setStato(StatoRiproduzione.FERMO); 
    this.indiciCorrenti.put(coda, -1); 
  }
  @Override 
  public void avanti(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    List<Brano> brani=coda.getListaCodaBrani(); 
    int indiceCorrente=this.indiciCorrenti.get(coda); 
    if (indiceCorrente==-1) { this.indiciCorrenti.put(coda, 0);
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
      return; 
    }
    if (coda.getRepeat()==ModalitaRepeat.RIPETI_BRANO) { coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
      return; 
    }
    if (indiceCorrente < brani.size()-1) { this.indiciCorrenti.put(coda, indiceCorrente + 1); 
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
    }
    else if (coda.getRepeat()==ModalitaRepeat.RIPETI_CODA) { this.indiciCorrenti.put(coda, 0);
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
    }
    else { this.indiciCorrenti.put(coda, -1); 
      coda.setStato(StatoRiproduzione.FERMO); 
    }
  }
  @Override
  public void indietro(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    List<Brano> brani=coda.getListaCodaBrani(); 
    int indiceCorrente=this.indiciCorrenti.get(coda); 
    if (indiceCorrente==-1) { this.indiciCorrenti.put(coda, brani.size()-1); 
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
      return; 
    }
    if (indiceCorrente > 0) { this.indiciCorrenti.put(coda, indiceCorrente-1); 
    }
    else if (coda.getRepeat()==ModalitaRepeat.RIPETI_CODA) { this.indiciCorrenti.put(coda, brani.size()-1); 
    }
    else { this.indiciCorrenti.put(coda, 0); }
    coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
  }
  @Override 
  public void shuffle(CodaRiproduzione coda) throws BusinessException { verificaCoda(coda); 
      List<Brano> brani=coda.getListaCodaBrani(); 
    if (brani.isEmpty()) { throw new OperazioneNonValidaException("Non è possibile effettuare lo shuffle su una coda vuota"); }
    int indiceCorrente=this.indiciCorrenti.get(coda);
      Brano branoCorrente=null; 
    if (indiceCorrente >= 0 && indiceCorrente < brani.size()) { branoCorrente=brani.get(indiceCorrente); }
    Collections.shuffle(brani); 
    coda.setListaCodaBrani(brani); 
    coda.setShuffle(true); 
    if (branoCorrente!=null) { this.indiciCorrenti.put(coda, brani.indexOf(branoCorrente)); 
    }
  }
  @Override
  public void impostaRepeatCoda(CodaRiproduzione coda, ModalitaRepeat modalita) throws BusinessException { verificaCoda(coda);
    if (modalita==null) { throw new OperazioneNonValidaException("La modalità repeat non può essere null"); }
    coda.setModalitaRepeat(modalita); 
  }
  @Override 
  public void branoTerminato(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    int indiceCorrente=this.indiciCorrenti.get(coda);
    if (indiceCorrente==-1) { this.indiciCorrenti.put(coda, 0); 
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
      return; 
    }
    if (coda.getRepeat()==ModalitaRepeat.RIPETI_BRANO) { coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
      return; 
    }
    List<Brano> brani=coda.getListaCodaBrani(); 
    if (indiceCorrente < brani.size()-1) { this.indiciCorrenti.put(coda, indiceCorrente+1); 
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
    }
    else if (coda.getRepeat()==ModalitaRepeat.RIPETI_CODA) { this.indiciCorrenti.put(coda, 0); 
      coda.setStato(StatoRiproduzione.IN_RIPRODUZIONE); 
    }
    else { this.indiciCorrenti.put(coda, -1); 
      coda.setStato(StatoRiproduzione.FERMO); 
    }
  }
  @Override
  public Brano getBranoCorrente(CodaRiproduzione coda) throws BusinessException { verificaCodaNonVuota(coda); 
    int indiceCorrente=this.indiciCorrenti.get(coda); 
    if (indiceCorrente==-1) { return null; }
    List<Brano> brani=coda.getListaCodaBrani(); 
    if (indiceCorrente >= brani.size()) { throw new OperazioneNonValidaException("L'indice del brano corrente non è valido"); }
    return brani.get(indiceCorrente); 
  }
  @Override
  public List<Brano> getCoda(CodaRiproduzione coda) throws BusinessException { verificaCoda(coda); 
    return new ArrayList<>(coda.getListaCodaBrani()); 
  }
}
