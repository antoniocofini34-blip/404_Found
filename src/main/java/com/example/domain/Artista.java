import java.util.*;
package com.example.domain;

public abstract class Artista {
  private String biografia; 
  private Genere genere_principale; 
  private Set<Genere> generi_secondari; 
  private List<Album> discografia; 
  public Artista (String biografia, Genere genere_pricipale, Set<Genere> generi_secondari, List<Album> discografia) {
    this.biografia=biografia; 
    this.genere_principale=new Genere(); 
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
    return new HashSet<>(this.generi_secondari);  
  }
  public List<Album> getDiscografia() {
    return new ArrayList<>(this.discografia); 
  }
  public void setBiografia(String biografia) {
    this.biografia=biografia; 
  }
  public void setGenerePrincipale(Genere genere_principale) {
    this.genere_principale=genere_principale; 
  }
  public void setGeneriSecondari(Set<Genere> generi_secondari) {
    if (generi_secondari!=null) this.generi_secondari=new HashSet<>(generi_secondari);
    else this.generi_secondari=new HashSet<>(); 
  }
  public void setDiscografia(List<Album> discografia) {
    if (discografia!=null) this.discografia=new ArrayList<>(discografia); 
    else this.discografia=new ArrayList<>(); 
  }
} 
