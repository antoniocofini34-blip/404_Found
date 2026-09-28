import java.util.*; 
public class Brano implements Ricercabile, Riproducibile {
  private String titolo;
  private int durata; 
  private String testo;
  private Set<Genere> genere_Brano; 
  private Album album_Brano; 
  private int numero_Ascolti; 
  public Brano (String titolo, int durata, String testo, Set<Genere> genere_Brano, Album album_Brano, int numero_Ascolti) {
    this.titolo=titolo; 
    this.durata=durata; 
    this.testo=testo; 
    this.genere_Brano=new HashSet<>(genere_Brano); 
    this.album_Brano=album_Brano; 
    this.numero_Ascolti=numero_Ascolti; 
  }
  public String getTitolo() {
    return titolo;
  }
  public int getDurata() {
    return durata; 
  }
  public String getTesto() {
    return testo; 
  }
  public Set<Genere> getGenereBrano() {
    return genere_Brano;
  }
  public Album getAlbumBrano() {
    return album_Brano; 
  }
  public int getNumeroAscolti() {
    return numero_Ascolti; 
  }
  public void setTitolo(String titolo) {
    this.titolo=titolo; 
  }
  public void setDurata(int durata) {
    this.durata=durata; 
  }
  public void setTesto(String testo) {
    this.testo=testo; 
  }
  public void inserisciGenereBrano(Genere genere_Brano) {
    this.genere_Brano.add(genere_Brano);  
  }
  public void setAlbumBrano(Album album_Brano) {
    this.album_Brano=album_Brano; 
  }
  public void setNumeroAscolti(int numero_Ascolti) {
    return numero_Ascolti=numero_Ascolti;
  }
  public void incrementaAscolti() {
    numero_Ascolti ++;
  }
  
  @Override
  public void play() {
    System.out.println("Riproduzione di" + titolo); 
  }
  
  @Override
  public void pausa() {
    System.out.println("Pausa di" + titolo); 
  }

  @Override 
  public boolean corrispondeA(String testo) {
    return titolo.toLowerCase().contains(testo.toLowerCase()); 
  }
}
  
  



  
    
    
  
