import java.util.*; 
package com.example.domain;

public class Brano implements Ricercabile, Riproducibile {
  private String titolo;
  private int durata; 
  private String testo;
  private Set<GenereMusicale> genere_Brano; 
  private Album album_Brano; 
  private int numero_Ascolti; 
  public Brano (String titolo, int durata, String testo, Set<GenereMusicale> genere_Brano, Album album_Brano, int numero_Ascolti) {
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
  public Set<GenereMusicale> getGenereBrano() {
    return new HashSet<>(this.genere_Brano); 
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
  public void setGenereBrano(Set<GenereMusicale> genere_Brano) {
    if (genere_Brano!=null) this.genere_Brano=new HashSet<>(genere_Brano); 
    else this.genere_Brano=new HashSet<>();
  }
  public void setAlbumBrano(Album album_Brano) {
    this.album_Brano=album_Brano; 
  }
  public void setNumeroAscolti(int numero_Ascolti) {
    this.numero_Ascolti=numero_Ascolti; 
  }
}
  
  



  
    
    
  
