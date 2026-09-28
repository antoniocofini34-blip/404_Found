public class Ascolto{
  private Brano brano;
  private LocalDateTime istanteAscolto;
  private int durataEffettiva;
  public String getBrano(){
    return brano;
  }
  public LocalDateTime getIstanteAscolto(){
    return istanteAscolto;
  }
  public int getDurataEffettiva(){
    return durataEffettiva;
  }
  public void setBrano(Brano brano){
    this brano=brano;
  }
  public void setIstanteAscolto(LocalDateTime istanteAscolto){
    this.durataAscolto=durataAscolto;
  }
  public void setDurataEffettiva(int durataEffettiva){
    this.durataEffettiva=durataEffettiva;
  }
  public Ascolto(Brano brano, LocalDateTime istanteAscolto, int durataEffettiva){
    this.brano=brano;
    this.istanteAscolto=istanteAscolto;
    this.durataEffettiva=durataEffettiva;
  }
  public void aggiornaDurata(int durata){
    this.durataEffettiva=durata;
  }
  public int getSecondiAscoltati(){
    return this.durataEffettiva;
    Ascolto ascolto= new Ascolto();
    int secondi=ascolto.registraAscolto(brano, LocalDateTime.now, 120);
    System.out.println("Numero di secondi ascoltati:" + getSecondiAscoltati);
  }
}
