package com.example.domain;
import java.util.*;
public class ArtistaGruppo extends Artista implements Ricercabile {
  private String nome_gruppo;
  private List<Artista> lista_membri;
  public ArtistaGruppo(String biografia, GenereMusicale genere_principale, Set<GenereMusicale> generi_secondari, List<Album> discografia, String nome_gruppo, List<Artista> lista_membri) {
    super (biografia, genere_principale, generi_secondari, discografia);
    this.nome_gruppo=nome_gruppo;
    if (lista_membri!=null) this.lista_membri=new ArrayList<>(lista_membri); 
    else this.lista_membri=new ArrayList<>();    
  }
  public String getNomeGruppo() { return nome_gruppo; }
  public List<Artista> getListaMembri() { return new ArrayList<>(this.lista_membri); }
  public void setNomeGruppo(String nome_gruppo) { this.nome_gruppo=nome_gruppo; }
  public void setListaMembri(List<Artista> lista_membri) {
    if (lista_membri!=null) this.lista_membri=new ArrayList<>(lista_membri); 
    else this.lista_membri=new ArrayList<>(); 
  }
}
