import java.util.*;
public abstract class Artista {
  private String biografia; 
  private Genere genere_principale; 
  private Set<Genere> generi_secondari; 
  private List<Album> discografia; 
  public Artista (String biografia, Genere genere_pricipale, Set<Genere> generi_secondari, List<Album> discografia) {
    this.biografia=biografia; 
    this.genere_principale=genere_principale;
    this.generi_secondari=new HashSet<>(generi_secondari); 
    this.discografia=new ArrayList<>(); 
  }
  public String getBiografia() {
    return biografia; 
  }
  public Genere getGenerePrincipale() {
    return genere_principale; 
  }
  public Set<Genere> getGeneriSecondari() {
    return generi_secondari; 
  }
  public List<Album> getDiscografia() {
    return discografia; 
  }
} 
