package com.example.domain; 
import java.util.*;

public class Album implements Ricercabile {
  private List<Brano> lista_brani;
  private Artista artista;
  private String titolo; 
  private int anno_pubblicazione; 
  private Set<GenereMusicale> genere; 
  private String copertina;
  public Album(List<Brano> lista_brani, Artista artista, String titolo, int anno_pubblicazione, Set<GenereMusicale> genere, String copertina) {
    if (lista_brani!=null) this.lista_brani=new ArrayList<>(lista_brani); 
    else this.lista_brani=new ArrayList<>();
    this.artista=artista;  
    this.titolo=titolo;
    this.anno_pubblicazione=anno_pubblicazione;
    if (genere!=null) this.genere=new HashSet<>(genere); 
    else this.genere=new HashSet<>(); 
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
    return anno_pubblicazione;
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
  public void setAnnoPubblicazione(int anno_pubblicazione) {
    this.anno_pubblicazione=anno_pubblicazione;
  }
  public void setGenereMusicale(Set<GenereMusicale> genere) {
    if (genere!=null) this.genere=new HashSet<>(genere); 
    else this.genere=new HashSet<>();  
  }
  public void setCopertina(String copertina) {
    this.copertina=copertina;
  }
  @Override
  public boolean contieneTesto(String testo) {
    return false;
  }
}
