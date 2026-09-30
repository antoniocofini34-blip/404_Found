import java.util.*;
public class Album implements Ricercabile{
  private List<Brano> lista_brani;
  private Artista artista;
  private String titolo; 
  private int annoPubblicazione; 
  private Set<Genere> genere; 
  private String copertina;
  public Album(List<Brano> lista_brani, Artista artista, String titolo, int annoPubblicazione, Set<Genere> genere, String copertina){
    this.lista_brani=new ArrayList<>();
    this.artista=new Artista(); 
    this.titolo=titolo;
    this.annoPubblicazione=annoPubblicazione;
    this.genere=new HashSet<>(genere); 
    this.copertina=copertina;
  } 

  public List<Brano> getListaBrani () {
    return new ArrayList<>(this.lista_brani); 
  }
  public Artista getArtista() {
    return artista;
  }
  public String getTitolo() {
    return titolo;
  }
  public int getAnnoPubblicazione() {
    return annoPubblicazione;
  }
  public Set<Genere> getGenere() {
   return new HashSet<>(this.genere); 
  }
  public String getCopertina() {
    return copertina;
  }
  public void setBrano(List<Brano> lista_brani) {
    if (lista_brani!=null) this.lista_brani=newArrayList<>(lista_brani); 
    else this.lista_brani=newArrayList<>(); 
  }
  public void setArtista(Artista artista) {
    this.artista=artista; 
  }
  public void setTitolo(String titolo) {
    this.titolo=titolo;
  }
  public void setAnnoPubblicazione(int annoPubblicazione) {
    this.annoPubblicazione=annoPubblicazione;
  }
  public void setGenereMusicale(Set<Genere> genere) {
    if (genere!=null) this.genere=new HashSet<>(genere); 
    else this.genere=new HashSet<>();  
  }
  public void aggiungiCopertina(String copertina) {
    this.copertina=copertina;
  }
  public int getDurataTotale() {
    int durataTotale=0; 
    for (Brano brano : lista_brani) {
      durataTotale += brano.getDurata(); 
    }
    return durataTotale; 
  }
  public boolean equals (Object obj) {
    if (this==obj) return true; 
    if (!(obj instanceof Album)) return false; 
    Album a=(Album) obj; 
    return 

  
  @Override
  public String getNome() {
    return titolo;
  }
  public String getTitolo() {
    return titolo;
  }
  public int getAnnoPubblicazione() {
    return annoPubblicazione;
  }
  public Genere getGenereMusicale() {
    return GenereMusicale;
  }
  public String getCopertina() {
    return copertina;
  }
  public List<Brano> getBrano() {
    return brano;
  }
}
