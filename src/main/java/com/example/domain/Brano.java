package com.example.domain;
import java.util.*; 
public class Brano implements Ricercabile, Riproducibile {
  private String titolo;
  private int durata; 
  private String testo;
  private Set<GenereMusicale> genere_brano; 
  private Album album_brano; 
  private int numero_ascolti; 
  public Brano (String titolo, int durata, String testo, Set<GenereMusicale> genere_brano, Album album_brano, int numero_ascolti) {
    this.titolo=titolo; 
    this.durata=durata; 
    this.testo=testo; 
    if (genere_brano!=null) this.genere_brano=new HashSet<>(genere_brano); 
    else this.genere_brano=new HashSet<>(); 
    this.album_brano=album_brano;  
    this.numero_ascolti=numero_ascolti; 
  }
  public String getTitolo() { return titolo; }
  public int getDurata() { return durata; }
  public String getTesto() { return testo; }
  public Set<GenereMusicale> getGenereBrano() { return new HashSet<>(this.genere_Brano); }
  public Album getAlbumBrano() { return album_Brano; }
  public int getNumeroAscolti() { return numero_Ascolti; }
  public void setTitolo(String titolo) { this.titolo=titolo; }
  public void setDurata(int durata) { this.durata=durata; }
  public void setTesto(String testo) { this.testo=testo; }
  public void setGenereBrano(Set<GenereMusicale> genere_Brano) {
    if (genere_Brano!=null) this.genere_Brano=new HashSet<>(genere_Brano); 
    else this.genere_Brano=new HashSet<>();
  }
  public void setAlbumBrano(Album album_Brano) { this.album_Brano=album_Brano; }
  public void setNumeroAscolti(int numero_Ascolti) { this.numero_Ascolti=numero_Ascolti; }
  @Override
  public boolean contieneTesto(String testo) { return false; }
}    
    
  
