import java.util.*;
public class Coda_Riproduzione implements Riproducibile {
  private List<Brano> lista_coda_brani;
  private boolean riproduzione_casuale;
  private Modalita_Repeat modalita_repeat;
  private Stato_Riproduzione stato;
  public Coda_Riproduzione(List<Brano> lista_coda_brani, boolean riproduzione_casuale, Modalita_Repeat modalita_repeat, Stato_Riproduzione stato) {
    this.lista_coda_brani=new ArrayList<>();
    this.riproduzione_casuale=riproduzione_casuale;
    this.modalita_repeat=modalita_repeat;
    this.stato=stato;
  }
  public List<Brano> getListaCodaBrani() {
    return lista_coda_brani;
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
  public void aggiungiBranoCoda(Brano brano) {
    this.lista_coda_brani.add(brano);
  }
  public void rimuoviBranoCoda(Brano: brano) {
    this.lista_coda_brani.remove(brano);
  }
  public boolean svuotaCoda() {
    this.lista_coda_brani.clear();
  }
  public Brano getBranoCorrente() {
    





    

