package com.example.domain;
import java.util.*; 
public class Brano implements Ricercabile, Riproducibile { private String titolo;
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
  public Set<GenereMusicale> getGenereBrano() { return new HashSet<>(this.genere_brano); }
  public Album getAlbumBrano() { return album_brano; }
  public int getNumeroAscolti() { return numero_ascolti; }
  public void setTitolo(String titolo) { this.titolo=titolo; }
  public void setDurata(int durata) { this.durata=durata; }
  public void setTesto(String testo) { this.testo=testo; }
  public void setAlbumBrano(Album album_brano) { this.album_brano=album_brano; }
  public void setNumeroAscolti(int numero_ascolti) { this.numero_ascolti=numero_ascolti; }
}
