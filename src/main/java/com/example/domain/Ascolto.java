import java.util.*;
public class Ascolto{
  private Brano brano_Ascoltato;
  private LocalDateTime istanteAscolto;
  private int tempo_brano_ascoltato;
    public Ascolto(Brano brano, LocalDateTime istanteAscolto, int tempo_brano_ascoltato){
    this.brano=brano;
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
}
