public class Album implements Ricercabile{
  private String titolo; 
  private int annoPubblicazione; 
  private Genere GenereMusicale; 
  private String copertina;
  private List<Brano> brano;
  public String getTitolo(){
    return titolo;
  }
  public int getAnnoPubblicazione(){
    return annoPubblicazione;
  }
  public Genere getGenereMusicale(){
    return GenereMusicale;
  } 
  public String copertina(){
    return copertina;
  }
  public List<Brano> brano(){
    return brano;
  }
  public void setTitolo(String titolo){
    this.titolo=titolo;
  }
  public void setAnnoPubblicazione(int annoPubblicazione){
    this.annoPubblicazione=annopubblicazione;
  }
  public void setGenereMusicale(Genere GenereMusicale){
    this.GenereMusicale=GenereMusicale;
  }
  public void setCopertina(String copertina){
    this.copertina=copertina;
  }
  public void setList<Brano>(List<Branoo> brano){
    this.brano=brano;
  }
  public Album(String titolo, int annoPubblicazione, genere GenereMusicale, String copertina){
    this.titolo=titolo;
    this.annoPubblicazione=annoPubblicazione;
    this.Genere=Genere;
    this.copertina=copertina;
    this.brano=new ArrayList<>();
  } 
  public int getDurataTotale(){
    return brani.stream().mapToInt(Brano::getDurata).sum();
  }
  public void modificaCopertina(String copertina){
    this.copertina=copertina;
  }
  @Override
  public String getNome(){
    return titolo;
  }
  public String getTitolo(){
    return titolo;
  }
  public int getAnnoPubblicazione(){
    return annoPubblicazione;
  }
  public Genere getGenereMusicale(){
    return GenereMusicale;
  }
  public String getCopertina(){
    return copertina;
  }
  public List<Brano> getBrano(){
    return brano;
  }
}
