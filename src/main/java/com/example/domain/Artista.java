import java.util.*;
package com.example.domain;

public abstract class Artista {
  private String biografia; 
  private GenereMusicale genere_principale; 
  private Set<GenereMusicale> generi_secondari; 
  private List<Album> discografia; 
  public Artista (String biografia, GenereMusicale genere_pricipale, Set<GenereMusicale> generi_secondari, List<Album> discografia) {
    this.biografia=biografia; 
    this.genere_principale=genere_principale;  
    if (generi_secondari!=null) this.generi_secondari=new HashSet<>(generi_secondari); 
    else this.generi_secondari=new HashSet<>();
    if (discografia!=null) this.discografia=new ArrayList<>(discografia); 
    else this.discografia=new ArrayList<>();
  }
  public String getBiografia() {
    return biografia; 
  }
  public GenereMusicale getGenerePrincipale() {
    return genere_principale; 
  }
  public Set<GenereMusicale> getGeneriSecondari() {
    return new HashSet<>(this.generi_secondari);  
  }
  public List<Album> getDiscografia() {
    return new ArrayList<>(this.discografia); 
  }
  public void setBiografia(String biografia) {
    this.biografia=biografia; 
  }
  public void setGenerePrincipale(GenereMusicale genere_principale) {
    this.genere_principale=genere_principale; 
  }
  public void setGeneriSecondari(Set<GenereMusicale> generi_secondari) {
    if (generi_secondari!=null) this.generi_secondari=new HashSet<>(generi_secondari);
    else this.generi_secondari=new HashSet<>(); 
  }
  public void setDiscografia(List<Album> discografia) {
    if (discografia!=null) this.discografia=new ArrayList<>(discografia); 
    else this.discografia=new ArrayList<>(); 
  }
} 
