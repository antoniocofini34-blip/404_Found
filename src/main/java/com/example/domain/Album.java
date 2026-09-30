import java.util.*;
package com.example.domain; 

public class Album implements Ricercabile{
  private List<Brano> lista_brani;
  private Artista artista;
  private String titolo; 
  private int annoPubblicazione; 
  private Set<GenereMusicale> genere; 
  private String copertina;
  public Album(List<Brano> lista_brani, Artista artista, String titolo, int annoPubblicazione, Set<GenereMusicale> genere, String copertina){
    this.lista_brani=new ArrayList<>();
    this.artista=artista;  
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
  public Set<GenereMusicale> getGenere() {
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
  public void setGenereMusicale(Set<GenereMusicale> genere) {
    if (genere!=null) this.genere=new HashSet<>(genere); 
    else this.genere=new HashSet<>();  
  }
  public void setCopertina(String copertina) {
    this.copertina=copertina;
  }
}
